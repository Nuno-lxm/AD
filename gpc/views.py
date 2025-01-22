from django.contrib.auth import login
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
from django.contrib.auth.models import User
from django.contrib.auth.views import LoginView
from django.http import HttpResponseForbidden
from django.shortcuts import render, redirect, get_object_or_404
from django.urls import reverse
from rest_framework import viewsets, status
from rest_framework.response import Response

from glm.serializers import UserSerializer
from .forms import UtenteForm, ProfissionalForm, AtoForm, ReceitaForm, PrescricaoForm
from .models import Prescricao, Receita, Ato, Utente, Profissional
from .serializers import PrescricaoSerializer, ReceitaSerializer, AtoSerializer, UtenteSerializer, \
    ProfissionalSerializer
from .utils import enviar_atualizacao_para_glm


def gpc_client(request):

    utentes = None
    profissionais = None
    atos = None
    receitas = None
    prescricoes = None

    if request.user.is_authenticated:
        if request.user.is_staff:
            utentes = Utente.objects.all()
            profissionais = Profissional.objects.all()
            atos = Ato.objects.all()
            receitas = Receita.objects.all()
            prescricoes = Prescricao.objects.all()

        else:
            utente = getattr(request.user, 'utente', None)
            profissional = getattr(request.user, 'profissional', None)
            if utente:
                utentes = Utente.objects.filter(user=request.user)
                atos = Ato.objects.filter(utente=utente)
                receitas = Receita.objects.filter(utente=utente)

            elif profissional:
                profissionais = Profissional.objects.filter(user=request.user)
                atos = Ato.objects.filter(profissional=profissional)
                receitas = Receita.objects.filter(profissional=profissional)
                prescricoes = Prescricao.objects.filter(profissional=profissional)

        return render(request, 'gpc/gpc_client.html', {
            'utentes': utentes,
            'profissionais': profissionais,
            'atos': atos,
            'receitas': receitas,
            'prescricoes': prescricoes,
        })
    else:
        return render(request, 'gpc/gpc_client.html')


def adicionar_utente(request):
    if not request.user.is_authenticated:
        return HttpResponseForbidden("Você precisa estar logado para criar um utente.")
    if request.method == "POST":
        form = UtenteForm(request.POST)
        user_id = request.POST.get('user')
        if form.is_valid():
            print('top')
            utente = form.save(commit=False)
            user = User.objects.get(id=user_id)
            utente.user = user
            utente.save()
            return redirect('gpc_client')
        else:
            print(form.errors)
    else:
        form = UtenteForm()
    users_without = User.objects.filter(utente__isnull=True).filter(profissional__isnull=True)
    return render(request, 'gpc/utentes/adicionar_utente.html', {'form': form, 'users': users_without})


def editar_utente(request, pk):
    utente = get_object_or_404(Utente, pk=pk)
    if request.method == "POST":
        form = UtenteForm(request.POST, instance=utente)
        if form.is_valid():
            form.save()
            return redirect('detalhes_utente', pk=utente.pk)
    else:
        form = UtenteForm(instance=utente)
    return render(request, 'gpc/utentes/editar_utente.html', {'form': form})

def detalhes_utente(request, pk):
    utente = get_object_or_404(Utente, pk=pk)
    return render(request, 'gpc/utentes/detalhes_utente.html', {'utente': utente})

def adicionar_profissional(request):
    if not request.user.is_authenticated:
        return HttpResponseForbidden("Você precisa estar logado para criar um profissional.")
    if request.method == "POST":
        form = ProfissionalForm(request.POST)
        user_id = request.POST.get('user')
        if form.is_valid():
            profissional = form.save(commit=False)
            user = User.objects.get(id=user_id)
            profissional.user = user
            profissional.save()
            return redirect('gpc_client')
    else:
        form = ProfissionalForm()
    users_without = User.objects.filter(utente__isnull=True).filter(profissional__isnull=True)
    return render(request, 'gpc/profissionais/adicionar_profissional.html', {'form': form, 'users': users_without})

def editar_profissional(request, pk):
    profissional = get_object_or_404(Profissional, pk=pk)
    if request.method == "POST":
        form = ProfissionalForm(request.POST, instance=profissional)
        if form.is_valid():
            form.save()
            return redirect('detalhes_profissional', pk=profissional.pk)
    else:
        form = ProfissionalForm(instance=profissional)
    return render(request, 'gpc/profissionais/editar_profissional.html', {'form': form})

