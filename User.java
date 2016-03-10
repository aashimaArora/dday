package com.example.mypc.myapplication;

/**
 * Created by MY PC on 3/8/2016.
 */
public class User {
    String username, password, name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.name = "";
    }

}
