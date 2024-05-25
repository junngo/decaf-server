from rest_framework import serializers
from .models import Category, Account, Entry, Transaction

class CategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = Category
        fields = '__all__'
        read_only_fields = ['user']

    def __init__(self, *args, **kwargs):
        super(CategorySerializer, self).__init__(*args, **kwargs)
        request = self.context.get('request', None)
        if request.method in ['GET', 'PUT', 'PATCH', 'POST']:
            allowed_fields = {'id', 'name', 'type'}
            for field_name in list(self.fields):
                if field_name not in allowed_fields:
                    self.fields.pop(field_name)

    def create(self, validated_data):
        validated_data['user'] = self.context['request'].user
        return super().create(validated_data)


class AccountSerializer(serializers.ModelSerializer):
    class Meta:
        model = Account
        fields = ['id', 'user', 'name', 'type', 'created_at']
        read_only_fields = ['user', 'deleted']  # prevent API clients from directly setting these fields

    def create(self, validated_data):
        validated_data['user'] = self.context['request'].user
        return super().create(validated_data)


class EntrySerializer(serializers.ModelSerializer):
    class Meta:
        model = Entry
        fields = ['id', 'account', 'amount', 'type', 'category', 'date']


class TransactionSerializer(serializers.ModelSerializer):
    entries = EntrySerializer(many=True)

    class Meta:
        model = Transaction
        fields = ['id', 'description', 'date', 'user', 'entries', 'deleted', 'created_at', 'updated_at']
        read_only_fields = ['user', 'created_at', 'updated_at']

    def create(self, validated_data):
        entries_data = validated_data.pop('entries', [])
        transaction = Transaction.objects.create(**validated_data, user=self.context['request'].user)
        for entry_data in entries_data:
            Entry.objects.create(transaction=transaction, **entry_data)
        return transaction

    def update(self, instance, validated_data):
        entries_data = validated_data.pop('entries', [])
        instance.description = validated_data.get('description', instance.description)
        instance.date = validated_data.get('date', instance.date)
        instance.deleted = validated_data.get('deleted', instance.deleted)
        instance.save()

        # Handle updating entries
        for entry_data in entries_data:
            entry_id = entry_data.get('id', None)
            if entry_id:
                entry = Entry.objects.get(id=entry_id, transaction=instance)
                for attr, value in entry_data.items():
                    setattr(entry, attr, value)
                entry.save()
            else:
                # If no id, create a new entry
                Entry.objects.create(transaction=instance, **entry_data)

        return instance
