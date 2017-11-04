package com.expressly.mysecrets.mysecrets.model;

import java.util.List;

/**
 * Created by gustavo on 29/07/2017.
 */

public class User {

    private String email;
    private List<Scrap> scraps;
    private String nome;

    public User(){

    }

    public User(String email, String nome) {
        this.email = email;
        this.nome = nome;
    }

    public User(String email, List<Scrap> scraps, String nome) {
        this.email = email;
        this.scraps = scraps;
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Scrap> getScraps() {
        return scraps;
    }

    public void setScraps(List<Scrap> scraps) {
        this.scraps = scraps;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
