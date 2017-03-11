package com.example.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.MongoConnect.GetUserAsyncTask;

import java.util.concurrent.ExecutionException;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity  {

    private EditText passwordTextView,emailTextView;
    private View signUpTextView,forgotpassword;
    private Button loginButton;
    String email,password12;
    UserDetails user;
    boolean homepageLaunched = false;
    public int HOMEPAGE_REQUEST_CODE = 270;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    NetworkInfo ni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homepageLaunched = false;

        initialize();
        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo != null && ni != null && (netInfo.isConnected() || ni.isConnected())) {
                    email = emailTextView.getText().toString();
                    password12 = passwordTextView.getText().toString();
                    if (email.equals("") || password12.equals("")) {
                        Toast.makeText(getBaseContext(), "Enter All the Fields", Toast.LENGTH_SHORT).show();
                    } else {
                        GetUserAsyncTask task = new GetUserAsyncTask(email);
                        try {
                            user = task.execute().get();
                            if (user != null) {
                                String repass = user.getPassword();
                                passwordTextView.setText(repass);
                                if ((repass != null) && (repass.equals(password12))) {
                                    launchHomepage();       //launching the homepage
                                    // Hiding Keyboard
                                    InputMethodManager i = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    i.hideSoftInputFromInputMethod(passwordTextView.getWindowToken(), 0);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Emai ID or Password Incorrect.. ", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(MainActivity.this, "Are you sure that you have registered yourself with us?", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "The email ID you entered is not registered with the database", Toast.LENGTH_SHORT).show();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                } else
                    Toast.makeText(MainActivity.this, "No Data Connection Available", Toast.LENGTH_SHORT).show();
            }});

        forgotpassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo != null && ni != null && (netInfo.isConnected() || ni.isConnected())){
                    Intent frgtpswd = new Intent(MainActivity.this,ForgotPassword.class);
                    startActivity(frgtpswd);
                    finish();
            }else
                    Toast.makeText(MainActivity.this,"No data Connection Available",Toast.LENGTH_SHORT).show();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (netInfo != null && ni != null && (netInfo.isConnected() || ni.isConnected())){
                // TODO Auto-generated method stub
                Intent signup = new Intent(MainActivity.this,SignUp.class);
                startActivity(signup);
                finish();
            }else
                    Toast.makeText(MainActivity.this,"No data Conection Available",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initialize() {
        // TODO Auto-generated method stub
        emailTextView = (EditText) findViewById(R.id.email);
        passwordTextView = (EditText) findViewById(R.id.password);
        signUpTextView = findViewById(R.id.signUpTextView);
        forgotpassword = findViewById(R.id.ForgotPasswrodTextView);
        loginButton = (Button) findViewById(R.id.email_sign_in_button);

    }

    public void launchHomepage() {

        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userID", user.getUserID());
        editor.putString("department",user.getDepartment());
        editor.putString("userMail", user.getEmail());
        editor.putString("firstName", user.getFirst_name());
        editor.putBoolean("userLoggedIn", true);
        editor.commit();
        Intent intent = new Intent(MainActivity.this, HomePage.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
