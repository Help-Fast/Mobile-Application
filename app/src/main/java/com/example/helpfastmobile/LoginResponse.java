package com.example.helpfastmobile;

import com.google.gson.annotations.SerializedName;

// Esta classe representa a resposta JSON do endpoint de login.
// Ex: {"token":"seu_jwt_token_aqui", "usuario":{...}}
public class LoginResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("usuario")
    private User user;

    // Getters
    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}
