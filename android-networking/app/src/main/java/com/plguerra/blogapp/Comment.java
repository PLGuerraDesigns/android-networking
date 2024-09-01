package com.plguerra.blogapp;

// Comment Class
public class Comment {
    private int CommentID;     //Stores Comment ID
    private String Name;       //Stores Comment Title
    private String Email;      //Stores Commenter's Email
    private String Body;       //Stores Comment Body

    //Constructor
    public Comment(int CommentID, String Name, String Email, String Body){
        //Assigning Variables
        this.CommentID = CommentID;
        this.Name = Name;
        this.Email = Email;
        this.Body = Body;

    }

    public int getCommentID() {
        return CommentID;
    }

    public String getName() {
        return Name;
    }

    public String getEmail() {
        return Email;
    }

    public String getBody() {
        return Body;
    }
}
