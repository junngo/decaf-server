# Generated by Django 5.0.6 on 2024-05-16 14:19

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('accounts', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='account',
            name='deleted',
            field=models.BooleanField(default=False),
        ),
    ]