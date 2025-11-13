package com.example.helpfastmobile.data.model;

import com.google.gson.annotations.SerializedName;

public class CreateChatDto {


    @SerializedName("pergunta")
    private String pergunta;

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }
}
