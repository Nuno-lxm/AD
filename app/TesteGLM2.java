package projeto.app;

import projeto.model.glm.APIGLM;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class TesteGLM2 {
    public static void main(String[] args) {
        try {
            APIGLM glm = (APIGLM) Naming.lookup("rmi://localhost:50001/GLM");

           String id_brufen = glm.procuraMedicamento("brufen", glm.getMedicamentos()).getFirst();

            System.out.println(glm.getMedicamento(id_brufen));

        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            System.out.println("Erro de RMI: " + e.getMessage());
        }
    }
}