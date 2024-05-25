from django.contrib import admin
from .models import Category, Account, Transaction, Entry


admin.site.register(Category)
admin.site.register(Account)
admin.site.register(Transaction)
admin.site.register(Entry)
