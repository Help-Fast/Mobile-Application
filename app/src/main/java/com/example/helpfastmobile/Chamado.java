package com.example.helpfastmobile;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

// Esta classe agora serve como modelo para o Room (banco de dados) e para o Retrofit (API).

@Entity(tableName = "chamados")
public class Chamado {

    // --- Anotações para Room e Gson (API) ---
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id") // Nome do campo no JSON da API
    private int id;

    @ColumnInfo(name = "assunto") // Nome da coluna no banco de dados
    @SerializedName("assunto")    // Nome do campo no JSON da API
    private String assunto;

    @ColumnInfo(name = "descricao")
    @SerializedName("descricao")
    private String descricao;

    @ColumnInfo(name = "status")
    @SerializedName("status")
    private String status;

    // --- Getters e Setters ---
    // Necessários para ambas as bibliotecas

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
