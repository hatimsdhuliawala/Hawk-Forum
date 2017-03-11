package com.example.login.MongoConnect;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.login.SignUp;
import com.example.login.UserDetails;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SaveUserAsyncTask extends AsyncTask<UserDetails, Void, Boolean>{

    UserDetails mydata;

    protected Boolean doInBackground(UserDetails... arg0) {
        try {
            mydata=arg0[0];
            QueryBuilderGetUser query = new QueryBuilderGetUser();
            String params = query.createContact(mydata);

            URL url = new URL(query.buildContactsSaveURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("content-type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(conn.getOutputStream());
            outputStreamWriter.write(params);
            outputStreamWriter.flush();

            int responseCode = conn.getResponseCode();
            if(responseCode < 205) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            String val = e.getMessage();
            String val2 = val;
            return false;
        }
    }
}
