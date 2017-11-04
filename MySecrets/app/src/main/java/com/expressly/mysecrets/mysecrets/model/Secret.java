package com.expressly.mysecrets.mysecrets.model;

/**
 * Created by gustavo on 28/07/2017.
 */

public class Secret {
    private String conteudo;
    private String data;

    public Secret(String conteudo, String data) {
        this.conteudo = conteudo;
        this.data = data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
