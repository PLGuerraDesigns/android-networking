package com.plguerra.blogapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



public class ViewCommentAdapter extends  RecyclerView.Adapter<ViewCommentAdapter.ViewHolder>{
    ArrayList <Comment> Comment_List;
    private Context context;

    //Constructor
    public ViewCommentAdapter(Context contextIn, ArrayList<Comment> comment_List) {
        //Assigning Variables
        this.context = contextIn;
        Comment_List = comment_List;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
                holder.title.setText(Comment_List.get(position).getName());
                holder.email.setText(Comment_List.get(position).getEmail());
                holder.body.setText(Comment_List.get(position).getBody());

    }


    @Override
    public int getItemCount() {
        return Comment_List.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView email;
        TextView body;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.Title);
            email = itemView.findViewById(R.id.Email);
            body = itemView.findViewById(R.id.Body);

        }
    }
}
