package com.example.helpfastmobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class TelaFaqActivity extends AppCompatActivity implements View.OnClickListener {

    // Chave para o "Extra" do Intent
    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";
    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";

    private Button btnImpressora, btnComputador, btnWifi, btnCelular, buttonIrChat;
    private Integer chamadoId;
    private ChamadoViewModel chamadoViewModel;
    private SessionManager sessionManager;
    private boolean buscandoChamado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_faq);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        // Obtém o ID do chamado se foi passado
        chamadoId = getIntent().getIntExtra(EXTRA_CHAMADO_ID, -1);
        if (chamadoId == -1) {
            chamadoId = null; // Converte -1 para null para indicar que não foi fornecido
            // Se não foi fornecido, tenta buscar o chamado mais recente do usuário
            buscarChamadoMaisRecente();
        }

        // 1. Inicializa todos os botões
        btnImpressora = findViewById(R.id.btn_impressora);
        btnComputador = findViewById(R.id.btn_computador);
        btnWifi = findViewById(R.id.btn_wifi);
        btnCelular = findViewById(R.id.btn_celular);
        buttonIrChat = findViewById(R.id.button_ir_chat);

        // 2. Configura o listener de clique para todos os botões
        btnImpressora.setOnClickListener(this);
        btnComputador.setOnClickListener(this);
        btnWifi.setOnClickListener(this);
        btnCelular.setOnClickListener(this);
        buttonIrChat.setOnClickListener(this);

        // Configura observadores para buscar o chamado mais recente
        setupObservers();
    }

    private void setupObservers() {
        chamadoViewModel.getMeusChamadosResult().observe(this, chamados -> {
            if (chamados != null && !chamados.isEmpty() && chamadoId == null) {
                // Pega o primeiro chamado aberto, ou o primeiro da lista
                Chamado chamadoMaisRecente = null;
                for (Chamado chamado : chamados) {
                    // Prioriza chamados abertos
                    if (chamado.getStatus() != null && chamado.getStatus().equalsIgnoreCase("Aberto")) {
                        chamadoMaisRecente = chamado;
                        break;
                    }
                }
                // Se não encontrou um aberto, pega o primeiro da lista
                if (chamadoMaisRecente == null) {
                    chamadoMaisRecente = chamados.get(0);
                }
                chamadoId = chamadoMaisRecente.getId();
                Log.d("TelaFaqActivity", "Chamado mais recente encontrado: ID " + chamadoId);
            }
            buscandoChamado = false;
        });

        chamadoViewModel.getMeusChamadosError().observe(this, error -> {
            if (error != null) {
                Log.e("TelaFaqActivity", "Erro ao buscar chamados: " + error);
            }
            buscandoChamado = false;
        });
    }

    private void buscarChamadoMaisRecente() {
        int userId = sessionManager.getUserId();
        if (userId != -1 && !buscandoChamado) {
            buscandoChamado = true;
            chamadoViewModel.getMeusChamados(userId);
        }
    }

    @Override
    public void onClick(View v) {
        // 3. Obtém o texto do botão clicado
        Button botaoClicado = (Button) v;
        String pergunta = botaoClicado.getText().toString();

        // Se for o botão "Ir para o Chat", não envia texto pré-definido
        if (v.getId() == R.id.button_ir_chat) {
            irParaChatComPergunta(null); // Passa nulo para não preencher nada
        } else {
            irParaChatComPergunta(pergunta);
        }
    }

    // 4. Método centralizado para iniciar o chat
    private void irParaChatComPergunta(String pergunta) {
        // Se não há chamadoId, verifica se ainda está buscando ou se precisa buscar
        if (chamadoId == null) {
            if (buscandoChamado) {
                // Se ainda está buscando, aguarda um pouco
                Toast.makeText(this, "Buscando chamado... Por favor, aguarde.", Toast.LENGTH_SHORT).show();
                new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                    if (chamadoId != null) {
                        abrirChat(pergunta);
                    } else {
                        Toast.makeText(this, "Nenhum chamado encontrado. Por favor, crie um chamado primeiro.", Toast.LENGTH_LONG).show();
                    }
                }, 2000); // Aguarda 2 segundos
            } else {
                // Se não está buscando e não tem chamadoId, não há chamados
                Toast.makeText(this, "Nenhum chamado encontrado. Por favor, crie um chamado primeiro ou abra o chat a partir de 'Meus Chamados'.", Toast.LENGTH_LONG).show();
            }
            return;
        }

        abrirChat(pergunta);
    }

    private void abrirChat(String pergunta) {
        Intent intent = new Intent(this, ChatChamadoActivity.class);
        
        // Passa o ID do chamado (obrigatório)
        intent.putExtra(ChatChamadoActivity.EXTRA_CHAMADO_ID, chamadoId);

        // Anexa a pergunta como um "Extra" se ela não for nula
        if (pergunta != null) {
            intent.putExtra(EXTRA_PREENCHER_MENSAGEM, pergunta);
        }

        startActivity(intent);
        finish(); // Fecha a tela de FAQ
    }
}
