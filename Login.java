package com.example.mypc.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.view.View.*;

public class Login extends Activity implements View.OnClickListener{

    Button bLogin;
    EditText etUsername, etPwd;
    TextView regLink;
    UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etUsername = (EditText)findViewById(R.id.lUsername);
        etPwd = (EditText)findViewById(R.id.lPassword);
        bLogin = (Button)findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);
        regLink = (TextView)findViewById(R.id.registerLink);
        regLink.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bLogin :
                String username = etUsername.getText().toString();
                String pwd = etPwd.getText().toString();
                User user = new User(username,pwd);
                authenticate(user);
                break;
            case R.id.registerLink:
                startActivity(new Intent(this, Register.class));
                break;
        }
    }

    private void authenticate(User user) {
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if(returnedUser == null) {
                    //Throw error
                    showErrorMessage();
                }else {
                    logIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect Credentials");
        dialogBuilder.setPositiveButton("Ok",null);
        dialogBuilder.show();
    }

    private void logIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this,MainActivity.class));
    }


}
