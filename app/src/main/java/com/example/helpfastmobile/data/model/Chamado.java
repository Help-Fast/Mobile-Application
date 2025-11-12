package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

public class Chamado {

    @SerializedName("id")
    private Integer id; // Alterado de int para Integer

    @SerializedName("tecnicoId")
    private Integer tecnicoId;

    @SerializedName("motivo")
    private String motivo;

    @SerializedName("status")
    private String status;

    // CORREÇÃO: Alterado para String para evitar erros de parsing de data
    @SerializedName("dataAbertura")
    private String dataAbertura;

    // Getters and Setters
    public Integer getId() { // Alterado de int para Integer
        return id;
    }

    public void setId(Integer id) { // Alterado de int para Integer
        this.id = id;
    }

    public Integer getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Integer tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }
}
