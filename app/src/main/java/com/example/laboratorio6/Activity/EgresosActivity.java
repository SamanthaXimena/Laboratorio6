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

import com.example.laboratorio6.Adapter.EgresosAdapter;
import com.example.laboratorio6.Data.Egresos_Data;
import com.example.laboratorio6.MainActivity;
import com.example.laboratorio6.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EgresosActivity extends AppCompatActivity {

    FloatingActionButton fab;
    private EgresosAdapter adapter;
    private RecyclerView recyclerView;
    List<Egresos_Data> egresos_List = new ArrayList<>();
    TextView nombre_user;
    Button button_logout;
    FirebaseUser user;
    private FirebaseAuth auth;
    FirebaseFirestore firestore_lista_egresos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egresos);
        firestore_lista_egresos = FirebaseFirestore.getInstance();
        nombre_user = findViewById(R.id.nombre_user);
        //button_logout = findViewById(R.id.btn_logout);
        recyclerView = findViewById(R.id.recyclerView_egresos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EgresosAdapter(egresos_List);
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
                Intent intent = new Intent(EgresosActivity.this, EgresosNuevoActivity.class);
                startActivity(intent);
            }
        });

        /*cargar datos de la Firebase a recycler*/
        CargarDatos_lista_ingresos();


    }

    private void CargarDatos_lista_ingresos() {
        egresos_List.clear();
        firestore_lista_egresos.collection("Egresos")
                .addSnapshotListener((value, error) -> {

                    if (error != null){
                        Toast.makeText(this, "Lista de Egresos Cargando" , Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (DocumentChange dc : Objects.requireNonNull(value).getDocumentChanges()){

                        if (dc.getType() == DocumentChange.Type.ADDED){
                            //ingresos_List.add(dc.getDocument().toObject(Ingresos_Data.class));
                            egresos_List.add(dc.getDocument().toObject(Egresos_Data.class));
                        }

                        adapter.notifyDataSetChanged();
                    }

                });

    }





}