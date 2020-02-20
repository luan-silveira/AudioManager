package br.com.luansilveira.audiomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;
import java.util.List;

import br.com.luansilveira.audiomanager.db.DB;
import br.com.luansilveira.audiomanager.db.Model.Horario;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Dao<Horario, ?> dao = DB.get(context).getDao(Horario.class);
            List<Horario> horarios = dao.queryForAll();
            if (HorarioManager.from(context).isManagerAtivado()) {
                HorarioManager.agendarHorarios(context, horarios);
                Toast.makeText(context, "Horários Silencioso/Vibratório reagendados!", Toast.LENGTH_LONG).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
