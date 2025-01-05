from django.shortcuts import render
from rest_framework import viewsets, status
from rest_framework.response import Response
from .models import Prescricao, Receita, Ato, Utente, Profissional
from .serializers import PrescricaoSerializer, ReceitaSerializer, AtoSerializer, UtenteSerializer, \
    ProfissionalSerializer
from .utils import enviar_atualizacao_para_glm

def gpc_html(request):
    return render(request, 'gpc/gpc.html')

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