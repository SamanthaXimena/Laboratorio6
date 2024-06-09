package com.example.laboratorio6.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laboratorio6.Adapter.IngresosAdapter;
import com.example.laboratorio6.Data.Ingresos_Data;
import com.example.laboratorio6.MainActivity;
import com.example.laboratorio6.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IngresosActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private IngresosAdapter adapter;
    private RecyclerView recyclerView;
    List<Ingresos_Data> ingresos_List = new ArrayList<>();
    TextView nombre_user;
    Button button_logout;
    FirebaseUser user;
    private FirebaseAuth auth;
    FirebaseFirestore firestore_lista_ingresos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);
        firestore_lista_ingresos = FirebaseFirestore.getInstance();
        nombre_user = findViewById(R.id.nombre_user);
        //button_logout = findViewById(R.id.btn_logout);
        recyclerView = findViewById(R.id.recyclerView_ingresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IngresosAdapter(ingresos_List);
        recyclerView.setAdapter(adapter);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            nombre_user.setText(user.getEmail());
        }

        fab = findViewById(R.id.fab_user);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngresosActivity.this, IngresosNuevoActivity.class);
                startActivity(intent);
            }
        });

        /*cargar datos de la Firebase a recycler*/
        CargarDatos_lista_ingresos();


    }

    private void CargarDatos_lista_ingresos() {
        ingresos_List.clear();
        firestore_lista_ingresos.collection("Ingresos")
                .addSnapshotListener((value, error) -> {

                    if (error != null){
                        Toast.makeText(this, "Lista de Ingresos Cargando" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){

                        if (dc.getType() == DocumentChange.Type.ADDED){
                            //ingresos_List.add(dc.getDocument().toObject(Ingresos_Data.class));
                            ingresos_List.add(dc.getDocument().toObject(Ingresos_Data.class));
                        }

                        adapter.notifyDataSetChanged();
                    }

                });

    }





}