package br.com.security.cliente.filters;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.security.cliente.activities.R;

/**
 * Created by mariomartins on 16/09/17.
 */

public class CheckinFilterDialog extends Dialog implements DatePickerDialog.OnDateSetListener {

    private Activity activity;
    public EditText edFiltroDataInicial, edFiltroDataFinal;
    public CheckBox cbNormal, cbSuspeito, cbPerigo;
    private Button btnConfirmar;
    private ImageView btnClose;
    private OnConfirmListener onConfirmListener;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private int fieldSelectedDate;

    public CheckinFilterDialog(@NonNull Activity context) {
        super(context);
        this.activity = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.dialog_filtro_checkins);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // cria dialogo date picker
        createDatePickerDialog();

        btnClose = findViewById(R.id.btn_close);
        cbNormal = findViewById(R.id.cb_normal);
        cbSuspeito = findViewById(R.id.cb_suspeito);
        cbPerigo = findViewById(R.id.cb_periogo);
        edFiltroDataInicial = findViewById(R.id.ed_filtro_data_inicial);
        edFiltroDataFinal = findViewById(R.id.ed_filtro_data_final);
        btnConfirmar = findViewById(R.id.btn_confirm);

        edFiltroDataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldSelectedDate = 1;
                datePickerDialog.show();
            }
        });

        edFiltroDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fieldSelectedDate = 2;
                datePickerDialog.show();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onConfirmListener != null)
                    onConfirmListener.onConfirm();
            }
        });
    }

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public void setFiltro(FiltroCheckin filtro) {

        if (filtro != null) {

            if (filtro.getCheckedStatus() != null) {

                for (String checked : filtro.getCheckedStatus()) {

                    if (checked.equals("NORMAL"))
                        cbNormal.setChecked(true);
                    else if (checked.equals("SUSPEITO"))
                        cbSuspeito.setChecked(true);
                    else if (checked.equals("PERIGO"))
                        cbPerigo.setChecked(true);

                }
            }

            if (filtro.getDataInicial() != null)
                edFiltroDataInicial.setText(dateFormat.format(filtro.getDataInicial()));

            if (filtro.getDataFinal() != null)
                edFiltroDataFinal.setText(dateFormat.format(filtro.getDataFinal()));
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        if (fieldSelectedDate == 1)
            edFiltroDataInicial.setText(String.format("%d/%02d/%d", day, month + 1, year));
        else if (fieldSelectedDate == 2)
            edFiltroDataFinal.setText(String.format("%d/%02d/%d", day, month + 1, year));

    }

    private void createDatePickerDialog() {

        Calendar calendar = Calendar.getInstance();
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePickerDialog = new DatePickerDialog(activity, this, year, month, dayOfMonth);
        datePickerDialog.setCancelable(false);
        datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Limpar", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (fieldSelectedDate == 1)
                    edFiltroDataInicial.getText().clear();
                else if (fieldSelectedDate == 2)
                    edFiltroDataFinal.getText().clear();
            }
        });
    }

    public interface OnConfirmListener {
        void onConfirm();
    }

}
