package com.example.logintest;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import in.goodiebag.carouselpicker.CarouselPicker;

public class block extends AppCompatActivity implements DroidListener {
    ImageView imageView;
    Button button;
    private DroidNet mDroidNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);
        DroidNet.init(block.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(block.this);
        imageView = findViewById(R.id.blockbg);
        Picasso.get().load("https://i.imgur.com/vlmRY4Z.jpg").into(imageView);
        button = findViewById(R.id.report);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://forms.gle/ZpQ69vcgWnF9SR1t5 "));
                startActivity(intent);
            }
        });
        CarouselPicker carouselPicker = findViewById(R.id.carousel);
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new CarouselPicker.TextItem("Abusive", 10));
        textItems.add(new CarouselPicker.TextItem("Porn", 10));
        textItems.add(new CarouselPicker.TextItem("identity theft", 10));
        textItems.add(new CarouselPicker.TextItem("Fake data", 10));
        textItems.add(new CarouselPicker.TextItem("Bullying", 10));
        textItems.add(new CarouselPicker.TextItem("Sensitive", 10));
        textItems.add(new CarouselPicker.TextItem("Humiliating ", 10));
        textItems.add(new CarouselPicker.TextItem("lewd", 10));
        textItems.add(new CarouselPicker.TextItem("malicious notes", 10));
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(this, textItems, 0);
        carouselPicker.setAdapter(textAdapter);
        textAdapter.setTextColor((Color.parseColor("#FFFFFF")));

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
        Toast toast = Toast.makeText(block.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(block.this);
    }
}




