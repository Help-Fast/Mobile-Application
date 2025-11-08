package com.example.helpfastmobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

// O Adapter é o responsável por conectar os dados (a lista de chamados) 
// com a RecyclerView, gerenciando a criação e a reciclagem das views.
public class ChamadoAdapter extends RecyclerView.Adapter<ChamadoAdapter.ChamadoViewHolder> {

    // Lista que armazena os dados a serem exibidos
    private List<Chamado> chamados = new ArrayList<>();

    // Este método é chamado quando a RecyclerView precisa de uma nova view.
    // Ele infla (cria) o layout do item a partir do XML.
    @NonNull
    @Override
    public ChamadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chamado, parent, false);
        return new ChamadoViewHolder(itemView);
    }

    // Este método é chamado para exibir os dados em uma posição específica.
    // Ele pega os dados do chamado e os coloca nas views do ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull ChamadoViewHolder holder, int position) {
        Chamado currentChamado = chamados.get(position);
        holder.tvChamadoIdAssunto.setText("ID: #" + currentChamado.getId() + " - " + currentChamado.getAssunto());
        holder.tvChamadoStatus.setText("Status: " + currentChamado.getStatus());
    }

    // Retorna o número total de itens na lista.
    @Override
    public int getItemCount() {
        return chamados.size();
    }

    // Método para atualizar a lista de chamados no adapter.
    public void setChamados(List<Chamado> chamados) {
        this.chamados = chamados;
        notifyDataSetChanged(); // Notifica a RecyclerView que os dados mudaram.
    }

    // A classe ViewHolder descreve a view de um item e seus metadados.
    // Ela "segura" as referências para as views de cada item (evita findViewById repetidos).
    class ChamadoViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvChamadoIdAssunto;
        private final TextView tvChamadoStatus;

        public ChamadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChamadoIdAssunto = itemView.findViewById(R.id.tv_chamado_id_assunto);
            tvChamadoStatus = itemView.findViewById(R.id.tv_chamado_status);
        }
    }
}
