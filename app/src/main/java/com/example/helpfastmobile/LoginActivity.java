package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnRegister;
    private ProgressBar progressBar;

    private LoginViewModel loginViewModel;
    private SessionManager sessionManager; // NOVO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // NOVO: Inicializa o SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        // 1. Inicializa as Views
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        // progressBar = findViewById(R.id.progress_bar);

        // 2. Obtém a instância da ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 3. Configura o observador para a resposta do login
        setupObserver();

        // 4. Configura os cliques dos botões
        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> {
            Intent intentCadastro = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(intentCadastro);
        });
    }

    private void setupObserver() {
        loginViewModel.getLoginResponseLiveData().observe(this, loginResponse -> {
            // progressBar.setVisibility(View.GONE);
            // btnLogin.setEnabled(true);

            if (loginResponse != null && loginResponse.getToken() != null) {
                // Sucesso no login
                Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

                // NOVO: Salva o token usando o SessionManager
                sessionManager.saveAuthToken(loginResponse.getToken());

                // Navega para a tela de Menu
                Intent intentMenu = new Intent(LoginActivity.this, MenuUsuarioActivity.class);
                startActivity(intentMenu);
                finish();
            } else {
                // Falha no login
                Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // progressBar.setVisibility(View.VISIBLE);
        // btnLogin.setEnabled(false);

        // 5. Chama o método da ViewModel para iniciar o login
        loginViewModel.login(email, password);
    }
}