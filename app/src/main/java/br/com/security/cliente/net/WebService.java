package br.com.security.cliente.net;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import br.com.security.cliente.application.MyApplication;
import br.com.security.cliente.application.Session;

/**
 * Created by mariomartins on 15/09/17.
 */

public class WebService {

    public static final String TAG = "SecurityRESTful";
    public static final String URI = "/app-clientes";
    public static final String GET_CLIENTE = URI;
    public static final String GET_CHECKINS = URI + "/buscar-checkins";
    private static final String GET_FOTO_CHECKIN = "/checkins/foto/";

    private static RequestQueue requestQueue;

    private static RequestQueue getRequestQueue(final Context context) {
        return requestQueue == null ? Volley.newRequestQueue(context) : requestQueue;
    }

    public static void getSyncCheckins(final Context context, Response.Listener listener, Response.ErrorListener errorListener) {
        Long id = Session.newInstance().getUserPreferences().getId();

        RequestQueue queue = getRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, MyApplication.HOST.concat(GET_CHECKINS).concat(String.format("/%s", String.valueOf(id))).concat(String.format("/%s", String.valueOf(Session.newInstance().getDateSyncCheckins().getTime()))), listener, errorListener);
        request.setTag(TAG);
        queue.add(request);
    }

    public static void getCliente(final Context context, String phone, Response.Listener listener, Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, MyApplication.HOST.concat(GET_CLIENTE).concat(String.format("/%s", phone.trim())), listener, errorListener);
        request.setTag(TAG);
        queue.add(request);
    }

    public static void getCheckinFoto(final Context context, Long id, Response.Listener<Bitmap> listener, Response.ErrorListener errorListener) {
        RequestQueue queue = getRequestQueue(context);
        ImageRequest imageRequest = new ImageRequest(MyApplication.HOST.concat(GET_FOTO_CHECKIN).concat(String.valueOf(id)), listener, 0, 0, null, Bitmap.Config.RGB_565, errorListener);

        queue.add(imageRequest);
    }
}
