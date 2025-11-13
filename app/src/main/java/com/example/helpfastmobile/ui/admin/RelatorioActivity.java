package com.example.helpfastmobile.ui.admin;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.helpfastmobile.R;
import com.example.helpfastmobile.data.model.Chamado;
import com.example.helpfastmobile.viewmodel.ChamadoViewModel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RelatorioActivity extends AppCompatActivity {

    private static final String TAG = "RelatorioActivity";

    private Spinner spinnerMes;
    private Button buttonBaixarRelatorio;
    private Button buttonVoltarMenu;
    private ChamadoViewModel chamadoViewModel;

    // Launcher para a permissão de escrita
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Permissão concedida, tenta baixar o relatório novamente
                    baixarRelatorio();
                } else {
                    // Permissão negada
                    Toast.makeText(this, "Permissão de escrita negada. Não é possível salvar o relatório.", Toast.LENGTH_LONG).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        chamadoViewModel = new ViewModelProvider(this).get(ChamadoViewModel.class);

        spinnerMes = findViewById(R.id.spinner_mes);
        buttonBaixarRelatorio = findViewById(R.id.button_baixar_relatorio);
        buttonVoltarMenu = findViewById(R.id.button_voltar_menu);

        setupSpinnerMes();
        setupObservers();

        buttonBaixarRelatorio.setOnClickListener(v -> baixarRelatorio());
        buttonVoltarMenu.setOnClickListener(v -> finish()); // Ação para o botão voltar
    }

    private void setupSpinnerMes() {
        ArrayAdapter<CharSequence> mesAdapter = ArrayAdapter.createFromResource(this,
                R.array.meses_array, android.R.layout.simple_spinner_item);
        mesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMes.setAdapter(mesAdapter);

        int mesAtual = Calendar.getInstance().get(Calendar.MONTH);
        spinnerMes.setSelection(mesAtual);
    }

    private void setupObservers() {
        chamadoViewModel.getTodosChamadosResult().observe(this, chamados -> {
            if (chamados != null && !chamados.isEmpty()) {
                // Filtra os chamados pelo mês selecionado
                int mesSelecionado = spinnerMes.getSelectedItemPosition() + 1; // +1 porque o spinner é 0-indexed
                List<Chamado> chamadosFiltrados = chamados.stream()
                        .filter(c -> {
                            try {
                                // Extrai o mês da data (formato esperado: AAAA-MM-DD...)
                                int mesDoChamado = Integer.parseInt(c.getDataAbertura().substring(5, 7));
                                return mesDoChamado == mesSelecionado;
                            } catch (Exception e) {
                                return false;
                            }
                        })
                        .collect(Collectors.toList());

                if (chamadosFiltrados.isEmpty()) {
                    Toast.makeText(this, "Nenhum chamado encontrado para o mês selecionado.", Toast.LENGTH_SHORT).show();
                } else {
                    gerarCsv(chamadosFiltrados);
                }
            } else {
                Toast.makeText(this, "Nenhum chamado para gerar relatório.", Toast.LENGTH_SHORT).show();
            }
        });

        chamadoViewModel.getTodosChamadosError().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Erro ao buscar chamados: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void baixarRelatorio() {
        // A busca de dados é o primeiro passo
        Toast.makeText(this, "Buscando dados do relatório...", Toast.LENGTH_SHORT).show();
        chamadoViewModel.getTodosChamados();
    }

    private void gerarCsv(List<Chamado> chamados) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Pede permissão para Android antigo
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return;
        }

        StringBuilder csvContent = new StringBuilder();
        // Cabeçalho
        csvContent.append("ID,Motivo,Status,DataAbertura,TecnicoId\n");

        // Linhas
        for (Chamado chamado : chamados) {
            csvContent.append(chamado.getId()).append(",");
            csvContent.append(escapeCsvField(chamado.getMotivo())).append(",");
            csvContent.append(escapeCsvField(chamado.getStatus())).append(",");
            csvContent.append(chamado.getDataAbertura()).append(",");
            csvContent.append(Objects.toString(chamado.getTecnicoId(), "")).append("\n");
        }

        salvarArquivoCsv(csvContent.toString());
    }

    private void salvarArquivoCsv(String csvContent) {
        String mesSelecionado = spinnerMes.getSelectedItem().toString();
        int anoAtual = Calendar.getInstance().get(Calendar.YEAR);
        String fileName = "relatorio_" + mesSelecionado.toLowerCase() + "_" + anoAtual + ".csv";

        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "text/csv");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

            Uri uri = getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);
            if (uri == null) {
                throw new IOException("Falha ao criar o arquivo na pasta de Downloads.");
            }

            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                if (outputStream == null) {
                    throw new IOException("Falha ao abrir o stream de saída para o arquivo.");
                }
                outputStream.write(csvContent.getBytes());
            }

            Toast.makeText(this, "Relatório salvo em Downloads!", Toast.LENGTH_LONG).show();
            Log.i(TAG, "Relatório salvo com sucesso em: " + uri.toString());

        } catch (IOException e) {
            Toast.makeText(this, "Falha ao salvar o arquivo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e(TAG, "Erro ao salvar o arquivo CSV", e);
        }
    }

    // Função para lidar com vírgulas e aspas dentro dos campos
    private String escapeCsvField(String data) {
        if (data == null) {
            return "";
        }
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }
}
