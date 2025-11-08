package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MenuUsuarioActivity extends AppCompatActivity {

    private TextView textBoasVindas;
    private Button buttonNovoChamado;
    private Button buttonMeusChamados;
    private Button buttonSair;

    private SessionManager sessionManager; // NOVO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);

        // NOVO: Inicializa o SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        // Inicialização dos componentes da UI
        textBoasVindas = findViewById(R.id.text_boas_vindas);
        buttonNovoChamado = findViewById(R.id.button_novo_chamado);
        buttonMeusChamados = findViewById(R.id.button_meus_chamados);
        buttonSair = findViewById(R.id.button_sair);

        textBoasVindas.setText("Seja bem vindo!");

        buttonNovoChamado.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUsuarioActivity.this, NovoChamadoActivity.class);
            startActivity(intent);
        });

        buttonMeusChamados.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUsuarioActivity.this, MeusChamadosActivity.class);
            startActivity(intent);
        });

        // Ação do botão "Sair"
        buttonSair.setOnClickListener(v -> {
            // NOVO: Limpa a sessão do usuário
            sessionManager.clearSession();

            // Navega de volta para a tela de Login
            Intent intent = new Intent(MenuUsuarioActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}