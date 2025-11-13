package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Representa a estrutura de dados de um Chamado, mapeando os campos recebidos da API.
 * Os campos de ID (`id`, `tecnicoId`) são do tipo Integer em vez de int para permitir
 * valores nulos, tornando o parsing do JSON mais seguro caso um desses campos não seja enviado.
 * A data de abertura é armazenada como String para evitar erros de parsing com formatos de data inesperados.
 */
public class Chamado {

    @SerializedName("id")
    private Integer id;

    @SerializedName("tecnicoId")
    private Integer tecnicoId;

    @SerializedName("motivo")
    private String motivo;

    @SerializedName("status")
    private String status;

    @SerializedName("dataAbertura")
    private String dataAbertura;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
