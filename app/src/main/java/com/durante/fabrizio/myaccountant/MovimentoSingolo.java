package com.durante.fabrizio.myaccountant;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by Fabrizio on 10/01/2017.
 */

public class MovimentoSingolo {
    private String Luogo, Categoria, Data, ID;
    private float Importo;

    public MovimentoSingolo(String id, String data, String categoria, float importo, String luogo) {
        Luogo=luogo;
        Categoria = categoria;
        Data = data;
        Importo = importo;
        ID=id;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
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
