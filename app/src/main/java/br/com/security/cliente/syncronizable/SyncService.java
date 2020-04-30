package br.com.security.cliente.syncronizable;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.sql.SQLException;
import java.util.Date;

import br.com.security.cliente.application.Session;
import br.com.security.cliente.dao.CheckinDAO;
import br.com.security.cliente.model.Checkin;
import br.com.security.cliente.net.WebService;
import br.com.security.cliente.stub.SyncCheckins;
import br.com.security.cliente.utils.Constants;
import br.com.security.cliente.utils.GsonUTCDateAdapter;

/**
 * Created by mariomartins on 14/09/17.
 */

public class SyncService extends IntentService {

    public static final String ARG_SYNC_TYPE = "sync_type";
    public static final String ARG_SYNC_BOTH = "sync_both";

    private CheckinDAO checkinDAO;

    public SyncService() {
        super("SyncService");
    }

    public SyncService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        checkinDAO = CheckinDAO.getInstance(getApplicationContext());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String syncType = intent.getExtras().getString(ARG_SYNC_TYPE);

        if (syncType.equals(ARG_SYNC_BOTH))
            syncronizeCheckins();
    }

    private void syncronizeCheckins() {

        WebService.getSyncCheckins(getApplicationContext(), new Response.Listener() {

            @Override
            public void onResponse(Object response) {

                SyncCheckins syncCheckins = new GsonBuilder()
                        .registerTypeAdapter(Date.class, new GsonUTCDateAdapter())
                        .create()
                        .fromJson(response.toString(), new TypeToken<SyncCheckins>() {}.getType());

                // salva todos os checkins no banco local
                for (Checkin checkin : syncCheckins.getListAppClienteCheckin()) {

                    try {
                        checkinDAO.save(checkin);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

                // atualiza a última data de atualização dos checkins
                Session.newInstance().saveDataSyncCheckins(new Date(syncCheckins.getTimeSync()));

                try {
                    Thread.sleep(500l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                done();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v(getClass().getName(), "Exception Volley: " + error.networkResponse.statusCode);
                done();
            }
        });

    }

    private void done() {
        Intent localIntent = new Intent(Constants.BROADCAST_SYNC_SERVICE_ALL);
        localIntent.putExtra(Constants.SYNC_SERVICE_DATA_CHECKINS, Constants.SYNC_SERVICE_DATA_CHECKINS_VALUE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

}
