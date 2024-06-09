package com.example.laboratorio6.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.laboratorio6.Adapter.IngresosAdapter;
import com.example.laboratorio6.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class IngresosDetallesActivity extends AppCompatActivity {

    TextView titulo_tw, monto_tw, descripcion_tw,
            fecha_tw;

    Button editButton_user , deleteButton_back_user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresos_detalles);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id_titulo_ingreso = intent.getStringExtra("id_titulo_ingreso");
        String id_monto_ingreso =intent.getStringExtra("id_monto_ingreso");
        String id_desc_ingreso = intent.getStringExtra("id_desc_ingreso");
        String id_fecha_ingreso = intent.getStringExtra("id_fecha_ingreso");

        FirebaseApp.initializeApp(this);

        titulo_tw = findViewById(R.id.id_titulo_ingreso);
        monto_tw = findViewById(R.id.id_monto_ingreso);
        descripcion_tw = findViewById(R.id.id_desc_ingreso);
        fecha_tw = findViewById(R.id.id_fecha_ingreso);


        titulo_tw.setText(id_titulo_ingreso);
        monto_tw.setText(id_monto_ingreso);
        descripcion_tw.setText(id_desc_ingreso);
        fecha_tw.setText(id_fecha_ingreso);


        editButton_user = findViewById(R.id.editButton_ingreso);
        editButton_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                Intent intent = new Intent(IngresosDetallesActivity.this, IngresosEditarActivity.class);
                intent.putExtra("id_titulo_ingreso", titulo_tw.getText().toString());
                intent.putExtra("id_monto_ingreso", monto_tw.getText().toString());
                intent.putExtra("id_desc_ingreso", descripcion_tw.getText().toString());
                intent.putExtra("id_fecha_ingreso", fecha_tw.getText().toString());
                startActivity(intent);
            }
        });

        deleteButton_back_user= findViewById(R.id.deleteButton_ingreso);
        deleteButton_back_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmacionPopup(id_titulo_ingreso);
               /* Intent intent = new Intent(IngresosDetallesActivity.this, IngresosActivity.class);
                ConfirmacionPopup(id_titulo_ingreso);
                startActivity(intent);*/
            }
        });


        }

    private void ConfirmacionPopup(String id_titulo_ingreso){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro de eliminar el Ingreso?");

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Intent intent = new Intent(IngresosDetallesActivity.this, IngresosActivity.class);
                borrarEquipoPorTitulo(id_titulo_ingreso);
                //startActivity(intent);
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
    private void borrarEquipoPorTitulo(String id_titulo_ingreso) {
        db.collection("Ingresos")
                .whereEqualTo("titulo", id_titulo_ingreso)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String borraID = document.getId();

                            db.collection("Ingresos").document(borraID)
                                    .delete()
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(IngresosDetallesActivity.this, "Ingreso con Título " + id_titulo_ingreso + " eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(IngresosDetallesActivity.this, IngresosActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(IngresosDetallesActivity.this, "No se pudo eliminar el Título " + id_titulo_ingreso, Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        // Error al realizar la consulta
                        Toast.makeText(this, "Error al buscar el  Titulo " + id_titulo_ingreso, Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

