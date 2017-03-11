package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.MongoConnect.GetUserAsyncTask;

import java.util.concurrent.ExecutionException;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener{

    EditText emailid,sanswer;
    String answer,email;
    Button back,getpassword,go;
    TextView qts,gotpassword;
    UserDetails user;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    NetworkInfo ni;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forgot Password");
        initilize();
    }

    private void initilize() {
        emailid = (EditText)findViewById(R.id.etpasswordrecover);

        sanswer = (EditText)findViewById(R.id.etanswer);
        sanswer.setVisibility(View.INVISIBLE);

        back = (Button)findViewById(R.id.backtoMain);
        back.setVisibility(View.INVISIBLE);

        getpassword=(Button)findViewById(R.id.emailsend);
        getpassword.setVisibility(View.INVISIBLE);

        go=(Button)findViewById(R.id.bgo);

        qts = (TextView) findViewById(R.id.tvquestion);
        qts.setVisibility(View.INVISIBLE);

        gotpassword= (TextView)findViewById(R.id.getpass);
        gotpassword.setVisibility(View.INVISIBLE);

        back.setOnClickListener(this);
        getpassword.setOnClickListener(this);
        go.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.backtoMain:
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo.isConnected() || ni.isConnected()){

               Intent backtomain=new Intent(ForgotPassword.this,MainActivity.class);
                startActivity(backtomain);
                finish();
                }
                else
                Toast.makeText(getBaseContext(),"No Data Connection Availabel",Toast.LENGTH_SHORT).show();
                break;

            case R.id.emailsend:
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo.isConnected() || ni.isConnected()) {
                    answer = sanswer.getText().toString();
                    if (answer.equalsIgnoreCase("")) {
                        Toast.makeText(getBaseContext(), "Please enter Answer", Toast.LENGTH_LONG).show();
                    } else {
                        String gotanswer = user.getSecretAnswer();
                        if (gotanswer.equalsIgnoreCase(answer)) {
                            String repass = user.getPassword();
                            gotpassword.setVisibility(View.VISIBLE);
                            gotpassword.setText("You Password is : ' " + repass + " '");
                        } else
                            Toast.makeText(getBaseContext(), "Answer do not Match please try again", Toast.LENGTH_LONG).show();
                    }
                }
                else
                    Toast.makeText(getBaseContext(),"No Data Connection Availabel",Toast.LENGTH_SHORT).show();

                break;
            case R.id.bgo:
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo.isConnected() || ni.isConnected()) {

                    email = emailid.getText().toString();
                if ((email != null) && (!email.equals(""))) {
                    GetUserAsyncTask task = new GetUserAsyncTask(email);
                    try {
                        user = task.execute().get();
                        if(user != null) {
                            String questionsec = user.getSecretQuestion();
                            qts.setVisibility(View.VISIBLE);
                            qts.setText(questionsec);
                            back.setVisibility(View.VISIBLE);
                            sanswer.setVisibility(View.VISIBLE);
                            getpassword.setVisibility(View.VISIBLE);
                        }
                    } catch (InterruptedException e) {
                        Toast.makeText(this, "We do not recognize your email ID please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        Toast.makeText(this, "We do not recognize your email ID please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    } catch (Exception e) {
                        Toast.makeText(this, "We do not recognize your email ID please try again", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(this,"This field cannot be blank",Toast.LENGTH_SHORT).show();
                }
                else
                Toast.makeText(getBaseContext(),"No data Connection Available",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
