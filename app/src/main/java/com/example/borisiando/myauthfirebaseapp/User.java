package com.example.borisiando.myauthfirebaseapp;

/**
 * Created by Borisiando on 1/20/2017.
 */

public class User {
    public String id;
    public String userName;
    public String eMail;
    public int openedEyesOnMe;
    public int myOpenedEyes;


    //CONSTRUCTORS
    public User() {
    }

    public User(String userName, String eMail) {
        this.id = "no id";
        this.userName = userName;
        this.eMail = eMail;
        this.openedEyesOnMe = 0;
        this.myOpenedEyes = 3;
    }



    public User(String id, String userName, String eMail) {
        this.id = id;
        this.userName = userName;
        this.eMail = eMail;
        this.openedEyesOnMe = 0;
        this.myOpenedEyes = 3;

    }

    //GETTERS & SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public int getOpenedEyesOnMe() {
        return openedEyesOnMe;
    }

    public void setOpenedEyesOnMe(int openedEyesOnMe) {
        this.openedEyesOnMe = openedEyesOnMe;
    }

    public int getMyOpenedEyes() {
        return myOpenedEyes;
    }

    public void setMyOpenedEyes(int myOpenedEyes) {
        this.myOpenedEyes = myOpenedEyes;
    }
}
