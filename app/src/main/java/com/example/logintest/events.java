package com.example.logintest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class events extends AppCompatActivity  implements DroidListener {
    FloatingActionButton fab;
    LinearLayoutManager LayoutManager;
    LinearLayoutManager blayoutManager;
    RecyclerView aRecyclerView;
    RecyclerView bRecyclerView;
    FirebaseDatabase aFirebaseDatabase;
    FirebaseDatabase bFirebaseDatabase;
    DatabaseReference aRef;
    DatabaseReference bRef;
    private DroidNet mDroidNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        DroidNet.init(events.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(events.this);

        fab=findViewById( R.id.floatingActionButton3 );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData( Uri.parse("https://forms.gle/MkFfASsEvZTLSACe7"));
                startActivity(intent);
            }
        } );
        aRecyclerView = findViewById(R.id.eventbanrec );
        aRecyclerView.setHasFixedSize(true);
        aRecyclerView.setLayoutManager(LayoutManager);
        aRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        aFirebaseDatabase = FirebaseDatabase.getInstance();
        aRef = aFirebaseDatabase.getReference("eventbanner");
        aRef.keepSynced(true);
        bRecyclerView = findViewById(R.id.eventcardrec );
        bRecyclerView.setHasFixedSize(true);
        bRecyclerView.setLayoutManager(blayoutManager);
        bRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        bFirebaseDatabase = FirebaseDatabase.getInstance();
        bRef = bFirebaseDatabase.getReference("eventcard");
        bRef.keepSynced(true);
        Toast toast = Toast.makeText(events.this, "Events Around You", Toast.LENGTH_LONG);
        toast.show();





    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder2> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder2>(
                        Model.class,
                        R.layout.eventcard,
                        ViewHolder2.class,
                        aRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder2 viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());
                    }
                };

        aRecyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Model, ViewHolder2>(
                Model.class,
                R.layout.eventcard,
                ViewHolder2.class,
                bRef
        ) {
            @Override
            protected void populateViewHolder(ViewHolder2 viewHolder, Model model, int position) {
                viewHolder.setDetails( getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage() );
            }
        };
        bRecyclerView.setAdapter(firebaseRecyclerAdapter);
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
        Toast toast = Toast.makeText(events.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(events.this);
    }
}