def detalhes_profissional(request, pk):
    profissional = get_object_or_404(Profissional, pk=pk)
    return render(request, 'gpc/profissionais/detalhes_profissional.html', {'profissional': profissional})

def adicionar_ato(request):
    if not request.user.is_authenticated:
        return HttpResponseForbidden("Você precisa estar logado para criar um ato.")

    if request.method == "POST":
        form = AtoForm(request.POST)

        if form.is_valid():
            tipo_ato = form.cleaned_data.get('tipo')
            medicamento_codigo = form.cleaned_data.get('descricao')

            if tipo_ato == 'dispensa' and medicamento_codigo:
                try:
                    error = enviar_atualizacao_para_glm(medicamento_codigo, 1)
                    print('testest')
                    print(error)
                    if error['code'] == 0:
                        form.save()
                    elif error['code'] == 1:
                        return render(request, 'gpc/atos/adicionar_ato.html', {
                            'form': form,
                            'error': "Stock insuficiente para realizar a dispensa."
                        })
                    elif error['code'] == 2:
                        form.save()
                        return render(request, 'gpc/atos/adicionar_ato.html', {
                            'form': form,
                            'error': error['warning']
                        })
                    elif error['code'] == 3:
                        return render(request, 'gpc/atos/adicionar_ato.html', {
                            'form': form,
                            'error': error['error']
                        })
                except Exception as e:
                    return render(request, 'gpc/atos/adicionar_ato.html', {
                        'form': form,
                        'error': f"Erro ao atualizar medicamento no GLM: {str(e)}"
                    })
            return redirect('gpc_client')
    else:
        form = AtoForm()
    return render(request, 'gpc/atos/adicionar_ato.html', {'form': form})

def editar_ato(request, pk):
    ato = get_object_or_404(Ato, pk=pk)
    if request.method == "POST":
        form = AtoForm(request.POST, instance=ato)
        if form.is_valid():
            form.save()
            return redirect('detalhes_ato', pk=ato.pk)
    else:
        form = AtoForm(instance=ato)
    return render(request, 'gpc/atos/editar_ato.html', {'form': form})

def detalhes_ato(request, pk):
    ato = get_object_or_404(Ato, pk=pk)
    return render(request, 'gpc/atos/detalhes_ato.html', {'ato': ato})

def adicionar_receita(request):
    if not request.user.is_authenticated:
        return HttpResponseForbidden("Você precisa estar logado para criar uma receita.")
    if request.method == "POST":
        form = ReceitaForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('gpc_client')  # Supondo que você tenha uma URL para listar receitas
    else:
        form = ReceitaForm()
    prescricoes = Prescricao.objects.filter(receita__isnull=True)
    return render(request, 'gpc/receitas/adicionar_receita.html', {'form': form, 'prescricoes': prescricoes})

def editar_receita(request, pk):
    receita = get_object_or_404(Receita, pk=pk)
    if request.method == "POST":
        form = ReceitaForm(request.POST, instance=receita)
        if form.is_valid():
            form.save()
            return redirect('detalhes_receita', pk=receita.pk)
    else:
        form = ReceitaForm(instance=receita)
    prescricoes = Prescricao.objects.filter(receita__isnull=True) | Prescricao.objects.filter(receita=receita)
    return render(request, 'gpc/receitas/editar_receita.html', {'form': form, 'prescricoes': prescricoes})

def detalhes_receita(request, pk):
    receita = get_object_or_404(Receita, pk=pk)
    return render(request, 'gpc/receitas/detalhes_receita.html', {'receita': receita})

def adicionar_prescricao(request):
    if not request.user.is_authenticated:
        return HttpResponseForbidden("Você precisa estar logado para criar uma prescrição.")
    if request.method == "POST":
        form = PrescricaoForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('gpc_client')
    else:
        form = PrescricaoForm()
    return render(request, 'gpc/prescricoes/adicionar_prescricao.html', {'form': form})

def editar_prescricao(request, pk):
    prescricao = get_object_or_404(Prescricao, pk=pk)
    if request.method == "POST":
        form = PrescricaoForm(request.POST, instance=prescricao)
        if form.is_valid():
            form.save()
            return redirect('detalhes_prescricao', pk=prescricao.id)
    else:
        form = PrescricaoForm(instance=prescricao)
    return render(request, 'gpc/prescricoes/editar_prescricao.html', {'form': form})

