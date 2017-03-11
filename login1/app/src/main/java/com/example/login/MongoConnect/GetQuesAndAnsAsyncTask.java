package com.example.login.MongoConnect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.login.PostedQuestion;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class GetQuesAndAnsAsyncTask extends AsyncTask<PostedQuestion, Void, ArrayList<PostedQuestion>> {

    Long selectionID;
    static String server_output = null;
    static String temp_output = null;

    public GetQuesAndAnsAsyncTask(Long ID) {
        selectionID = ID;
    }

    @Override
    protected ArrayList<PostedQuestion> doInBackground(PostedQuestion... params) {

        ArrayList<PostedQuestion> posts = new ArrayList<PostedQuestion>();
        QueryBuilderGetQuestions qbuilder = new QueryBuilderGetQuestions();
        qbuilder.setQuestionID(selectionID);

        try {

            URL url = new URL(qbuilder.buildQuesAndAnsFilterURL());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                Log.i("Exception", "Fetching data from postedQuery error code: "+conn.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            while ((temp_output = br.readLine()) != null) {
                server_output = temp_output;
            }

            String mongoarray = "{ artificial_basicdb_list: "+server_output+"}";
            Object o = com.mongodb.util.JSON.parse(mongoarray);
            DBObject dbObj = (DBObject) o;
            BasicDBList questions = (BasicDBList) dbObj.get("artificial_basicdb_list");

            for(Object obj: questions) {
                DBObject userObj = (DBObject) obj;
                PostedQuestion ques = new PostedQuestion();
                ques.setUniqueID(userObj.get("_id").toString());
                ques.setQuestionID(Long.parseLong(userObj.get("questionID").toString()));
                ques.setQuestion(userObj.get("post").toString());
                ques.setUserName(userObj.get("userID").toString());
                ques.setDateTime(userObj.get("dateTime").toString());
                ques.setDepartment(userObj.get("department").toString());
                ques.setType(userObj.get("typeOfPost").toString());
                posts.add(ques);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }
}
