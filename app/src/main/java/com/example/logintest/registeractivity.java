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

public class registeractivity extends AppCompatActivity  {
    private EditText reg_email_field;
    private EditText reg_pass_field;
    private EditText reg_confirm_pass_field;
    private Button reg_btn;
    private TextView reg_login_btn;
    private ProgressBar reg_progress;
    private FirebaseAuth mAuth;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    private DroidNet mDroidNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registeractivity );
        imageView1=findViewById(R.id.image111);
        imageView1=findViewById(R.id.image111);
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
        reg_email_field = findViewById(R.id.reg_emailx);
        reg_pass_field = findViewById(R.id.reg_passyx);
        reg_confirm_pass_field = findViewById(R.id.reg_passy );
        reg_btn = findViewById(R.id.reg_btn);
        reg_login_btn = findViewById(R.id.reg_login_btn);
        reg_progress = findViewById(R.id.reg_progress);
        reg_progress.setVisibility( View.INVISIBLE );
        reg_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = reg_email_field.getText().toString();
                String pass = reg_pass_field.getText().toString();
                String confirm_pass = reg_confirm_pass_field.getText().toString();
               if(TextUtils.isEmpty( email )&&TextUtils.isEmpty( pass )&&TextUtils.isEmpty(confirm_pass))
               {
                   Toast.makeText(registeractivity.this, "Fields Are Empty", Toast.LENGTH_LONG).show();
                   YoYo.with( Techniques.Shake)
                           .duration(750)
                           .repeat(2)
                           .playOn(findViewById(R.id.reg_passyx));
                   YoYo.with(Techniques.Shake)
                           .duration(750)
                           .repeat(2)
                           .playOn(findViewById(R.id.reg_passy));
                   YoYo.with(Techniques.Shake)
                           .duration(750)
                           .repeat(2)
                           .playOn(findViewById(R.id.reg_emailx));
               }else {
                   if (!TextUtils.isEmpty( email ) && !TextUtils.isEmpty( pass ) & !TextUtils.isEmpty( confirm_pass )) {
                       if (pass.equals( confirm_pass )) {
                           reg_progress.setVisibility( View.VISIBLE );
                           mAuth.createUserWithEmailAndPassword( email, pass ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       Intent setupIntent = new Intent( registeractivity.this, setupactivity.class );
                                       startActivity( setupIntent );
                                       finish();
                                   } else {
                                       String errorMessage = task.getException().getMessage();
                                       Toast.makeText( registeractivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG ).show();
                                   }
                                   reg_progress.setVisibility( View.INVISIBLE );
                               }
                           } );

                       } else {
                           Toast.makeText( registeractivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG ).show();
                           YoYo.with( Techniques.Shake )
                                   .duration( 750 )
                                   .repeat( 2 )
                                   .playOn( findViewById( R.id.reg_passyx ) );
                           YoYo.with( Techniques.Shake )
                                   .duration( 750 )
                                   .repeat( 2 )
                                   .playOn( findViewById( R.id.reg_passy ) );
                       }
                   }
                   //add else below this
               }
            }
        });
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
        Intent mainIntent = new Intent(registeractivity.this, home.class);
        startActivity(mainIntent);
        finish();
    }



}



