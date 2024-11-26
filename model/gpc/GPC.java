package projeto.model.gpc;

import projeto.model.Excecao;
import projeto.model.classes.Ato;
import projeto.model.classes.Medicamento;
import projeto.model.classes.receitas.Prescricao;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;
import projeto.model.classes.receitas.Receita;
import projeto.model.glm.APIGLM;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GPC extends UnicastRemoteObject implements APIGPC, Serializable {

    APIGLM glm = (APIGLM) Naming.lookup("rmi://localhost:50001/GLM");

    private Map<String, Utente> utentes;
    private Map<String, Ato> atos;
    private Map<String, Receita> receitas;
    private Map<String, Profissional> profissionais;

    public GPC() throws RemoteException, MalformedURLException, NotBoundException {
        super();
        utentes = new HashMap<>();
        atos = new HashMap<>();
        receitas = new HashMap<>();
        profissionais = new HashMap<>();
    }


    // Atos médicos

    public Ato createAto(Utente utente, Profissional profissional, String tipo, LocalDateTime data, String descricao) throws RemoteException, Excecao {
        if (tipo.equals("Administracao") || tipo.equals("Dispensa")) {
            int res = glm.consomeMedicamento(descricao, 1);
            if (res == 0) {
                System.out.println("Stock insuficiente");
            }
        }
        Ato a = new Ato(utente, profissional, tipo, data, descricao);
        atos.put(a.getId(), a);
        utente.adicionaAto(a);
        profissional.adicionaAto(a);
        return a;
    }

    public Ato getAto(String id) { return atos.get(id); }

    public List<String> procuraAtoUtente(Utente utente) {
        List<String> res = new ArrayList<>();
        for (Ato a : this.atos.values()) {
            if (a.getUtente().getId().equals(utente.getId())) {
                res.add(a.getId());
            }
        }
        return res;
    }

    public List<String> procuraAtoProfissional(Profissional profissional) {
        List<String> res = new ArrayList<>();
        for (Ato a : this.atos.values()) {
            if (a.getProfissional().getId().equals(profissional.getId())) {
                res.add(a.getId());
            }
        }
        return res;
    }

    public List<String> procuraAtoTipo(String tipo) {
        List<String> res = new ArrayList<>();
        for (Ato a : this.atos.values()) {
            if (a.getTipo().equals(tipo)) {
                res.add(a.getId());
            }
        }
        return res;
    }

    public List<String> procuraAtoData(LocalDateTime data) {
        List<String> res = new ArrayList<>();
        for (Ato a : this.atos.values()) {
            if (a.getData().equals(data)) {
                res.add(a.getId());
            }
        }
        return res;
    }

    public void alteraAtoUtente(String id, Utente utente) {
        if(atos.containsKey(id)) {
            atos.get(id).setUtente(utente);
        }
    }

    public void alteraAtoProfissional(String id, Profissional profissional) {
        if(atos.containsKey(id)) {
            atos.get(id).setProfissional(profissional);
        }
    }

    public void alteraAtoTipo(String id, String tipo) {
        if(atos.containsKey(id)) {
            atos.get(id).setTipo(tipo);
        }
    }

    public void alteraAtoData(String id, LocalDateTime data) {
        if(atos.containsKey(id)) {
            atos.get(id).setData(data);
        }
    }

    public void alteraAtoDescricao(String id, String descricao) {
        if(atos.containsKey(id)) {
            atos.get(id).setDescricao(descricao);
        }
    }

    public void alteraAto(String id, Ato ato) {
        if(atos.containsKey(id)) {
            if(ato.getId().equals(id)) {
                atos.put(id, ato);
            }
        }
    }

    public Receita createReceita(Utente utente, Profissional profissional, LocalDateTime dataValidade, String descricao) throws Excecao {
        Receita r = new Receita(utente, profissional, dataValidade, descricao);

        receitas.put(r.getId(), r);
        utente.adicionaReceita(r);
        profissional.adicionaReceita(r);
        return r;
    }

    public Prescricao createPrescricao(Receita receita, Medicamento medicamento, int quantidade, String descricao) throws Excecao, RemoteException {
        Prescricao prescricao = new Prescricao(medicamento, quantidade, descricao);

        receita.adicionaPrescricao(prescricao);
        glm.adicionaPrescricaoMedicamento(medicamento, prescricao);
        return prescricao;
    }

    public Receita getReceita(String id) { return receitas.get(id); }

    public List<String> procuraReceitaUtente(Utente utente) {
        List<String> res = new ArrayList<>();
        for (Receita r : this.receitas.values()) {
            if (r.getUtente().getId().equals(utente.getId())) {
                res.add(r.getId());
            }
        }
        return res;
    }

    public List<String> procuraReceitaProfissional(Profissional profissional) {
        List<String> res = new ArrayList<>();
        for (Receita r : this.receitas.values()) {
            if (r.getProfissional().getId().equals(profissional.getId())) {
                res.add(r.getId());
            }
        }
        return res;
    }

    // Criar um novo utente
    public Utente createUtente(String nome, String apelido, String cc) throws Excecao {
        for (Utente u : utentes.values()) {
            if (u.getCc().equals(cc)) {
                throw new Excecao("Já existe um Utente com o CC associado.");
            }
        }
        Utente u = new Utente(nome, apelido, cc);
        utentes.put(u.getId(), u);
        return u;
    }

    // Obter um utente por id
    public Utente getUtente(String id) {return utentes.get(id);}

    public List<String> procuraUtente(String nome) {
        List<String> res = new ArrayList<>();
        for (Utente u : this.utentes.values()) {
            if (u.getNome().equals(nome)) {
                res.add(u.getId());
            }
        }
        return res;
    }

    // Obter um utente por id
    public List<String> procuraUtenteApelido(String apelido) {
        List<String> res = new ArrayList<>();
        for (Utente u : this.utentes.values()) {
            if (u.getApelido().equals(apelido)) {
                res.add(u.getId());
            }
        }
        return res;
    }

    public List<String> procuraUtenteCc(String cc) {
        List<String> res = new ArrayList<>();
        for (Utente u : this.utentes.values()) {
            if (u.getCc().equals(cc)) {
                res.add(u.getId());
            }
        }
        return res;
    }

    public List<String> procuraUtenteGrupoSanguineo(String grupo) {
        List<String> res = new ArrayList<>();
        for (Utente u : this.utentes.values()) {
            if (u.getGrupoSanguineo().equals(grupo)) {
                res.add(u.getId());
            }
        }
        return res;
    }

    // Alterar o nome de um utente
    public void alteraUtenteNome(String id, String nome) {
        if (utentes.containsKey(id)) {
            utentes.get(id).setNome(nome);
        }
    }
    // Alterar o contacto de um utente
    public void alteraUtenteContacto(String id, String contacto) {
        if (utentes.containsKey(id)) {
            utentes.get(id).setContacto(contacto);
        }
    }

    // Alterar alergias de um utente
    public void alteraUtenteAlergias(String id, String alergias) {
        if (utentes.containsKey(id)) {
            utentes.get(id).setAlergias(alergias);
        }
    }

    // Alterar condições crónicas de um utente
    public void alteraUtenteCondicoes(String id, String condicoes) {
        if (utentes.containsKey(id)) {
            utentes.get(id).setCondicoes(condicoes);
        }
    }

    // Remover um utente
    public void removeUtente(String id) {
        utentes.remove(id);
    }
    // Listar todos os utentes
    public List<String> listaTodosUtentes() {
        return new ArrayList<>(utentes.keySet());
    }

    // Histórico de um utente
    public List<String> getHistoricoUtente(String id) {
        List<String> historico = new ArrayList<>();
        for (Ato a : atos.values()) {
            String a_id_utente = a.getUtente().getId();
            if (a_id_utente.equals(id)) {
                historico.add(a_id_utente);
            }
        }
        return historico;
    }

    public Profissional createProfissional(String nome, String apelido, String cc) throws Excecao {
        for (Profissional profissional : profissionais.values()) {
            if (profissional.getCc().equals(cc)) {
                throw new Excecao("Já existe um Profissional com o CC associado.");
            }
        }
        Profissional p = new Profissional(nome, apelido, cc);
        profissionais.put(p.getId(), p);
        return p;
    }

    public Profissional getProfissional(String id) {return profissionais.get(id);}

    public List<String> procuraProfissional(String nome) {
        List<String> res = new ArrayList<>();
        for (Profissional p : profissionais.values()) {
            if (p.getNome().equals(nome)) {
                res.add(p.getId());
            }
        }
        return res;
    }

    public List<String> procuraProfissionalApelido(String apelido) {
        List<String> res = new ArrayList<>();
        for (Profissional p : profissionais.values()) {
            if (p.getApelido().equals(apelido)) {
                res.add(p.getId());
            }
        }
        return res;
    }

    public List<String> procuraProfissionalProfissao(String profissao) {
        List<String> res = new ArrayList<>();
        for (Profissional p : profissionais.values()) {
            if (p.getProfissao().equals(profissao)) {
                res.add(p.getId());
            }
        }
        return res;
    }


    // Estatísticas

    // Número total de utentes
    public int totalUtentes() {
        return utentes.size();
    }

    // Número total de atos clínicos realizados
    public int totalAtosClinicos() {
        return atos.size();
    }

    public int totalProfissionais() { return profissionais.size(); }

    public int totalReceitas() {return receitas.size(); }

    // Número de atos realizados por um profissional
    public int numAtosPorProfissional(String idProfissional) {
        return getProfissional(idProfissional).getAtos().size();
    }


    // Número de atos realizados por cada profissional
    public String atosPorProfissional() {
        StringBuilder res = new StringBuilder();
        for (Profissional p : profissionais.values()) {
            res.append(p.getNome()).append(":").append(p.getAtos().size()).append(";\n");
        }
        return res.toString();
    }

    // Número de atos realizados por um profissional, classificados por tipo
    public Map<String, Map<String, Integer>> atosPorProfissionalPorTipo() {
        Map<String, Map<String, Integer>> resultado = new HashMap<>();

        for (Ato ato : atos.values()) {
            String idProfissional = ato.getProfissional().getId();
            String tipoAto = ato.getTipo();

            // Obtém o mapa de contagem de tipos de atos para o profissional, ou inicializa um novo
            Map<String, Integer> contagemPorTipo = resultado.getOrDefault(idProfissional, new HashMap<>());

            // Incrementa a contagem para o tipo do ato
            contagemPorTipo.put(tipoAto, contagemPorTipo.getOrDefault(tipoAto, 0) + 1);

            // Atualiza o mapa principal com as contagens por tipo
            resultado.put(idProfissional, contagemPorTipo);
        }
        return resultado;
    }

    // Número de receitas emitidas para um utente
    public int receitasPorUtente(String idUtente) {
        return getUtente(idUtente).getReceitas().size();
    }

    // Número de atos realizados num período específico, classificados por tipo
    public Map<String, Integer> atosPorPeriodoPorTipo(LocalDateTime inicio, LocalDateTime fim) {
        Map<String, Integer> resultado = new HashMap<>();

        for (Ato ato : atos.values()) {
            LocalDateTime data = ato.getData();
            if ((data.isAfter(inicio) || data.isEqual(inicio)) && (data.isBefore(fim) || data.isEqual(fim))) {
                String tipoAto = ato.getTipo();
                resultado.put(tipoAto, resultado.getOrDefault(tipoAto, 0) + 1);
            }
        }
        return resultado;
    }
}