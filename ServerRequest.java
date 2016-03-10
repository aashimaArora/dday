package com.example.mypc.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by MY PC on 3/10/2016.
 */
public class ServerRequest {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 15*1000;
    public static final String SERVER_ADDRESS = "http://hahha.16mb.com/";

    public ServerRequest(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait..");
    }

    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0)
                sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }

    public void storeUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new StoredDataAsyncTask(user, callback).execute();
    }

    public void fetchUserDataInBackground(User user, GetUserCallback callback) {
        progressDialog.show();
        new FetchDataAsyncTask(user, callback).execute();
    }

    public class StoredDataAsyncTask extends AsyncTask<Void,Void,Void> {
    User user;
        GetUserCallback userCallback;
        public StoredDataAsyncTask(User user , GetUserCallback userCallback){
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Map<String,String> hashMap = new HashMap<String, String>();
            hashMap.put("name",user.name);
            hashMap.put("username",user.username);
            hashMap.put("password",user.password);

            //Server Communication part - it's relatively long but uses standard methods

            //Encoded String - we will have to encode string by our custom method (Very easy)
            String encodedStr = getEncodedData(hashMap);

            //Will be used if we want to read some data from server
            BufferedReader reader = null;

            //Connection Handling
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "register.php");
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Post Method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                //(Basically, after this we can write the dataToSend to the body of POST method)
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Writing dataToSend to outputstreamwriter
                writer.write(encodedStr);
                //Sending the data to the server - This much is enough to send data to server
                //But to read the response of the server, you will have to implement the procedure below
                writer.flush();

                //Data Read Procedure - Basically reading the data coming line by line
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) { //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();           //Saving complete data received in string, you can do it differently

                //Just check to the values received in Logcat
                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check",line);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Same return null, but if you want to return the read string (stored in line)
            //then change the parameters of AsyncTask and return that type, by converting
            //the string - to say JSON or user in your case

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            userCallback.done(null);
            super.onPostExecute(aVoid);
        }
    }

    public class FetchDataAsyncTask extends AsyncTask<Void,Void,User> {
        User user;
        GetUserCallback userCallback;

        public FetchDataAsyncTask(User user, GetUserCallback userCallback) {
            this.user = user;
            this.userCallback = userCallback;
        }

        @Override
        protected User doInBackground(Void... params) {

            User returnedUser = null;
            Map<String,String> hashMap = new HashMap<String, String>();
            hashMap.put("username",user.username);
            hashMap.put("password",user.password);

            BufferedReader reader = null;
            String encodedStr = getEncodedData(hashMap);

            //Connection Handling
            try {
                //Converting address String to URL
                URL url = new URL(SERVER_ADDRESS + "fetchUserData.php");
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Post Method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                //(Basically, after this we can write the dataToSend to the body of POST method)
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Writing dataToSend to outputstreamwriter
                writer.write(encodedStr);
                //Sending the data to the server - This much is enough to send data to server
                //But to read the response of the server, you will have to implement the procedure below
                writer.flush();

                //Data Read Procedure - Basically reading the data coming line by line
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while((line = reader.readLine()) != null) { //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();           //Saving complete data received in string, you can do it differently

                JSONObject  jsonObject = new JSONObject(line);

                if(jsonObject.length() == 0) {
                    returnedUser = null;
                } else {
                    String name = jsonObject.getString("name");
                    returnedUser = new User(user.username, user.password, name);
                }

                //Just check to the values received in Logcat
                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check",line);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    try {
                        reader.close();     //Closing the
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return returnedUser;
        }
        @Override
        protected void onPostExecute(User returnedUser) {
            progressDialog.dismiss();
            userCallback.done(returnedUser);
            super.onPostExecute(returnedUser);
        }
    }
}
