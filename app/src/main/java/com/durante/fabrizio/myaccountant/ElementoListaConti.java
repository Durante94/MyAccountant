package com.durante.fabrizio.myaccountant;

/**
 * Created by Fabrizio on 29/12/2016.
 */
public class ElementoListaConti {
    private String Nome;
    private float Bilancio;

    public ElementoListaConti(){
        Nome="";
        Bilancio=0;
    }

    public ElementoListaConti(String n, float b){
        Nome=n;
        Bilancio=b;
    }

    public void set_Nome(String n){
        Nome=n;
    }

    public void set_Bil(float b){
        Bilancio=b;
    }

    public String getNome(){
        return Nome;
    }

    public float getBilancio(){
        return Bilancio;
    }
}