def detalhes_prescricao(request, pk):
    prescricao = get_object_or_404(Prescricao, pk=pk)
    return render(request, 'gpc/prescricoes/detalhes_prescricao.html', {'prescricao': prescricao})

class PrescricaoViewSet(viewsets.ModelViewSet):
    queryset = Prescricao.objects.all()
    serializer_class = PrescricaoSerializer

class ReceitaViewSet(viewsets.ModelViewSet):
    queryset = Receita.objects.all()
    serializer_class = ReceitaSerializer

class UtenteViewSet(viewsets.ModelViewSet):
    queryset = Utente.objects.all()
    serializer_class = UtenteSerializer

class ProfissionalViewSet(viewsets.ModelViewSet):
    queryset = Profissional.objects.all()
    serializer_class = ProfissionalSerializer

class AtoViewSet(viewsets.ModelViewSet):
    queryset = Ato.objects.all()
    serializer_class = AtoSerializer

    def perform_create(self, serializer):
        tipo_ato = self.request.data.get('tipo')
        medicamento_codigo = self.request.data.get('descricao')

        if tipo_ato == 'dispensa' and medicamento_codigo:
            try:
                enviar_atualizacao_para_glm(medicamento_codigo, 1)
                serializer.save()
                return Response({"message": "Ato criado com sucesso e stock atualizado no GLM."}, status=status.HTTP_201_CREATED)
            except Exception as e:
                return Response({"error": str(e)}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)
        else:
            serializer.save()

class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer

def login_gpc_view(request):
    if request.method == 'POST':
        form = AuthenticationForm(request, data=request.POST)
        if form.is_valid():
            user = form.get_user()
            login(request, user)

            next_url = request.GET.get('next', reverse('gpc_client'))
            return redirect(next_url)
    else:
        form = AuthenticationForm()

    return render(request, 'registration/login_gpc.html', {'form': form})

def editar_utente(request, pk):
    utente = get_object_or_404(Utente, pk=pk)
    if request.method == "POST":
        form = UtenteForm(request.POST, instance=utente)
        if form.is_valid():
            form.save()
            return redirect('detalhes_utente', pk=utente.pk)
    else:
        form = UtenteForm(instance=utente)
    return render(request, 'gpc/utentes/editar_utente.html', {'form': form})

def detalhes_utente(request, pk):
    utente = get_object_or_404(Utente, pk=pk)
    return render(request, 'gpc/utentes/detalhes_utente.html', {'utente': utente})


def register_gpc_user(request):
    if request.method == 'POST':
        user_form = UserCreationForm(request.POST)
        if user_form.is_valid():
            user = user_form.save(commit=False)
            user.set_password(user_form.cleaned_data['password1'])
            user.save()

            login(request, user)

            return redirect('login_gpc')
    else:
        user_form = UserCreationForm()

    return render(request, 'registration/register_gpc.html', {'user_form': user_form})


def apagar_utente(request, id):
    utente = get_object_or_404(Utente, id=id)
    if request.method == 'POST':
        utente.delete()
        return redirect('gpc_client')
    return render(request, 'gpc/confirmar_apagar.html', {'utente': utente})


def apagar_profissional(request, id):
    profissional = get_object_or_404(Profissional, id=id)
    if request.method == 'POST':
        profissional.delete()
        return redirect('gpc_client')
    return render(request, 'gpc/confirmar_apagar.html', {'profissional': profissional})


def apagar_ato(request, id):
    ato = get_object_or_404(Ato, id=id)
    if request.method == 'POST':
        ato.delete()
        return redirect('gpc_client')
    return render(request, 'gpc/confirmar_apagar.html', {'ato': ato})


def apagar_prescricao(request, id):
    prescricao = get_object_or_404(Prescricao, id=id)
    if request.method == 'POST':
        prescricao.delete()
        return redirect('gpc_client')
    return render(request, 'gpc/confirmar_apagar.html', {'prescricao': prescricao})


def apagar_receita(request, id):
    receita = get_object_or_404(Receita, id=id)
    if request.method == 'POST':
        receita.delete()
        return redirect('gpc_client')
    return render(request, 'gpc/confirmar_apagar.html', {'receita': receita})




