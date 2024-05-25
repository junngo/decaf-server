from rest_framework import viewsets, status
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from .models import Category, Account, Transaction
from .serializers import CategorySerializer, AccountSerializer, TransactionSerializer


class CategoryViewSet(viewsets.ModelViewSet):
    serializer_class = CategorySerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        # Ensure that users see only their own categories
        return Category.objects.filter(user=self.request.user, deleted=False)

    def perform_create(self, serializer):
        # Automatically assign the logged-in user as the category's user
        serializer.save(user=self.request.user)


class AccountViewSet(viewsets.ModelViewSet):
    serializer_class = AccountSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        # Filter out deleted accounts
        return Account.objects.filter(user=self.request.user, deleted=False)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)

    def destroy(self, request, *args, **kwargs):
        """ Soft delete an account by setting the 'deleted' flag instead of actual deletion. """
        account = self.get_object()
        account.deleted = True
        account.save()
        return Response(status=status.HTTP_204_NO_CONTENT)


class TransactionViewSet(viewsets.ModelViewSet):
    queryset = Transaction.objects.all()
    serializer_class = TransactionSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return self.queryset.filter(user=self.request.user, deleted=False)

    def perform_create(self, serializer):
        serializer.save(user=self.request.user)
    
    def destroy(self, request, *args, **kwargs):
        transaction = self.get_object()
        transaction.deleted = True
        transaction.save()
        transaction.entries.all().update(deleted=True)
        return Response(status=status.HTTP_204_NO_CONTENT)
