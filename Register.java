package com.example.mypc.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import javax.microedition.khronos.egl.EGLDisplay;

public class Register extends Activity implements View.OnClickListener {

    Button register;
    EditText etUsername, etPwd, etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        etUsername = (EditText)findViewById(R.id.rUsername);
        etName = (EditText)findViewById(R.id.rName);
        etPwd = (EditText)findViewById(R.id.rPassword);
        register = (Button)findViewById(R.id.bRegister);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bRegister:
                String username = etUsername.getText().toString();
                String pwd = etPwd.getText().toString();
                String name = etName.getText().toString();
                User user = new User(username,pwd,name);

                registerUser(user);

                break;
        }

    }

    public  void registerUser (User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                startActivity(new Intent(Register.this , Login.class));
            }
        });
    }
}
