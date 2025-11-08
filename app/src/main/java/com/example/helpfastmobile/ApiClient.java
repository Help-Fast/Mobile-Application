package com.example.helpfastmobile;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Esta classe é responsável por criar e gerenciar a instância do Retrofit.
// Usamos o padrão Singleton para garantir que haja apenas uma instância para todo o app.
public class ApiClient {

    // IMPORTANTE: Substitua pela URL base da sua API.
    // Se você estiver testando com um servidor local no mesmo computador que o emulador,
    // use http://10.0.2.2:PORTA/ em vez de http://localhost:PORTA/
    private static final String BASE_URL = "http://10.0.2.2:8080/api/";

    private static Retrofit retrofit = null;

    // Método público e estático para obter a instância do cliente Retrofit.
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Define a URL base para todas as requisições.
                    .addConverterFactory(GsonConverterFactory.create()) // Adiciona o conversor Gson.
                    .build(); // Constrói a instância do Retrofit.
        }
        return retrofit;
    }
}
