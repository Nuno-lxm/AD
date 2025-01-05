from django.db import models
import uuid

from glm.models import Medicamento


class Utente(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    nome = models.CharField(max_length=100)
    apelido = models.CharField(max_length=100)
    data_nascimento = models.DateField()
    morada = models.TextField()
    alergias = models.TextField(blank=True, null=True)
    condicoes_cronicas = models.TextField(blank=True, null=True)
    grupo_sanguineo = models.CharField(max_length=3, blank=True, null=True)

    def __str__(self):
        return f"{self.nome} {self.apelido}"

class Profissional(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    nome = models.CharField(max_length=100)
    apelido = models.CharField(max_length=100)
    profissao = models.CharField(max_length=50, choices=[('Médico', 'Médico'), ('Enfermeiro', 'Enfermeiro'), ('Farmacêutico', 'Farmacêutico')])

    def __str__(self):
        return f"{self.nome} {self.apelido} ({self.profissao})"

class Ato(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    utente = models.ForeignKey(Utente, on_delete=models.CASCADE)
    profissional = models.ForeignKey(Profissional, on_delete=models.CASCADE)
    tipo = models.CharField(max_length=50)
    descricao = models.TextField()  # A descrição será o código do medicamento quando for do tipo "dispensa"
    data_hora = models.DateTimeField(auto_now_add=True)

    def __str__(self):
        return f"Ato {self.id}: {self.tipo} em {self.data_hora}"

class Prescricao(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    medicamento = models.ForeignKey(Medicamento, on_delete=models.CASCADE)
    quantidade = models.PositiveIntegerField()
    descricao = models.TextField(blank=True, null=True)

    def __str__(self):
        return f"Prescrição de {self.quantidade} {self.medicamento.nome}"

class Receita(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    utente = models.ForeignKey(Utente, on_delete=models.CASCADE)
    profissional = models.ForeignKey(Profissional, on_delete=models.CASCADE)
    prescricoes = models.ManyToManyField(Prescricao)
    data_emissao = models.DateTimeField(auto_now_add=True)
    data_validade = models.DateField()
    descricao = models.TextField(blank=True, null=True)

    def __str__(self):
        return f"Receita {self.id} para {self.utente.nome}"