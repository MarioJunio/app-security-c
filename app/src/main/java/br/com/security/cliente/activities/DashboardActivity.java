package br.com.security.cliente.activities;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.security.cliente.adapters.CheckinsRecyclerAdapter;
import br.com.security.cliente.application.MyApplication;
import br.com.security.cliente.application.Router;
import br.com.security.cliente.application.Session;
import br.com.security.cliente.dao.CheckinDAO;
import br.com.security.cliente.filters.CheckinFilterDialog;
import br.com.security.cliente.filters.FiltroCheckin;
import br.com.security.cliente.model.Checkin;
import br.com.security.cliente.utils.AppDialog;
import br.com.security.cliente.wrappers.CheckinType;
import br.com.security.cliente.wrappers.DateType;
import br.com.security.cliente.wrappers.ListItemCheckin;

public class DashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout layoutProgress;
    private CheckinDAO checkinDAO;
    private View layoutNoRecords, layoutRecords;
    private List<ListItemCheckin> checkins = new ArrayList<>();
    private FiltroCheckin filtroCheckin = new FiltroCheckin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setActionBarUserProfile();

        checkinDAO = CheckinDAO.getInstance(this);
        adapter = new CheckinsRecyclerAdapter(this, checkins);
        layoutManager = new LinearLayoutManager(this); // Linear layout

        layoutProgress = findViewById(R.id.layout_progress);
        layoutNoRecords = findViewById(R.id.layout_no_records);
        layoutRecords = findViewById(R.id.layout_records);

        recyclerView = findViewById(R.id.checkins_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        consultarCheckins();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.search:
                filtrar();
                break;

            case R.id.logoff:

                AppDialog.showDialog(this, null, "Você está efetuando o logout, deseja continuar?", "Sim", "Não", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Session.newInstance().clearSession();
                        Router.goAuthView(DashboardActivity.this);
                    }

                }, null);

                return false;
        }

        return false;

    }

    private void consultarCheckins() {

        AsyncTask checkinsTask = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                layoutProgress.setVisibility(View.VISIBLE);
            }

            @Override
            protected Object doInBackground(Object[] objects) {

                List<ListItemCheckin> historico = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                try {
                    Calendar dataAnterior = null;
                    List<Checkin> checkins = checkinDAO.buscarHistorico(filtroCheckin);

                    int c = 0, j = 0;

                    for (int i = 0; i < checkins.size(); i++) {

                        Checkin checkin = checkins.get(i);
                        Log.v(getClass().getName(), "Checkin: " + checkin.toString());

                        Calendar dataAtual = toCalendar(checkin.getData());

                        if (dataAnterior == null || (dataAtual.get(Calendar.DAY_OF_MONTH) != dataAnterior.get(Calendar.DAY_OF_MONTH) || dataAtual.get(Calendar.MONTH) != dataAnterior.get(Calendar.MONTH) || dataAtual.get(Calendar.YEAR) != dataAnterior.get(Calendar.YEAR))) {

                            if (dataAnterior != null) {

                                CheckinType checkinType = (CheckinType) historico.get(j - 1);

                                // se existe apenas um item no grupo anterior
                                if (c == 1)
                                    checkinType.setType(ListItemCheckin.TYPE_CHECKIN_FULL);
                                else
                                    checkinType.setType(ListItemCheckin.TYPE_CHECKIN_BOTTOM);
                            }

                            c = 0;

                            historico.add(new DateType(dateFormat.format(checkin.getData())));
                            j++;
                            historico.add(new CheckinType(checkin, ListItemCheckin.TYPE_CHECKIN_TOP));

                        } else
                            historico.add(new CheckinType(checkin, ListItemCheckin.TYPE_CHECKIN_MIDDLE));

                        j++;

                        // contador de elementos em cada grupo
                        c++;

                        // altera a data anterior
                        dataAnterior = dataAtual;

                    }

                    if (j > 0) {

                        CheckinType checkinType = (CheckinType) historico.get(j - 1);

                        // ajusta o último elemento
                        if (c == 1)
                            checkinType.setType(ListItemCheckin.TYPE_CHECKIN_FULL);
                        else
                            checkinType.setType(ListItemCheckin.TYPE_CHECKIN_BOTTOM);
                    }


                } catch (SQLException e) {
                    e.printStackTrace();
                }

                return historico;
            }

            @Override
            protected void onPostExecute(Object o) {

                try {

                    if (o != null && o instanceof List) {
                        checkins.clear();
                        checkins.addAll((List<ListItemCheckin>) o);
                    }

                } finally {
                    layoutProgress.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();

                    if (filtroCheckin.isNotEmpty())
                        ((TextView) layoutNoRecords.findViewById(R.id.tx_filter_result)).setText("Nenhuma inspeção encontrada");
                    else
                        ((TextView) layoutNoRecords.findViewById(R.id.tx_filter_result)).setText("Nenhuma inspeção sincronizada");

                    // mostra determinado layout
                    if (!checkins.isEmpty()) {
                        showLayoutNoRecords(false);
                        showLayoutRecords(true);
                    } else {
                        showLayoutNoRecords(true);
                        showLayoutRecords(false);
                    }
                }
            }
        };

        checkinsTask.execute();
    }

    private void setActionBarUserProfile() {

        toolbar = findViewById(R.id.toolbar);

        TextView txUsername = toolbar.findViewById(R.id.profile_username);
        txUsername.setText(MyApplication.packToolbarTitle(Session.newInstance().getUserPreferences().getNome()));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }


    private static Calendar toCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar;
    }

    private void showLayoutNoRecords(boolean show) {
        layoutNoRecords.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void showLayoutRecords(boolean show) {
        layoutRecords.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void filtrar() {

        final CheckinFilterDialog checkinFilterDialog = new CheckinFilterDialog(this);

        checkinFilterDialog.setOnConfirmListener(new CheckinFilterDialog.OnConfirmListener() {

            @Override
            public void onConfirm() {

                try {

                    List<String> checkeds = new ArrayList<>();

                    if (checkinFilterDialog.cbNormal.isChecked())
                        checkeds.add(checkinFilterDialog.cbNormal.getText().toString().toUpperCase());

                    if (checkinFilterDialog.cbSuspeito.isChecked())
                        checkeds.add(checkinFilterDialog.cbSuspeito.getText().toString().toUpperCase());

                    if (checkinFilterDialog.cbPerigo.isChecked())
                        checkeds.add(checkinFilterDialog.cbPerigo.getText().toString().toUpperCase());

                    filtroCheckin.setCheckedStatus(checkeds);

                    if (!checkinFilterDialog.edFiltroDataInicial.getText().toString().isEmpty()) {
                        filtroCheckin.setDataInicial(MyApplication.parseDate(checkinFilterDialog.edFiltroDataInicial.getText().toString()));
                        filtroCheckin.getDataInicial().setHours(0);
                        filtroCheckin.getDataInicial().setMinutes(0);
                        filtroCheckin.getDataInicial().setSeconds(0);
                    } else
                        filtroCheckin.setDataInicial(null);

                    if (!checkinFilterDialog.edFiltroDataFinal.getText().toString().isEmpty()) {
                        filtroCheckin.setDataFinal(MyApplication.parseDate(checkinFilterDialog.edFiltroDataFinal.getText().toString()));
                        filtroCheckin.getDataFinal().setHours(23);
                        filtroCheckin.getDataFinal().setMinutes(59);
                        filtroCheckin.getDataFinal().setSeconds(59);
                    } else
                        filtroCheckin.setDataFinal(null);

                    checkinFilterDialog.dismiss();

                    consultarCheckins();

                } catch (ParseException e) {
                    Toast.makeText(getApplicationContext(), "Data está no formato inválido", Toast.LENGTH_SHORT).show();
                }

            }
        });

        checkinFilterDialog.show();
        checkinFilterDialog.setFiltro(filtroCheckin);

    }


}
