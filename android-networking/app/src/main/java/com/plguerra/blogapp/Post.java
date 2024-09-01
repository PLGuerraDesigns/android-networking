package com.plguerra.blogapp;

import android.os.Parcel;
import android.os.Parcelable;

//Post Class
public class Post implements Parcelable {
    private int PostID;     //Stores Post ID
    private int UserID;     //Stores User ID
    private String Title;   //Stores Post Title
    private String Body;    //Stores Post Body


    //Constructor
    public Post(int PID, int UID, String title, String body){
            //Assigning Variables
            PostID = PID;
            UserID = UID;
            Title = title;
            Body = body;
    }

    protected Post(Parcel in) {
        PostID = in.readInt();
        UserID = in.readInt();
        Title = in.readString();
        Body = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

    public int getPostID(){
        return PostID;
    }

    public int getUserID(){
        return UserID;
    }

    public String getTitle(){
        return Title;
    }

    public String getBody(){
        return Body;
    }

    public void setPostID(int PostID) {
        this.PostID = PostID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(PostID);
        dest.writeInt(UserID);
        dest.writeString(Title);
        dest.writeString(Body);
    }
}
