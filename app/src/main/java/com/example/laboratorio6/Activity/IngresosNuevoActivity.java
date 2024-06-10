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

import com.example.laboratorio6.Adapter.IngresosAdapter;
import com.example.laboratorio6.Data.Ingresos_Data;
import com.example.laboratorio6.R;
import com.example.laboratorio6.databinding.ActivityIngresosNuevoBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class IngresosNuevoActivity extends AppCompatActivity {

    private List<Ingresos_Data> ingresos_dataList;
    IngresosAdapter adapter;

    ListenerRegistration snapshotListener;
    ActivityIngresosNuevoBinding binding ;
    FirebaseFirestore db;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos_nuevo);

        db = FirebaseFirestore.getInstance();

        binding = ActivityIngresosNuevoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        binding.saveButtonIngreso.setOnClickListener(view -> {
            ConfirmacionPopup();
        });

    }

    private void ConfirmacionPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro de guardar los cambios?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                guardarIngreso();
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

    private void guardarIngreso() {

        String titulo = binding.idTituloIngreso.getText().toString();
        String monto = binding.idMontoIngreso.getText().toString();
        String descripcion = binding.idDescIngreso.getText().toString();
        String fecha = binding.idFechaIngreso.getText().toString();


        Ingresos_Data ingresosData  = new Ingresos_Data(titulo, monto,descripcion, fecha);
        ingresosData.setTitulo(titulo);
        ingresosData.setMonto(monto);
        ingresosData.setDescripcion(descripcion);
        ingresosData.setFecha(fecha);




        db.collection("Ingresos")
                .document(titulo)
                .set(ingresosData)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(IngresosNuevoActivity.this, "Ingreso guardado", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IngresosNuevoActivity.this, IngresosActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(IngresosNuevoActivity.this, "Algo pasó al guardar ", Toast.LENGTH_SHORT).show();
                });
    }


}