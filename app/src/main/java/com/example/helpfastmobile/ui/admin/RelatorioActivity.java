package com.example.helpfastmobile.ui.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpfastmobile.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {

    private Spinner spinnerMes;
    private Spinner spinnerAno;
    private Button buttonBaixarRelatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        spinnerMes = findViewById(R.id.spinner_mes);
        spinnerAno = findViewById(R.id.spinner_ano);
        buttonBaixarRelatorio = findViewById(R.id.button_baixar_relatorio);

        setupSpinners();

        buttonBaixarRelatorio.setOnClickListener(v -> {
            // Lógica para baixar o relatório será implementada aqui.
            // Por enquanto, apenas exibimos uma mensagem.

            String mesSelecionado = spinnerMes.getSelectedItem().toString();
            String anoSelecionado = spinnerAno.getSelectedItem().toString();

            Toast.makeText(this, "Em breve: Download do relatório para " + mesSelecionado + " de " + anoSelecionado, Toast.LENGTH_LONG).show();
        });
    }

    private void setupSpinners() {
        // Configura o Spinner de Mês
        ArrayAdapter<CharSequence> mesAdapter = ArrayAdapter.createFromResource(this,
                R.array.meses_array, android.R.layout.simple_spinner_item);
        mesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(mesAdapter);

        // Configura o Spinner de Ano
        ArrayList<String> anos = new ArrayList<>();
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = anoAtual; i >= anoAtual - 5; i--) { // Popula com os últimos 5 anos
            anos.add(Integer.toString(i));
        }
        ArrayAdapter<String> anoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, anos);
        anoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAno.setAdapter(anoAdapter);

        // Pré-seleciona o mês e ano atuais
        int mesAtual = Calendar.getInstance().get(Calendar.MONTH);
        spinnerMes.setSelection(mesAtual);
        spinnerAno.setSelection(0); // O primeiro item é o ano atual
    }
}
