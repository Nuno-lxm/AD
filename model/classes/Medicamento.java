package projeto.model.classes;

import projeto.model.Excecao;
import projeto.model.classes.receitas.Prescricao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Medicamento implements Serializable {
    private String id;
    private String nome;
    private int stock;
    private Fornecedor fornecedor;
    private String descricao;
    private int threshold;
    private Map<String, Prescricao> prescricoes;
    private Map<String, Encomenda> encomendas;

    public Medicamento(String nome, Fornecedor fornecedor, String descricao) throws Excecao {
        if (nome==null || nome.isBlank()){
            throw new Excecao("Nome vazio.");
        }
        if (fornecedor==null){
            throw new Excecao("Fornecedor vazio.");
        }
        if (descricao==null || descricao.isBlank()){
            throw new Excecao("Descricao vazia.");
        }
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.prescricoes = new HashMap<>();
        this.threshold = 5;
    }

   public Medicamento(String nome, Fornecedor fornecedor, String descricao, int stock, int threshold) {
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.fornecedor = fornecedor;
        this.descricao = descricao;
        this.prescricoes = new HashMap<>();
        this.stock = stock;
        this.threshold = threshold;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public int getStock() {return stock;}
    public void setStock(int stock) {this.stock = stock;}

    public Fornecedor getFornecedor() {return fornecedor;}
    public void setFornecedor(Fornecedor fornecedor) {this.fornecedor = fornecedor;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public int getThreshold() {return threshold;}
    public void setThreshold(int threshold) {this.threshold = threshold;}

    public Map<String, Prescricao> getPrescricoes() {return prescricoes;}
    public void setPrescricoes(Map<String, Prescricao> prescricoes) {this.prescricoes = prescricoes;}
    public void adicionaPrescricao(Prescricao prescricao) {this.prescricoes.put(prescricao.getId(), prescricao);}
    public void removePrescricao(String id) {this.prescricoes.remove(id);}

    public Map<String, Encomenda> getEncomendas() {return encomendas;}
    public void setEncomendas(Map<String, Encomenda> encomendas) {this.encomendas = encomendas;}
    public void adicionaEncomenda(Encomenda encomenda) {this.encomendas.put(encomenda.getId(), encomenda);}
    public void removeEncomenda(String id) {this.encomendas.remove(id);}

    @Override
    public String toString() {
        return "Medicamento" +
                "{\n" +
                "id = " + id +
                "nome = " + nome + ",\n" +
                "stock = " + stock + ",\n" +
                "fornecedor = " + fornecedor.getNome() + ",\n" +
                "descricao = " + descricao + "\n" +
                "threshold" + threshold + "\n" +
                "}";
    }
}