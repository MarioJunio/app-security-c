package br.com.security.cliente.stub;

import java.util.List;

import br.com.security.cliente.model.Checkin;

/**
 * Created by mariomartins on 03/10/17.
 */

public class SyncCheckins {

    private Long timeSync;
    private List<Checkin> listAppClienteCheckin;

    public SyncCheckins(Long timeSync, List<Checkin> listAppClienteCheckin) {
        super();
        this.timeSync = timeSync;
        this.listAppClienteCheckin = listAppClienteCheckin;
    }

    public Long getTimeSync() {
        return timeSync;
    }

    public void setTimeSync(Long timeSync) {
        this.timeSync = timeSync;
    }

    public List<Checkin> getListAppClienteCheckin() {
        return listAppClienteCheckin;
    }

    public void setListAppClienteCheckin(List<Checkin> listAppClienteCheckin) {
        this.listAppClienteCheckin = listAppClienteCheckin;
    }

}
