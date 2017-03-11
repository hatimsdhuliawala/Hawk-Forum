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
import android.widget.Toast;

import com.example.login.MongoConnect.PostQuestionAsyncTask;

import java.util.concurrent.ExecutionException;

public class PostQueryFragment extends BackHandledFragment {

    Button finish;
    EditText result1;
    String department, userID;
    SharedPreferences sharedPref;
    private OnPostQueryFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_query, container, false);
        final Context context = getActivity();

        if (context instanceof OnPostQueryFragmentInteractionListener) {
            mListener = (OnPostQueryFragmentInteractionListener) context;
        } else {
            Log.i("postquery fragment",context.toString()+ " must implement OnFragmentInteractionListener");
        }

        sharedPref = this.getActivity().getSharedPreferences("LoggedUserInfo", 0);
        department = sharedPref.getString("PostDepartment", "Electronics");
        userID = sharedPref.getString("LoggedUser", "test");
        finish = (Button)view.findViewById(R.id.button);
        result1 = (EditText) view.findViewById(R.id.addr_edittext);

        finish.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String result = result1.getText().toString();
                if(result == null || result.equals("")) {
                    Toast.makeText(context, "The question cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    Boolean success = addToDatabase(result);
                    mListener.onPostQueryFragmentInteraction(success);
                }
            }
        });
        return view;
    }

    public boolean addToDatabase(String question) {
        PostedQuestion currentRow = new PostedQuestion();
        currentRow.setUserName(userID);
        currentRow.setQuestion(question);
        currentRow.setDepartment(department);
        currentRow.setType("question");

        PostQuestionAsyncTask task = new PostQuestionAsyncTask(currentRow);
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
        return "postfrag";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    public interface OnPostQueryFragmentInteractionListener {
        void onPostQueryFragmentInteraction(boolean success);
    }
}