from django.contrib.auth import get_user_model, authenticate
from rest_framework import serializers
from rest_framework_simplejwt.serializers import TokenRefreshSerializer
from rest_framework_simplejwt.tokens import RefreshToken


User = get_user_model()

class UserRegistrationSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('username', 'email', 'password')
        extra_kwargs = {
            'password': {'write_only': True},
            'email': {'required': True},
        }

    def create(self, validated_data):
        user = User.objects.create_user(
            username=validated_data['username'],
            email=validated_data['email'],
            password=validated_data['password']
        )
        return user

    def validate_email(self, value):
        if User.objects.filter(email=value).exists():
            raise serializers.ValidationError("This email is already in use.")
        return value

    def validate_username(self, value):
        return value


class UserSignInSerializer(serializers.Serializer):
    username = serializers.CharField(label="Username or Email")
    password = serializers.CharField(style={'input_type': 'password'}, trim_whitespace=False)

    def validate(self, data):
        user = authenticate(username=data['username'], password=data['password'])
        if user is not None:
            return {'user': user}
        raise serializers.ValidationError("Invalid credentials")


class CustomTokenRefreshSerializer(TokenRefreshSerializer):
    def validate(self, attrs):
        data = super().validate(attrs)
        try:
            fresh_token = data['refresh']
            token = RefreshToken(fresh_token)
            user_id = token['user_id']
            user = User.objects.get(id=user_id)
            
            data['user'] = {
                'username': user.username,
            }

        except User.DoesNotExist:
            raise serializers.ValidationError('User does not exist')
        except KeyError:
            raise serializers.ValidationError('Malformed token - no user_id field')
        except Exception as e:
            raise serializers.ValidationError(f'Unknown error: {str(e)}')

        return data
