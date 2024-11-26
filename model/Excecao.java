package projeto.model;

import java.io.Serializable;

public class Excecao extends java.lang.Exception implements Serializable {
    public Excecao(String message) {
        super(message);
    }
}
