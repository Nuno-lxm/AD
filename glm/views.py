from datetime import datetime

from django.contrib.auth import login
from django.contrib.auth.decorators import login_required, user_passes_test
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from django.contrib.auth.models import User
from django.core.exceptions import ValidationError
from django.http import JsonResponse
from django.shortcuts import render, redirect, get_object_or_404
from rest_framework import viewsets, status
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
import requests

from .forms import FornecedorForm, MedicamentoForm, EncomendaForm, UserForm, FornecedorUserForm
from .models import Encomenda, Medicamento, Fornecedor
from .serializers import EncomendaSerializer, MedicamentoSerializer, FornecedorSerializer, UserSerializer

FORNECEDORES_API_URL = 'http://127.0.0.1:8000/glm/api/fornecedores/'
MEDICAMENTOS_API_URL = 'http://127.0.0.1:8000/glm/api/medicamentos/'
ENCOMENDAS_API_URL = 'http://127.0.0.1:8000/glm/api/encomendas/'

def is_staff(user):
    return user.is_staff

@login_required
def glm_client(request):
    # Verificar se o usuário logado é staff ou fornecedor
    is_staff = request.user.is_staff
    fornecedor = getattr(request.user, 'fornecedor', None)

    # Filtrar medicamentos e encomendas com base no tipo de usuário
    fornecedores = Fornecedor.objects.all() if is_staff else None

    if is_staff:
        medicamentos = Medicamento.objects.all()
        encomendas = Encomenda.objects.all()
    else:
        medicamentos = (
            Medicamento.objects.filter(fornecedores=fornecedor)
            if fornecedor else Medicamento.objects.none()
        )
        encomendas = (
            Encomenda.objects.filter(fornecedor=fornecedor)
            if fornecedor else Encomenda.objects.none()
        )
    users = User.objects.all() if is_staff else None

    if request.method == 'POST':
        if 'new-supplier' in request.POST:
            form = FornecedorForm(request.POST)
            if form.is_valid():
                form.save()
                return redirect('glm_client')
        elif 'new-medicine' in request.POST:
            form = MedicamentoForm(request.POST)
            if form.is_valid():
                form.save()
                return redirect('glm_client')
        elif 'new-order' in request.POST:
            form = EncomendaForm(request.POST)
            if form.is_valid():
                form.save()
                return redirect('glm_client')

    context = {
        'fornecedores': fornecedores,
        'medicamentos': medicamentos,
        'encomendas': encomendas,
        'users': users,
    }

    return render(request, 'glm/glm_client.html', context)


@login_required
@user_passes_test(is_staff)
def adicionar_fornecedor(request):
    if request.method == 'POST':
        form = FornecedorForm(request.POST)
        user_id = request.POST.get('user')  # Obtém o ID do utilizador selecionado
        if form.is_valid():
            fornecedor = form.save(commit=False)
            user = User.objects.get(id=user_id)  # Obtém o utilizador pelo ID
            fornecedor.user = user  # Associa o fornecedor ao utilizador
            fornecedor.save()
            return redirect('glm_client')
    else:
        form = FornecedorForm()

    # Filtra os utilizadores que ainda não têm fornecedor associado
    users_without_supplier = User.objects.filter(fornecedor__isnull=True)
    return render(request, 'glm/fornecedores/adicionar_fornecedor.html',
                  {'form': form, 'users': users_without_supplier})

@login_required
@user_passes_test(is_staff)
def adicionar_medicamento(request):
    if request.method == 'POST':
        form = MedicamentoForm(request.POST)
        if form.is_valid():
            medicamento = form.save()

            fornecedores_ids = request.POST.getlist('fornecedores')
            fornecedores = Fornecedor.objects.filter(id__in=fornecedores_ids)
            medicamento.fornecedores.set(fornecedores)  # Associa os fornecedores selecionados

            return redirect('glm_client')
    else:
        form = MedicamentoForm()

    fornecedores = Fornecedor.objects.all()

    return render(request, 'glm/medicamentos/adicionar_medicamento.html', {'form': form, 'fornecedores': fornecedores})


@login_required
def adicionar_encomenda(request):
    medicamentos = Medicamento.objects.all()  # Obtendo todos os medicamentos
    fornecedores = Fornecedor.objects.all()   # Obtendo todos os fornecedores

    if request.method == 'POST':
        form = EncomendaForm(request.POST)
        if form.is_valid():
            encomenda = form.save(commit=False)
            try:
                encomenda.clean()
                encomenda.save()
                return redirect('glm_client')
            except ValidationError as e:
                form.add_error(None, e)
    else:
        form = EncomendaForm()

    return render(request, 'glm/encomendas/adicionar_encomenda.html', {
        'form': form,
        'medicamentos': medicamentos,  # Passando medicamentos para o template
        'fornecedores': fornecedores   # Passando fornecedores para o template
    })


