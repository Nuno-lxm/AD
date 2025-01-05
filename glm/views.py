from datetime import datetime

from django.core.exceptions import ValidationError
from django.http import JsonResponse
from django.shortcuts import render, redirect, get_object_or_404
from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.views import APIView
import requests

from .forms import FornecedorForm, MedicamentoForm, EncomendaForm
from .models import Encomenda, Medicamento, Fornecedor
from .serializers import EncomendaSerializer, MedicamentoSerializer, FornecedorSerializer

FORNECEDORES_API_URL = 'http://127.0.0.1:8000/glm/api/fornecedores/'
MEDICAMENTOS_API_URL = 'http://127.0.0.1:8000/glm/api/medicamentos/'
ENCOMENDAS_API_URL = 'http://127.0.0.1:8000/glm/api/encomendas/'

def glm_client(request):
    fornecedores = requests.get(FORNECEDORES_API_URL).json()
    medicamentos = requests.get(MEDICAMENTOS_API_URL).json()
    encomendas = Encomenda.objects.select_related('medicamento').all()

    if request.method == 'POST':
        if 'new-supplier' in request.POST:
            form = FornecedorForm(request.POST)
            if form.is_valid():
                form.save()
                return redirect('glm_client')  # Redireciona para a mesma página
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
        'encomendas': encomendas
    }

    return render(request, 'glm/glm_client.html', context)

def adicionar_fornecedor(request):
    if request.method == 'POST':
        form = FornecedorForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('glm_client')  # Redireciona para a página principal
    else:
        form = FornecedorForm()

    return render(request, 'glm/fornecedores/adicionar_fornecedor.html', {'form': form})

def adicionar_medicamento(request):
    if request.method == 'POST':
        form = MedicamentoForm(request.POST)
        if form.is_valid():
            form.save()
            return redirect('glm_client')
    else:
        form = MedicamentoForm()
    return render(request, 'glm/medicamentos/adicionar_medicamento.html', {'form': form})

def adicionar_encomenda(request):
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

    return render(request, 'glm/encomendas/adicionar_encomenda.html', {'form': form})

def editar_fornecedor(request, id):
    fornecedor = get_object_or_404(Fornecedor, id=id)
    if request.method == 'POST':
        form = FornecedorForm(request.POST, instance=fornecedor)
        if form.is_valid():
            form.save()
            return redirect('glm_client')
    else:
        form = FornecedorForm(instance=fornecedor)
    return render(request, 'glm/fornecedores/editar_fornecedor.html', {'form': form})

def apagar_fornecedor(request, id):
    fornecedor = get_object_or_404(Fornecedor, id=id)
    if request.method == 'POST':
        fornecedor.delete()
        return redirect('glm_client')  # Isso depende de sua implementação
    return render(request, 'glm/confirmar_apagar.html', {'fornecedor': fornecedor})

# Medicamento
def editar_medicamento(request, id):
    medicamento = get_object_or_404(Medicamento, id=id)
    if request.method == 'POST':
        form = MedicamentoForm(request.POST, instance=medicamento)
        if form.is_valid():
            form.save()
            return redirect('glm_client')  # Isso depende de sua implementação
    else:
        form = MedicamentoForm(instance=medicamento)
    return render(request, 'glm/medicamentos/editar_medicamento.html', {'form': form})

def apagar_medicamento(request, id):
    medicamento = get_object_or_404(Medicamento, id=id)
    if request.method == 'POST':
        medicamento.delete()
        return redirect('glm_client')  # Isso depende de sua implementação
    return render(request, 'glm/confirmar_apagar.html', {'medicamento': medicamento})

# Encomenda
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

def apagar_encomenda(request, id):
    encomenda = get_object_or_404(Encomenda, id=id)
    if request.method == 'POST':
        encomenda.delete()
        return redirect('glm_client')
    return render(request, 'glm/confirmar_apagar.html', {'encomenda': encomenda})

# View para Fornecedor
def fornecedor_detalhes(request, id):
    fornecedor = get_object_or_404(Fornecedor, id=id)
    encomendas = Encomenda.objects.filter(fornecedor=fornecedor)
    medicamentos = Medicamento.objects.filter(fornecedor=fornecedor)
    return render(request, 'glm/fornecedores/detalhes_fornecedor.html', {'fornecedor': fornecedor, 'encomendas': encomendas, 'medicamentos': medicamentos})

# View para Medicamento
def medicamento_detalhes(request, id):
    medicamento = get_object_or_404(Medicamento, id=id)
    return render(request, 'glm/medicamentos/detalhes_medicamento.html', {'medicamento': medicamento})

# View para Encomenda
def encomenda_detalhes(request, id):
    encomenda = get_object_or_404(Encomenda, id=id)
    return render(request, 'glm/encomendas/detalhes_encomenda.html', {'encomenda': encomenda})

class EncomendaViewSet(viewsets.ModelViewSet):
    queryset = Encomenda.objects.all()
    serializer_class = EncomendaSerializer

class MedicamentoViewSet(viewsets.ModelViewSet):
    queryset = Medicamento.objects.all()
    serializer_class = MedicamentoSerializer

class FornecedorViewSet(viewsets.ModelViewSet):
    queryset = Fornecedor.objects.all()
    serializer_class = FornecedorSerializer


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

        try:
            medicamento = Medicamento.objects.get(id=medicamento_id)

            if medicamento.stock < quantidade:
                return Response({"error": "Stock insuficiente para realizar a dispensa."},
                                status=status.HTTP_400_BAD_REQUEST)

            medicamento.stock -= quantidade
            medicamento.save()
            return Response({"message": "Stock atualizado com sucesso."}, status=status.HTTP_200_OK)
        except Medicamento.DoesNotExist:
            return Response({"error": "Medicamento não encontrado."}, status=status.HTTP_404_NOT_FOUND)