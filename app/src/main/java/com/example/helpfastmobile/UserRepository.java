package com.example.helpfastmobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {

    private static volatile UserRepository instance;
    private final ApiService apiService;

    private UserRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public LiveData<LoginResponse> login(String email, String password) {
        final MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        LoginRequest loginRequest = new LoginRequest(email, password);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    // NOVO: Método para realizar o cadastro
    public LiveData<User> register(String nome, String email, String senha, String telefone) {
        final MutableLiveData<User> data = new MutableLiveData<>();
        RegisterRequest registerRequest = new RegisterRequest(nome, email, senha, telefone);
        apiService.register(registerRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
