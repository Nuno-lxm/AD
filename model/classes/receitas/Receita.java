package projeto.model.classes.receitas;

import projeto.model.Excecao;
import projeto.model.classes.Medicamento;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Receita implements Serializable {
    private String id;
    private Utente utente;
    private Profissional profissional;
    private List<Prescricao> prescricoes;
    private LocalDateTime dataEmissao;
    private LocalDateTime dataValidade;
    private String descricao;


    // pensar como criar e passar lista
    public Receita(Utente utente, Profissional profissional, LocalDateTime dataValidade, String descricao) throws Excecao {
        if (utente==null){
            throw new Excecao("Utente vazio.");
        }
        if (profissional==null){
            throw new Excecao("Profissional vazio.");
        }
        this.id = UUID.randomUUID().toString();
        this.utente = utente;
        this.profissional = profissional;
        this.dataEmissao = LocalDateTime.now();
        this.dataValidade = dataValidade;
        this.descricao = descricao;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Utente getUtente() {return utente;}
    public void setUtente(Utente utente) {this.utente = utente;}

    public Profissional getProfissional() {return profissional;}
    public void setProfissional(Profissional profissional) {this.profissional = profissional;}

    public List<Prescricao> getPrescricoes() {return prescricoes;}
    public void setPrescricoes(List<Prescricao> prescricoes) {this.prescricoes = prescricoes;}

    public LocalDateTime getDataEmissao() {return dataEmissao;}
    public void setDataEmissao(LocalDateTime dataEmissao) {this.dataEmissao = dataEmissao;}

    public LocalDateTime getDataValidade() {return dataValidade;}
    public void setDataValidade(LocalDateTime dataValidade) {this.dataValidade = dataValidade;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    public void adicionaPrescricao(Prescricao prescricao){prescricoes.add(prescricao);}
}
