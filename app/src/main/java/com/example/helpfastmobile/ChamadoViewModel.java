package com.example.helpfastmobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

// A ViewModel para a tela MeusChamadosActivity.
public class ChamadoViewModel extends ViewModel {

    private ChamadoRepository chamadoRepository;
    private LiveData<List<Chamado>> chamadosLiveData;

    public ChamadoViewModel() {
        // Obtém a instância do nosso repositório
        chamadoRepository = ChamadoRepository.getInstance();
    }

    // Método que a Activity chamará para iniciar a busca dos chamados.
    public void getChamados(String authToken) {
        // O resultado da chamada ao repositório é atribuído ao nosso LiveData.
        chamadosLiveData = chamadoRepository.getChamados(authToken);
    }

    // Getter para que a Activity possa obter e observar o LiveData.
    public LiveData<List<Chamado>> getChamadosLiveData() {
        return chamadosLiveData;
    }
}
