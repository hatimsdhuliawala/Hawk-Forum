package com.example.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AboutUsFragment extends BackHandledFragment {

    ListView aboutus;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        final Context context = getActivity();
        aboutus = (ListView) view.findViewById(R.id.listViewAboutUs);
        aboutus.setAdapter(new AboutUsAdapter(context));
        return view;
    }

    @Override
    public String getTagText() {
        return "abtusFrag";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}

class AboutUsAdapter extends BaseAdapter {

    String[] abtus;
    Context context;

    AboutUsAdapter(Context c) {
        context = c;
        abtus = context.getResources().getStringArray(R.array.AboutUs);
    }

    @Override
    public int getCount() {
        return abtus.length;
    }

    @Override
    public Object getItem(int position) {
        return abtus[position];
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
            row = inflater.inflate(R.layout.aboutus_template, parent, false);
        } else  {
            row = convertView;
        }

        TextView AbtusRow = (TextView) row.findViewById(R.id.abtusRow);

        AbtusRow.setText(abtus[position]);
        AbtusRow.setText(abtus[position]);
        return row;
    }
}
