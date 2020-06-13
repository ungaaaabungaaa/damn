package com.example.logintest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.droidnet.DroidListener;
import com.droidnet.DroidNet;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;
public class group extends AppCompatActivity  implements DroidListener {
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    FloatingActionButton fab;
    ImageView imageView;
    ImageView cimageView;
    DatabaseReference dref;
    private DroidNet mDroidNet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);


        DroidNet.init(group.this);
        mDroidNet = DroidNet.getInstance();
        mDroidNet.addInternetConnectivityListener(group.this);

       fab=findViewById( R.id.floatingActionButton2);
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData( Uri.parse("https://forms.gle/MmsruV4DYhsZRCWKA"));
                startActivity(intent);
            }
        } );

        mRecyclerView = findViewById(R.id.grouprecycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("imagesvce");
        mRef.keepSynced(true);
        imageView=findViewById(R.id.groupbgdatabase);
        dref=FirebaseDatabase.getInstance().getReference();
        Query query=dref.child("imagex");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=dataSnapshot.child("url").getValue().toString();
                if (!url.isEmpty())
                {
                    Picasso.get()
                            .load(url)
                            .fit()
                            .into(imageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        cimageView=findViewById(R.id.groupbg);
        dref=FirebaseDatabase.getInstance().getReference();
        query = dref.child("imagexbg");
        query.keepSynced(true);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String url=dataSnapshot.child("url").getValue().toString();
                if (!url.isEmpty())
                {
                    Picasso.get()
                            .load(url)
                            .fit()
                            .into(cimageView);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(group.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.eyecard,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());
                    }
                };
        FirebaseRecyclerAdapter<Model, ViewHolder> mAdapter = (firebaseRecyclerAdapter);
        SlideInBottomAnimatorAdapter animatorAdapter=new SlideInBottomAnimatorAdapter(mAdapter,mRecyclerView);
        mRecyclerView.setAdapter(animatorAdapter);
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

        Toast toast = Toast.makeText(group.this, " No internet ", Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDroidNet.removeInternetConnectivityChangeListener(group.this);
    }
}
