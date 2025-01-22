from django.urls import path, include
from rest_framework.routers import DefaultRouter
from django.contrib.auth import views as auth_views
from .views import EncomendaViewSet, MedicamentoViewSet, FornecedorViewSet, AtualizarMedicamentoView, glm_client, \
    adicionar_fornecedor, adicionar_medicamento, adicionar_encomenda, editar_fornecedor, apagar_fornecedor, \
    editar_medicamento, apagar_medicamento, editar_encomenda, apagar_encomenda, fornecedor_detalhes, encomenda_detalhes, \
    medicamento_detalhes, confirmar_encomenda, register_glm_user, UserViewSet, apagar_user, user_detalhes, login_glm_view, \
    get_fornecedores

router = DefaultRouter()
router.register(r'encomendas', EncomendaViewSet)
router.register(r'medicamentos', MedicamentoViewSet)
router.register(r'fornecedores', FornecedorViewSet)
router.register(r'users', UserViewSet)

urlpatterns = [
    path('api/', include(router.urls)),
    path('atualizar-medicamento/', AtualizarMedicamentoView.as_view(), name='atualizar-medicamento'),
    path('', glm_client, name='glm_client'),
    path('adicionar-fornecedor/', adicionar_fornecedor, name='adicionar_fornecedor'),
    path('adicionar-medicamento/', adicionar_medicamento, name='adicionar_medicamento'),
    path('adicionar-encomenda/', adicionar_encomenda, name='adicionar_encomenda'),

    path('fornecedor/detalhes/<uuid:id>/', fornecedor_detalhes, name='detalhes_fornecedor'),
    path('fornecedor/editar/<uuid:id>/', editar_fornecedor, name='editar_fornecedor'),
    path('fornecedor/apagar/<uuid:id>/', apagar_fornecedor, name='apagar_fornecedor'),

    # URLs para Medicamento
    path('medicamento/detalhes/<uuid:id>/', medicamento_detalhes, name='detalhes_medicamento'),
    path('medicamento/editar/<uuid:id>/', editar_medicamento, name='editar_medicamento'),
    path('medicamento/apagar/<uuid:id>/', apagar_medicamento, name='apagar_medicamento'),

    # URLs para Encomenda
    path('encomenda/detalhes/<uuid:id>/', encomenda_detalhes, name='detalhes_encomenda'),
    path('encomenda/editar/<uuid:id>/', editar_encomenda, name='editar_encomenda'),
    path('encomenda/apagar/<uuid:id>/', apagar_encomenda, name='apagar_encomenda'),
    path('confirmar_encomenda/<uuid:id>/', confirmar_encomenda, name='confirmar_encomenda'),
    path('get_fornecedores/<uuid:medicamento_id>/', get_fornecedores, name='get_fornecedores'),

    #URLs para User
    path('login/', login_glm_view, name='login_glm'),
    path('logout/', auth_views.LogoutView.as_view(), name='logout'),
    path('register/', register_glm_user, name='register_glm'),
# URLs para User
    path('user/detalhes/<int:user_id>/', user_detalhes, name='detalhes_user'),
    path('users/apagar/<int:user_id>/', apagar_user, name='apagar_user'),

] + router.urls
