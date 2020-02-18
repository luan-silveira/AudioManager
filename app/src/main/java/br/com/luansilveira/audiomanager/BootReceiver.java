package br.com.luansilveira.audiomanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

import java.sql.SQLException;

import br.com.luansilveira.audiomanager.db.DB;
import br.com.luansilveira.audiomanager.db.Model.Horario;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        try {
            Dao<Horario, ?> dao = DB.get(context).getDao(Horario.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
