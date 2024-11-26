package projeto.app;

import projeto.model.Excecao;
import projeto.model.classes.Fornecedor;
import projeto.model.glm.APIGLM;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TesteGLM {
    public static void main(String[] args) {
        try {
            APIGLM glm = (APIGLM) Naming.lookup("rmi://localhost:50001/GLM");

            glm.createFornecedor("meddrop");

            Fornecedor fornecedor = glm.getFornecedor(glm.procuraFornecedor("meddrop", glm.getFornecedores()).getFirst());

            glm.createMedicamento("brufen", fornecedor, "cura febre");

            String id_brufen = glm.procuraMedicamento("brufen", glm.getMedicamentos()).getFirst();

            System.out.println(glm.getMedicamento(id_brufen));

            glm.setStock(id_brufen, 5);

            System.out.println(glm.getMedicamento(id_brufen));

            System.out.println(glm.createMedicamento("benuron", fornecedor, "cura dor de axc", 50, 10));

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Erro de RMI: " + e.getMessage());
        } catch (Excecao e) {
            throw new RuntimeException(e);
        }
    }
}




/*
Gestor GLM
criar fornecedor
criar medicamento
criar encomenda

Fornecedor
confirma encomenda

Gestor GPC
criar profissional
criar utente

Profissional
criar ato (alerta de stocks baixo e outro é stock insuficiente/tem de ser separado, erro/)
criar receita/prescricao

Utente
ver estatisticas

Profissional
ver estatisticas

Fornecedor
ver estatisticas

Gestor GLM
altera informacao fornecedor
altera informacao medicamento
ver estatiticas gerais

Gestor GPC
altera informcao utente
altera informcao profissional
ver estatitsicas gerais
 */