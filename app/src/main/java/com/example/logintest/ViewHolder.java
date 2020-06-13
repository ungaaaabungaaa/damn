package com.example.logintest;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }
    public void setDetails(final Context ctx, String title, String description, final String image) {
        TextView mTitleTv = mView.findViewById(R.id.text_view_name );
        TextView mDetailTv = mView.findViewById(R.id.rDescriptionTv );
        ImageView mImageIv = mView.findViewById(R.id.image_view_upload );
        CardView cardView =mView.findViewById( R.id.cardgroup );
        mTitleTv.setText(title);
        mDetailTv.setText(description);
       Picasso.get().load(image).into(mImageIv);
       cardView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ctx,groupzoom.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("gp",image);
                ctx.startActivity(intent);


            }
        } );
    }
}

