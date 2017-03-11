package com.example.login;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FAQFragment extends BackHandledFragment {

    public ListView FAQList;
    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_faqs, container, false);
        context = getActivity();
        FAQList = (ListView) view.findViewById(R.id.listViewFAQ);
        FAQList.setAdapter(new FAQAdapter(context));
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
        return "postfrag";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}

class FAQAdapter extends BaseAdapter {

    private Context context;
    String[] questions, answers;

    public FAQAdapter(Context c) {
        context = c;
        questions = context.getResources().getStringArray(R.array.FAQquestions);
        answers = context.getResources().getStringArray(R.array.FAQanswers);
    }

    @Override
    public int getCount() {
        return questions.length;
    }

    @Override
    public Object getItem(int position) {
        return questions[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = null;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.faq_template, parent, false);
        } else  {
            row = convertView;
        }

        TextView FAQQuestion = (TextView) row.findViewById(R.id.FAQquestion);
        TextView FAQAnswer = (TextView) row.findViewById(R.id.FAQAnswer);

        FAQQuestion.setText(questions[position]);
        FAQAnswer.setText(answers[position]);
        return row;
    }
}