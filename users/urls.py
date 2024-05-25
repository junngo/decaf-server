from django.urls import path
from .views import CreateUserView, CustomTokenObtainPairView, LogoutAndBlacklistRefreshTokenForUserView, CustomTokenRefreshView

urlpatterns = [
    path('register/', CreateUserView.as_view(), name='register'),
    path('login/', CustomTokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('logout/', LogoutAndBlacklistRefreshTokenForUserView.as_view(), name='token_blacklist'),
    path('refresh/', CustomTokenRefreshView.as_view(), name='token_refresh'),
]
