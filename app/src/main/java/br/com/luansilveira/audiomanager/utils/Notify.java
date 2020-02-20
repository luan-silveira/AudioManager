package br.com.luansilveira.audiomanager.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class Notify {

    public static final String ID_PADRAO_CANAL = "padrao";
    public static final String DESCRICAO_PADRAO_CANAL = "Padrão";

    private Context context;
    private Notification.Builder builder;
    private NotificationManager manager;
    private NotificationChannel channel;

    private Notify(Context context) {
        this(context, (NotificationChannel) null);
    }

    @SuppressLint("WrongConstant")
    private Notify(Context context, NotificationChannel channel) {
        this.context = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (channel == null)
                channel = new NotificationChannel(ID_PADRAO_CANAL, DESCRICAO_PADRAO_CANAL, NotificationManager.IMPORTANCE_DEFAULT);
            this.channel = channel;
            this.builder = new Notification.Builder(context, channel.getId());
        } else {
            this.builder = new Notification.Builder(context);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Notify(Context context, String idCanal) {
        this.context = context;
        this.channel = new NotificationChannel(ID_PADRAO_CANAL, DESCRICAO_PADRAO_CANAL, NotificationManager.IMPORTANCE_DEFAULT);
        this.builder = new Notification.Builder(context, channel.getId());

    }

    private static Notify from(Context context) {
        return new Notify(context);
    }
}