@login_required
def get_fornecedores(request, medicamento_id):
    medicamento = get_object_or_404(Medicamento, id=medicamento_id)
    fornecedores = medicamento.fornecedores.all()

    fornecedores_data = [{"id": fornecedor.id, "nome": fornecedor.nome} for fornecedor in fornecedores]
    return JsonResponse({"fornecedores": fornecedores_data})


@login_required
def editar_fornecedor(request, id):
    fornecedor = get_object_or_404(Fornecedor, id=id)

    if request.method == 'POST':
        if request.user.is_staff:
            form = FornecedorForm(request.POST, instance=fornecedor)
            # Lógica de associar o usuário ao fornecedor
            if request.user.is_staff:
                user_id = request.POST.get('user')
                if user_id:
                    user = User.objects.get(id=user_id)
                    fornecedor.user = user
        else:
            form = FornecedorUserForm(request.POST, instance=fornecedor)

        if form.is_valid():
            form.save()
            return redirect('glm_client')
    else:
        if request.user.is_staff:
            form = FornecedorForm(instance=fornecedor)
        else:
            form = FornecedorUserForm(instance=fornecedor)

    # Filtra os utilizadores que ainda não têm fornecedor associado
    users_without_supplier = User.objects.filter(fornecedor__isnull=True)
    return render(request, 'glm/fornecedores/editar_fornecedor.html', {'form': form, 'users': users_without_supplier})


@login_required
@user_passes_test(is_staff)
def apagar_fornecedor(request, id):
    fornecedor = get_object_or_404(Fornecedor, id=id)
    if request.method == 'POST':
        fornecedor.delete()
        return redirect('glm_client')  # Isso depende de sua implementação
    return render(request, 'glm/confirmar_apagar.html', {'fornecedor': fornecedor})

@login_required
@user_passes_test(is_staff)
def editar_medicamento(request, id):
    medicamento = get_object_or_404(Medicamento, id=id)
    fornecedores = Fornecedor.objects.all()

    if request.method == 'POST':
        form = MedicamentoForm(request.POST, instance=medicamento)
        if form.is_valid():
            form.save()
            return redirect('glm_client')
    else:
        form = MedicamentoForm(instance=medicamento)

    return render(request, 'glm/medicamentos/editar_medicamento.html', {
        'form': form,
        'fornecedores': fornecedores  # Passando os fornecedores para o template
    })

@login_required
@user_passes_test(is_staff)
def apagar_medicamento(request, id):
    medicamento = get_object_or_404(Medicamento, id=id)
    if request.method == 'POST':
        medicamento.delete()
        return redirect('glm_client')  # Isso depende de sua implementação
    return render(request, 'glm/confirmar_apagar.html', {'medicamento': medicamento})

@login_required
@user_passes_test(is_staff)
def editar_encomenda(request, id):
    encomenda = get_object_or_404(Encomenda, id=id)
    if request.method == 'POST':
        form = EncomendaForm(request.POST, instance=encomenda)
        if form.is_valid():
            form.save()
            return redirect('glm_client')
    else:
        form = EncomendaForm(instance=encomenda)
    return render(request, 'glm/encomendas/editar_encomenda.html', {'form': form})

@login_required
@user_passes_test(is_staff)
def apagar_encomenda(request, id):
    encomenda = get_object_or_404(Encomenda, id=id)
    if request.method == 'POST':
        encomenda.delete()
        return redirect('glm_client')
    return render(request, 'glm/confirmar_apagar.html', {'encomenda': encomenda})

@login_required
@user_passes_test(is_staff)
def apagar_user(request, user_id):
    user = get_object_or_404(User, id=user_id)
    user.delete()  # Exclui o usuário
    return redirect('glm_client')

@login_required
def fornecedor_detalhes(request, id):
    fornecedor = get_object_or_404(Fornecedor, id=id)
    encomendas = Encomenda.objects.filter(fornecedor=fornecedor)
    medicamentos = Medicamento.objects.filter(fornecedores=fornecedor)
    return render(request, 'glm/fornecedores/detalhes_fornecedor.html', {'fornecedor': fornecedor, 'encomendas': encomendas, 'medicamentos': medicamentos})

@login_required
def medicamento_detalhes(request, id):
    medicamento = get_object_or_404(Medicamento, id=id)
    fornecedores = None

    if request.user.is_staff:
        fornecedores = Fornecedor.objects.filter(medicamento=medicamento)

    return render(request, 'glm/medicamentos/detalhes_medicamento.html', {
        'medicamento': medicamento,
        'fornecedores': fornecedores
    })


@login_required
def encomenda_detalhes(request, id):
    encomenda = get_object_or_404(Encomenda, id=id)
    return render(request, 'glm/encomendas/detalhes_encomenda.html', {'encomenda': encomenda})


