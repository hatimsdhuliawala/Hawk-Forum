package com.example.login.MongoConnect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.login.PostedQuestion;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostAnswerAsyncTask extends AsyncTask<PostedQuestion ,Void,Boolean> {

    PostedQuestion row;

    public PostAnswerAsyncTask(PostedQuestion question) {
        row = question;
    }

    @Override
    protected Boolean doInBackground(PostedQuestion... SingleRow) {

        QueryBuilderGetQuestions qbuilder = new QueryBuilderGetQuestions();

        try {

            String params = qbuilder.createAnswer(row);
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
