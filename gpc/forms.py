from django import forms

from gpc.models import Utente, Profissional, Ato, Receita, Prescricao


class UtenteForm(forms.ModelForm):
    class Meta:
        model = Utente
        fields = ['nome', 'apelido', 'data_nascimento', 'morada', 'alergias', 'condicoes_cronicas', 'grupo_sanguineo']

class ProfissionalForm(forms.ModelForm):
    class Meta:
        model = Profissional
        fields = ['nome', 'apelido', 'profissao']

class AtoForm(forms.ModelForm):
    class Meta:
        model = Ato
        fields = ['utente', 'profissional', 'tipo', 'descricao']

class ReceitaForm(forms.ModelForm):
    class Meta:
        model = Receita
        fields = ['utente', 'profissional', 'prescricoes', 'data_validade', 'descricao']
        widgets = {
            'prescricoes': forms.CheckboxSelectMultiple,
        }


class PrescricaoForm(forms.ModelForm):
    class Meta:
        model = Prescricao
        fields = ['medicamento', 'quantidade', 'descricao']