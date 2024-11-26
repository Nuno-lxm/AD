package projeto.model.classes;

import projeto.model.Excecao;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Encomenda implements Serializable {
    private String id;
    private Medicamento medicamento;
    private Fornecedor fornecedor;
    private int quantidade;
    private LocalDateTime dataEncomenda;
    private Boolean concluida;
    private LocalDateTime dataConfirmacao;

    public Encomenda(Medicamento medicamento, Fornecedor fornecedor, int quantidade) throws Excecao {
        if (medicamento==null){
            throw new Excecao(("Medicamento vazio."));
        }
        if (fornecedor==null){
            throw new Excecao(("Fornecedor vazio."));
        }
        if (quantidade<=0){
            throw new Excecao(("A quantidade deve ser maior que zero"));
        }
        this.id = UUID.randomUUID().toString();
        this.medicamento = medicamento;
        this.fornecedor = fornecedor;
        this.quantidade = quantidade;
        this.dataEncomenda = LocalDateTime.now();
        this.concluida = false;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Medicamento getMedicamento() {return medicamento;}
    public void setMedicamento(Medicamento medicamento) {this.medicamento = medicamento;}

    public Fornecedor getFornecedor() {return fornecedor;}
    public void setFornecedor(Fornecedor fornecedor) {this.fornecedor = fornecedor;}

    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) throws Excecao {
        if (quantidade<=0){
            throw new Excecao("A quantidade deve ser maior que zero.");
        }
        this.quantidade = quantidade;
    }

    public LocalDateTime getDataEncomenda() {return dataEncomenda;}
    public void setDataEncomenda(LocalDateTime dataEncomenda) {this.dataEncomenda = dataEncomenda;}

    public Boolean getConcluida() {return concluida;}
    public void setConcluida(Boolean concluida) {this.concluida = concluida;}

    public LocalDateTime getDataConfirmacao() {return dataConfirmacao;}
    public void setDataConfirmacao(LocalDateTime dataConfirmacao) {this.dataConfirmacao = dataConfirmacao;}

    @Override
    public String toString() {
        return "Encomenda{" +
                "medicamento=" + medicamento.getNome() +
                ", fornecedor=" + fornecedor.getNome() +
                ", quantidade=" + quantidade +
                ", dataEncomenda=" + dataEncomenda +
                ", concluida=" + concluida +
                ", dataConfirmacao=" + dataConfirmacao +
                '}';
    }
}