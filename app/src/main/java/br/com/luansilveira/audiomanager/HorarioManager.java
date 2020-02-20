package br.com.luansilveira.audiomanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.List;

import br.com.luansilveira.audiomanager.db.Model.Horario;
import br.com.luansilveira.audiomanager.utils.DateCalendar;

public class HorarioManager {

    private Context context;

    private HorarioManager(Context context) {
        this.context = context;
    }

    public static HorarioManager from(Context context) {
        return new HorarioManager(context);
    }

    public static void agendarOuCancelarHorarios(Context context, List<Horario> horarios, boolean cancelar) {
        for (Horario h : horarios) {
            HorarioManager.agendarHorario(context, h, cancelar);
        }
    }

    public static void agendarHorarios(Context context, List<Horario> horarios) {
        agendarOuCancelarHorarios(context, horarios, false);
    }

    public static void cancelarHorarios(Context context, List<Horario> horarios) {
        agendarOuCancelarHorarios(context, horarios, true);
    }


    private SharedPreferences getPrefs() {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    private void setPrefsAtivarManager(boolean ativar) {
        SharedPreferences.Editor editor = this.getPrefs().edit();
        editor.putBoolean("ativar", ativar).apply();
    }

    public static void agendarHorario(Context context, Horario horario) {
        agendarHorario(context, horario, false);
    }

    public static void agendarHorario(Context context, Horario horario, boolean cancelar) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntentInicial = PendingIntent.getBroadcast(context, 1, new Intent(context, HorarioReceiver.class).putExtra(HorarioReceiver.EXTRA_MODO, horario.getModo()), 0);
        PendingIntent pendingIntentFinal = PendingIntent.getBroadcast(context, 2, new Intent(context, HorarioReceiver.class).putExtra(HorarioReceiver.EXTRA_FINAL, true), 0);
        Hora horaInicial = horario.getHoraInicial();
        Hora horaFinal = horario.getHoraFinal();

        if (cancelar) {
            manager.cancel(pendingIntentInicial);
            manager.cancel(pendingIntentFinal);
            Log.d("HorarioManager", "Horário cancelado: " + horaInicial + " - " + horaFinal);
        } else {
            DateCalendar calendarInicial = DateCalendar.today().setHour(horaInicial.getHora()).setMinute(horaInicial.getMinuto());
            DateCalendar calendarFinal = DateCalendar.today().setHour(horaFinal.getHora()).setMinute(horaFinal.getMinuto());
            Log.d("HorarioManager", "Data Inicial: " + calendarInicial);
            Log.d("HorarioManager", "Data Final: " + calendarFinal);
            if (horaFinal.getTotalMinutos() < horaInicial.getTotalMinutos())
                calendarFinal.addDays(1);

            manager.setRepeating(AlarmManager.RTC, calendarInicial.getTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntentInicial);
            manager.setRepeating(AlarmManager.RTC, calendarFinal.getTimeMillis(), AlarmManager.INTERVAL_DAY, pendingIntentFinal);

            Log.d("HorarioManager", "Horário agendado: " + horaInicial + " - " + horaFinal);
        }
    }

    public static void cancelarAgendamentoHorario(Context context, Horario horario) {
        agendarHorario(context, horario, true);
    }

    public boolean isManagerAtivado() {
        return this.getPrefs().getBoolean("ativar", false);
    }

    public void ativarHorarioManager(boolean ativar) {
        this.setPrefsAtivarManager(ativar);
    }

}
