# Generated by Django 5.1.4 on 2025-01-20 23:45

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('glm', '0004_fornecedor_user'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='medicamento',
            name='fornecedor',
        ),
        migrations.AddField(
            model_name='medicamento',
            name='fornecedores',
            field=models.ManyToManyField(to='glm.fornecedor'),
        ),
    ]
