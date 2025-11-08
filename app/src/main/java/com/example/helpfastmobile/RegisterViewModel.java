package com.example.helpfastmobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

// A ViewModel para a tela de Cadastro.
public class RegisterViewModel extends ViewModel {

    private UserRepository userRepository;
    private LiveData<User> registerResponseLiveData;

    public RegisterViewModel() {
        // Obtém a instância do nosso repositório
        userRepository = UserRepository.getInstance();
    }

    // Método que a Activity chamará para iniciar o processo de cadastro.
    public void register(String nome, String email, String senha, String telefone) {
        // O resultado da chamada ao repositório é atribuído ao nosso LiveData.
        registerResponseLiveData = userRepository.register(nome, email, senha, telefone);
    }

    // Getter para que a Activity possa obter e observar o LiveData.
    public LiveData<User> getRegisterResponseLiveData() {
        return registerResponseLiveData;
    }
}
