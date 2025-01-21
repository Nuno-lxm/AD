from django.shortcuts import redirect
from django.conf import settings

class DynamicLoginRedirectMiddleware:
    def __init__(self, get_response):
        self.get_response = get_response

    def __call__(self, request):
        if not request.user.is_authenticated and request.path not in settings.LOGIN_EXEMPT_URLS:
            if request.path.startswith('/glm/'):
                return redirect('login_glm')
            elif request.path.startswith('/gpc/'):
                return redirect('login_gpc')
        return self.get_response(request)