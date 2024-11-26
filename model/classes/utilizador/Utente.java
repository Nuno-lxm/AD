package projeto.model.classes.utilizador;

import projeto.model.Excecao;
import projeto.model.classes.Ato;
import projeto.model.classes.receitas.Receita;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

public class Utente extends Utilizador implements Serializable {
    private LocalDate dataNascimento;
    private String morada;
    private String alergias;
    private String condicoesCronicas;
    private String grupoSanguineo;
    private Map<String, Ato> atos;
    private Map<String, Receita> receitas;

    public Utente(String nome, String apelido, String cc) throws Excecao {
        super(nome, apelido, cc);
        this.atos = new HashMap<>();
    }

    public LocalDate getDataNascimento() {return dataNascimento;}

    public void setDataNascimento(String dataString) throws Excecao{
        try {
            // Configurar o formato esperado (DD/MM/YYYY)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate data = LocalDate.parse(dataString, formatter);

            // Verificar se a data é no futuro
            if (data.isAfter(LocalDate.now())) {
                throw new Excecao("Erro: Data de nascimento não pode ser no futuro.");
            }
            this.dataNascimento = data;
        } catch (DateTimeParseException e) {
            throw new Excecao("Erro: Data de nascimento inválida! Certifique-se de usar o formato DD/MM/YYYY.");
        }
    }

    public Integer calcularIdade() {
        if (dataNascimento == null || dataNascimento.isAfter(LocalDate.now())) {return null;}
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public String getMorada() {return morada;}
    public void setMorada(String morada) {this.morada = morada;}

    public String getAlergias() {return alergias;}
    public void setAlergias(String alergias) {this.alergias = alergias;}

    public String getCondicoes() {return condicoesCronicas;}
    public void setCondicoes(String condicoes) {this.condicoesCronicas = condicoesCronicas;}

    public String getGrupoSanguineo() {return grupoSanguineo;}

    public void setGrupoSanguineo(String grupoSanguineo) throws Excecao {
        if (!grupoSanguineo.matches("^(A|B|AB|O)[+-]$")) {
            throw new Excecao("Erro: Grupo sanguíneo inválido! Use um dos formatos: A+, A-, B+, B-, AB+, AB-, O+, O-.");
        } else {
            this.grupoSanguineo = grupoSanguineo.trim();
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
                "Idade: " + calcularIdade() + "\n" +
                "Morada: " + morada + "\n" +
                "Alergias: " + alergias + "\n" +
                "Condições Crónicas: " + condicoesCronicas + "\n" +
                "Grupo Sanguíneo: " + grupoSanguineo + "\n";

    }
}