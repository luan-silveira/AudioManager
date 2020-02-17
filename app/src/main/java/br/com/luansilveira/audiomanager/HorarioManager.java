package br.com.luansilveira.audiomanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import br.com.luansilveira.audiomanager.db.Model.Horario;
import br.com.luansilveira.audiomanager.utils.DateCalendar;

public abstract class HorarioManager {

    public static void agendarHorario(Context context, Horario horario) {
        agendarHorario(context, horario, false);
    }

    public static void agendarHorario(Context context, Horario horario, boolean cancelar) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntentInicial = PendingIntent.getBroadcast(context, horario.getId(), new Intent(context, HorarioReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntentFinal = PendingIntent.getBroadcast(context, horario.getId(), new Intent(context, HorarioReceiver.class).putExtra("final", true), PendingIntent.FLAG_UPDATE_CURRENT);
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

}
