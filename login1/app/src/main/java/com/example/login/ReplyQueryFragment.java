package com.example.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.MongoConnect.PostAnswerAsyncTask;

import java.util.concurrent.ExecutionException;

public class ReplyQueryFragment extends BackHandledFragment {

    Button submit;
    TextView tv;
    EditText answer;
    Long defID = 1001L;
    SharedPreferences sharedPref;
    OnReplyQueryFragmentInteractionListener mListener;
    String department, userID, question;
    Long questionID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_reply_query, container, false);
        final Context context = getActivity();

        if (context instanceof OnReplyQueryFragmentInteractionListener) {
            mListener = (OnReplyQueryFragmentInteractionListener) context;
        } else {
            Log.i("replyquery fragment", context.toString() + " must implement OnFragmentInteractionListener");
        }

        submit = (Button)view.findViewById(R.id.replyButton);
        tv = (TextView)view.findViewById(R.id.textView5);
        answer = (EditText)view.findViewById(R.id.editTextReply);

        sharedPref = this.getActivity().getSharedPreferences("QueryDetails", 0);
        department = sharedPref.getString("department", "Electronics");
        userID = sharedPref.getString("userID", "test");
        question = sharedPref.getString("question", "Is doing masters in ECE at IIT worth it?");
        questionID = sharedPref.getLong("questionID", 1001L);

        tv.setText(question);
        submit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {

                // Hiding Keyboard
                InputMethodManager inputManager = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                String ans = answer.getText().toString();
                if(ans == null || ans.equals("")) {
                    Toast.makeText(context, "The question cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    boolean success = addAnswerToDatabase(userID, department, ans, questionID);
                    mListener.onReplyQueryFragmentInteraction(success);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public String getTagText() {
        return "replyFrag";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    public boolean addAnswerToDatabase(String userName, String department, String answer, Long questionID) {
        PostedQuestion currentRow = new PostedQuestion();
        currentRow.setUserName(userName);
        currentRow.setQuestion(answer);
        currentRow.setDepartment(department);
        currentRow.setType("answer");
        currentRow.setQuestionID(questionID);

        PostAnswerAsyncTask task = new PostAnswerAsyncTask(currentRow);

        try {
            Boolean result = task.execute().get();
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface OnReplyQueryFragmentInteractionListener {
        void onReplyQueryFragmentInteraction(boolean success);
    }
}
