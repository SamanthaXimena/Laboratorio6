package com.example.laboratorio6;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.laboratorio6.Activity.EgresosActivity;
import com.example.laboratorio6.Activity.IngresosActivity;
import com.example.laboratorio6.Activity.ResumenActivity;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MenuBarFragment extends Fragment {
    private boolean isIngresosSelected = false;
    private boolean isEgresosSelected = false;
    private boolean isResumenSelected = false;
    private boolean isLogoutSelected = false;

    private ImageButton imageButtonIngresos, imageButtonEgresos, imageButtonResumen, imageButtonLogout;
    private TextView textViewIngresos, textViewEgresos, textViewResumen, textViewLogout;
    private LinearLayout layoutIngresos, layoutEgresos, layoutResumen, layoutLogout;

    FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu_bar_fragment, container, false);

        layoutIngresos = view.findViewById(R.id.layoutIngresos);
        layoutEgresos = view.findViewById(R.id.layoutEgresos);
        layoutResumen = view.findViewById(R.id.layoutResumen);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        imageButtonIngresos = view.findViewById(R.id.imageButtonIngresos);
        imageButtonEgresos = view.findViewById(R.id.imageButtonEgresos);
        imageButtonResumen = view.findViewById(R.id.imageButtonResumen);
        imageButtonLogout = view.findViewById(R.id.imageButtonLogout);

        textViewIngresos = view.findViewById(R.id.textViewIngresos);
        textViewEgresos = view.findViewById(R.id.textViewEgresos);
        textViewResumen = view.findViewById(R.id.textViewResumen);
        textViewLogout = view.findViewById(R.id.textViewLogout);

        configurarListeners();

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    private void configurarListeners() {
        imageButtonIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(true, false, false, false);
            }
        });

        imageButtonEgresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(false, true, false, false);
            }
        });

        imageButtonResumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(false, false, true, false);
            }
        });

        imageButtonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOptionClick(false, false, false, true);
                //LoginManager.getInstance().logOut();
                mAuth.signOut();
                // Iniciar MainActivity y finalizar la actividad actual
                Intent intent = new Intent(getActivity() , MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                //getActivity().finish();  // Finaliza la actividad actual

            }
        });

    }

    private void handleOptionClick(boolean isIngresos, boolean isEgresos, boolean isResumen, boolean isLogout) {
        resetOtherOptions();

        if (isIngresos) {
            isIngresosSelected  = true;
            layoutIngresos.setBackgroundColor(Color.parseColor("#b57c00"));
            imageButtonIngresos.setScaleX(0.8f);
            imageButtonIngresos.setScaleY(0.8f);
            textViewIngresos.setVisibility(View.VISIBLE);

            Intent intent = new Intent(getActivity(), IngresosActivity.class);
            startActivity(intent);
        }

        if (isEgresos) {
            isEgresosSelected  = true;
            layoutEgresos.setBackgroundColor(Color.parseColor("#b57c00"));
            imageButtonEgresos.setScaleX(0.8f);
            imageButtonEgresos.setScaleY(0.8f);
            textViewEgresos.setVisibility(View.VISIBLE);

            Intent intent = new Intent(getActivity(), EgresosActivity.class);
            startActivity(intent);
        }

        if (isResumen) {
            isResumenSelected = true;
            layoutResumen.setBackgroundColor(Color.parseColor("#b57c00"));
            imageButtonResumen.setScaleX(0.8f);
            imageButtonResumen.setScaleY(0.8f);
            textViewResumen.setVisibility(View.VISIBLE);

            Intent intent = new Intent(getActivity(), ResumenActivity.class);
            startActivity(intent);
        }

        if (isLogout) {
            isLogoutSelected = true;
            layoutLogout.setBackgroundColor(Color.parseColor("#b57c00"));
            imageButtonLogout.setScaleX(0.8f);
            imageButtonLogout.setScaleY(0.8f);
            textViewLogout.setVisibility(View.VISIBLE);


        }


    }

    private void resetOtherOptions() {
        if (isIngresosSelected) {
            isIngresosSelected = false;
            layoutIngresos.setBackgroundColor(Color.TRANSPARENT);
            imageButtonIngresos.setScaleX(1.0f);
            imageButtonIngresos.setScaleY(1.0f);
            textViewIngresos.setVisibility(View.GONE);
        }

        if (isEgresosSelected) {
            isEgresosSelected = false;
            layoutEgresos.setBackgroundColor(Color.TRANSPARENT);
            imageButtonEgresos.setScaleX(1.0f);
            imageButtonEgresos.setScaleY(1.0f);
            textViewEgresos.setVisibility(View.GONE);

        }

        if (isResumenSelected) {
            isResumenSelected = false;
            layoutResumen.setBackgroundColor(Color.TRANSPARENT);
            imageButtonResumen.setScaleX(1.0f);
            imageButtonResumen.setScaleY(1.0f);
            textViewResumen.setVisibility(View.GONE);

        }

        if (isLogoutSelected) {
            isLogoutSelected = false;
            layoutLogout.setBackgroundColor(Color.TRANSPARENT);
            imageButtonLogout.setScaleX(1.0f);
            imageButtonLogout.setScaleY(1.0f);
            textViewLogout.setVisibility(View.GONE);


        }


    }
}

