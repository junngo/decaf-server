from rest_framework.response import Response

class ResponseFormattingMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        response = self.get_response(request)

        if isinstance(response, Response):
            if response.status_code < 400:
                response.data = {'data': response.data}
            else:
                response.data = {
                    'errors': response.data,
                    'status': response.status_code
                }

        print(response.data)
        if not response.is_rendered:
                response.render()

        return response
