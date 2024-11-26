package projeto.model.classes.receitas;

import projeto.model.Excecao;
import projeto.model.classes.Medicamento;

import java.io.Serializable;
import java.util.UUID;

public class Prescricao implements Serializable {
    private String id;
    private Medicamento medicamento;
    private int quantidade;
    private String descricao;

    public Prescricao(Medicamento medicamento, int quantidade, String descricao) throws Excecao {
        if (medicamento==null){
            throw new Excecao("Medicamento vazio.");
        }
        if (quantidade<=0){
            throw new Excecao("A quantidade deve ser maior que zero");
        }
        if (descricao==null){
            throw new Excecao("Descrição vazia.");
        }
        this.id = UUID.randomUUID().toString();
        this.medicamento = medicamento;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Medicamento getMedicamento() {return medicamento;}
    public void setMedicamento(Medicamento medicamento) {this.medicamento = medicamento;}

    public int getQuantidade() {return quantidade;}
    public void setQuantidade(int quantidade) {this.quantidade = quantidade;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}
}