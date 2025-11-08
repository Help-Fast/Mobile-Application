package com.example.helpfastmobile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MeusChamadosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChamadoAdapter chamadoAdapter;
    private ChamadoViewModel chamadoViewModel;
    private ProgressBar progressBar;
    private Button btnVoltar;

    private SessionManager sessionManager; // NOVO

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_chamados);

        // NOVO: Inicializa o SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        // 1. Inicializa as Views
        recyclerView = findViewById(R.id.recycler_view_chamados);
        btnVoltar = findViewById(R.id.button_voltar_menu);
        // progressBar = findViewById(R.id.progress_bar);

        // 2. Configura a RecyclerView
        setupRecyclerView();

        // 3. Obtém a instância da ViewModel
        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        // 4. Configura o observador
        setupObserver();

        // 5. Carrega os dados usando o token salvo
        loadChamados();

        // 6. Configura o botão de voltar
        btnVoltar.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {
        chamadoAdapter = new ChamadoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chamadoAdapter);
    }

    private void setupObserver() {
        chamadoViewModel.getChamadosLiveData().observe(this, chamados -> {
            // progressBar.setVisibility(View.GONE);

            if (chamados != null && !chamados.isEmpty()) {
                chamadoAdapter.setChamados(chamados);
            } else {
                Toast.makeText(this, "Nenhum chamado encontrado.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadChamados() {
        // NOVO: Recupera o token salvo
        String authToken = sessionManager.getAuthToken();

        if (authToken == null) {
            Toast.makeText(this, "Sessão inválida. Faça login novamente.", Toast.LENGTH_LONG).show();
            // TODO: Redirecionar para a tela de login
            return;
        }

        // progressBar.setVisibility(View.VISIBLE);

        // Chama a ViewModel para buscar os chamados com o token real
        chamadoViewModel.getChamados(authToken);
    }
}
