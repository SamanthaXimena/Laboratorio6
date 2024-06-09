package com.example.laboratorio6.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio6.Activity.EgresosDetallesActivity;
import com.example.laboratorio6.Data.Egresos_Data;
import com.example.laboratorio6.R;

import java.util.List;

public class EgresosAdapter extends RecyclerView.Adapter<EgresosAdapter.ViewHolder>{

    private List<Egresos_Data> egresos_dataList;

    public EgresosAdapter(List<Egresos_Data> egresos_dataList){
        this.egresos_dataList = egresos_dataList;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_egresos_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Egresos_Data egresos_data = egresos_dataList.get(position);
        holder.titulo.setText(egresos_data.getTitulo());
        holder.fecha.setText(egresos_data.getFecha());
        holder.monto.setText(egresos_data.getMonto());

        holder.boton_mas_dtalles_egresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EgresosDetallesActivity.class);

                intent.putExtra("id_titulo_egresos", egresos_data.getTitulo());
                intent.putExtra("id_monto_egresos", egresos_data.getMonto());
                intent.putExtra("id_desc_egresos", egresos_data.getDescripcion());
                intent.putExtra("id_fecha_egresos", egresos_data.getFecha());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return egresos_dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fecha, monto;
        ImageButton boton_mas_dtalles_egresos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_titulo_egresos);
            fecha = itemView.findViewById(R.id.item_fecha_egresos);
            monto = itemView.findViewById(R.id.item_monto_egresos);
            boton_mas_dtalles_egresos = itemView.findViewById(R.id.boton_detalles_egresos);

        }
    }
}
