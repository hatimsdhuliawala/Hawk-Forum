package com.example.login;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.MongoConnect.GetQuestionsAsyncTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LoadQueriesFragment extends BackHandledFragment {

    public ListView posted_questions;
    ArrayList<PostedQuestion> returnQuestions = new ArrayList<>();
    SharedPreferences sharedPref;
    String userID, department;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_queries, container, false);
        context = getActivity();
        posted_questions = (ListView) view.findViewById(R.id.listView);

        sharedPref = this.getActivity().getSharedPreferences("LoggedUserInfo", 0);
        department = sharedPref.getString("PostDepartment", "Electronics");
        userID = sharedPref.getString("LoggedUser", "test");
        fetchData(view);
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

    public void fetchData(View view) {
        TextView deptHeader = (TextView) view.findViewById(R.id.deptHeader);
            GetQuestionsAsyncTask task = new GetQuestionsAsyncTask(department);
            try {
                returnQuestions = task.execute().get();
                if ((returnQuestions == null) || (returnQuestions.size() == 0)) {
                    returnQuestions = new ArrayList<>();
                    posted_questions.setAdapter(new QueryAdapter(getActivity(), returnQuestions));
                    deptHeader.setText("Waiting for your questions!");
                    Toast.makeText(context, "There are no questions posted in the department " + department + " yet", Toast.LENGTH_LONG).show();
                } else {
                    deptHeader.setText("Department: " + returnQuestions.get(0).getDepartment());
                    posted_questions.setAdapter(new QueryAdapter(context, returnQuestions, userID, true));
                }
            } catch (Exception e) {
                Toast.makeText(context, "No internet to load queries", Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public String getTagText() {
        return "loadFrag";
    }

    @Override
    public boolean onBackPressed() {
        return  false;
    }
}
