package br.com.luansilveira.audiomanager;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import br.com.luansilveira.audiomanager.utils.Notify;

public class App extends Application {

    private static App instance;
    public static String CANAL_NOTIFICACAO = "notificacoes";

    @Override
    public void onCreate() {
        super.onCreate();
        Notify.from(this).criarCanalNotificacao(CANAL_NOTIFICACAO, "Notificações diversas", 5);
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public static void mostrarNotificacao(Intent intent, String titulo, String mensagem) {
        Notify.from(getInstance()).criarNotificacao(intent, R.drawable.ic_ring, titulo, mensagem, CANAL_NOTIFICACAO, true);
    }

    public static void mostrarNotificacao(PendingIntent pendingIntent, String titulo, String mensagem) {
        Notify.from(getInstance()).criarNotificacao(pendingIntent, R.drawable.ic_ring, titulo, mensagem, CANAL_NOTIFICACAO, true);
    }
}
