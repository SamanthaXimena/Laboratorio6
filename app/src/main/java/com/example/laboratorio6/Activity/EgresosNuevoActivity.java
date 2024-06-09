package com.example.laboratorio6.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.laboratorio6.Adapter.EgresosAdapter;
import com.example.laboratorio6.Data.Egresos_Data;

import com.example.laboratorio6.R;
import com.example.laboratorio6.databinding.ActivityEgresosNuevoBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class EgresosNuevoActivity extends AppCompatActivity {

    private List<Egresos_Data> egresos_dataList;
    EgresosAdapter adapter;


    ListenerRegistration snapshotListener;
    ActivityEgresosNuevoBinding binding ;
    FirebaseFirestore db;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egresos_nuevo);

        db = FirebaseFirestore.getInstance();

        binding = ActivityEgresosNuevoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        binding.saveButtonEgresos.setOnClickListener(view -> {
            ConfirmacionPopup();
        });

    }

    private void ConfirmacionPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro de guardar los cambios?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardarEgresos();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (snapshotListener != null)
            snapshotListener.remove();
    }

    private void guardarEgresos() {

        String titulo = binding.idTituloEgresos.getText().toString();
        String monto =binding.idMontoEgresos.getText().toString();
        String descripcion = binding.idDescEgresos.getText().toString();
        String fecha = binding.idFechaEgresos.getText().toString();


        Egresos_Data egresosData  = new Egresos_Data(titulo, monto,descripcion, fecha);
        egresosData.setTitulo(titulo);
        egresosData.setMonto(monto);
        egresosData.setDescripcion(descripcion);
        egresosData.setFecha(fecha);




        db.collection("Egresos")
                .document(titulo)
                .set(egresosData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(EgresosNuevoActivity.this, "Egreso guardado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EgresosNuevoActivity.this, EgresosActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EgresosNuevoActivity.this, "Algo pasó al guardar ", Toast.LENGTH_SHORT).show();
                });
    }


}