# Generated by Django 5.1.4 on 2025-01-04 19:10

import django.db.models.deletion
import uuid
from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('glm', '0002_encomenda'),
        ('gpc', '0001_initial'),
    ]

    operations = [
        migrations.CreateModel(
            name='Prescricao',
            fields=[
                ('id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False)),
                ('quantidade', models.PositiveIntegerField()),
                ('descricao', models.TextField(blank=True, null=True)),
                ('medicamento', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='glm.medicamento')),
            ],
        ),
        migrations.CreateModel(
            name='Receita',
            fields=[
                ('id', models.UUIDField(default=uuid.uuid4, editable=False, primary_key=True, serialize=False)),
                ('data_emissao', models.DateTimeField(auto_now_add=True)),
                ('data_validade', models.DateField()),
                ('descricao', models.TextField(blank=True, null=True)),
                ('prescricoes', models.ManyToManyField(to='gpc.prescricao')),
                ('profissional', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='gpc.profissional')),
                ('utente', models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='gpc.utente')),
            ],
        ),
    ]
