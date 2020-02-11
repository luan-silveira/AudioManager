package br.com.luansilveira.audiomanager.db.Model;

import com.j256.ormlite.field.DataType;
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
        this.minutoInicial = horaInicial.getTotalMinutos();
        this.minutoFinal= horaFinal.getTotalMinutos();
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

    public Horario setMinutoFinal(int minutoFinal) {
        this.minutoFinal = minutoFinal;
        return this;
    }

    public Hora getHoraInicial() {
        return new Hora(this.minutoInicial);
    }

    public Hora getHoraFinal(){
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
