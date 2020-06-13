package com.example.logintest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.squareup.picasso.Picasso;
public class eyezoom extends AppCompatActivity implements DroidListener {
    ImageView imageView;
    TextView textView;
    private DroidNet mDroidNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_eyezoom );
        DroidNet.init(eyezoom.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(eyezoom.this);
        imageView=findViewById(R.id.zoom);
        textView=findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        Bundle bundle2=getIntent().getExtras();
        if(bundle2!=null){
            Picasso.get().load( bundle2.getString( "event")).into(imageView);
            textView.setVisibility(View.VISIBLE);
            textView.setText( bundle2.getString( "des"));
        }
        else {
            Intent intent = new Intent(this, events.class);
            startActivity(intent);
            finish();
        }
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
        Toast toast = Toast.makeText(eyezoom.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(eyezoom.this);
    }
}
