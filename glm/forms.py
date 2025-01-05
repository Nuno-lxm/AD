from django import forms
from .models import Fornecedor, Medicamento, Encomenda

class FornecedorForm(forms.ModelForm):
    class Meta:
        model = Fornecedor
        fields = ['nome', 'contacto', 'email']  # Os campos no modelo 'Fornecedor'

class MedicamentoForm(forms.ModelForm):
    class Meta:
        model = Medicamento
        fields = ['nome', 'descricao', 'fornecedor', 'stock', 'threshold']  # Campos no modelo 'Medicamento'

class EncomendaForm(forms.ModelForm):
    class Meta:
        model = Encomenda
        fields = ['medicamento', 'fornecedor', 'quantidade']
