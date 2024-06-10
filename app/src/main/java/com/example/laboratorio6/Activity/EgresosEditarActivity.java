package com.example.laboratorio6.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laboratorio6.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class EgresosEditarActivity extends AppCompatActivity {

    private Button atras;
    private Button Guardar;
    FirebaseFirestore db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_egresos_editar);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String id_titulo_egresos = intent.getStringExtra("id_titulo_egresos");
        String id_monto_egresos =intent.getStringExtra("id_monto_egresos");
        String id_desc_egresos = intent.getStringExtra("id_desc_egresos");
        String id_fecha_egresos= intent.getStringExtra("id_fecha_egresos");

        TextView titulo_et= findViewById(R.id.id_titulo_egresos);
        EditText monto_et = findViewById(R.id.id_monto_egresos);
        EditText descripcion_et = findViewById(R.id.id_desc_egresos);
        TextView fecha_et = findViewById(R.id.id_fecha_egresos);

        titulo_et.setText(id_titulo_egresos);
        monto_et.setText(id_monto_egresos);
        descripcion_et.setText(id_desc_egresos);
        fecha_et.setText(id_fecha_egresos);

        atras =  findViewById(R.id.backButton_egresos);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(EgresosEditarActivity.this,EgresosActivity.class);
                intent.putExtra("id_titulo_egresos", id_titulo_egresos);
                intent.putExtra("id_monto_egresos", id_monto_egresos );
                intent.putExtra("id_desc_egresos", id_desc_egresos);
                intent.putExtra("id_fecha_egresos", id_fecha_egresos);
                v.getContext().startActivity(intent);
                startActivity(intent);
                finish();
            }
        });

        Guardar =  findViewById(R.id.saveButton_egresos);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nuevaMonto = monto_et.getText().toString();
                String nuevoDesc = descripcion_et.getText().toString();

                Log.d("EgresosEditarActivity", "Datos obtenidos para edición: " +
                        "titulo=" + id_titulo_egresos + ", nuevaMonto=" + nuevaMonto + ", nuevoDesc=" + nuevoDesc + ", fecha=" + id_fecha_egresos);

                ConfirmacionPopup(id_titulo_egresos, nuevaMonto, nuevoDesc, id_fecha_egresos);
            }
        });

    }


    private void ConfirmacionPopup(String id_titulo_egresos, String nuevaMonto, String nuevoDesc, String fecha) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estas seguro de guardar los cambios?");


        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Validar que nuevaMonto no esté vacío y sea un número válido
                if (nuevaMonto != null && !nuevaMonto.isEmpty()) {
                    try {
                        String nuevoMontoVal = String.valueOf(Double.parseDouble(nuevaMonto));

                        // Actualizar el ingreso con el nuevo monto
                        editarEgreso(id_titulo_egresos, nuevaMonto, nuevoDesc, fecha);

                        // Navegar a IngresosActivity
                        Intent intent = new Intent(EgresosEditarActivity.this, IngresosActivity.class);
                        startActivity(intent);

                        // Finalizar la actividad actual
                        finish();
                    } catch (NumberFormatException e) {
                        // Mostrar un mensaje de error si el monto no es un número válido
                        Toast.makeText(EgresosEditarActivity.this, "El monto debe ser un número válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Mostrar un mensaje de error si el monto está vacío
                    Toast.makeText(EgresosEditarActivity.this, "El monto no puede estar vacío", Toast.LENGTH_SHORT).show();
                }

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

    private void editarEgreso(String id_titulo_egresos, String nuevaMonto, String nuevoDesc, String fecha) {
        db.collection("Egresos")
                .whereEqualTo("titulo", id_titulo_egresos)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            DocumentReference EgresosRef = task.getResult().getDocuments().get(0).getReference();

                            // Actualizar el documento
                            EgresosRef.update("monto", nuevaMonto,
                                            "descripcion", nuevoDesc)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d("EgresosEditarActivity", "Egreso actualizado con éxito");
                                        // Éxito al actualizar
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("EgresosEditarActivity", "Error al actualizar ", e);
                                        // Error al actualizar
                                    });
                        } else {
                            // No se encontró ningún documento con el `sku` especificado
                            Log.e("EgresosEditarActivity", "El documento con titulo " + id_titulo_egresos + " no existe.");
                        }
                    } else {
                        Log.e("EgresosEditarActivity", "Error al obtener el documento", task.getException());
                    }
                });
    }


}
