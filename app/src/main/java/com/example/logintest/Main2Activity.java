package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.droidnet.DroidNet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Main2Activity extends AppCompatActivity  {
    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private TextView loginRegBtn;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    private DroidNet mDroidNet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        imageView1=findViewById(R.id.image111);
        Picasso.get().load("https://i.imgur.com/oISHJ3E.jpg").into(imageView1);
        imageView2=findViewById(R.id.image112);
        Picasso.get().load("https://i.imgur.com/5Is9jIp.jpg").into(imageView2);
        imageView3=findViewById(R.id.image113);
        Picasso.get().load("https://i.imgur.com/JEjOiuf.jpg").into(imageView3);
        imageView4=findViewById(R.id.image114);
        Picasso.get().load("https://i.imgur.com/yV5QqrA.jpg").into(imageView4);
        imageView5=findViewById(R.id.image115);
        Picasso.get().load("https://i.imgur.com/J6iKb5i.jpg").into(imageView5);

        mAuth = FirebaseAuth.getInstance();
        loginEmailText = findViewById( R.id.reg_email);
        loginPassText = findViewById( R.id.reg_confirm_pass );
        loginBtn = findViewById( R.id.login_btn );
        loginRegBtn = findViewById( R.id.login_reg_btn );
        loginProgress = findViewById( R.id.login_progress );
        loginRegBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent( Main2Activity.this, registeractivity.class );
                startActivity( regIntent );

            }
        } );

        loginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();
                if (TextUtils.isEmpty( loginEmail ) && TextUtils.isEmpty( loginPass )) {
                    Toast.makeText( Main2Activity.this, "Fields Are Empty", Toast.LENGTH_LONG ).show();
                    YoYo.with( Techniques.Shake )
                            .duration( 750 )
                            .repeat( 2 )
                            .playOn( findViewById( R.id.reg_email ) );
                    YoYo.with( Techniques.Shake )
                            .duration( 750 )
                            .repeat( 2 )
                            .playOn( findViewById(R.id.reg_confirm_pass));
                } else {
                    if (!TextUtils.isEmpty( loginEmail ) && !TextUtils.isEmpty( loginPass )) {
                        loginProgress.setVisibility( View.VISIBLE );
                        mAuth.signInWithEmailAndPassword( loginEmail, loginPass ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendToMain();
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText( Main2Activity.this, "Error : " + errorMessage, Toast.LENGTH_LONG ).show();
                                }
                                loginProgress.setVisibility( View.INVISIBLE );
                            }
                        } );
                    }
                }
            }
        } );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            sendToMain();
        }
    }
    private void sendToMain() {
        Intent mainIntent = new Intent(Main2Activity.this, home.class);
        startActivity(mainIntent);
        finish();
    }




}
