package projeto.model.glm;

import projeto.model.Excecao;
import projeto.model.classes.Encomenda;
import projeto.model.classes.Fornecedor;
import projeto.model.classes.Medicamento;
import projeto.model.classes.receitas.Prescricao;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GLM extends UnicastRemoteObject implements APIGLM, Serializable {

    private Map<String, Medicamento> medicamentos;
    private Map<String, Fornecedor> fornecedores;
    private Map<String, Encomenda> encomendas;

    public GLM() throws RemoteException {
        super();
        medicamentos = new HashMap<>();
        fornecedores = new HashMap<>();
        encomendas = new HashMap<>();
    }


    public void confirmaEncomenda(Encomenda e) {
        Medicamento m = e.getMedicamento();
        int new_stock = m.getStock() + e.getQuantidade();

        m.setStock(new_stock);

        e.setConcluida(true);
        e.setDataConfirmacao(LocalDateTime.now());

        alteraEncomenda(e.getId(), e);
        medicamentos.put(m.getId(), m);
    }


    public void adicionaPrescricaoMedicamento(Medicamento medicamento, Prescricao prescricao) throws Excecao {
        medicamento.adicionaPrescricao(prescricao);
    }

    /*public Medicamento createMedicamento(String nome, Fornecedor fornecedor, String descricao) throws Excecao {
        for (Medicamento m : medicamentos.values()) {
            if (m.getNome().equals(nome) && m.getFornecedor().equals(fornecedor)) {
                throw new Excecao("Já existe um medicamento com o mesmo nome e fornecedor.");
            }
        }
        Medicamento m = new Medicamento(nome, fornecedor, descricao);
        medicamentos.put(m.getId(), m);
        return m;
    }*/

    public Medicamento createMedicamento(String nome, Fornecedor fornecedor, String descricao, int stock, int threshold) {
        Medicamento med = new Medicamento(nome, fornecedor, descricao, stock, threshold);
        medicamentos.put(med.getId(), med);
        return med;
    }

    public Medicamento getMedicamento(String id) { return medicamentos.get(id); }

    public List<String> procuraMedicamento(String nome) {
        List<String> res = new ArrayList<>();
        for (Medicamento m : this.medicamentos.values()) {
            if (m.getNome().equals(nome)) {
                res.add(m.getId());
            }
        }
        return res;
    }

    public List<String> procuraMedicamentoFornecedor(Fornecedor fornecedor) {
        List<String> res = new ArrayList<>();
        for (Medicamento m : this.medicamentos.values()) {
            if (m.getFornecedor().getId().equals(fornecedor.getId())) {
                res.add(m.getId());
            }
        }
        return res;
    }

    public int consomeMedicamento(String id, int quantidade) throws Excecao {
        Medicamento med = medicamentos.get(id);
        if (med == null) {
            throw new Excecao("Medicamento não encontrado para o ID: " + id);
        }
        int stock = med.getStock();
        int thresh = med.getThreshold();
        int res = -1;
        if (stock < quantidade) {
            throw new Excecao("Stock Insuficiente");
        }
        int new_stock = med.getStock() - quantidade;
        med.setStock(new_stock);
        res = new_stock;
        if (new_stock < thresh) {
            res = 0;
        }
        return res;
    }



    public void alteraMedicamentoDescricao(String id, String descricao) {
        if(medicamentos.containsKey(id)) {
            medicamentos.get(id).setDescricao(descricao);
        }
    }

    public void alteraMedicamentoFornecedor(String id, Fornecedor fornecedor) {
        if(medicamentos.containsKey(id)) {
            medicamentos.get(id).setFornecedor(fornecedor);
        }
    }

    public void alteraMedicamento(String id, Medicamento medicamento) {
        if(medicamentos.containsKey(id)) {
            if(medicamento.getId().equals(id)) {
                medicamentos.put(id, medicamento);
            }
        }
    }

    public Fornecedor createFornecedor(String nome) throws Excecao {
        for (Fornecedor f : fornecedores.values()) {
            if (f.getNome().equals(nome)) {
                throw new Excecao("O fornecedor já existe.");
            }
        }
        Fornecedor f = new Fornecedor(nome);
        fornecedores.put(f.getId(), f);
        return f;
    }

    public Fornecedor getFornecedor(String id) {return fornecedores.get(id);}

    public List<String> procuraFornecedor(String nome) {
        List<String> res = new ArrayList<>();
        for (Fornecedor f : fornecedores.values()) {
            if (f.getNome().equals(nome)) {
                res.add(f.getId());
            }
        }
        return res;
    }

    public void alteraFornecedorNome(String id, String nome) throws RemoteException {
        if(fornecedores.containsKey(id)){
            fornecedores.get(id).setNome(nome);
        }
    }

    public Encomenda createEncomenda(Medicamento medicamento, Fornecedor fornecedor, int quantidade) throws Excecao {
        Encomenda e = new Encomenda(medicamento, fornecedor, quantidade);

        encomendas.put(e.getId(), e);
        fornecedor.adicionaEncomenda(e);
        return e;
    }

    public Encomenda getEncomenda(String id) {return encomendas.get(id);}

    public List<String> procuraEncomendaMedicamento(Medicamento medicamento) {
        List<String> res = new ArrayList<>();
        for (Encomenda e : encomendas.values()) {
            if (e.getMedicamento().getId().equals(medicamento.getId())) {
                res.add(e.getId());
            }
        }
        return res;
    }

    public List<String> procuraEncomendaFornecedor(Fornecedor fornecedor) {
        List<String> res = new ArrayList<>();
        for (Encomenda e : encomendas.values()) {
            if (e.getFornecedor().getId().equals(fornecedor.getId())) {
                res.add(e.getId());
            }
        }
        return res;
    }

    public void alteraEncomendaMedicamento(String id, Medicamento medicamento) {
        if(encomendas.containsKey(id)) {
            encomendas.get(id).setMedicamento(medicamento);
        }
    }

    public void alteraEncomendaFornecedor(String id, Fornecedor fornecedor) {
        if(encomendas.containsKey(id)) {
            encomendas.get(id).setFornecedor(fornecedor);
        }
    }

    public void alteraEncomenda(String id, Encomenda encomenda) {
        if(encomendas.containsKey(id)) {
            if(encomenda.getId().equals(id)) {
                encomendas.put(id, encomenda);
            }
        }
    }

    public int totalMedicamentos() { return medicamentos.size(); }

    public int totalFornecedores() { return fornecedores.size(); }

    public int totalEncomenda() { return encomendas.size(); }

    public int encomendasPorFornecedor() { return 0; }
}