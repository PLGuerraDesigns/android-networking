package com.plguerra.blogapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewListAdapter extends RecyclerView.Adapter<ViewListAdapter.ViewHolder>{
    ArrayList <Post> Posts_List;
    ArrayList <User> Users_List;

    private ArrayList<String> Post_ID_List;
    private ArrayList <String> Title_List;
    private ArrayList <String> User_List;
    private Context context;

    //Constructor
    public ViewListAdapter(Context contextIn, ArrayList<Post> posts_List, ArrayList<User> users_List) {
        //Assigning Variables
        this.context = contextIn;
        Posts_List = posts_List;
        Users_List = users_List;
        Post_ID_List = new ArrayList<>();
        Title_List = new ArrayList<>();
        User_List = new ArrayList<>();

        //Loop through List of Posts
        for(int i = 0; i < Posts_List.size(); i++) {
            //Add Post IDs and Post Title to the lists
            Post_ID_List.add(String.valueOf(Posts_List.get(i).getPostID()));
            Title_List.add(String.valueOf(Posts_List.get(i).getTitle()));

            //Add Username if their ID matches the ID of the user that posted
            for(int x = 0; x < Users_List.size(); x++) {
                if(Posts_List.get(i).getUserID() == Users_List.get(x).getUserID()) {
                    User_List.add(String.valueOf(Users_List.get(x).getUsername()));
                }
            }
        }

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Display the content
        holder.postID.setText(Post_ID_List.get(position));
        holder.username.setText(User_List.get(position));
        holder.title.setText(Title_List.get(position));

        //Listen for user inputs and open the Post clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent= new Intent(context, ViewPostActivity.class);
                intent.putExtra("PostPosition", position);
                intent.putParcelableArrayListExtra("Posts", Posts_List);
                intent.putParcelableArrayListExtra("Users", Users_List);

                    for(int x = 0; x < Users_List.size(); x++) {
                        if(Posts_List.get(position).getUserID() == Users_List.get(x).getUserID()) {
                            intent.putExtra("UserPosition", x);
                        }
                    }

                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return Post_ID_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView postID;
            TextView title;
            TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postID = itemView.findViewById(R.id.Post_ID);
            title = itemView.findViewById(R.id.Title);
            username = itemView.findViewById(R.id.Username);

        }
    }
}
