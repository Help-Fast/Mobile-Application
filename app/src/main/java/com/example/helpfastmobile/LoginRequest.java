package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

// Esta classe representa o corpo da requisição JSON para o endpoint de login.
// Ex: {"email":"teste@email.com", "senha":"12345"}
public class LoginRequest {

    // @SerializedName garante que o nome no JSON será exatamente "email",
    // mesmo que o nome da variável mude.
    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters são necessários para que a biblioteca Gson possa ler os valores.
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
