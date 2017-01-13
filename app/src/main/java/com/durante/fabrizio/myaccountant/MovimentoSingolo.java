package com.durante.fabrizio.myaccountant;

/**
 * Created by Fabrizio on 10/01/2017.
 */

public class MovimentoSingolo {
    private String Luogo, Categoria, Data;
    private float Importo;

    public MovimentoSingolo(String data, String categoria, float importo, String luogo) {
        Luogo = luogo;
        Categoria = categoria;
        Data = data;
        Importo = importo;
    }

    public String getLuogo() {
        return Luogo;
    }

    public void setLuogo(String luogo) {
        Luogo = luogo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public float getImporto() {
        return Importo;
    }

    public void setImporto(float importo) {
        Importo = importo;
    }
}