@login_required
def user_detalhes(request, user_id):
    user = get_object_or_404(User, id=user_id)
    # Se o usuário for fornecedor, obter os detalhes do fornecedor
    fornecedor = None
    if hasattr(user, 'fornecedor'):
        fornecedor = user.fornecedor

    context = {
        'user': user,
        'fornecedor': fornecedor,  # Passando os detalhes do fornecedor, se existirem
    }
    return render(request, 'glm/users/detalhes_user.html', context)


@login_required
def medicamentos_fornecedor(request):
    fornecedor = getattr(request.user, 'fornecedor', None)  # Obter o fornecedor associado ao usuário logado

    if not fornecedor:
        return JsonResponse({"error": "O usuário logado não é associado a nenhum fornecedor."}, status=403)

    medicamentos = Medicamento.objects.filter(fornecedores=fornecedor)

    context = {
        'fornecedor': fornecedor,
        'medicamentos': medicamentos,
        'lista_vazia': not medicamentos.exists(),  # Verifica se a lista de medicamentos está vazia
    }

    return render(request, 'medicamentos_fornecedor.html', context)

@login_required
def confirmar_encomenda(request, id):
    encomenda = get_object_or_404(Encomenda, id=id)
    medicamento = encomenda.medicamento

    if encomenda.concluida:
        return JsonResponse({"error": "A encomenda já foi confirmada."}, status=400)

    encomenda.concluida = True
    encomenda.save()

    medicamento.stock += encomenda.quantidade
    medicamento.save()

    encomenda.data_confirmacao = datetime.now()
    encomenda.save()

    encomenda.editavel = False
    encomenda.save()

    return redirect('detalhes_encomenda', id=encomenda.id)


class AtualizarMedicamentoView(APIView):
    def post(self, request):
        medicamento_id = request.data.get('medicamento_id')
        quantidade = request.data.get('quantidade')
        threshold = request.data.get('threshold')

        try:
            medicamento = Medicamento.objects.get(id=medicamento_id)

            if medicamento.stock < quantidade:
                return Response({"error": "Stock insuficiente para realizar a dispensa."},
                                status=status.HTTP_400_BAD_REQUEST)

            medicamento.stock -= quantidade
            medicamento.save()
            if medicamento.stock <= threshold:
                msg = "Stock atualizado com sucesso. STOCK UNDER THRESHOLD"

            return Response({"message": "Stock atualizado com sucesso."}, status=status.HTTP_200_OK)
        except Medicamento.DoesNotExist:
            return Response({"error": "Medicamento não encontrado."}, status=status.HTTP_404_NOT_FOUND)


def register_user(request):
    if request.method == 'POST':
        user_form = UserCreationForm(request.POST)  # Use seu formulário de usuário customizado, se necessário
        if user_form.is_valid():
            # Salva o usuário, mas não define a senha por enquanto
            user = user_form.save(commit=False)
            user.set_password(user_form.cleaned_data['password1'])  # Define a senha
            user.save()

            # Faz login do usuário após o registro
            login(request, user)

            return redirect('login')  # Redireciona para a página de login após o registro
    else:
        user_form = UserCreationForm()  # Ou o seu formulário customizado, se necessário

    return render(request, 'registration/register.html', {'user_form': user_form})


def login_view(request):
    if request.method == 'POST':
        form = AuthenticationForm(request, data=request.POST)
        if form.is_valid():
            user = form.get_user()
            login(request, user)
            return redirect(request.GET.get('next', 'glm/glm_client/'))  # Redirecionamento para o app glm
    else:
        form = AuthenticationForm()

    return render(request, 'registration/login.html', {'form': form})

class MedicamentoViewSet(viewsets.ModelViewSet):
    queryset = Medicamento.objects.all()  # Default queryset for all users (admin)
    serializer_class = MedicamentoSerializer

    def get_queryset(self):
        user = self.request.user
        if user.is_staff:
            return Medicamento.objects.all()  # Staff sees all medicines
        else:
            return Medicamento.objects.filter(fornecedores__user=user)  # Supplier sees their own medicines



class EncomendaViewSet(viewsets.ModelViewSet):
    queryset = Encomenda.objects.all()
    serializer_class = EncomendaSerializer


class FornecedorViewSet(viewsets.ModelViewSet):
    queryset = Fornecedor.objects.all()
    serializer_class = FornecedorSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        """
        Filtra os fornecedores para que apenas o staff veja todos os fornecedores.
        """
        user = self.request.user
        if user.is_staff:
            return Fornecedor.objects.all()  # Staff vê todos os fornecedores
        return Fornecedor.objects.filter(user=user)  # Fornecedor vê apenas sua própria conta


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):

        user = self.request.user
        if user.is_staff:
            return User.objects.all()  # Staff vê todos os usuários
        return User.objects.filter(id=user.id)  # Usuário vê apenas seu próprio perfil