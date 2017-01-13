package com.durante.fabrizio.myaccountant;

/**
 * Created by Fabrizio on 30/12/2016.
 */
public class ElementoListaMovimenti {
    private String NomeC;
    private String Categoria;
    private float Importo;

    public ElementoListaMovimenti(String nomeC, String categoria, float imp) {
        NomeC = nomeC;
        Categoria = categoria;
        Importo = imp;
    }

    public String getNomeC() {
        return NomeC;
    }

    public void setNomeC(String nomeC) {
        NomeC = nomeC;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public float getImporto() {
        return Importo;
    }

    public void setImporto(float imp) {
        Importo = imp;
    }
}
