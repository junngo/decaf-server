from django.contrib import admin
from .models import Category, Account, Transaction, Entry


class AccountAdmin(admin.ModelAdmin):
    list_display = ('user', 'name', 'type',)


admin.site.register(Category)
admin.site.register(Account, AccountAdmin)
admin.site.register(Transaction)
admin.site.register(Entry)
