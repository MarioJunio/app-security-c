package br.com.security.cliente.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by mariomartins on 02/10/17.
 */

@DatabaseTable
public class Checkin {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true)
    private Long id;

    @DatabaseField
    private String funcionario;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "dd/MM/yyyy HH:mm:ss")
    private Date data;

    @DatabaseField
    private String status;

    @DatabaseField
    private String descricao;

    @DatabaseField(dataType = DataType.BOOLEAN)
    private boolean foto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isFoto() {
        return foto;
    }

    public void setFoto(boolean foto) {
        this.foto = foto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Checkin checkin = (Checkin) o;

        return id != null ? id.equals(checkin.id) : checkin.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Checkin{" +
                "id=" + id +
                ", funcionario='" + funcionario + '\'' +
                ", data=" + data +
                ", status='" + status + '\'' +
                ", descricao='" + descricao + '\'' +
                ", foto=" + (foto ? "Tem foto" : "Nao tem foto") +
                '}';
    }
}
