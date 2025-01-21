from datetime import datetime

from django.contrib.auth.models import User
from django.core.exceptions import ValidationError
from django.db import models
import uuid

class Fornecedor(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    nome = models.CharField(max_length=100)
    contacto = models.CharField(max_length=15)
    email = models.EmailField()

    def __str__(self):
        return self.nome

class Medicamento(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    nome = models.CharField(max_length=100)
    descricao = models.TextField()
    fornecedores = models.ManyToManyField(Fornecedor, blank=True)
    stock = models.IntegerField()
    threshold = models.IntegerField()

    def __str__(self):
        return self.nome


class Encomenda(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    medicamento = models.ForeignKey(Medicamento, on_delete=models.CASCADE)
    fornecedor = models.ForeignKey(Fornecedor, on_delete=models.CASCADE)
    quantidade = models.PositiveIntegerField()
    data_encomenda = models.DateTimeField(auto_now_add=True)
    concluida = models.BooleanField(default=False)
    data_confirmacao = models.DateTimeField(null=True, blank=True)

    def clean(self):
        if self.quantidade <= 0:
            raise ValidationError("A quantidade deve ser maior que zero.")
        if self.medicamento is None:
            raise ValidationError("O medicamento não pode ser nulo.")
        if self.fornecedor is None:
            raise ValidationError("O fornecedor não pode ser nulo.")

    def confirmar(self):
        if not self.concluida:
            self.concluida = True
            self.data_confirmacao = datetime.now()  # Definindo a data de confirmação
            self.save()

    def __str__(self):
        return f"Encomenda {self.id} ({self.medicamento.nome})"