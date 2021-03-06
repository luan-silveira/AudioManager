package br.com.luansilveira.audiomanager.db.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

import br.com.luansilveira.audiomanager.Hora;

@DatabaseTable
public class Horario implements Serializable {

    public static final int MODO_SILENCIOSO = 0;
    public static final int MODO_VIBRAR = 1;

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int minutoInicial;
    @DatabaseField
    private int minutoFinal;

    @DatabaseField
    private int modo = MODO_SILENCIOSO;

    public Horario() {
    }

    public Horario(Hora horaInicial, Hora horaFinal) {
        this(horaInicial, horaFinal, MODO_SILENCIOSO);
    }

    public Horario(Hora horaInicial, Hora horaFinal, int modo) {
        this.setHoraInicial(horaInicial);
        this.setHoraFinal(horaFinal);
        this.modo = modo;
    }

    public int getId() {
        return id;
    }

    public Horario setId(int id) {
        this.id = id;
        return this;
    }

    public int getMinutoInicial() {
        return minutoInicial;
    }

    public Horario setMinutoInicial(int minutoInicial) {
        this.minutoInicial = minutoInicial;
        return this;
    }

    public int getMinutoFinal() {
        return minutoFinal;
    }

    public int getMinutoFinalReal() {
        if (this.minutoFinal < this.minutoInicial) return this.minutoFinal + 1440;
        return this.minutoFinal;
    }

    public Horario setMinutoFinal(int minutoFinal) {
        this.minutoFinal = minutoFinal;
        return this;
    }

    public Horario setHoraInicial(Hora horaInicial) {
        this.minutoInicial = horaInicial.getTotalMinutos();
        return this;
    }

    public Horario setHoraFinal(Hora horaFinal) {
        this.minutoFinal = horaFinal.getTotalMinutos();
        return this;
    }

    public Hora getHoraInicial() {
        return new Hora(this.minutoInicial);
    }

    public Hora getHoraFinal() {
        return new Hora(this.minutoFinal);
    }

    public int getModo() {
        return modo;
    }

    public Horario setModo(int modo) {
        this.modo = modo;
        return this;
    }

}
