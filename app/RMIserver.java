package projeto.app;

import projeto.model.glm.APIGLM;
import projeto.model.glm.GLM;
import projeto.model.gpc.APIGPC;
import projeto.model.gpc.GPC;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIserver {
    public static void main(String[] args) {
        try {
            APIGLM glmserver = new GLM();
            Naming.rebind("rmi://localhost:50001/GLM", glmserver);
            System.out.println("GLM server running");

            APIGPC gpcserver = new GPC();
            Naming.rebind("rmi://localhost:50001/GPC", gpcserver);
            System.out.println("GPC server running");

        } catch (RemoteException | MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }
}
