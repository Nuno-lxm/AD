from django.urls import path, include
from rest_framework.routers import DefaultRouter
from django.contrib.auth import views as auth_views

from glm.views import user_detalhes, apagar_user
from .views import AtoViewSet, PrescricaoViewSet, ReceitaViewSet, UtenteViewSet, ProfissionalViewSet, gpc_client, \
    login_gpc_view, register_gpc_user, apagar_utente, apagar_profissional, apagar_ato, apagar_prescricao, \
    apagar_receita, adicionar_utente, editar_utente, detalhes_utente, adicionar_profissional, editar_profissional, \
    detalhes_profissional, adicionar_ato, editar_ato, detalhes_ato, adicionar_receita, editar_receita, detalhes_receita, \
    adicionar_prescricao, editar_prescricao, detalhes_prescricao

router = DefaultRouter()
router.register(r'atos', AtoViewSet)
router.register(r'prescricoes', PrescricaoViewSet)
router.register(r'receitas', ReceitaViewSet)
router.register(r'utentes', UtenteViewSet)
router.register(r'profissionais', ProfissionalViewSet)

urlpatterns = [
    path('api/', include(router.urls)),
    path('', gpc_client, name='gpc_client'),

    # URLs para Utentes
    path('adicionar_utente/', adicionar_utente, name='adicionar_utente'),
    path('editar_utente/<uuid:pk>/', editar_utente, name='editar_utente'),
    path('detalhes_utente/<uuid:pk>/', detalhes_utente, name='detalhes_utente'),
    path('utente/apagar/<uuid:id>/', apagar_utente, name='apagar_utente'),

    # URLs para Profissionais
    path('adicionar_profissional/', adicionar_profissional, name='adicionar_profissional'),
    path('editar_profissional/<uuid:pk>/', editar_profissional, name='editar_profissional'),
    path('detalhes_profissional/<uuid:pk>/', detalhes_profissional, name='detalhes_profissional'),
    path('profissional/apagar/<uuid:id>/', apagar_profissional, name='apagar_profissional'),

    # URLs para Atos
    path('adicionar_ato/', adicionar_ato, name='adicionar_ato'),
    path('editar_ato/<uuid:pk>/', editar_ato, name='editar_ato'),
    path('detalhes_ato/<uuid:pk>/', detalhes_ato, name='detalhes_ato'),
    path('ato/apagar/<uuid:id>/', apagar_ato, name='apagar_ato'),

    # URLs para Receitas
    path('adicionar_receita/', adicionar_receita, name='adicionar_receita'),
    path('editar_receita/<uuid:pk>/', editar_receita, name='editar_receita'),
    path('detalhes_receita/<uuid:pk>/', detalhes_receita, name='detalhes_receita'),
    path('receita/apagar/<uuid:id>/', apagar_receita, name='apagar_receita'),

    # URLs para Prescrições
    path('adicionar_prescricao/', adicionar_prescricao, name='adicionar_prescricao'),
    path('editar_prescricao/<uuid:pk>/', editar_prescricao, name='editar_prescricao'),
    path('detalhes_prescricao/<uuid:pk>/', detalhes_prescricao, name='detalhes_prescricao'),
    path('prescricao/apagar/<uuid:id>/', apagar_prescricao, name='apagar_prescricao'),

    # URLs para User
    path('login/', login_gpc_view, name='login_gpc'),
    path('logout/', auth_views.LogoutView.as_view(), name='logout'),
    path('register/', register_gpc_user, name='register_gpc'),

    path('user/detalhes/<int:user_id>/', user_detalhes, name='detalhes_user'),
    path('users/apagar/<int:user_id>/', apagar_user, name='apagar_user'),
]
