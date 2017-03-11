package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.login.MongoConnect.GetUserAsyncTask;
import com.example.login.MongoConnect.SaveUserAsyncTask;

import java.util.concurrent.ExecutionException;

public class SignUp extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    EditText firstname,lastname,email,password,repassword, securityanswer;
    RadioGroup department;
    RadioGroup type;
    Spinner spinner;
    String fname,lname,emailid,pass,repass,typest,departmentst,question="",answer, userName;
    UserDetails mydata, checkUser;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    NetworkInfo ni;
    private static final String[]paths = {"What is your Mother's Maiden Name?", "What is your nick name?", "In what city or town does your nearest sibling live?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initilize();
        final Button finish = (Button)findViewById(R.id.Finish_button);
        finish.setOnClickListener(this);
    }

    private void initilize() {
        firstname=(EditText)findViewById(R.id.editText);
        lastname=(EditText)findViewById(R.id.editText3);
        email=(EditText)findViewById(R.id.editText4);
        password=(EditText)findViewById(R.id.editText5);
        repassword=(EditText)findViewById(R.id.editText6);
        type=(RadioGroup)findViewById(R.id.rg1);
        department=(RadioGroup)findViewById(R.id.depGroup);
        securityanswer=(EditText)findViewById(R.id.editText12);
        spinner=(Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SignUp.this,
                android.R.layout.simple_spinner_item,paths);


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(SignUp.this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Finish_button:
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo.isConnected() || ni.isConnected()) {
                    fname = firstname.getText().toString();
                    lname = lastname.getText().toString();
                    emailid = email.getText().toString();
                    pass = password.getText().toString();
                    repass = repassword.getText().toString();
                    answer = securityanswer.getText().toString();

                    int id = type.getCheckedRadioButtonId();
                    if (id == -1)
                        typest = "";
                    else if (id == R.id.radioButton)
                        typest = "Current";
                    else if (id == R.id.radioButton2)
                        typest = "Alumni";
                    else
                        typest = "";

                    int id2 = department.getCheckedRadioButtonId();
                    if (id2 == -1)
                        departmentst = "";
                    else if (id2 == R.id.radioButton3)
                        departmentst = "BioTechnology";
                    else if (id2 == R.id.radioButton4)
                        departmentst = "Civil";
                    else if (id2 == R.id.radioButton5)
                        departmentst = "Chemical";
                    else if (id2 == R.id.radioButton6)
                        departmentst = "Computer Science";

                    else if (id2 == R.id.radioButton8)
                        departmentst = "Electronics";
                    else if (id2 == R.id.radioButton11)
                        departmentst = "Mechanical";
                    else if (id2 == R.id.radioButton12)
                        departmentst = "Structural";
                    else
                        departmentst = "";

                    if (fname.equals("") || lname.equals("")
                            || emailid.equals("") || pass.equals("") || repass.equals("") || typest.equals("")
                            || departmentst.equals("") || question.equals("") || answer.equals("")) {
                        Toast.makeText(getBaseContext(), "Please Enter All the fields", Toast.LENGTH_LONG).show();
                    } else {
                        if (pass.equals(repass)) {
                            String[] splitName = emailid.split("@");
                            userName = splitName[0];

                            if (splitName[1].equals("hawk.iit.edu")) {

                                GetUserAsyncTask task = new GetUserAsyncTask(emailid);

                                try {
                                    checkUser = task.execute().get();
                                    if (checkUser == null) {
                                        mydata = new UserDetails();
                                        mydata.setFirst_name(fname);
                                        mydata.setLast_name(lname);
                                        mydata.setEmail(emailid);
                                        mydata.setPassword(pass);
                                        mydata.setUserID(userName);
                                        mydata.setDepartment(departmentst);
                                        mydata.setType(typest);
                                        mydata.setSecretQuestion(question);
                                        mydata.setSecretAnswer(answer);

                                        SaveUserAsyncTask tsk = new SaveUserAsyncTask();
                                        Boolean success = tsk.execute(mydata).get();
                                        if (success) {
                                            Toast.makeText(SignUp.this, "You have signed up successfully.. Please login to continue", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(SignUp.this, "Registration not successful.. Please try again", Toast.LENGTH_LONG).show();
                                        }
                                        Intent i = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(getBaseContext(), "There is already an account associated with this email ID", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getBaseContext(), "You can sign up only with your HAWK mail id", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getBaseContext(), "Passwords do not Match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else
                Toast.makeText(getBaseContext(),"No data Conection Availale",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        question = paths[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        question = "";
    }
}
