package com.example.helpfastmobile.ui.faq;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpfastmobile.viewmodel.ChamadoViewModel;
import com.example.helpfastmobile.R;
import com.example.helpfastmobile.data.model.Chamado;
import com.example.helpfastmobile.ui.chat.ChatChamadoActivity;
import com.example.helpfastmobile.util.SessionManager;

public class TelaFaqActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";
    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";

    private Button btnImpressora, btnComputador, btnWifi, btnCelular, buttonIrChat;
    private Integer chamadoId;
    private ChamadoViewModel chamadoViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_faq);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        // 1. Inicializa todos os botões
        btnImpressora = findViewById(R.id.btn_impressora);
        btnComputador = findViewById(R.id.btn_computador);
        btnWifi = findViewById(R.id.btn_wifi);
        btnCelular = findViewById(R.id.btn_celular);
        buttonIrChat = findViewById(R.id.button_ir_chat);

        // 2. Desabilita os botões enquanto busca o chamado
        setButtonsEnabled(false);

        // 3. Configura o listener de clique para todos os botões
        btnImpressora.setOnClickListener(this);
        btnComputador.setOnClickListener(this);
        btnWifi.setOnClickListener(this);
        btnCelular.setOnClickListener(this);
        buttonIrChat.setOnClickListener(this);

        // Configura observadores para buscar o chamado mais recente
        setupObservers();

        // Obtém o ID do chamado se foi passado
        chamadoId = getIntent().getIntExtra(EXTRA_CHAMADO_ID, -1);
        if (chamadoId == -1) {
            chamadoId = null; // Converte -1 para null para indicar que não foi fornecido
            buscarChamadoMaisRecente();
        } else {
            setButtonsEnabled(true); // Se o ID já veio, habilita os botões
        }
    }

    private void setupObservers() {
        chamadoViewModel.getMeusChamadosResult().observe(this, chamados -> {
            if (chamados != null && !chamados.isEmpty()) {
                // Pega o primeiro chamado aberto, ou o primeiro da lista
                Chamado chamadoMaisRecente = chamados.stream()
                        .filter(c -> c.getStatus() != null && c.getStatus().equalsIgnoreCase("Aberto"))
                        .findFirst()
                        .orElse(chamados.get(0)); // Pega o primeiro se nenhum estiver aberto

                chamadoId = chamadoMaisRecente.getId();
                setButtonsEnabled(true); // Habilita os botões
                Log.d("TelaFaqActivity", "Chamado ID " + chamadoId + " encontrado. Botões habilitados.");
            } else {
                // Se não há chamados, informa o usuário
                Toast.makeText(this, "Nenhum chamado ativo encontrado para associar.", Toast.LENGTH_LONG).show();
                Log.d("TelaFaqActivity", "Nenhum chamado encontrado. Botões permanecem desabilitados.");
            }
        });

        chamadoViewModel.getMeusChamadosError().observe(this, error -> {
            if (error != null) {
                Log.e("TelaFaqActivity", "Erro ao buscar chamados: " + error);
                Toast.makeText(this, "Erro ao buscar seus chamados.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void buscarChamadoMaisRecente() {
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            Log.d("TelaFaqActivity", "Buscando chamado mais recente para o usuário ID: " + userId);
            chamadoViewModel.getMeusChamados(userId);
        } else {
            Toast.makeText(this, "Sessão de usuário inválida.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        String pergunta = null;
        if (v.getId() != R.id.button_ir_chat) {
            pergunta = ((Button) v).getText().toString();
        }
        abrirChat(pergunta);
    }

    private void abrirChat(String pergunta) {
        if (chamadoId == null) {
            Toast.makeText(this, "Aguarde, buscando chamado associado...", Toast.LENGTH_SHORT).show();
            return; // Impede a abertura do chat se o ID ainda não estiver pronto
        }

        Intent intent = new Intent(this, ChatChamadoActivity.class);
        intent.putExtra(ChatChamadoActivity.EXTRA_CHAMADO_ID, chamadoId);

        if (pergunta != null) {
            intent.putExtra(ChatChamadoActivity.EXTRA_PREENCHER_MENSAGEM, pergunta);
        }

        startActivity(intent);
        finish(); // Fecha a tela de FAQ
    }

    private void setButtonsEnabled(boolean enabled) {
        btnImpressora.setEnabled(enabled);
        btnComputador.setEnabled(enabled);
        btnWifi.setEnabled(enabled);
        btnCelular.setEnabled(enabled);
        buttonIrChat.setEnabled(enabled);
    }
}
