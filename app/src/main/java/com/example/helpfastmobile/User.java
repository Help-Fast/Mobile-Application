package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

// Esta classe representa o objeto 'Usuario' que virá da sua API.
public class User {

    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
