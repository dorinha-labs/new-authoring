package org.uberfire.ext.aut.api;

import org.jboss.errai.common.client.api.annotations.Portable;

@Portable
public class Project {

    private String nome;


    public Project() {
    }

    public String getNome() {
        return nome;
    }


    public Project( String nome ) {
        this.nome = nome;
    }
}