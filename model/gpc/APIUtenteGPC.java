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

public interface APIUtenteGPC extends Remote {


    /* novas funções de procura que so mostram resultados do uetente em questao
    id passado no inicio da app
    procura AtoProfissional, por exemplo, passsa a receber tb lista de atos que pode ser os atos todos
    na implementacao default e os atos do uetnte na especifica
     */
    List<String> procuraAtoUtente(Utente utente) throws RemoteException;
    List<String> procuraAtoProfissional(Profissional profissional) throws RemoteException;
    List<String> procuraAtoTipo(String tipo) throws RemoteException;
    List<String> procuraAtoData(LocalDateTime data) throws RemoteException;
    List<String> procuraReceitaUtente(Utente utente) throws RemoteException;
    List<String> procuraReceitaProfissional(Profissional profissional) throws RemoteException;
    void alteraUtenteContacto(String id, String contacto) throws RemoteException;

    // ver como está o historico
    List<String> getHistoricoUtente(String id) throws RemoteException;
    List<String> procuraProfissional(String nome) throws RemoteException;
    List<String> procuraProfissionalApelido(String apelido) throws RemoteException;
    List<String> procuraProfissionalEspecialidade(String especialidade) throws RemoteException;

    int numAtosPorProfissional(String idProfissional) throws RemoteException;
    String atosPorProfissional() throws RemoteException;
    Map<String, Map<String, Integer>> atosPorProfissionalPorTipo() throws RemoteException;

    Map<String, Integer> atosPorPeriodoPorTipo(LocalDateTime inicio, LocalDateTime fim) throws RemoteException;
}
