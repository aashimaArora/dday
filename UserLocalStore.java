package com.example.mypc.myapplication;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by MY PC on 3/8/2016.
 */
public class UserLocalStore {
    public static final String SHARED_PREF_NAME = "UserDetails";
    SharedPreferences userLocalDB;

    public UserLocalStore(Context context) {
        userLocalDB = context.getSharedPreferences(SHARED_PREF_NAME,0);
    }

    public void storeUserData(User user) {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putString("name",user.name);
        spEditor.putString("username",user.username);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

    public User getLoggedInUser() {
        String name = userLocalDB.getString("name", "");
        String username = userLocalDB.getString("username","");
        String pwd = userLocalDB.getString("password","");
        User tmp = new User(username,pwd,name);
        return tmp;
    }

    public void setUserLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    public boolean getUserLoggedIn() {
        if(userLocalDB.getBoolean("loggedIn",false) == true)
            return true;
        else
            return false;
    }

    public void clearUserData() {
        SharedPreferences.Editor spEditor = userLocalDB.edit();
        spEditor.clear();
        spEditor.commit();
    }
}
