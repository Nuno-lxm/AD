from django.contrib.auth.models import User
from rest_framework import serializers
from .models import Encomenda, Medicamento, Fornecedor

class EncomendaSerializer(serializers.ModelSerializer):
    class Meta:
        model = Encomenda
        fields = '__all__'

class MedicamentoSerializer(serializers.ModelSerializer):
    fornecedores = serializers.PrimaryKeyRelatedField(many=True, queryset=Fornecedor.objects.all())

    class Meta:
        model = Medicamento
        fields = ['id', 'nome', 'descricao', 'fornecedores', 'stock', 'threshold']

class FornecedorSerializer(serializers.ModelSerializer):
    class Meta:
        model = Fornecedor
        fields = '__all__'


class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'username', 'password',]
        extra_kwargs = {
            'password': {'write_only': True}
        }

    def create(self, validated_data):
        user = User.objects.create_user(**validated_data)
        return user