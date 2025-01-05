from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import AtoViewSet, PrescricaoViewSet, ReceitaViewSet, UtenteViewSet, ProfissionalViewSet, gpc_html

router = DefaultRouter()
router.register(r'atos', AtoViewSet)
router.register(r'prescricoes', PrescricaoViewSet)
router.register(r'receitas', ReceitaViewSet)
router.register(r'utentes', UtenteViewSet)
router.register(r'profissionais', ProfissionalViewSet)

urlpatterns = [
    path('api/', include(router.urls)),
    path('', gpc_html, name='gpc_html'),
]
