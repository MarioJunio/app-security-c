package br.com.security.cliente.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import br.com.security.cliente.application.MyApplication;
import br.com.security.cliente.application.Router;
import br.com.security.cliente.net.WebService;
import br.com.security.cliente.stub.UserPreferences;
import br.com.security.cliente.utils.AppDialog;

public class LoginActivity extends AppCompatActivity {

    private EditText edPhone;
    private ImageButton btnOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.edPhone = findViewById(R.id.ed_phone);
        this.btnOk = findViewById(R.id.btn_ok);

        init();
    }

    public void init() {

        this.btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String phone = edPhone.getText().toString().replaceAll("[^0-9]+", "");

                if (phone.isEmpty()) {
                    edPhone.setError("Informe seu número");
                    edPhone.requestFocus();
                    return;
                }

                MyApplication.closeKeyboard(LoginActivity.this);

                checarNumero(phone);
            }
        });
    }

    private void checarNumero(String phone) {

        final ProgressDialog progress = ProgressDialog.show(LoginActivity.this, "Aguarde",
                "Autorizando dispositivo....", true);
        progress.show();

        WebService.getCliente(getApplicationContext(), phone, new Response.Listener() {

            @Override
            public void onResponse(Object response) {

                UserPreferences userPreferences = new GsonBuilder().create().fromJson(response.toString(), new TypeToken<UserPreferences>() {
                }.getType());

                progress.dismiss();

                Router.goCheckSmsView(LoginActivity.this, userPreferences);

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                AppDialog.showDialog(LoginActivity.this, null, "Seu número não consta em nossa base de dados, caso o número esteja correto, entre em contato para mais informações", "Ok", null, null, null);
                progress.dismiss();
            }
        });

    }
}
