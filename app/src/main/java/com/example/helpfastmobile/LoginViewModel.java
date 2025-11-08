package com.example.helpfastmobile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

// A ViewModel serve como uma ponte entre o Repositório (dados) e a View (tela).
// Ela sobrevive a mudanças de configuração, como a rotação da tela.
public class LoginViewModel extends ViewModel {

    private UserRepository userRepository;
    private LiveData<LoginResponse> loginResponseLiveData;

    public LoginViewModel() {
        // Obtém a instância do nosso repositório
        userRepository = UserRepository.getInstance();
    }

    // Método que a Activity chamará para iniciar o processo de login.
    public void login(String email, String password) {
        // O resultado da chamada ao repositório é atribuído ao nosso LiveData.
        // A Activity estará observando este LiveData para qualquer mudança.
        loginResponseLiveData = userRepository.login(email, password);
    }

    // Getter para que a Activity possa obter e observar o LiveData.
    public LiveData<LoginResponse> getLoginResponseLiveData() {
        return loginResponseLiveData;
    }
}
