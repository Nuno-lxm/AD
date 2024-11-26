package projeto.model.classes.utilizador;

import projeto.model.Excecao;
import projeto.model.classes.Ato;
import projeto.model.classes.receitas.Receita;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Profissional extends Utilizador implements Serializable {
    private String profissao;
    private Map<String, Ato> atos;
    private Map<String, Receita> receitas;

    public Profissional(String nome, String apelido, String cc) throws Excecao {
        super(nome, apelido, cc);
        this.atos = new HashMap<>();
    }

    public String getProfissao() {return profissao;}
    public void setProfissao(String profissao) throws Excecao{
        if (!profissao.matches("(?i)^Médico$|^Enfermeiro$|^Farmacêutico$")) {
            throw new Excecao("Erro: Profissão inválida!");
        }
        else {
            this.profissao = profissao.trim();
        }
    }


    public Map<String, Ato> getAtos() {return atos;}
    public void setAtos(Map<String, Ato> atos) {this.atos = atos;}
    public void adicionaAto(Ato ato) {this.atos.put(ato.getId(), ato);}
    public void removeAto(String id) {this.atos.remove(id);}

    public Map<String, Receita> getReceitas() {return receitas;}
    public void setReceitas(Map<String, Receita> receitas) {this.receitas = receitas;}
    public void adicionaReceita(Receita receita) {this.receitas.put(receita.getId(), receita);}
    public void removeReceita(String id) {this.receitas.remove(id);}

    @Override
    public String toString() {
        return super.toString() +
                "Profissão: " + profissao + "\n";
    }
}