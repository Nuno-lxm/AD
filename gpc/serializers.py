from rest_framework import serializers
from .models import Prescricao, Receita, Ato, Utente, Profissional

class PrescricaoSerializer(serializers.ModelSerializer):
    class Meta:
        model = Prescricao
        fields = '__all__'

class ReceitaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Receita
        fields = '__all__'


class AtoSerializer(serializers.ModelSerializer):
    descricao = serializers.CharField(required=False)

    class Meta:
        model = Ato
        fields = ['id', 'utente', 'profissional', 'tipo', 'descricao', 'data_hora']

    def validate(self, data):
        if data['tipo'] == 'dispensa' and not data.get('descricao'):
            raise serializers.ValidationError("Quando o tipo for 'dispensa', medicamento é obrigatório.")
        return data

class UtenteSerializer(serializers.ModelSerializer):
    class Meta:
        model = Utente
        fields = '__all__'

class ProfissionalSerializer(serializers.ModelSerializer):
    class Meta:
        model = Profissional
        fields = '__all__'

