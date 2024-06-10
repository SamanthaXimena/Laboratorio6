package com.example.laboratorio6.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laboratorio6.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class EgresosDetallesActivity  extends AppCompatActivity {
    TextView titulo_tw, monto_tw, descripcion_tw,
            fecha_tw;

    Button editButton_user , deleteButton_back_user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_egresos_detalles);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id_titulo_egresos = intent.getStringExtra("id_titulo_egresos");
        String id_monto_egresos = String.valueOf(intent.getStringExtra("id_monto_egresos"));
        String id_desc_egresos = intent.getStringExtra("id_desc_egresos");
        String id_fecha_egresos= intent.getStringExtra("id_fecha_egresos");

        FirebaseApp.initializeApp(this);

        titulo_tw = findViewById(R.id.id_titulo_egresos);
        monto_tw = findViewById(R.id.id_monto_egresos);
        descripcion_tw = findViewById(R.id.id_desc_egresos);
        fecha_tw = findViewById(R.id.id_fecha_egresos);


        titulo_tw.setText(id_titulo_egresos);
        monto_tw.setText(id_monto_egresos);
        descripcion_tw.setText(id_desc_egresos);
        fecha_tw.setText(id_fecha_egresos);


        editButton_user = findViewById(R.id.editButton_egresos);
        editButton_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(EgresosDetallesActivity.this, EgresosEditarActivity.class);
                intent.putExtra("id_titulo_egresos", id_titulo_egresos);
                intent.putExtra("id_monto_egresos", id_monto_egresos);
                intent.putExtra("id_desc_egresos", id_desc_egresos);
                intent.putExtra("id_fecha_egresos", id_fecha_egresos);
                startActivity(intent);
            }
        });

        deleteButton_back_user= findViewById(R.id.deleteButton_egresos);
        deleteButton_back_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmacionPopup(id_titulo_egresos);
                Intent intent = new Intent(EgresosDetallesActivity.this, EgresosActivity.class);
                startActivity(intent);
            }
        });


    }

    private void ConfirmacionPopup(String id_titulo_egresos){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro de eliminar el equipo?");


        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent intent = new Intent(EgresosDetallesActivity.this, EgresosActivity.class);
                borrarEquipoPorTitulo(id_titulo_egresos);
               // startActivity(intent);
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();


    }
    private void borrarEquipoPorTitulo(String id_titulo_egresos) {
        db.collection("Egresos")
                .whereEqualTo("titulo", id_titulo_egresos)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String ID = document.getId();

                            db.collection("Egresos").document(ID)
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(EgresosDetallesActivity.this, "Egreso con Título " + id_titulo_egresos + " eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EgresosDetallesActivity.this, EgresosActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(EgresosDetallesActivity.this, "No se pudo eliminar el Título " + id_titulo_egresos, Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Error al realizar la consulta
                        Toast.makeText(this, "Error al buscar el  Titulo " + id_titulo_egresos, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}