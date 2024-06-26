package com.example.laboratorio6;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laboratorio6.Activity.IngresosActivity;
import com.example.laboratorio6.Activity.RegisterActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPasword;
    Button buttonLogin_correo_passwrd;
    ImageButton login_button_facebook;

    FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar progressBar;
    Button registrar_inicio;
    CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;

    private GoogleSignInClient mGoogleSignInClient;
    ImageButton login_button_Google;


    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(MainActivity.this , IngresosActivity.class );
            startActivity(intent);
        }
        updateUI(currentUser);

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.email);
        editTextPasword = findViewById(R.id.password);
        buttonLogin_correo_passwrd = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        registrar_inicio = findViewById(R.id.registerNow);
        String TAG = "GoogleSignIn";

//-------------------AUTHENTICATION CORREO Y CONTRASEÑA -------------------------//
        buttonLogin_correo_passwrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPasword.getText());
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(MainActivity.this, "Ingresa un correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Logueo Exitosamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), IngresosActivity.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication Fallido.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
//-------------------AUTHENTICATION GOOGLE -------------------------//

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(com.firebase.ui.auth.R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, options);



        login_button_Google = findViewById(R.id.btn_login_google);
        login_button_Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sigIn();

            }
        });


        //-------------------AUTHENTICATION FACEBOOK -------------------------//
        callbackManager = CallbackManager.Factory.create();

        login_button_facebook = findViewById(R.id.btn_login_fb);
        login_button_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("email", "public_profile"));
            }
        });

       // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        //startActivity(new Intent(MainActivity.this , IngresosActivity.class));
                        handleFacebookAccessToken(loginResult.getAccessToken());
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel");
                        // App code
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:onError", error);
                        // App code
                    }
                });


        //-------------------REGISTRO USURIO -------------------------//
        registrar_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();


    }

    private void sigIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {

                            Toast.makeText(MainActivity.this, "" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);

                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user){
        Intent intent = new Intent(MainActivity.this , IngresosActivity.class );
        startActivity(intent);

    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful()){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId()); firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In fallido, actualizar GUI
                    Log.w(TAG, "Google sign in failed", e);
                    }

                } else{
                    Log.d(TAG, "Error, login no exitoso:" + task.getException().toString());
                    Toast.makeText(this, "Ocurrio un error. "+task.getException().toString(),
                    Toast.LENGTH_LONG).show(); }


            }


    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential) .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @SuppressLint("RestrictedApi")
            @Override public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                Log.d(TAG, "signInWithCredential:success");
                    //FirebaseUser user = mAuth.getCurrentUser();
                    Intent intent = new Intent(MainActivity.this , IngresosActivity.class );
                    startActivity(intent);
                    finish();

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException()); } } }); }

}