package com.example.login.MongoConnect;

import android.os.AsyncTask;
import android.util.Log;

import com.example.login.PostedQuestion;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DeleteQuestionAsyncTask extends AsyncTask<PostedQuestion,Void,Boolean> {

    PostedQuestion row;

    public DeleteQuestionAsyncTask(PostedQuestion question) {
        row = question;
    }

    @Override
    protected Boolean doInBackground(PostedQuestion... params) {
        QueryBuilderGetQuestions qbuilder = new QueryBuilderGetQuestions();
        qbuilder.setQuestionID(row.getQuestionID());
        qbuilder.setDocID(row.getUniqueID());
        URL delURL;
        try {
            delURL = new URL(qbuilder.buildQuesDeleteURL());

            HttpURLConnection connDel = (HttpURLConnection) delURL.openConnection();
            connDel.setRequestMethod("DELETE");
            connDel.setRequestProperty("Accept", "application/json");

            if (connDel.getResponseCode() != 200) {
                Log.i("Exception", "Delete question error code: "+connDel.getResponseCode());
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
