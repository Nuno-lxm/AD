package projeto.app;

import projeto.model.classes.Ato;
import projeto.model.classes.Fornecedor;
import projeto.model.classes.Medicamento;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;
import projeto.model.glm.APIGLM;
import projeto.model.gpc.APIGPC;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppGPC {
    public static void main(String[] args) {
        try {
            // Conectar ao serviço GPC via RMI
            APIGPC gpc = (APIGPC) Naming.lookup("rmi://localhost:50001/GPC");

            // Criação de scanner para leitura de inputs do usuário
            Scanner scanner = new Scanner(System.in);

            // 1. Criar utentes
            criarUtentes(gpc);

            // 2. Criar profissionais
            criarProfissionais(gpc);

            // 3. Número total de utentes
            TotalUtentes(gpc);

            // 4. Procurar utente por CC
            procurarUtentePorCC(gpc);

            // 5. Atualizar dados de um tente
            atualizarDadosUtente(gpc);

            // 6. Criar um ato
            criarAtoDispensa(gpc);




            // Fechar o scanner
            scanner.close();

        } catch (RemoteException e) {
            System.out.println("Erro de RMI: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Criar utententes
    private static void criarUtentes(APIGPC gpc) {
        try {
            String[][] dadosUtentes = {
                    {"João", "Silva", "123456789", "01/01/1980"},
                    {"Maria", "Santos", "234567890", "15/05/1992"},
                    {"Carlos", "Pereira", "345678901", "22/11/1985"},
                    {"Ana", "Oliveira", "456789012", "03/09/1990"},
                    {"Pedro", "Fernandes", "567890123", "11/02/1982"},
                    {"Cláudia", "Martins", "678901234", "05/07/1984"},
                    {"Rafael", "Rocha", "789012345", "21/03/1993"},
                    {"Lucia", "Gomes", "890123456", "14/06/1988"},
                    {"Ricardo", "Almeida", "901234567", "02/04/1987"},
                    {"Fernanda", "Cardoso", "112233445", "09/12/1991"},
                    {"Bruna", "Silveira", "223344556", "23/10/1986"},
                    {"Fábio", "Martins", "334455667", "17/05/1989"},
                    {"Patrícia", "Ribeiro", "445566778", "19/08/1994"},
                    {"Juliana", "Teixeira", "556677889", "28/02/1983"},
                    {"José", "Barros", "667788990", "25/01/1981"},
                    {"Tatiane", "Costa", "778899001", "07/11/1990"},
                    {"Sérgio", "Lima", "889900112", "12/03/1987"},
                    {"Raquel", "Freitas", "990011223", "16/04/1992"},
                    {"André", "Carvalho", "123789456", "04/09/1984"},
                    {"Marta", "Castro", "234890567", "08/02/1990"},
                    {"Gustavo", "Alves", "345901678", "10/07/1983"},
                    {"Vanessa", "Henrique", "456012789", "13/06/1988"},
                    {"Diego", "Leal", "567123890", "25/12/1991"},
                    {"Roberta", "Souza", "678234901", "11/05/1982"},
                    {"Luana", "Machado", "789345012", "01/01/1994"},
                    {"Marcos", "Oliveira", "890456123", "03/09/1980"},
                    {"Carla", "Azevedo", "901567234", "19/07/1987"},
                    {"Simone", "Barreto", "123678345", "06/10/1993"},
                    {"Marcelo", "Silva", "234789456", "21/05/1985"},
                    {"Jéssica", "Guimarães", "345890562", "14/08/1989"},
                    {"Fernando", "Rosário", "456901823", "18/03/1986"},
                    {"Érica", "Monteiro", "567012934", "15/11/1984"},
                    {"Douglas", "Lopes", "678129045", "29/12/1992"},
                    {"Camila", "Teixeira", "723490156", "09/04/1981"},
                    {"Gustavo", "Ribeiro", "890345067", "24/10/1988"},
                    {"Karine", "Cardoso", "901456378", "17/02/1990"}
            };

            // Criando utentes a partir dos dados simulados
            for (String[] dados : dadosUtentes) {
                String nome = dados[0];
                String apelido = dados[1];
                String numeroDocumento = dados[2];
                String dataNascimento = dados[3];

                // Criar o Utente a partir dos dados
                Utente utente = gpc.createUtente(nome, apelido, numeroDocumento);
                utente.setDataNascimento(dataNascimento); // Definindo a data de nascimento

                // Exibindo o Utente no console
                System.out.println(utente);
            }
            pressionarEnterParaContinuar();
        } catch (Exception e) {
            System.out.println("Erro ao criar os utentes: " + e.getMessage());
        }
    }

    // Criar profissionais
    private static void criarProfissionais(APIGPC gpc) {
        try {
            String[][] dadosUtentes = {
                    {"João", "Silva", "993456789"},
                    {"Maria", "Santos", "994567890"},
                    {"Carlos", "Pereira", "995678901"},
                    {"Ana", "Oliveira", "996789012"},
                    {"Pedro", "Fernandes", "997890123"},
                    {"Cláudia", "Martins", "998901234"},
                    {"Rafael", "Rocha", "999012345"},
                    {"Lucia", "Gomes", "990123456"},
                    {"Ricardo", "Almeida", "991234567"},
                    {"Fernanda", "Cardoso", "992233445"},
                    {"Bruna", "Silveira", "993344556"},
                    {"Fábio", "Martins", "994455667"},
                    {"Patrícia", "Ribeiro", "995566778"},
                    {"Juliana", "Teixeira", "996677889"},
                    {"José", "Barros", "997788990"},
                    {"Tatiane", "Costa", "998899001"},
                    {"Sérgio", "Lima", "999900112"},
                    {"Raquel", "Freitas", "880011223"},
                    {"André", "Carvalho", "993789456"},
                    {"Marta", "Castro", "994890567"},
                    {"Gustavo", "Alves", "995901678"},
                    {"Vanessa", "Henrique", "996012789"},
                    {"Diego", "Leal", "997123890"},
                    {"Roberta", "Souza", "998234901"},
                    {"Luana", "Machado", "999345012"},
                    {"Marcos", "Oliveira", "990456123"},
                    {"Carla", "Azevedo", "991567234"},
                    {"Simone", "Barreto", "993678345"},
                    {"Marcelo", "Silva", "994789456"},
                    {"Jéssica", "Guimarães", "995890562"},
                    {"Fernando", "Rosário", "996901823"},
                    {"Érica", "Monteiro", "997012934"},
                    {"Douglas", "Lopes", "998129045"},
                    {"Camila", "Teixeira", "993490156"},
                    {"Gustavo", "Ribeiro", "990345067"},
                    {"Karine", "Cardoso", "991456378"}
            };

            // Criando utentes a partir dos dados simulados
            for (String[] dados : dadosUtentes) {
                String nome = dados[0];
                String apelido = dados[1];
                String numeroDocumento = dados[2];

                // Criar o Utente a partir dos dados
                Profissional profissional = gpc.createProfissional(nome, apelido, numeroDocumento);
                // Exibindo o Utente no console
                System.out.println(profissional);
            }
            pressionarEnterParaContinuar();
        } catch (Exception e) {
            System.out.println("Erro ao criar os profissionais: " + e.getMessage());
        }
    }


    // Exibir número total de utentes
    private static void TotalUtentes(APIGPC gpc) {
        try {
            int totalUtentes = gpc.totalUtentes();
            System.out.println("Número total de utentes: " + totalUtentes);
            pressionarEnterParaContinuar();
        } catch (RemoteException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Procurar utente por cc
    private static void procurarUtentePorCC(APIGPC gpc) {
        try {
            List<String> idsUtentes = gpc.procuraUtenteCc("123456789");
            for (String idUtente : idsUtentes) {
                Utente utente = gpc.getUtente(idUtente);
                System.out.println("Utente encontrado:");
                System.out.println(utente);
        }
        pressionarEnterParaContinuar();
    } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Atualizar nome de um utente
    private static void atualizarDadosUtente(APIGPC gpc) {
        try {
            List<String> idsUtentes = gpc.procuraUtenteCc("123456789");
            for (String idUtente : idsUtentes) {
                Utente utente = gpc.getUtente(idUtente);
                System.out.println("Utente: " + idUtente);

                gpc.alteraUtenteNome("1709865b-9e94-41ab-bae9-d48e2a819ed9", "Clara");

                Utente utente_atualizado = gpc.getUtente(idUtente);
                System.out.println("Utente: " + utente_atualizado);
                System.out.print("Nome do utente atualizado com sucesso.");
                pressionarEnterParaContinuar();
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    // Criar um ato de dispensa

    private static void criarAtoDispensa(APIGPC gpc) {
        try {
            String idUtente = "2aed9c3a-2ef9-41f1-bf4f-ce7f01945589";
            String idProfissional = "3732e5dd-0818-4a2c-8d36-4365b655c609";
            String tipo = "Dispensa";
            LocalDateTime data = LocalDateTime.now();
            String descricao = "d53c4083-cb25-40ca-b49a-f68597429339";


            Utente utente = gpc.getUtente(idUtente); // Obter o primeiro utente encontrado
            Profissional profissional = gpc.getProfissional(idProfissional);

            // Criar o ato
            Ato ato = gpc.createAto(utente, profissional, tipo, data, descricao);

            // Exibir os detalhes do ato criado
            System.out.println("Ato de dispensa criado com sucesso!");
            System.out.println(ato);

            pressionarEnterParaContinuar();
        } catch (RemoteException e) {
            System.out.println("Erro de RMI ao criar ato de dispensa: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erro ao criar ato de dispensa: " + e.getMessage());
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
