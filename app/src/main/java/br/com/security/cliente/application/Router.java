package br.com.security.cliente.application;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.security.cliente.activities.CheckSMS;
import br.com.security.cliente.activities.DashboardActivity;
import br.com.security.cliente.activities.LoginActivity;
import br.com.security.cliente.activities.SplashScreenActivity;
import br.com.security.cliente.stub.UserPreferences;

/**
 * Created by mariomartins on 12/09/17.
 */

public class Router {

    public static void goSplashScreenView(AppCompatActivity activity) {
        Intent intent = new Intent(activity, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void goDashboardView(AppCompatActivity activity) {
        Intent intent = new Intent(activity, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void goAuthView(AppCompatActivity activity) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void goCheckSmsView(AppCompatActivity activity, UserPreferences userPreferences) {
        Intent intent = new Intent(activity, CheckSMS.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putSerializable(CheckSMS.ARG_USER, userPreferences);
        intent.putExtras(bundle);

        activity.startActivity(intent);
    }

}
