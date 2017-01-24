package com.durante.fabrizio.myaccountant;

/**
 * Created by Fabrizio on 24/01/2017.
 */

public class Dato {
    private double giorno;
    private double importo;

    public Dato(double giorno, double importo) {
        this.giorno = giorno;
        this.importo = importo;
    }

    public double getGiorno() {
        return giorno;
    }

    public void setGiorno(double giorno) {
        this.giorno = giorno;
    }

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }
}
