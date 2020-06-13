package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.squareup.picasso.Picasso;

public class groupzoom extends AppCompatActivity implements DroidListener {
    ImageView imageView;
    private DroidNet mDroidNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_groupzoom );
        DroidNet.init(groupzoom.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(groupzoom.this);
        imageView=findViewById(R.id.zoom);
        Bundle bundle1=getIntent().getExtras();
        if (bundle1!=null){
            Picasso.get().load( bundle1.getString( "gp" )).into(imageView);
        }
        else {
            Intent intent = new Intent(this, group.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {
            netIsOn();
        } else {
            netIsOff();
        }
    }


    private void netIsOn(){

    }

    private void netIsOff(){
        Toast toast = Toast.makeText(groupzoom.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(groupzoom.this);
    }
}
