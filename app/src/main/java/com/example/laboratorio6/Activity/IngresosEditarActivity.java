package com.example.laboratorio6.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.laboratorio6.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class IngresosEditarActivity extends AppCompatActivity {
    private Button atras;
    private Button Guardar;
    FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingresos_editar);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id_titulo_ingreso = intent.getStringExtra("id_titulo_ingreso");
        String id_monto_ingreso = intent.getStringExtra("id_monto_ingreso");
        String id_desc_ingreso = intent.getStringExtra("id_desc_ingreso");
        String id_fecha_ingreso =  intent.getStringExtra("id_fecha_ingreso");


        TextView titulo_et= findViewById(R.id.id_titulo_ingreso);
        EditText monto_et = findViewById(R.id.id_monto_ingreso);
        EditText descripcion_et = findViewById(R.id.id_desc_ingreso);
        TextView fecha_et = findViewById(R.id.id_fecha_ingreso);

        titulo_et.setText(id_titulo_ingreso);
        monto_et.setText(id_monto_ingreso);
        descripcion_et.setText(id_desc_ingreso);
        fecha_et.setText(id_fecha_ingreso);

        atras =  findViewById(R.id.backButton_ingreso);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(IngresosEditarActivity.this,IngresosActivity.class);
                intent.putExtra("id_titulo_ingreso", id_titulo_ingreso);
                intent.putExtra("id_monto_ingreso", id_monto_ingreso );
                intent.putExtra("id_desc_ingreso", id_desc_ingreso);
                intent.putExtra("id_fecha_ingreso", id_fecha_ingreso);
                v.getContext().startActivity(intent);
                startActivity(intent);
                finish();
            }
        });

        Guardar =  findViewById(R.id.saveButton_ingreso);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nuevaMonto = monto_et.getText().toString();
                String nuevoDesc = descripcion_et.getText().toString();

                Log.d("IngresosEditarActivity", "Datos obtenidos para edición: " +
                        "titulo=" + id_titulo_ingreso + ", nuevaMonto=" + nuevaMonto + ", nuevoDesc=" + nuevoDesc + ", fecha=" + id_fecha_ingreso);

                ConfirmacionPopup(id_titulo_ingreso, nuevaMonto, nuevoDesc, id_fecha_ingreso);
            }
        });

    }


    private void ConfirmacionPopup(String id_titulo_ingreso, String nuevaMonto, String nuevoDesc, String fecha) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro de guardar los cambios?");


        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                editarIngreso(id_titulo_ingreso, nuevaMonto, nuevoDesc, fecha);
                Intent intent = new Intent(IngresosEditarActivity.this, IngresosActivity.class);
                startActivity(intent);
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

    private void editarIngreso(String id_titulo_ingreso, String nuevaMonto, String nuevoDesc, String fecha) {

        db.collection("Ingresos")
                .whereEqualTo("titulo", id_titulo_ingreso)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Obtener la referencia del primer documento encontrado
                            DocumentReference IngresosRef = task.getResult().getDocuments().get(0).getReference();

                            // Actualizar el documento
                            IngresosRef.update("monto", nuevaMonto,
                                            "descripcion", nuevoDesc)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("IngresosEditarActivity", "Ingreso actualizado con éxito");
                                        // Éxito al actualizar
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("IngresosEditarActivity", "Error al actualizar ", e);
                                        // Error al actualizar
                                    });
                        } else {
                            // No se encontró ningún documento con el `sku` especificado
                            Log.e("IngresosEditarActivity", "El documento con titulo " + id_titulo_ingreso + " no existe.");
                        }
                    } else {
                        Log.e("IngresosEditarActivity", "Error al obtener el documento", task.getException());
                    }
                });
    }


}

