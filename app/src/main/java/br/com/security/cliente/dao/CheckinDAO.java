package br.com.security.cliente.dao;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

import br.com.security.cliente.filters.FiltroCheckin;
import br.com.security.cliente.model.Checkin;
import br.com.security.cliente.utils.DatabaseHelper;


/**
 * Created by mariomartins on 15/09/17.
 */

public class CheckinDAO {

    private Dao<Checkin, Long> checkinDao;

    public static CheckinDAO getInstance(Context context) {
        return new CheckinDAO(new DatabaseHelper(context));
    }

    private CheckinDAO(DatabaseHelper databaseHelper) {
        this.checkinDao = databaseHelper.getCheckinDao();
    }

    public void save(Checkin checkin) throws SQLException {
        this.checkinDao.createOrUpdate(checkin);
    }

    public List<Checkin> buscarHistorico(FiltroCheckin filtro) throws SQLException {

        QueryBuilder<Checkin, Long> queryBuilder = checkinDao.queryBuilder();
        queryBuilder.orderBy("data", false);

        // se possui filtro
        if (filtro != null) {

            if (filtro.getDataFinal() != null && filtro.getDataInicial() != null) {

                if (filtro.getCheckedStatus() != null && !filtro.getCheckedStatus().isEmpty())
                    queryBuilder.where().between("data", filtro.getDataInicial(), filtro.getDataFinal()).and().in("status", filtro.getCheckedStatus().toArray(new String[]{}));
                else
                    queryBuilder.where().between("data", filtro.getDataInicial(), filtro.getDataFinal());

            } else if (filtro.getDataInicial() != null) {

                if (filtro.getCheckedStatus() != null && !filtro.getCheckedStatus().isEmpty())
                    queryBuilder.where().ge("data", filtro.getDataInicial()).and().in("status", filtro.getCheckedStatus().toArray(new String[]{}));
                else
                    queryBuilder.where().ge("data", filtro.getDataInicial());

            } else if (filtro.getDataFinal() != null) {

                if (filtro.getCheckedStatus() != null && !filtro.getCheckedStatus().isEmpty())
                    queryBuilder.where().le("data", filtro.getDataFinal()).and().in("status", filtro.getCheckedStatus().toArray(new String[]{}));
                else
                    queryBuilder.where().le("data", filtro.getDataFinal());
            }

        }

        Log.v(getClass().getName(), queryBuilder.prepareStatementString());

        return queryBuilder.query();
    }

}
