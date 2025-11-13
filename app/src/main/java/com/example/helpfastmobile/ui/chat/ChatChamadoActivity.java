package com.example.helpfastmobile.ui.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helpfastmobile.viewmodel.ChamadoViewModel;
import com.example.helpfastmobile.R;
import com.example.helpfastmobile.data.model.Chat;
import com.example.helpfastmobile.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChatChamadoActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";
    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";
    private static final String TAG = "HelpFastDebug";

    private EditText editMensagem;
    private ImageButton btnSend;
    private Button buttonVoltar;
    private RecyclerView recyclerViewMensagens;

    private ChamadoViewModel chamadoViewModel;
    private MensagemAdapter mensagemAdapter;
    private SessionManager sessionManager;
    private int chamadoId;
    private List<Chat> mensagensLocais = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_chat_chamado);

        sessionManager = new SessionManager(getApplicationContext());
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        editMensagem = findViewById(R.id.edit_mensagem);
        btnSend = findViewById(R.id.btn_send);
        buttonVoltar = findViewById(R.id.button_voltar);
        recyclerViewMensagens = findViewById(R.id.recycler_view_mensagens);

        chamadoId = getIntent().getIntExtra(EXTRA_CHAMADO_ID, -1);

        if (chamadoId == -1) {
            Toast.makeText(this, "Erro: ID do chamado não fornecido.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        String mensagemPreenchida = getIntent().getStringExtra(EXTRA_PREENCHER_MENSAGEM);
        if (mensagemPreenchida != null && !mensagemPreenchida.trim().isEmpty()) {
            editMensagem.setText(mensagemPreenchida);
        }

        setupRecyclerView();
        setupObservers();

        buttonVoltar.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> handleEnviarMensagem());
    }


    private void setupRecyclerView() {
        int usuarioLogadoId = sessionManager.getUserId();
        mensagemAdapter = new MensagemAdapter(usuarioLogadoId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setAdapter(mensagemAdapter);
    }

    private void adicionarMensagemAoChat(String texto, Integer usuarioId) {
        Chat novaMensagem = new Chat();
        novaMensagem.setMotivo(texto);
        novaMensagem.setUsuarioId(usuarioId);
        novaMensagem.setChamadoId(chamadoId);
        mensagensLocais.add(novaMensagem);
        mensagemAdapter.setMensagens(mensagensLocais);
        if (mensagemAdapter.getItemCount() > 0) {
            recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
        }
    }

    private void setupObservers() {
        chamadoViewModel.getDocumentAssistantSuccess().observe(this, resposta -> {
            if (resposta != null) {
                Log.i(TAG, "Resposta recebida do DocumentAssistant. Resposta: " + resposta.getResposta() + 
                      ", Escalar para humano: " + resposta.isEscalarParaHumano());
                
                // Adiciona a resposta da IA ao chat (sem usuarioId para aparecer como mensagem recebida)
                adicionarMensagemAoChat(resposta.getResposta(), null);
                
                // Se precisar escalar para humano, adiciona mensagem de escalação
                if (resposta.isEscalarParaHumano()) {
                    Log.w(TAG, "A IA indicou que deve escalar para humano.");
                    adicionarMensagemAoChat("Você será atendido pelo técnico, aguarde um instante.", null);
                }
            }
        });

        chamadoViewModel.getDocumentAssistantError().observe(this, error -> {
            if (error != null) {
                Log.e(TAG, "Falha ao enviar pergunta para DocumentAssistant: " + error);
                Toast.makeText(this, "Erro ao processar pergunta: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleEnviarMensagem() {
        String mensagem = editMensagem.getText().toString().trim();
        if (mensagem.isEmpty()) {
            return;
        }

        // Adiciona a mensagem do usuário ao chat localmente
        Integer usuarioId = sessionManager.getUserId();
        adicionarMensagemAoChat(mensagem, usuarioId);
        editMensagem.setText("");

        // Envia para a IA através do endpoint api/DocumentAssistant/perguntar
        chamadoViewModel.perguntarDocumentAssistant(mensagem);
    }
}
