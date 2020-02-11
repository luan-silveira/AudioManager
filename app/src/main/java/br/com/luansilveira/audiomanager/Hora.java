package br.com.luansilveira.audiomanager;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;


/**
 * Classe utilizada para representar horas no formato hh:mm, com conversão para número inteiro de minutos (Ex.: 08:20 = 500 min; 21:30 = 1290 min).
 */
public class Hora {

    private int hora = 0;
    private int minuto = 0;

    public Hora() {
    }

    public Hora(int hora, int minuto) {
        this.setHora(hora);
        this.setMinuto(minuto);
    }

    public Hora(int minutos) {
        this.hora = minutos / 60;
        this.minuto = minutos % 60;
    }

    public int getHora() {
        return hora;
    }

    public Hora setHora(int hora) {
        this.hora = (hora > 23) ? 23 : hora;
        return this;
    }

    public Hora addHora(int qtde){
        this.hora += qtde;
        return this;
    }

    public Hora addMinuto(int qtde){
        this.minuto += qtde;
        return this;
    }

    public int getMinuto() {
        return minuto;
    }

    public int getTotalMinutos(){
        return this.hora * 60 + this.minuto;
    }

    public Hora setMinuto(int minuto) {
        this.minuto = (minuto > 59) ? 59 : minuto;
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(this.hora) + ":" + decimalFormat.format(this.minuto);
    }
}
