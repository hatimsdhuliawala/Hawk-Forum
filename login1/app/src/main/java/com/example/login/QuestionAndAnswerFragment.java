package com.example.login;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.MongoConnect.GetQuesAndAnsAsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class QuestionAndAnswerFragment extends BackHandledFragment {

    Long selectID;
    String userID;

    ArrayList<PostedQuestion> returnPosts = new ArrayList<>();
    ArrayList<PostedQuestion> returnAns = new ArrayList<>();
    ListView QAlist;
    TextView question;
    Context context;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_and_answer, container, false);
        context = getActivity();

        QAlist = (ListView) view.findViewById(R.id.listViewAns);
        question = (TextView) view.findViewById(R.id.postedQ);

        sharedPref = this.getActivity().getSharedPreferences("QueryDetails", 0);
        selectID = sharedPref.getLong("questionID", 1001L);
        userID = sharedPref.getString("userID", "test");

        GetQuesAndAnsAsyncTask task = new GetQuesAndAnsAsyncTask(selectID);

        try {
            returnPosts = task.execute().get();
            for(PostedQuestion row: returnPosts) {
                if(row.getType().equalsIgnoreCase("answer")) {
                    returnAns.add(row);
                } else {
                    question.setText(row.getQuestion());
                }
            }
            if((returnAns == null)|| (returnAns.size() == 0)) {
                returnAns = new ArrayList<>();
                QAlist.setAdapter(new QueryAdapter(context, returnAns));
                Toast.makeText(context, "There are no answers posted for this question", Toast.LENGTH_LONG).show();
            } else {
                QAlist.setAdapter(new QueryAdapter(context, returnAns, userID, false));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public String getTagText() {
        return "quesFrag";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
