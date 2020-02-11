package br.com.luansilveira.audiomanager.db;

import android.content.Context;

import com.j256.ormlite.dao.BaseDaoImpl;

import java.sql.SQLException;

import br.com.luansilveira.audiomanager.db.Model.Horario;

public class DaoHorario extends BaseDaoImpl<Horario, Integer> {

    public DaoHorario(Context context, Class<Horario> dataClass) throws SQLException {
        super(DB.get(context).getConnectionSource(), dataClass);
    }

    public boolean hasHorarioConcorrente(Horario horario){
        return false;
    }
}
