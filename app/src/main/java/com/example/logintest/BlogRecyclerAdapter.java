package com.example.logintest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public BlogRecyclerAdapter(List<BlogPost> blog_list){
        this.blog_list = blog_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_item, parent ,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     String  desc_data =blog_list.get( position ).getDesc();
     holder.setDestext(desc_data);
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
      private  TextView descview;
      private  View mview;


        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            mview=itemView;
        }

        public  void setDestext(String desctext)
        {
            descview=itemView.findViewById(R.id.blog_desc);
            descview.setText(desctext);
        }

    }

}
