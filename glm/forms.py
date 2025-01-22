from django import forms
from django.contrib.auth.models import User

from .models import Fornecedor, Medicamento, Encomenda

class UserForm(forms.ModelForm):
    password = forms.CharField(widget=forms.PasswordInput)

    class Meta:
        model = User
        fields = ['username', 'email', 'password']

class FornecedorForm(forms.ModelForm):
    class Meta:
        model = Fornecedor
        fields = ['nome', 'contacto', 'email', 'user']

class FornecedorUserForm(forms.ModelForm):
    class Meta:
        model = Fornecedor
        fields = ['nome', 'contacto', 'email']

class MedicamentoForm(forms.ModelForm):
    class Meta:
        model = Medicamento
        fields = ['nome', 'descricao', 'fornecedores', 'stock', 'threshold']

class EncomendaForm(forms.ModelForm):
    class Meta:
        model = Encomenda
        fields = ['medicamento', 'fornecedor', 'quantidade']

    fornecedor = forms.ModelChoiceField(queryset=Fornecedor.objects.all(), empty_label="Selecione um fornecedor", required=False)
