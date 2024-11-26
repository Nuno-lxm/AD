package projeto.app;

import projeto.model.Excecao;
import projeto.model.classes.Ato;
import projeto.model.classes.utilizador.Profissional;
import projeto.model.classes.utilizador.Utente;
import projeto.model.gpc.APIGPC;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.Map;

public class TesteGPC {
    public static void main(String[] args) {
        try {
            // Conectar ao serviço GPC via RMI
            APIGPC gpc = (APIGPC) Naming.lookup("rmi://localhost:50001/GPC");

            Utente quim = gpc.createUtente("quim", "barreiros", "123456789");

            Profissional toze = gpc.createProfissional("toze", "manuelino", "123456789");

            String id_brufen = gpc.getIdMedicamento("brufen");

            Ato ato = criarAtoAPP(gpc.createAto(quim, toze, "Dispensa", LocalDateTime.now(), id_brufen));

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Erro de RMI: " + e.getMessage());
        } catch (Excecao e) {
            throw new RuntimeException(e);
        }
    }

    public static Ato criarAtoAPP(Map<Ato, Integer> mapa) {
        int thresh = mapa.values().iterator().next();
        if (thresh > 0) {
            System.out.println("Cuidado, stock abaixo de " + thresh); }

        return mapa.keySet().iterator().next();
    }
}