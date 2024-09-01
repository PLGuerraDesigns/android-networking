package com.plguerra.blogapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewPostActivity extends AppCompatActivity {
    private RequestQueue queue;
    RecyclerView recyclerView;
    ArrayList <Comment> Comments_List;
    ArrayList <User> Users_List;
    ArrayList <Post> Posts_List;
    TextView Title;
    TextView Name;
    TextView Body;
    TextView newComment;
    int postPosition;
    int userPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view);
        queue = Volley.newRequestQueue(this);
        Title = findViewById(R.id.title);
        Name = findViewById(R.id.name);
        Body = findViewById(R.id.body);
        newComment = findViewById(R.id.new_comment);
        newComment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openForm(v);
            }
        });


        getintent();
        JSONParse();
    }



    //Listen for username clicked
    public void onClick(View v) {
        Intent intent= new Intent(this, ViewUserActivity.class);
        intent.putExtra("UserPosition", userPosition);
        intent.putParcelableArrayListExtra("Posts", Posts_List);
        intent.putParcelableArrayListExtra("Users", Users_List);

        this.startActivity(intent);
    }



    //Get Data from previous activity
    private void getintent() {
        Users_List = new ArrayList<>();
        Posts_List = new ArrayList<>();
        if (getIntent().hasExtra("PostPosition") && getIntent().hasExtra("Posts") && getIntent().hasExtra("Users")) {
            postPosition = getIntent().getIntExtra("PostPosition", 0);
            userPosition = getIntent().getIntExtra("UserPosition",0);
            Users_List = (ArrayList<User>)getIntent().getSerializableExtra("Users");
            Posts_List = (ArrayList<Post>)getIntent().getSerializableExtra("Posts");

            //Update views with content
            Title.setText(Posts_List.get(postPosition).getTitle());
            Name.setText(Users_List.get(userPosition).getUsername());
            Name.setPaintFlags(Name.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            Body.setText(Posts_List.get(postPosition).getBody());
        }
    }



    private void JSONParse() {
        final String url_comments = "https://jsonplaceholder.typicode.com/comments";
        Comments_List = new ArrayList<>();

        //Request Posts from server
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url_comments, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    //Return type is an array
                    JSONArray jsonArray = response;
                    //Loop through the returned array
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Get the object from the array
                        JSONObject commentObj = jsonArray.getJSONObject(i);
                        //Create a new Comment object from the returned content
                        Comment comment = new Comment(commentObj.getInt("postId"), commentObj.getString("name"), commentObj.getString("email"), commentObj.getString("body"));
                        //Check if Comment belongs to this post and add it to the list
                        if(comment.getCommentID() == Posts_List.get(postPosition).getPostID()) {
                            Comments_List.add(comment);
                        }
                    }
                    //Update comments with new content
                    LoadComments();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        queue.add(request);

    }



    //Send Data to server
    private boolean SendData(final int PostID, final String Name, final String Email, final String Body){
        final String url_put = "https://jsonplaceholder.typicode.com/comments/";
        StringRequest putRequest = new StringRequest(Request.Method.POST, url_put,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        //If sent successfully the display the data returned
                        Log.d("Response", response);
                        Toast.makeText(ViewPostActivity.this, "Comment Uploaded: " + response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(ViewPostActivity.this,"Uploading Failed", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            //Prepare the data to be sent
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("postId", String.valueOf(PostID));
                params.put("name", Name);
                params.put("email", Email);
                params.put("body", Body);

                return params;
            }
        };

        queue.add(putRequest);
        return true;
    }



    //Update view using the content from comments list
    private void LoadComments(){
        recyclerView = findViewById(R.id.comments);
        ViewCommentAdapter adapter = new ViewCommentAdapter(this, Comments_List);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    //Open a Form to add a new comment
    public void openForm(View view) {

        //Inflate layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.new_comment_view, null);

        //Create the popup
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);

        //Show the popup
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        final Button postButton = popupView.findViewById(R.id.postButton);
        final EditText email = popupView.findViewById(R.id.email);
        final EditText name = popupView.findViewById(R.id.name);
        final EditText body = popupView.findViewById(R.id.body);

        //Listen for Post Button Click
        postButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              //Check if all the fields have some content
                                              if(email.getText().length() != 0 && name.getText().length() != 0 && body.getText().length() != 0){
                                                  //Create a new comment object
                                                  Comment comment = new Comment(Posts_List.get(postPosition).getPostID(), name.getText().toString(), email.getText().toString(), body.getText().toString());
                                                  //Add the comment to the list
                                                  Comments_List.add(comment);
                                                  //Reload the comments view
                                                  LoadComments();
                                                  //Scroll to last comment
                                                  recyclerView.scrollToPosition(Comments_List.size() - 1);
                                                  //Hide the popup
                                                  popupWindow.dismiss();
                                                  //Send data to server
                                                  SendData(Posts_List.get(postPosition).getPostID(), name.getText().toString(), email.getText().toString(), body.getText().toString());
                                              }
                                              else{
                                                  Toast.makeText(ViewPostActivity.this,"Please complete the form.", Toast.LENGTH_SHORT).show();
                                              }
                                          }
                                      });

    }
}