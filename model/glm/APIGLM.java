package projeto.model.glm;

import projeto.model.Excecao;
import projeto.model.classes.Encomenda;
import projeto.model.classes.Fornecedor;
import projeto.model.classes.Medicamento;
import projeto.model.classes.receitas.Prescricao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/*
API gestor
API profissional (médico/enfermeiro)
API utente
 */

public interface APIGLM extends Remote {
     void confirmaEncomenda(Encomenda encomenda) throws RemoteException;
     int consomeMedicamento(String id, int quantidade) throws RemoteException, Excecao;
     void setStock(String id_med, int stock) throws RemoteException;
     void adicionaPrescricaoMedicamento(Medicamento medicamento, Prescricao prescricao) throws RemoteException, Excecao;
     Medicamento createMedicamento(String nome, Fornecedor fornecedor, String descricao, int stock, int threshold) throws RemoteException;
     Medicamento createMedicamento(String nome, Fornecedor fornecedor, String descricao) throws RemoteException, Excecao;
     Medicamento getMedicamento(String id) throws RemoteException;
     List<String> procuraMedicamento(String nome, Map<String, Medicamento> medicamentos) throws RemoteException;
     List<String> procuraMedicamentoFornecedor(Fornecedor fornecedor, Map<String, Medicamento>medicamentos) throws RemoteException;
     void alteraMedicamentoDescricao(String id, String descricao) throws RemoteException;
     void alteraMedicamentoFornecedor(String id, Fornecedor fornecedor) throws RemoteException;
     void alteraMedicamento(String id, Medicamento medicamento) throws RemoteException;
     Fornecedor createFornecedor(String nome) throws RemoteException, Excecao;
     Fornecedor getFornecedor(String id) throws RemoteException;
     List<String> procuraFornecedor(String nome, Map<String, Fornecedor>fornecedores) throws RemoteException;
     void alteraFornecedorNome(String id, String nome) throws RemoteException;
     Encomenda createEncomenda(Medicamento medicamento, Fornecedor fornecedor, int quantidade) throws RemoteException, Excecao;
     Encomenda getEncomenda(String id) throws RemoteException;
     List<String> procuraEncomendaMedicamento(Medicamento medicamento, Map<String, Encomenda>encomendas) throws RemoteException;
     List<String> procuraEncomendaFornecedor(Fornecedor fornecedor, Map<String, Encomenda>encomendas) throws RemoteException;
     void alteraEncomendaMedicamento(String id, Medicamento medicamento) throws RemoteException;
     void alteraEncomendaFornecedor(String id, Fornecedor fornecedor) throws RemoteException;
     void alteraEncomenda(String id, Encomenda encomenda) throws RemoteException;
     Map<String, Medicamento>getMedicamentos() throws RemoteException;
     Map<String, Fornecedor>getFornecedores() throws RemoteException;
     Map<String, Encomenda>getEncomendas() throws RemoteException;
}