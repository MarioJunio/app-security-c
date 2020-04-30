package br.com.security.cliente.application;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

import br.com.security.cliente.stub.UserPreferences;

/**
 * Created by mariomartins on 12/09/17.
 */

public class Session {

    public static final String KEY = "user_preferences";

    private SharedPreferences sharedPreferences;

    public static Session newInstance() {
        return new Session();
    }

    public UserPreferences getUserPreferences() {

        createSharedPreferences();

        UserPreferences user = new UserPreferences();
        user.setId(sharedPreferences.getLong(UserPreferences.ID_FIELD, 0l));
        user.setNome(sharedPreferences.getString(UserPreferences.NOME_FIELD, null));
        user.setTelefone1(sharedPreferences.getString(UserPreferences.TELEFONE_FIELD, null));
        user.setDataCriacao(new Date(sharedPreferences.getLong(UserPreferences.DATA_CRIACAO_FIELD, 0l)));
        user.setEmail(sharedPreferences.getString(UserPreferences.DATA_EMAIL_FIELD, null));
        user.setDataSyncCheckins(new Date(sharedPreferences.getLong(UserPreferences.DATA_SYNC_CHECKINS, 0l)));

        return user;
    }

    public void saveUserPreferences(UserPreferences user) {
        createSharedPreferences();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(UserPreferences.ID_FIELD, user.getId() == null ? 0l : user.getId());
        editor.putString(UserPreferences.NOME_FIELD, user.getNome());
        editor.putString(UserPreferences.TELEFONE_FIELD, user.getTelefone1());
        editor.putLong(UserPreferences.DATA_CRIACAO_FIELD, user.getDataCriacao().getTime());
        editor.putString(UserPreferences.DATA_EMAIL_FIELD, user.getEmail());
        editor.commit();
    }

    public void saveDataSyncCheckins(Date date) {
        createSharedPreferences();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(UserPreferences.DATA_SYNC_CHECKINS, date.getTime());
        editor.commit();
    }

    public void clearSession() {
        createSharedPreferences();
        sharedPreferences.edit().clear().commit();
    }

    private void createSharedPreferences() {
        this.sharedPreferences = MyApplication.context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }


    public Date getDateSyncCheckins() {
        createSharedPreferences();
        return new Date(sharedPreferences.getLong(UserPreferences.DATA_SYNC_CHECKINS, 0l));
    }
}
