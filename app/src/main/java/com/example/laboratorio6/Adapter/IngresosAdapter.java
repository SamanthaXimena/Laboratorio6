package com.example.laboratorio6.Adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio6.Activity.IngresosDetallesActivity;
import com.example.laboratorio6.Data.Ingresos_Data;
import com.example.laboratorio6.R;

import java.util.List;

public class IngresosAdapter extends RecyclerView.Adapter<IngresosAdapter.ViewHolder> {

    private List<Ingresos_Data> ingresos_dataList;

    public IngresosAdapter(List<Ingresos_Data> ingresos_dataList){
        this.ingresos_dataList = ingresos_dataList;
    }


    @Override
    public IngresosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_ingresos_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IngresosAdapter.ViewHolder holder, int position) {
        Ingresos_Data ingresos_data = ingresos_dataList.get(position);
        holder.titulo.setText(ingresos_data.getTitulo());
        holder.fecha.setText(ingresos_data.getFecha());
        holder.monto.setText(ingresos_data.getMonto());

        holder.boton_mas_dtalles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), IngresosDetallesActivity.class);

                intent.putExtra("id_titulo_ingreso", ingresos_data.getTitulo());
                intent.putExtra("id_monto_ingreso", ingresos_data.getMonto());
                intent.putExtra("id_desc_ingreso", ingresos_data.getDescripcion());
                intent.putExtra("id_fecha_ingreso", ingresos_data.getFecha());

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ingresos_dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, fecha, monto;
        ImageButton boton_mas_dtalles;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.item_titulo_ingresos);
            fecha = itemView.findViewById(R.id.item_fecha_ingresos);
            monto = itemView.findViewById(R.id.item_monto_ingresos);
            boton_mas_dtalles = itemView.findViewById(R.id.boton_detalles);

        }
    }
}
