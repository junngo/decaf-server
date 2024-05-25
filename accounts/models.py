from django.db import models
from django.utils import timezone
from users.models import Users


class Category(models.Model):
    class CategoryType(models.TextChoices):
        EXPENSE = 'EXPENSE', ('Expense')
        REVENUE = 'REVENUE', ('Revenue')

    name = models.CharField(max_length=255, null=False)
    user = models.ForeignKey(Users, on_delete=models.CASCADE, related_name='categories')
    type = models.CharField(max_length=7, choices=CategoryType.choices)
    deleted = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return self.name


class Account(models.Model):
    class AccountType(models.TextChoices):
        ASSET = 'ASSET', ('Asset')
        LIABILITY = 'LIABILITY', ('Liability')

    user = models.ForeignKey(Users, on_delete=models.CASCADE, related_name='accounts')
    name = models.CharField(max_length=100)
    type = models.CharField(max_length=10, choices=AccountType.choices)
    balance = models.DecimalField(max_digits=12, decimal_places=2, default=0.00)
    deleted = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return "self.name"


class Transaction(models.Model):
    description = models.TextField(blank=True)
    date = models.DateTimeField(default=timezone.now)
    user = models.ForeignKey(Users, on_delete=models.CASCADE, related_name='transactions')
    deleted = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"Transaction on {self.date.strftime('%Y-%m-%d')}"


class Entry(models.Model):
    class EntryType(models.TextChoices):
        DEBIT = 'DEBIT', ('Debit')
        CREDIT = 'CREDIT', ('Credit')

    account = models.ForeignKey(Account, on_delete=models.CASCADE, related_name='entries')
    transaction = models.ForeignKey(Transaction, on_delete=models.CASCADE, related_name='entries')
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    type = models.CharField(max_length=6, choices=EntryType.choices)
    category = models.ForeignKey(Category, on_delete=models.CASCADE, related_name='entries')
    date = models.DateTimeField(default=timezone.now)
    deleted = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.type} of {self.amount}"

    def save(self, *args, **kwargs):
        if not self.pk:  # Check if this is a new entry
            self.update_account_balance(self.amount, self.type)
        else:  # Entry is being updated
            old_entry = Entry.objects.get(pk=self.pk)
            if old_entry.amount != self.amount or old_entry.type != self.type:
                # Reverse the old effect
                self.update_account_balance(-old_entry.amount, old_entry.type)
                # Apply new effect
                self.update_account_balance(self.amount, self.type)
        super().save(*args, **kwargs)

    def delete(self, *args, **kwargs):
        # Reverse the balance effect when the entry is deleted
        self.update_account_balance(-self.amount, self.type)
        super().delete(*args, **kwargs)

    def update_account_balance(self, amount, type):
        if type == Entry.EntryType.DEBIT:
            self.account.balance -= amount  # Subtract for debits
        else:
            self.account.balance += amount  # Add for credits
        self.account.save()
