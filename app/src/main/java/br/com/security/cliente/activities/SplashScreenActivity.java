package br.com.security.cliente.activities;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import br.com.security.cliente.application.MyApplication;
import br.com.security.cliente.application.Router;
import br.com.security.cliente.application.Session;
import br.com.security.cliente.receivers.SyncStatusReceiver;
import br.com.security.cliente.stub.UserPreferences;
import br.com.security.cliente.syncronizable.SyncService;
import br.com.security.cliente.utils.Constants;
import br.com.security.cliente.utils.DatabaseHelper;

public class SplashScreenActivity extends AppCompatActivity {

    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter syncStatusFilter = new IntentFilter(Constants.BROADCAST_SYNC_SERVICE_ALL);
    private SyncStatusReceiver syncStatusReceiver = new SyncStatusReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_activity);
        localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());

        Log.v(getClass().getName(), "onCreate()");

        // ao alterar status da sincronização
        syncStatusReceiver.setSyncStatusListener(new SyncStatusReceiver.SyncStatusListener() {

            @Override
            public void onStatusChange(Object status) {

                if (status.toString().equals(Constants.SYNC_SERVICE_DATA_CHECKINS_VALUE)) {
                    done(2000l, true);
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final UserPreferences user = Session.newInstance().getUserPreferences();

        // verifica se existe algum usuário na sessão local
        if (user.getId() != null && user.getId().longValue() > 0l) {


            // inicializa banco de dados
            new DatabaseHelper(getApplicationContext()).getWritableDatabase();

            // registra o broadcast receiver para obter o status da sincronziação
            localBroadcastManager.registerReceiver(syncStatusReceiver, syncStatusFilter);

            // inicia sincronização de dados em background através da internet
            MyApplication.requestSyncService(SplashScreenActivity.this, SyncService.ARG_SYNC_BOTH);

        } else {
            done(2000l, false);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (syncStatusReceiver != null)
            localBroadcastManager.unregisterReceiver(syncStatusReceiver);
    }

    @Override
    public void onBackPressed() {
        ActivityCompat.finishAffinity(this);
    }

    private void done(long timeWait, final boolean navigatoToDashboard) {

        new CountDownTimer(timeWait, timeWait) {

            @Override
            public void onTick(long l) {
            }

            @Override
            public void onFinish() {

                if (navigatoToDashboard) {
                    Router.goDashboardView(SplashScreenActivity.this);
                } else {
                    Router.goAuthView(SplashScreenActivity.this);
                }

            }

        }.start();

    }
}
