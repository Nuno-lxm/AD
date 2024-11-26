package projeto.app;

import projeto.model.Excecao;
import projeto.model.classes.Ato;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;
import projeto.model.gpc.APIGPC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public class InterfaceGPC extends JFrame {

    private JTextField textFieldNome, textFieldApelido, textFieldCC, textFieldDataNascimento, textFieldMorada, textFieldAlergias, textFieldCondicoes, textFieldGrupoSanguineo;
    private JTextArea textAreaResultado;
    private CardLayout cardLayout;
    private JPanel mainPanel;  // Painel principal para segurar os "cards"
    private APIGPC gpc;

    public InterfaceGPC() {
        // Inicializa a interface RMI
        try {
            gpc = (APIGPC) Naming.lookup("rmi://localhost:50001/GPC");
        } catch (MalformedURLException | NotBoundException | RemoteException e) {
            exibirErro("Erro ao conectar com o servidor RMI: " + e.getMessage());
        }

        // Configurações da Janela Principal
        setTitle("Sistema GPC");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializando o CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel, BorderLayout.CENTER);

        // Tela Inicial
        JPanel telaInicial = new JPanel();
        telaInicial.setLayout(new GridLayout(3, 1));

        JButton buttonCriarUtente = new JButton("Criar Utente");
        JButton buttonCriarAto = new JButton("Criar Ato");
        JButton buttonProcurarAto = new JButton("Procurar Ato");

        telaInicial.add(buttonCriarUtente);
        telaInicial.add(buttonCriarAto);
        telaInicial.add(buttonProcurarAto);

        // Adicionando a tela inicial ao CardLayout
        mainPanel.add(telaInicial, "Tela Inicial");

        // Ação do botão Criar Utente
        buttonCriarUtente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirTelaCriarUtente();
            }
        });
    }

    // Exibe a tela de Criar Utente
    private void exibirTelaCriarUtente() {
        JPanel telaCriarUtente = new JPanel();
        telaCriarUtente.setLayout(new GridLayout(10, 2));

        JLabel labelNome = new JLabel("Nome:");
        textFieldNome = new JTextField();
        JLabel labelApelido = new JLabel("Apelido:");
        textFieldApelido = new JTextField();
        JLabel labelCC = new JLabel("CC:");
        textFieldCC = new JTextField();

        JLabel labelDataNascimento = new JLabel("Data de Nascimento (DD/MM/YYYY):");
        textFieldDataNascimento = new JTextField();
        JLabel labelMorada = new JLabel("Morada:");
        textFieldMorada = new JTextField();
        JLabel labelAlergias = new JLabel("Alergias:");
        textFieldAlergias = new JTextField();
        JLabel labelCondicoes = new JLabel("Condições Crónicas:");
        textFieldCondicoes = new JTextField();
        JLabel labelGrupoSanguineo = new JLabel("Grupo Sanguíneo:");
        textFieldGrupoSanguineo = new JTextField();

        JButton buttonCriar = new JButton("Criar Utente");
        JButton buttonVoltar = new JButton("Voltar");

        // Adicionando os componentes à tela de Criar Utente
        telaCriarUtente.add(labelNome);
        telaCriarUtente.add(textFieldNome);
        telaCriarUtente.add(labelApelido);
        telaCriarUtente.add(textFieldApelido);
        telaCriarUtente.add(labelCC);
        telaCriarUtente.add(textFieldCC);
        telaCriarUtente.add(labelDataNascimento);
        telaCriarUtente.add(textFieldDataNascimento);
        telaCriarUtente.add(labelMorada);
        telaCriarUtente.add(textFieldMorada);
        telaCriarUtente.add(labelAlergias);
        telaCriarUtente.add(textFieldAlergias);
        telaCriarUtente.add(labelCondicoes);
        telaCriarUtente.add(textFieldCondicoes);
        telaCriarUtente.add(labelGrupoSanguineo);
        telaCriarUtente.add(textFieldGrupoSanguineo);
        telaCriarUtente.add(buttonCriar);
        telaCriarUtente.add(buttonVoltar);

        textAreaResultado = new JTextArea(5, 20);
        textAreaResultado.setEditable(false);
        telaCriarUtente.add(new JScrollPane(textAreaResultado));

        // Adiciona a tela de Criar Utente ao CardLayout
        mainPanel.add(telaCriarUtente, "Criar Utente");

        // Ação do botão Criar Utente
        buttonCriar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                criarUtente();
            }
        });

        // Ação do botão Voltar
        buttonVoltar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "Tela Inicial");
            }
        });

        // Muda para a tela de Criar Utente
        cardLayout.show(mainPanel, "Criar Utente");
    }

    // Método para criar o Utente
    private void criarUtente() {
        String nome = textFieldNome.getText();
        String apelido = textFieldApelido.getText();
        String cc = textFieldCC.getText();
        String dataNascimento = textFieldDataNascimento.getText();
        String morada = textFieldMorada.getText();
        String alergias = textFieldAlergias.getText();
        String condicoes = textFieldCondicoes.getText();
        String grupoSanguineo = textFieldGrupoSanguineo.getText();

        try {
            if (!nome.isEmpty() && !apelido.isEmpty() && !cc.isEmpty()) {
                // Cria o utente com os atributos obrigatórios
                Utente novoUtente = gpc.createUtente(nome, apelido, cc);

                // Adiciona atributos opcionais, se preenchidos
                if (!dataNascimento.isEmpty()) {
                    novoUtente.setDataNascimento(dataNascimento);
                }
                if (!morada.isEmpty()) {
                    novoUtente.setMorada(morada);
                }
                if (!alergias.isEmpty()) {
                    novoUtente.setAlergias(alergias);
                }
                if (!condicoes.isEmpty()) {
                    novoUtente.setCondicoes(condicoes);
                }
                if (!grupoSanguineo.isEmpty()) {
                    novoUtente.setGrupoSanguineo(grupoSanguineo);
                }

                textAreaResultado.setText("Utente criado com sucesso!\n" + novoUtente);
            } else {
                textAreaResultado.setText("Por favor, preencha os campos obrigatórios.");
            }
        } catch (RemoteException | Excecao e) {
            exibirErro("Erro ao criar utente: " + e.getMessage());
        }
    }


    // Método para exibir uma mensagem de erro usando JOptionPane
    private void exibirErro(String mensagem) {
        JOptionPane.showMessageDialog(this, mensagem, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    // Método principal para executar o programa
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new InterfaceGPC().setVisible(true);
            }
        });
    }
}
