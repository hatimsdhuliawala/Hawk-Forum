package com.example.login.MongoConnect;

import android.os.AsyncTask;
import android.util.Log;
import com.example.login.PostedQuestion;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import com.mongodb.*;

public class PostQuestionAsyncTask extends AsyncTask<PostedQuestion ,Void,Boolean> {

    PostedQuestion row;
    Long maxID = 1001L;
    static String server_output = null;
    static String temp_output = null;

    public PostQuestionAsyncTask(PostedQuestion question) {
        row = question;
    }
    @Override
    protected Boolean doInBackground(PostedQuestion... SingleRow) {
        try{
            QueryBuilderGetQuestions qbuilder = new QueryBuilderGetQuestions();

            URL maxIDurl = new URL(qbuilder.getMaxIDURL());

            HttpURLConnection connMax = (HttpURLConnection) maxIDurl.openConnection();
            connMax.setRequestMethod("GET");
            connMax.setRequestProperty("Accept", "application/json");

            if (connMax.getResponseCode() != 200) {
                Log.i("Exception", "Posting question to database error code: "+connMax.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (connMax.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

            String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);
            DBObject dbObj = (DBObject) o;
            BasicDBList maxIDquestion = (BasicDBList) dbObj.get("artificial_basicdb_list");

            if((maxIDquestion != null)&&(maxIDquestion.size() > 0)) {
                for(Object obj: maxIDquestion) {
                    DBObject userObj = (DBObject) obj;
                    String ID = userObj.get("questionID").toString();
                    maxID = Long.parseLong(ID);
                    maxID++;
                }
            }

            //to save the new question posted
            qbuilder.setQuestionID(maxID);
            String params = qbuilder.createQuestion(row);
            URL url = new URL(qbuilder.buildQuestionSaveURL());
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
