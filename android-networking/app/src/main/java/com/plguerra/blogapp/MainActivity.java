package com.plguerra.blogapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    RecyclerView recyclerView;
    ArrayList <Post> Posts_List;        //Stores list of Posts
    ArrayList <User> Users_List;        //Stores list of Users



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);

        JSONParse();
    }



    //Update view using the contents from Post and User Lists
    private void UpdateView(){

        recyclerView = findViewById(R.id.recyclerView);
        ViewListAdapter adapter = new ViewListAdapter(this, Posts_List, Users_List);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }



    private void JSONParse(){
        final String url_posts = "https://jsonplaceholder.typicode.com/posts";
        final String url_users = "https://jsonplaceholder.typicode.com/users";
        Posts_List = new ArrayList<>();
        Users_List = new ArrayList<>();

        //Request Posts from server
        JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url_posts, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    //Return type is an array
                    JSONArray jsonArray = response;

                    //Loop through the returned array
                    for(int i = 0; i < jsonArray.length(); i++){
                        //Get the object from the array
                        JSONObject post = jsonArray.getJSONObject(i);
                        //Create a new Post object from the returned content
                        Post postObj = new Post(post.getInt("id"), post.getInt("userId"), post.getString("title"), post.getString("body"));
                        //Add the Post to the list
                        Posts_List.add(postObj);
                    }
                    //Update the view with the new content
                    UpdateView();
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

        queue.add(request1);


        //Request Users from server
        JsonArrayRequest request2 = new JsonArrayRequest(Request.Method.GET, url_users, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                try {
                    //Return type is an array
                    JSONArray jsonArray = response;

                    //Loop through the returned array
                    for(int i = 0; i < jsonArray.length(); i++){
                        //Get the object from the array
                        JSONObject user = jsonArray.getJSONObject(i);
                        //Get the LatLng data from the object
                        LatLng position = new LatLng(user.getJSONObject("address").getJSONObject("geo").getDouble("lat"),
                                user.getJSONObject("address").getJSONObject("geo").getDouble("lng"));
                        //Create a new User object from the returned content
                        User userObj = new User(user.getInt("id"), user.getString("name"), user.getString("username"),
                                user.getString("email"), user.getString("phone"), user.getString("website"), position);
                        //Add the User to the list
                        Users_List.add(userObj);
                    }
                    //Update the view with the new content
                    UpdateView();
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

        queue.add(request2);
    }
}
