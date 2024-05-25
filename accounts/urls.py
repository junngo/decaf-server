from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import CategoryViewSet, AccountViewSet, TransactionViewSet

router = DefaultRouter()
router.register(r'categories', CategoryViewSet, basename='category')
router.register(r'account', AccountViewSet, basename='account')
router.register(r'transactions', TransactionViewSet)

urlpatterns = [
    path('', include(router.urls)),
]
