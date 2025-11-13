package com.example.helpfastmobile.ui._legacy;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpfastmobile.R;
import com.example.helpfastmobile.viewmodel.ChamadoViewModel;

public class FinalizacaoChamadoActivity extends AppCompatActivity {

    private Button btnVoltar;
    private ChamadoViewModel chamadoViewModel;
    private int chamadoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizacao_chamado);

        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);
        chamadoId = getIntent().getIntExtra("CHAMADO_ID", -1);

        // Encontra o botão de voltar no layout.
        btnVoltar = findViewById(R.id.button_voltar_menu);

        // A lógica de finalização e seus observers foram removidos,
        // pois esta tela é apenas de confirmação visual.

        btnVoltar.setOnClickListener(v -> finish());
    }

    // Métodos 'setupObservers' e 'handleFinalizarChamado' foram removidos por não serem mais necessários.
}
