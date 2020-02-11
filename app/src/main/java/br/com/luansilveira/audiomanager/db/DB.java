package br.com.luansilveira.audiomanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.luansilveira.audiomanager.db.Model.Horario;

public class DB extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "audiomamager_horarios.db";
    private static final int DATABASE_VERSION = 1;

    private static DB mInstance;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DB get(Context context) {
        if (mInstance == null) {
            mInstance = new DB(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Horario.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
