package br.com.security.cliente.stub;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mariomartins on 02/10/17.
 */

public class UserPreferences implements Serializable {

    public static final String ID_FIELD = "id";
    public static final String NOME_FIELD = "login";
    public static final String TELEFONE_FIELD = "telefone1";
    public static final String DATA_CRIACAO_FIELD = "data_criacao";
    public static final String DATA_EMAIL_FIELD = "email";
    public static final String DATA_SYNC_CHECKINS = "data_sync_checkins";

    private Long id;
    private String nome;
    private String telefone1;
    private Date dataCriacao;
    private String email;
    private Date dataSyncCheckins;
    private String code;

    public UserPreferences() {
    }

    public UserPreferences(Long id, String nome, String telefone1, Date dataCriacao, String email, Date dataSyncCheckins, String code) {
        this.id = id;
        this.nome = nome;
        this.telefone1 = telefone1;
        this.dataCriacao = dataCriacao;
        this.email = email;
        this.dataSyncCheckins = dataSyncCheckins;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataSyncCheckins() {
        return dataSyncCheckins;
    }

    public void setDataSyncCheckins(Date dataSyncCheckins) {
        this.dataSyncCheckins = dataSyncCheckins;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPreferences that = (UserPreferences) o;

        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserPreferences{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", telefone1='" + telefone1 + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", email='" + email + '\'' +
                ", dataSyncCheckins=" + dataSyncCheckins +
                ", code='" + code + '\'' +
                '}';
    }
}
