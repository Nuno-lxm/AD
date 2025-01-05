from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status

class CustomApiRootView(APIView):
    def get(self, request, *args, **kwargs):
        return Response({
            "gpc": "http://127.0.0.1:8000/gpc/api/",
            "glm": "http://127.0.0.1:8000/glm/api/"
        }, status=status.HTTP_200_OK)