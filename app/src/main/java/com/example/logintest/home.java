package com.example.logintest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Random;

public class home extends AppCompatActivity implements View.OnClickListener, DroidListener {
    public FirebaseAuth mAuth;
    ConstraintLayout constraintLayout;
    Random numRandom = new Random();
    ImageView imageView1;
    ConstraintLayout constraintLayout2;
    private DroidNet mDroidNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        constraintLayout2=findViewById( R.id.infintybfgradient );
        constraintLayout2.setVisibility(View.INVISIBLE);
       ImageView onetree = findViewById(R.id.onetree);
       onetree.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_VIEW);
               intent.addCategory(Intent.CATEGORY_BROWSABLE);
               intent.setData( Uri.parse("https://onetreeplanted.org/"));
               startActivity(intent);
           }
       } );
       ImageView water=findViewById(R.id.water);
       water.setOnClickListener( new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent();
               intent.setAction(Intent.ACTION_VIEW);
               intent.addCategory(Intent.CATEGORY_BROWSABLE);
               intent.setData(Uri.parse("https://theoceancleanup.com/"));
               startActivity(intent);
           }
       } );

        ImageButton imageButton =findViewById( R.id.infintyhome );
        imageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             constraintLayout2.setVisibility(View.VISIBLE);
             constraintLayout.setVisibility( View.INVISIBLE);
                YoYo.with( Techniques.FadeIn)
                        .duration(1000)
                        .playOn(findViewById(R.id.water));
                YoYo.with(Techniques.FadeIn)
                        .duration(1000)
                        .playOn(findViewById(R.id.onetree));
             imageView1.setVisibility(View.INVISIBLE);
                Toast toast = Toast.makeText(home.this, "20 Post = OneTree", Toast.LENGTH_LONG);
                toast.show();
            }
        } );
        constraintLayout=findViewById( R.id.counterlayout );
        constraintLayout.setVisibility(View.INVISIBLE);
        ImageView imageView=findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
                imageView1.setVisibility( View.VISIBLE);
            }
        } );
        TextView textView1 =findViewById(R.id.textView21);
        textView1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.INVISIBLE);
                imageView1.setVisibility( View.VISIBLE);

            }
        } );
        imageView1=findViewById(R.id.randomview);
        imageView1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.VISIBLE);
                imageView1.setVisibility(View.INVISIBLE);
                TextView textView =findViewById(R.id.randomtext);
                textView.setText(String.valueOf(numRandom.nextInt(135)));
            }

        } );
        ImageButton imageButton1=findViewById(R.id.eyehome);
        ImageButton imageButton3=findViewById(R.id.camerahome);
        ImageButton imageButton4=findViewById(R.id.grouphome);
        ImageButton imageButton7=findViewById(R.id.treehome);
        ImageButton imageButton8=findViewById(R.id.account);
        ImageButton imageButton9=findViewById( R.id.event );
        ImageButton imageButton11=findViewById( R.id.logout );
        ImageButton imageButton12 =findViewById( R.id.block);
        ImageButton imageButton10=findViewById(R.id.heart);
        imageButton1.setOnClickListener(this);
        imageButton3.setOnClickListener(this);
        imageButton4.setOnClickListener(this);
        imageButton7.setOnClickListener(this);
        imageButton8.setOnClickListener(this);
        imageButton11.setOnClickListener(this);
        imageButton9.setOnClickListener(this);
        imageButton10.setOnClickListener(this);
        imageButton12.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eyehome:
                Intent intent = new Intent(this, eye.class);
                startActivity(intent);

                break;
            case R.id.camerahome:
                intent = new Intent( this, newpostactivity.class );
                startActivity(intent);
                break;
            case R.id.grouphome:
                intent = new Intent(this, group.class);
                startActivity(intent);
                break;

            case R.id.treehome:
                intent = new Intent(this, tree.class);
                startActivity(intent);
                break;
              case R.id.heart:
               // intent = new Intent(this, tree.class);
                //startActivity(intent);
              //  break;
            case R.id.account:
                intent = new Intent(this, setupactivity.class);
                startActivity(intent);
                break;
            case R.id.logout:
                logOut();
                break;
            case R.id.event:
                intent = new Intent(this, events.class);
                startActivity(intent);
                break;

            case R.id.block:
                intent = new Intent(this, block.class);
                startActivity(intent);
                break;

        }
    }
    private void logOut() {
        mAuth.signOut();
        sendToLogin();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            sendToLogin();
        }
        DroidNet.init(home.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(home.this);
    }
    private void sendToLogin()  {
        Intent loginIntent = new Intent(home.this, Main2Activity.class);
        startActivity(loginIntent);
        finish();
    }



    @Override
    public void onBackPressed() {
       constraintLayout.setVisibility(View.INVISIBLE);
        imageView1.setVisibility( View.VISIBLE);
        constraintLayout2.setVisibility( View.INVISIBLE);
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            //do Stuff with internet
            netIsOn();
        } else {
            //no internet
            netIsOff();
        }


    }





    private void netIsOn(){

    }

    private void netIsOff(){

        Toast toast = Toast.makeText(home.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(home.this);
    }


}
