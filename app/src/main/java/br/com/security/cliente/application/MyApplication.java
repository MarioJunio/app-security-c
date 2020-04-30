package br.com.security.cliente.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.security.cliente.syncronizable.SyncService;

/**
 * Created by mariomartins on 24/09/17.
 */

public class MyApplication extends Application {

    public static Context context;
//    public static final String HOST = "http://192.168.100.6:22016";
    public static final String HOST = "http://datasecurity.ddns.net:22016";
//    public static final String HOST = "http://18.228.31.137:9090";

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static void requestSyncService(Context context, final String syncType) {
        Bundle bundle = new Bundle();
        bundle.putString(SyncService.ARG_SYNC_TYPE, syncType);

        Intent mServiceIntent = new Intent(context, SyncService.class);
        mServiceIntent.putExtras(bundle);

        context.startService(mServiceIntent);
    }

    public static void closeKeyboard(AppCompatActivity activity) {

        View view = activity.getCurrentFocus();

        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    public static Date parseDate(String dateFormatted) throws ParseException {
        return dateFormat.parse(dateFormatted);
    }

    public static String packToolbarTitle(String title) {
        return title.split("\\s+")[0];
    }

}
