package com.example.helpfastmobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Repositório para gerenciar as operações de dados relacionadas a Chamados.
public class ChamadoRepository {

    private static volatile ChamadoRepository instance;
    private final ApiService apiService;

    private ChamadoRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    // Padrão Singleton para garantir uma única instância do repositório
    public static ChamadoRepository getInstance() {
        if (instance == null) {
            instance = new ChamadoRepository();
        }
        return instance;
    }

    // Método para buscar a lista de chamados da API
    public LiveData<List<Chamado>> getChamados(String authToken) {
        final MutableLiveData<List<Chamado>> data = new MutableLiveData<>();

        // Adiciona o prefixo "Bearer " ao token, que é o padrão de autenticação JWT.
        String header = "Bearer " + authToken;

        // Chama o endpoint da API de forma assíncrona
        apiService.getChamados(header).enqueue(new Callback<List<Chamado>>() {
            @Override
            public void onResponse(Call<List<Chamado>> call, Response<List<Chamado>> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body());
                } else {
                    // Em caso de erro (ex: token inválido), postamos uma lista nula
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Chamado>> call, Throwable t) {
                // Em caso de falha de rede, também postamos nulo
                data.setValue(null);
            }
        });

        return data;
    }
}
