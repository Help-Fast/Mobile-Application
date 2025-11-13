package com.example.helpfastmobile.ui.chamado;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpfastmobile.viewmodel.ChamadoViewModel;
import com.example.helpfastmobile.R;
import com.example.helpfastmobile.ui.faq.TelaFaqActivity;
import com.example.helpfastmobile.util.SessionManager;

public class NovoChamadoActivity extends AppCompatActivity {

    private static final String TAG = "HelpFastDebug";

    private EditText editAssunto;
    private EditText editMotivo;
    private Button buttonVoltar;
    private Button buttonAbrir;

    private ChamadoViewModel chamadoViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_chamado);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        editAssunto = findViewById(R.id.edit_assunto);
        editMotivo = findViewById(R.id.edit_motivo);
        buttonVoltar = findViewById(R.id.button_voltar);
        buttonAbrir = findViewById(R.id.button_abrir);

        setupObservers();

        buttonVoltar.setOnClickListener(v -> finish());
        buttonAbrir.setOnClickListener(v -> handleAbrirChamado());
    }

    private void setupObservers() {
        // Observa o resultado da criação de um novo chamado.
        chamadoViewModel.getAbrirChamadoResult().observe(this, chamado -> {
            if (chamado != null) {
                Toast.makeText(this, "Chamado aberto com sucesso! ID: " + chamado.getId(), Toast.LENGTH_LONG).show();

                // Após criar o chamado, o usuário é direcionado para a tela de FAQ.
                // O ID do novo chamado é passado para que a tela de FAQ possa associar as perguntas a ele.
                Intent intent = new Intent(NovoChamadoActivity.this, TelaFaqActivity.class);
                intent.putExtra(TelaFaqActivity.EXTRA_CHAMADO_ID, chamado.getId());
                startActivity(intent);
                finish(); // Fecha a tela de criação para não voltar a ela.
            }
        });

        chamadoViewModel.getAbrirChamadoError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Falha ao abrir o chamado: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleAbrirChamado() {
        // O campo 'motivo' é o principal para a API. O campo 'assunto' é apenas para a UI.
        String motivo = editMotivo.getText().toString().trim();

        if (motivo.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha o motivo.", Toast.LENGTH_SHORT).show();
            return;
        }

        int clienteId = sessionManager.getUserId();

        if (clienteId == -1) {
            Toast.makeText(this, "Sessão inválida. Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        Log.d(TAG, "Abrindo chamado para o cliente ID " + clienteId + " com o motivo: " + motivo);

        // Inicia o processo de abrir o chamado. O resultado será capturado pelo observador.
        chamadoViewModel.abrirChamado(clienteId, motivo);
    }
}
