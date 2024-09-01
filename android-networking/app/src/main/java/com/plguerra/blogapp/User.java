package com.plguerra.blogapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;


//User Class
public class User implements Parcelable{
    private int UserID;
    private String Name;
    private String Username;
    private String Email;
    private String Phone;
    private String Website;
    private LatLng Position;

    //Constructor
    public User(int UserID, String Name, String Username, String Email, String Phone, String Website, LatLng Position){
        //Assigning Variables
        this.UserID = UserID;
        this.Name = Name;
        this.Username = Username;
        this.Email = Email;
        this.Phone = Phone;
        this.Website = Website;
        this.Position = Position;
    }


    protected User(Parcel in) {
        UserID = in.readInt();
        Name = in.readString();
        Username = in.readString();
        Email = in.readString();
        Phone = in.readString();
        Website = in.readString();
        Position = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getUserID(){
        return UserID;
    }

    public String getName(){
        return Name;
    }

    public String getUsername(){
        return Username;
    }

    public String getEmail(){
        return Email;
    }

    public String getPhone(){
        return Phone;
    }

    public String getWebsite(){
        return Website;
    }

    public LatLng getLatLng(){
        return Position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(UserID);
        dest.writeString(Name);
        dest.writeString(Username);
        dest.writeString(Email);
        dest.writeString(Phone);
        dest.writeString(Website);
        dest.writeParcelable(Position, flags);
    }
}
