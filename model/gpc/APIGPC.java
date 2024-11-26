package projeto.model.gpc;

import projeto.model.Excecao;
import projeto.model.classes.Ato;
import projeto.model.classes.receitas.Receita;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/*
API gestor
API fornecedor
 */

public interface APIGPC extends Remote {

     Ato createAto(Utente utente, Profissional profissional, String tipo, LocalDateTime data, String descricao) throws RemoteException, Excecao;
     Ato getAto(String id) throws RemoteException;
     List<String> procuraAtoUtente(Utente utente) throws RemoteException;
     List<String> procuraAtoProfissional(Profissional profissional) throws RemoteException;
     List<String> procuraAtoTipo(String tipo) throws RemoteException;
     List<String> procuraAtoData(LocalDateTime data) throws RemoteException;
     void alteraAtoUtente(String id, Utente utente) throws RemoteException;
     void alteraAtoProfissional(String id, Profissional profissional) throws RemoteException;
     void alteraAtoTipo(String id, String tipo) throws RemoteException;
     void alteraAtoData(String id, LocalDateTime data) throws RemoteException;
     void alteraAtoDescricao(String id, String descricao) throws RemoteException;
     void alteraAto(String id, Ato ato) throws RemoteException;
     Receita createReceita(Utente utente, Profissional profissional, LocalDateTime dataValidade, String descricao) throws RemoteException, Excecao;
     Receita getReceita(String id) throws RemoteException;
     List<String> procuraReceitaUtente(Utente utente) throws RemoteException;
     List<String> procuraReceitaProfissional(Profissional profissional) throws RemoteException;
     Utente createUtente(String nome, String apelido, String cc) throws RemoteException, Excecao;
     Utente getUtente(String id) throws RemoteException;
     List<String> procuraUtente(String nome) throws RemoteException;
     List<String> procuraUtenteApelido(String apelido) throws RemoteException;
     List<String> procuraUtenteCc(String cc) throws RemoteException;
     List<String> procuraUtenteGrupoSanguineo(String grupo) throws RemoteException;
     void alteraUtenteNome(String id, String nome) throws RemoteException;
     void alteraUtenteContacto(String id, String contacto) throws RemoteException;
     void alteraUtenteAlergias(String id, String alergias) throws RemoteException;
     void alteraUtenteCondicoes(String id, String condicoes) throws RemoteException;
     void removeUtente(String id) throws RemoteException;
     List<String> listaTodosUtentes() throws RemoteException;
     List<String> getHistoricoUtente(String id) throws RemoteException;
     Profissional createProfissional(String nome, String apelido, String cc) throws RemoteException, Excecao;
     Profissional getProfissional(String id) throws RemoteException;
     List<String> procuraProfissional(String nome) throws RemoteException;
     List<String> procuraProfissionalApelido(String apelido) throws RemoteException;
     List<String> procuraProfissionalProfissao(String especialidade) throws RemoteException;
     int totalUtentes() throws RemoteException;
     int totalAtosClinicos() throws RemoteException;
     int numAtosPorProfissional(String idProfissional) throws RemoteException;
     String atosPorProfissional() throws RemoteException;
     Map<String, Map<String, Integer>> atosPorProfissionalPorTipo() throws RemoteException;
     int receitasPorUtente(String idUtente) throws RemoteException;
     Map<String, Integer> atosPorPeriodoPorTipo(LocalDateTime inicio, LocalDateTime fim) throws RemoteException;
}
