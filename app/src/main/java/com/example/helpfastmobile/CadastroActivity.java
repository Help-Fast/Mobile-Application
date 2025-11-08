package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class CadastroActivity extends AppCompatActivity {

    private EditText etNome, etEmail, etTelefone, etSenha, etConfirmarSenha;
    private Button btnCadastrar, btnLogin;
    private ProgressBar progressBar; // Adicionar ao XML para feedback visual

    private RegisterViewModel registerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // 1. Inicializa as Views
        etNome = findViewById(R.id.edit_nome);
        etEmail = findViewById(R.id.edit_email);
        etTelefone = findViewById(R.id.edit_telefone);
        etSenha = findViewById(R.id.edit_senha);
        etConfirmarSenha = findViewById(R.id.edit_confirmar_senha);
        btnCadastrar = findViewById(R.id.button_cadastrar);
        btnLogin = findViewById(R.id.button_login);
        // progressBar = findViewById(R.id.progress_bar); // Descomente quando adicionar ao XML

        // 2. Obtém a instância da ViewModel
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // 3. Configura o observador para a resposta do cadastro
        setupObserver();

        // 4. Configura os cliques dos botões
        btnCadastrar.setOnClickListener(v -> {
            handleRegistration();
        });

        btnLogin.setOnClickListener(v -> {
            finish(); // Volta para a tela anterior (Login)
        });
    }

    private void setupObserver() {
        registerViewModel.getRegisterResponseLiveData().observe(this, user -> {
            // Esconde o progress bar
            // progressBar.setVisibility(View.GONE);
            // btnCadastrar.setEnabled(true);

            if (user != null) {
                // Sucesso no cadastro
                Toast.makeText(this, "Usuário " + user.getNome() + " registrado com sucesso!", Toast.LENGTH_LONG).show();

                // Navega para a tela de Login
                Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                // Falha no cadastro
                Toast.makeText(this, "Erro ao realizar o cadastro. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleRegistration() {
        String nome = etNome.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telefone = etTelefone.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();
        String confirmarSenha = etConfirmarSenha.getText().toString().trim();

        // Validações básicas (a lógica principal estará na ViewModel/Repositório)
        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Mostra o progress bar
        // progressBar.setVisibility(View.VISIBLE);
        // btnCadastrar.setEnabled(false);

        // 5. Chama o método da ViewModel para iniciar o cadastro
        registerViewModel.register(nome, email, senha, telefone);
    }
}
