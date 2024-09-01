package com.plguerra.blogapp;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ViewUserActivity extends FragmentActivity implements OnMapReadyCallback{
    GoogleMap map;                                          //Map object
    SupportMapFragment mapFragment;                         //Map display fragment
    ArrayList <User> Users_List;
    ArrayList <Post> Posts_List;
    RecyclerView recyclerView;
    int userPosition;
    TextView username;
    TextView name;
    TextView email;
    TextView phone;
    TextView website;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_view);
        username = findViewById(R.id.Username);
        name = findViewById(R.id.Name);
        email = findViewById(R.id.Email);
        phone = findViewById(R.id.Phone);
        website = findViewById(R.id.Website);

        getintent();
        UpdatePosts();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    //Get Data from previous activity
    private void getintent() {
        Users_List = new ArrayList<>();
        Posts_List = new ArrayList<>();
        if (getIntent().hasExtra("UserPosition") && getIntent().hasExtra("Posts") && getIntent().hasExtra("Users")) {
            Users_List = (ArrayList<User>)getIntent().getSerializableExtra("Users");
            Posts_List = (ArrayList<Post>)getIntent().getSerializableExtra("Posts");
            userPosition = getIntent().getIntExtra("UserPosition", 0);

            name.setText(Users_List.get(userPosition).getName());
            username.setText(Users_List.get(userPosition).getUsername());
            email.setText("Email: " + Users_List.get(userPosition).getEmail());
            phone.setText("Phone: " + Users_List.get(userPosition).getPhone());
            website.setText("Website: " + Users_List.get(userPosition).getWebsite());
        }
    }



    //Update view using the contents from Post and User Lists
    private void UpdatePosts(){
        ArrayList <Post> Users_Posts_List;
        Users_Posts_List = new ArrayList<>();

        int counter = 1;
        //If the post was created by this user then add it to the list
        //Start ID from 1 (No longer references Post ID)
        for(int i = 0; i < Posts_List.size(); i++){
            if(Posts_List.get(i).getUserID() == Users_List.get(userPosition).getUserID()){
                Posts_List.get(i).setPostID(counter);
                Users_Posts_List.add(Posts_List.get(i));
                counter++;
            }
        }

        recyclerView = findViewById(R.id.PostView);
        ViewListAdapter adapter = new ViewListAdapter(this, Users_Posts_List, Users_List);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    //Display map with a marker at users location
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng position = Users_List.get(userPosition).getLatLng();
        map.addMarker(new MarkerOptions().position(position).title(position.toString()));
        map.moveCamera(CameraUpdateFactory.newLatLng(position));
    }
}
