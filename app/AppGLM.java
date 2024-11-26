package projeto.app;


import projeto.model.classes.Fornecedor;
import projeto.model.classes.Medicamento;
import projeto.model.glm.APIGLM;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class AppGLM {
    public static void main(String[] args) {
        try {
            // Conectar ao serviço GPC via RMI
            APIGLM glm = (APIGLM) Naming.lookup("rmi://localhost:50001/GLM");

            // Criação de scanner para leitura de inputs do usuário
            Scanner scanner = new Scanner(System.in);

            // 1. Criar fornecedores
            criarFornecedores(glm);

            // 2. Criar utentes
            criarMedicamentos(glm);

            /*Fornecedor fornecedorabs = glm.createFornecedor("acb");
            Medicamento medicamentoabs = glm.createMedicamento("abc", fornecedorabs, "aaa", 20, 5);

            System.out.println(fornecedorabs);
            System.out.println(medicamentoabs.getId());

            System.out.println(glm.consomeMedicamento(medicamentoabs.getId(),1));*/

            System.out.println(glm.getMedicamento("d53c4083-cb25-40ca-b49a-f68597429339"));



            // Fechar o scanner
            scanner.close();

        } catch (RemoteException e) {
            System.out.println("Erro de RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void criarFornecedores(APIGLM glm) {
        try {
            // Lista de nomes de fornecedores
            String[] nomesFornecedores = {
                    "Fornecedor A",
                    "Fornecedor B",
                    "Fornecedor C",
                    "Fornecedor D",
                    "Fornecedor E",
                    "Fornecedor F",
                    "Fornecedor G",
                    "Fornecedor H",
                    "Fornecedor I",
                    "Fornecedor J"
            };

            // Criando fornecedores a partir da lista de nomes
            for (String nome : nomesFornecedores) {
                // Criar o fornecedor
                Fornecedor fornecedor = glm.createFornecedor(nome);

                // Exibir o fornecedor criado no console
                System.out.println("Fornecedor criado com sucesso: " + fornecedor);
            }

            pressionarEnterParaContinuar();
        } catch (Exception e) {
            System.out.println("Erro ao criar os fornecedores: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private static void criarMedicamentos(APIGLM glm) {
        try {
            // Dados simulados para criação de medicamentos
            String[][] dadosMedicamentos = {
                    {"Paracetamol", "Fornecedor A", "Analgésico e antitérmico", "20", "5"},
                    {"Ibuprofeno", "Fornecedor B", "Anti-inflamatório não esteroide", "20", "5"},
                    {"Amoxicilina", "Fornecedor C", "Antibiótico para infecções bacterianas", "20", "5"},
                    {"Diclofenaco", "Fornecedor D", "Anti-inflamatório e analgésico", "20", "5"},
                    {"Omeprazol", "Fornecedor E", "Redução da acidez gástrica", "20", "5"},
                    {"Aspirina", "Fornecedor F", "Analgésico e anticoagulante", "20", "5"},
                    {"Metformina", "Fornecedor G", "Controle de glicose no diabetes tipo 2", "20", "5"},
                    {"Simvastatina", "Fornecedor H", "Redução de colesterol","20", "5"},
                    {"Losartana", "Fornecedor I", "Controle da hipertensão", "20", "5"},
                    {"Salbutamol", "Fornecedor J", "Tratamento de asma e broncoespasmos", "20", "5"}
            };

            // Criando medicamentos a partir dos dados simulados
            for (String[] dados : dadosMedicamentos) {
                String nome = dados[0];
                String nomeFornecedor = dados[1];
                String descricao = dados[2];
                String stockString = dados[3];
                String thresholdString = dados[4];
                int stock = Integer.parseInt(stockString);
                int threshold = Integer.parseInt(thresholdString);


                // Procurar fornecedor pelo nome
                List<String> idsFornecedores = glm.procuraFornecedor(nomeFornecedor);

                // Considera o primeiro fornecedor encontrado, você pode fazer isso dependendo da sua lógica
                String fornecedorId = idsFornecedores.get(0);

                // Obter o objeto Fornecedor pelo ID
                Fornecedor fornecedor = glm.getFornecedor(fornecedorId);  // Supondo que exista um método getFornecedor

                // Criar o medicamento
                Medicamento medicamento = glm.createMedicamento(nome, fornecedor, descricao, stock, threshold);


                // Exibir o medicamento criado no console
                System.out.println("Medicamento criado com sucesso: " + medicamento);
            }

            pressionarEnterParaContinuar();
        } catch (Exception e) {
            System.out.println("Erro ao criar os medicamentos: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Função para esperar o usuário pressionar Enter
    private static void pressionarEnterParaContinuar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();  // Espera que o usuário pressione Enter
    }

}
