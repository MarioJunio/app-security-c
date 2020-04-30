package br.com.security.cliente.activities;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import br.com.security.cliente.application.MyApplication;
import br.com.security.cliente.application.Router;
import br.com.security.cliente.application.Session;
import br.com.security.cliente.receivers.SmsBroadcastReceiver;
import br.com.security.cliente.stub.UserPreferences;
import br.com.security.cliente.utils.AppDialog;

public class CheckSMS extends AppCompatActivity {

    public static final String ARG_USER = "user";

    private UserPreferences user;

    private CountDownTimer timer;
    private TextView txPhoneNumber;
    private ImageButton btnEditPhone;
    private TextView txTimer;
    private ProgressBar progressBar;
    private EditText edConfirmCode;
    private SmsBroadcastReceiver smsBroadcastReceiver;
    private int attempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_sms);

        user = (UserPreferences) getIntent().getExtras().getSerializable(CheckSMS.ARG_USER);

        txPhoneNumber = findViewById(R.id.phoneNumber);
        btnEditPhone = findViewById(R.id.btn_edit_phone);
        txTimer = findViewById(R.id.tx_time);
        progressBar = findViewById(R.id.progress);
        edConfirmCode = findViewById(R.id.ed_confirm_code);

        registerReceivers();
        initTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        txPhoneNumber.setText(user.getTelefone1());
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceivers();
    }

    private void registerReceivers() {

        smsBroadcastReceiver = new SmsBroadcastReceiver(null, "Security app:");
        smsBroadcastReceiver.setSmsListener(new SmsBroadcastReceiver.SmsListener() {

            @Override
            public void onSmsReceived(String smsReceived) {

                if (user == null || user.getCode() == null)
                    return;

                if (smsReceived != null && !TextUtils.isEmpty(smsReceived) && user.getCode().equals(smsReceived.trim())) {
                    timer.cancel();
                    confirmar();
                }

            }
        });

        // registra receiver para ação de SMS
        registerReceiver(smsBroadcastReceiver, new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION));

    }

    private void initTimer() {

        btnEditPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                AppDialog.showDialog(CheckSMS.this, null, "Você cancelar essa operação para alterar o número?", "Sim", "Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer.cancel();
                        unregisterReceivers();
                        finish();
                    }

                }, null);

            }
        });

        // instancia temporizador
        timer = new CountDownTimer((1000 * 60 * 5), 1000) {

            @Override
            public void onTick(long l) {

                long minutes = (l / 1000) / 60;
                long seconds = (l / 1000) - (minutes * 60);

                txTimer.setText(String.format("%s:%s", minutes, seconds < 10 ? "0" + seconds : seconds));
                progressBar.incrementProgressBy(1);
            }

            @Override
            public void onFinish() {

                AppDialog.showDialog(CheckSMS.this, null, "O código de verificação não foi identificado, a operação atual será finalizada", "OK", null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        timer.cancel();
                        unregisterReceivers();
                        CheckSMS.this.finish();
                    }

                }, null);

            }
        };

        // ao digitar o código de verificação
        edConfirmCode.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                // valida o código digitado pelo usuário
                if (editable.toString().equals(user.getCode())) {
                    timer.cancel();
                    MyApplication.closeKeyboard(CheckSMS.this);
                    confirmar();
                } else if (editable.toString().length() >= 6) {
                    attempts++;

                    if (attempts >= 3) {

                        edConfirmCode.setEnabled(false);
                        AppDialog.showDialog(CheckSMS.this, null, "Você informou o código de verificação incorreto 3 vezes", "OK", null, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CheckSMS.this.finish();
                            }

                        }, null);

                    } else
                        Toast.makeText(CheckSMS.this, "O código informado está incorreto, restam " + (3 - attempts) + " tentativas", Toast.LENGTH_SHORT).show();

                    editable.clear();
                }

            }
        });

        timer.start();
    }

    private void confirmar() {

        timer.cancel();
        unregisterReceivers();

        // salva as preferencias do usuário na sessão atual
        user.setDataCriacao(new Date());
        Session.newInstance().saveUserPreferences(user);

        // Envia para o Splash Screen
        Router.goSplashScreenView(this);

    }

    private void unregisterReceivers() {

        try {

            if (smsBroadcastReceiver != null) {
                unregisterReceiver(smsBroadcastReceiver);
                smsBroadcastReceiver = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
