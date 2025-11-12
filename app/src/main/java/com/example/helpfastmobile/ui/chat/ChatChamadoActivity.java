package com.example.helpfastmobile.ui.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.helpfastmobile.data.model.CreateChatDto;
import com.example.helpfastmobile.util.SessionManager;

public class ChatChamadoActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMADO_ID = "com.example.helpfastmobile.EXTRA_CHAMADO_ID";
    public static final String EXTRA_PREENCHER_MENSAGEM = "com.example.helpfastmobile.PREENCHER_MENSAGEM";
    private static final String TAG = "HelpFastDebug";
    private static final long POLLING_INTERVAL_MS = 10000; // 10 segundos

    private EditText editMensagem;
    private ImageButton btnSend;
    private Button buttonVoltar;
    private RecyclerView recyclerViewMensagens;

    private ChamadoViewModel chamadoViewModel;
    private MensagemAdapter mensagemAdapter;
    private SessionManager sessionManager;
    private int chamadoId;

    private final Handler pollingHandler = new Handler(Looper.getMainLooper());
    private Runnable pollingRunnable;

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
        setupPolling();

        buttonVoltar.setOnClickListener(v -> finish());
        btnSend.setOnClickListener(v -> handleEnviarMensagem());
    }

    @Override
    protected void onResume() {
        super.onResume();
        pollingHandler.post(pollingRunnable);
        Log.d(TAG, "Polling de chat iniciado.");
    }

    @Override
    protected void onPause() {
        super.onPause();
        pollingHandler.removeCallbacks(pollingRunnable);
        Log.d(TAG, "Polling de chat pausado.");
    }

    private void setupRecyclerView() {
        int usuarioLogadoId = sessionManager.getUserId();
        mensagemAdapter = new MensagemAdapter(usuarioLogadoId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        recyclerViewMensagens.setLayoutManager(layoutManager);
        recyclerViewMensagens.setAdapter(mensagemAdapter);
    }

    private void setupPolling() {
        pollingRunnable = () -> {
            Log.d(TAG, "Verificando novas mensagens...");
            chamadoViewModel.getChat(chamadoId);
            pollingHandler.postDelayed(pollingRunnable, POLLING_INTERVAL_MS);
        };
    }

    private void setupObservers() {
        chamadoViewModel.getCreateChatResult().observe(this, chat -> {
            if (chat != null) {
                Log.d(TAG, "Mensagem salva, enviando para a IA e recarregando o chat.");
                editMensagem.setText("");
                // **CORREÇÃO**: Chama a IA e recarrega o chat DEPOIS que a mensagem foi salva
                chamadoViewModel.getChat(chamadoId);

                Integer usuarioId = sessionManager.getUserId();
                if (usuarioId != -1) {
                    chamadoViewModel.perguntarDocumentAssistant(chat.getMotivo(), usuarioId);
                } else {
                    Log.e(TAG, "Usuário não logado após salvar chat. Não é possível enviar pergunta para DocumentAssistant.");
                }
            }
        });

        chamadoViewModel.getChatResult().observe(this, chatMessages -> {
            if (chatMessages != null) {
                Log.d(TAG, "Chat atualizado com " + chatMessages.size() + " mensagens.");
                mensagemAdapter.setMensagens(chatMessages);
                if (mensagemAdapter.getItemCount() > 0) {
                    recyclerViewMensagens.smoothScrollToPosition(mensagemAdapter.getItemCount() - 1);
                }
            }
        });

        chamadoViewModel.getDocumentAssistantSuccess().observe(this, aVoid -> {
            Log.i(TAG, "Pergunta enviada para DocumentAssistant com sucesso! Recarregando chat para resposta.");
            chamadoViewModel.getChat(chamadoId);
        });

        chamadoViewModel.getCreateChatError().observe(this, error -> {
            if (error != null) Toast.makeText(this, "Falha ao enviar: " + error, Toast.LENGTH_SHORT).show();
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

        // **CORREÇÃO**: Apenas salva a mensagem do usuário. O observer cuidará do resto.
        CreateChatDto createChatDto = new CreateChatDto();
        createChatDto.setChamadoId(chamadoId);
        createChatDto.setMotivo(mensagem);
        chamadoViewModel.createChat(createChatDto);
    }
}
