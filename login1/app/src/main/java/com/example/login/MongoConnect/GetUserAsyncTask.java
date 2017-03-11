package com.example.login.MongoConnect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.login.UserDetails;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GetUserAsyncTask extends AsyncTask<Void, Void,UserDetails> {
    static String server_output = null;
    static String temp_output = null;
    String mainemail=null;


    public GetUserAsyncTask(String email)
    {

        mainemail=email;

    }


    @Override
    protected UserDetails doInBackground(Void... params) {

        QueryBuilderGetUser query = new QueryBuilderGetUser();
        query.setEmailid(mainemail);
        UserDetails detail = null;
        try {
            URL url = new URL(query.userDetailsFilterURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }
            String mongoarray = "{ artificial_basicdb_list: " + server_output + "}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);
            DBObject dbObj = (DBObject) o;
            BasicDBList questions = (BasicDBList) dbObj.get("artificial_basicdb_list");
            for (Object obj : questions) {
                DBObject userObj = (DBObject) obj;
                detail = new UserDetails();
                detail.setFirst_name(userObj.get("firstName").toString());
                detail.setUserID(userObj.get("userID").toString());
                detail.setLast_name(userObj.get("lastName").toString());
                detail.setPassword(userObj.get("password").toString());
                detail.setEmail(userObj.get("email").toString());
                detail.setDepartment(userObj.get("department").toString());
                detail.setType(userObj.get("type").toString());
                detail.setSecretQuestion(userObj.get("secretQuestion").toString());
                detail.setSecretAnswer(userObj.get("secretAnswer").toString());
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return detail;
    }
}
