package br.com.luansilveira.audiomanager;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import br.com.luansilveira.audiomanager.db.DB;
import br.com.luansilveira.audiomanager.db.Model.Horario;
import br.com.luansilveira.audiomanager.utils.DateCalendar;

public class HorarioActivity extends AppCompatActivity {

    public static final String EXTRA_HORARIO = "horario";

    TimePicker timePickerInicial;
    TimePicker timePickerFinal;
    CheckBox ckVibrar;

    private Horario horario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        timePickerInicial = findViewById(R.id.timePickerInicial);
        timePickerFinal = findViewById(R.id.timePickerFinal);
        boolean is24Hour = DateFormat.is24HourFormat(this);
        timePickerInicial.setIs24HourView(is24Hour);
        timePickerFinal.setIs24HourView(is24Hour);
        ckVibrar = findViewById(R.id.ckVibrar);

        this.horario = (Horario) getIntent().getSerializableExtra(EXTRA_HORARIO);
        if (this.horario != null){
            Hora horaInicial = horario.getHoraInicial();
            Hora horaFinal = horario.getHoraFinal();
            this.timePickerInicial.setHour(horaInicial.getHora());
            this.timePickerInicial.setMinute(horaInicial.getMinuto());
            this.timePickerFinal.setHour(horaFinal.getHora());
            this.timePickerFinal.setMinute(horaFinal.getMinuto());
            this.ckVibrar.setChecked(horario.getModo() == Horario.MODO_VIBRAR);
            setTitle("Editar horário modo silencioso");
        } else {
            this.timePickerFinal.setHour(DateCalendar.now().addHours(12).getHour());
            setTitle("Novo horário modo silencioso");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void btSalvarClick(View view) {
        Hora horaInicial = this.getHora(timePickerInicial);
        Hora horaFinal = this.getHora(timePickerFinal);
        int modo = ckVibrar.isChecked() ? Horario.MODO_VIBRAR : Horario.MODO_SILENCIOSO;

        try {
            if (this.horario == null) this.horario = new Horario();
            this.horario.setMinutoInicial(horaInicial.getTotalMinutos()).setMinutoFinal(horaFinal.getTotalMinutos())
                    .setModo(modo);
            Dao<Horario, ?> dao = DB.get(this).getDao(Horario.class);
            if (this.hasHorarioConcorrente(dao, horario)) {
                new AlertDialog.Builder(this).setTitle("Horário concorrente").setMessage("Já há um horário cadastrado dentro do período atual!")
                        .setPositiveButton("OK", null).show();
                return;
            }
            dao.createOrUpdate(horario);

            Toast.makeText(this, "Horário salvo", Toast.LENGTH_LONG).show();
            setResult(RESULT_OK);
            finish();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean hasHorarioConcorrente(Dao<Horario, ?> dao, Horario horario) throws SQLException {

        List<Horario> horarios = dao.queryForAll();
        for (Horario h : horarios) {
            if (h.getMinutoInicial() < horario.getMinutoFinalReal() ||
                    h.getMinutoFinalReal() > horario.getMinutoInicial()) return true;
        }
        return false;
    }

    public void btCancelarClick(View view) {
        onBackPressed();
    }

    private Hora getHora(TimePicker picker) {
        return new Hora(picker.getHour(), picker.getMinute());
    }
}
