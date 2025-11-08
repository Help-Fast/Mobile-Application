package com.example.helpfastmobile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

// Esta interface define os endpoints da sua API REST.
public interface ApiService {

    // --- Endpoint de Login ---
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // --- NOVO: Endpoint de Cadastro ---
    @POST("register")
    Call<User> register(@Body RegisterRequest registerRequest);

    // --- Endpoint para Buscar Chamados ---
    @GET("chamados")
    Call<List<Chamado>> getChamados(@Header("Authorization") String authToken);

}
