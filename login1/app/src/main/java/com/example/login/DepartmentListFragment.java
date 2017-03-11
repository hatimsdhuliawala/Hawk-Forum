package com.example.login;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DepartmentListFragment extends BackHandledFragment {

    private Button btnDisplay;
    private String departmenttst;
    RadioGroup department;
    private OnFragmentDepartmentListtInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_department, container, false);
        final Context context = getActivity();

        if (context instanceof OnFragmentDepartmentListtInteractionListener) {
            mListener = (OnFragmentDepartmentListtInteractionListener) context;
        } else {
            Log.i("list of dept fragment", context.toString() + " must implement OnFragmentInteractionListener");
        }

        department = (RadioGroup)view.findViewById(R.id.rg2);
        btnDisplay = (Button)view.findViewById(R.id.button);
        btnDisplay.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                boolean success = getSelectedDepartment();

                if(success) {
                    Toast.makeText(context, "Department selected: " + departmenttst, Toast.LENGTH_LONG).show();
                    mListener.onFragmentDepartmentListInteraction(departmenttst);
                } else {
                    Toast.makeText(context, "Please select a department and click submit", Toast.LENGTH_LONG).show();
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

    public boolean getSelectedDepartment() {
        int id2 = department.getCheckedRadioButtonId();
        boolean isSelected = true;
        if (id2 == -1) {
            departmenttst = "";
            isSelected = false;
        } else if (id2 == R.id.radioButton7) {
            departmenttst = "BioTechnology";
        } else if (id2 == R.id.radioButton8) {
            departmenttst = "Civil";
        } else if(id2==R.id.radioButton9) {
            departmenttst = "Chemical";
        } else if(id2==R.id.radioButton10) {
            departmenttst = "Computer Science";
        } else if(id2==R.id.radioButton11) {
            departmenttst = "Electronics";
        } else if(id2==R.id.radioButton12) {
            departmenttst = "Mechanical";
        } else if(id2==R.id.radioButton13) {
            departmenttst = "Structural";
        }

        return isSelected;
    }

    @Override
    public String getTagText() {
        return "fragDept";
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    public interface OnFragmentDepartmentListtInteractionListener {
        void onFragmentDepartmentListInteraction(String departmentResult);
    }
}
