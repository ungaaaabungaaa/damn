package com.example.logintest;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

public class ViewHolder2 extends RecyclerView.ViewHolder {
    View mView;
    public ViewHolder2(@NonNull View itemView) {
        super( itemView );
        mView = itemView;
    }

    public void setDetails(final Context ctx, String title, final String description, final String image) {
        TextView mTitleTv = mView.findViewById(R.id.text_view_name );
        TextView mDetailTv = mView.findViewById(R.id.rDescriptionTv );
        ImageView mImageIv = mView.findViewById(R.id.image_view_upload );
        mTitleTv.setText(title);
        mDetailTv.setText(description);
        Picasso.get().load(image).into(mImageIv);
        mImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ctx,eyezoom.class);
                intent.putExtra("event",image);
                intent.putExtra( "des",description);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        } );
    }
}
