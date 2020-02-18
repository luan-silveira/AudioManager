package br.com.luansilveira.audiomanager;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import br.com.luansilveira.audiomanager.db.Model.Horario;

public class HorarioReceiver extends BroadcastReceiver {

    public static final String EXTRA_FINAL = "final";
    public static final String EXTRA_MODO = "modo";

    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        boolean boolFinal = intent.getBooleanExtra(EXTRA_FINAL, false);
        if (boolFinal) {
            manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        } else {
            int modo = intent.getIntExtra(EXTRA_MODO, Horario.MODO_SILENCIOSO);
            manager.setRingerMode(modo == Horario.MODO_SILENCIOSO ? AudioManager.RINGER_MODE_SILENT : AudioManager.RINGER_MODE_VIBRATE);
        }
    }
}
