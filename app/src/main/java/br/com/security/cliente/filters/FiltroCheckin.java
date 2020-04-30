package br.com.security.cliente.filters;

import java.util.Date;
import java.util.List;

public class FiltroCheckin {

    private List<String> checkedStatus;
    private Date dataInicial, dataFinal;


    public List<String> getCheckedStatus() {
        return checkedStatus;
    }

    public void setCheckedStatus(List<String> checkedStatus) {
        this.checkedStatus = checkedStatus;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public boolean isNotEmpty() {
        return (checkedStatus != null && !checkedStatus.isEmpty()) || dataInicial != null || dataFinal != null;
    }
}