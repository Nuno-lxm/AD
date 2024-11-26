package projeto.model.classes;

import projeto.model.Excecao;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Fornecedor implements Serializable {
    private String id;
    private String nome;
    private String contacto;
    private String email;
    private Map<String, Medicamento> medicamentos;
    private Map<String, Encomenda> encomendas;

    public Fornecedor(String nome) throws Excecao {
        if (nome == null || nome.isEmpty()) {
            throw new Excecao("Nome vazio.");
        }
        this.id = UUID.randomUUID().toString();
        this.nome = nome;
        this.encomendas = new HashMap<>();
    }
    
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}

    public String getContacto() {return contacto;}
    public void setContacto(String contacto) throws Excecao {
        if (!contacto.matches("\\d{9}")) {
            throw new Excecao("Contacto inválido. Deve conter 9 dígitos.");
        }
        this.contacto = contacto;
    }

    public String getEmail() {return email;}
    public void setEmail(String email) throws Excecao {
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")) {
            throw new Excecao("Email inválido.");
        }
        this.email = email;
    }

    public Map<String, Medicamento> getMedicamentos() {return medicamentos;}
    public void setMedicamentos(Map<String, Medicamento> medicamentos) {this.medicamentos = medicamentos;}
    public void adicionaMedicamento(Medicamento medicamento) {this.medicamentos.put(medicamento.getId(), medicamento);}
    public void removeMedicamento(String id) {this.medicamentos.remove(id);}

    public Map<String, Encomenda> getEncomendas() {return encomendas;}
    public void setEncomendas(Map<String, Encomenda> encomendas) {this.encomendas = encomendas;}
    public void adicionaEncomenda(Encomenda encomenda) {this.encomendas.put(encomenda.getId(), encomenda);}
    public void removeEncomenda(String id) {this.encomendas.remove(id);}

    @Override
    public String toString() {
        return
                "Fornecedor [id=" + id + ", nome=" + nome + ", contacto=" + contacto + ", email=" + email + "]";
    }
}
