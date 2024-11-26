package projeto.model.classes;

import projeto.model.Excecao;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Ato implements Serializable {
    private String id;
    private Utente utente;
    private Profissional profissional;
    private String tipo;
    private LocalDateTime data;
    private String descricao;

    public Ato(Utente utente, Profissional profissional, String tipo, LocalDateTime data, String descricao) throws Excecao {
        if (utente==null) {
            throw new Excecao("Utente vazio.");
        }
        if (profissional==null) {
            throw new Excecao("Profissional vazio.");
        }
        if (tipo==null) {
            throw new Excecao("Tipo vazio.");
        }
        if (descricao==null) {
            throw new Excecao("Descricao vazia.");
        }

        this.id = UUID.randomUUID().toString();
        this.utente = utente;
        this.profissional = profissional;
        this.tipo = tipo;
        this.data = data;
        this.descricao = descricao;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public Utente getUtente() {return utente;}
    public void setUtente(Utente utente) {this.utente = utente;}

    public Profissional getProfissional() {return profissional;}
    public void setProfissional(Profissional profissional) {this.profissional = profissional;}

    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}

    public LocalDateTime getData() {return data;}
    public void setData(LocalDateTime data) {this.data = data;}

    public String getDescricao() {return descricao;}
    public void setDescricao(String descricao) {this.descricao = descricao;}

    @Override
    public String toString() {
        return "Ato{" +
                "utente = " + utente + "\n" +
                ", profissional = " + profissional + "\n" +
                ", tipo = " + tipo + "\n" +
                ", descricao = " + descricao + "\n" +
                ", data = " + data.toString() + "\n" +
                "}";
    }
}