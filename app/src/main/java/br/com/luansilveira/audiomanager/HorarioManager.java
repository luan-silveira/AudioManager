package br.com.luansilveira.audiomanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

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
        PendingIntent pendingIntentInicial = PendingIntent.getBroadcast(context, horario.getId(), new Intent(context, HorarioReceiver.class).putExtra(HorarioReceiver.EXTRA_MODO, horario.getModo()), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentFinal = PendingIntent.getBroadcast(context, horario.getId(), new Intent(context, HorarioReceiver.class).putExtra(HorarioReceiver.EXTRA_FINAL, true), PendingIntent.FLAG_UPDATE_CURRENT);
        if (cancelar) {
            manager.cancel(pendingIntentInicial);
            manager.cancel(pendingIntentFinal);
        } else {
            int intervalo = 60 * 60 * 24 * 1000; //Intervalo 24 horas
            Hora horaInicial = horario.getHoraInicial();
            Hora horaFinal = horario.getHoraFinal();
            DateCalendar calendarInicial = DateCalendar.today().setHour(horaInicial.getHora()).setMinute(horaInicial.getMinuto());
            DateCalendar calendarFinal = DateCalendar.today().setHour(horaFinal.getHora()).setMinute(horaFinal.getMinuto());
            if (horaFinal.getTotalMinutos() < horaInicial.getTotalMinutos())
                calendarFinal.addDays(1);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendarInicial.getTimeMillis(), intervalo, pendingIntentInicial);
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendarFinal.getTimeMillis(), intervalo, pendingIntentFinal);
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
