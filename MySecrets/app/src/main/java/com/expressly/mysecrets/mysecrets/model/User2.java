package com.expressly.mysecrets.mysecrets.model;

/**
 * Created by gustavo on 30/07/2017.
 */

public class User2 {
    private  String email;
    private String nome;

    public User2(String email, String nome) {
        this.email = email;
        this.nome = nome;
    }
    public User2() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
