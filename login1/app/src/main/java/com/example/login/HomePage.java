package com.example.login;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity implements PostQueryFragment.OnPostQueryFragmentInteractionListener, BackHandledFragment.BackHandlerInterface,
        DepartmentListFragment.OnFragmentDepartmentListtInteractionListener, ReplyQueryFragment.OnReplyQueryFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawListener;
    private static final int PERMISSIONS_REQUEST_INTERNET = 100;
    String department, userDepartment;
    String userID, userMail, firstName;
    private boolean fromLogin;
    SharedPreferences sharedPref;
    FragmentManager manager;
    FragmentTransaction transaction;
    ConnectivityManager cm;
    NetworkInfo netInfo;
    NetworkInfo ni;
    NavigationView navigationView;
    TextView displayName, displayMail;
    LoadQueriesFragment loadFrag;
    PostQueryFragment postfrag;
    DepartmentListFragment fragDept;
    FAQFragment fragFAQ;
    AboutUsFragment abtusFrag;
    NoInternetFragment internetFrag;
    private BackHandledFragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE);
            fromLogin = sharedPref.getBoolean("userLoggedIn", false);
            userID = sharedPref.getString("userID", "test");
            userMail = sharedPref.getString("userMail", "test@hawk.iit.edu");
            firstName = sharedPref.getString("firstName", "test");
            userDepartment = sharedPref.getString("department", "Electronics");
            setContentView(R.layout.activity_load_query);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle("Home");
            department = userDepartment;

            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_draw);
            drawListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
                @Override
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    super.onDrawerClosed(drawerView);
                }
            };
            drawerLayout.setDrawerListener(drawListener);

            navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            displayName = (TextView) headerView.findViewById(R.id.navDisplayname);
            displayMail = (TextView) headerView.findViewById(R.id.navDisplaymail);

            displayName.setText("Welcome " + firstName);
            displayMail.setText("" + userMail);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(MenuItem item) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    switch (item.getItemId()) {
                        case R.id.home:
                            boolean netOnHome = checkNetwork();
                            if (netOnHome) {
                                department = userDepartment;
                                setTitle("Home");
                                loadData();
                            } else {
                                displayNoInternet();
                            }
                            break;
                        case R.id.post:
                            boolean netOnPost = checkNetwork();
                            if (netOnPost) {
                                storeDetails();
                                setTitle("Post A New Question");
                                postfrag = new PostQueryFragment();
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.homePageLayout, postfrag, "postfrag");
                                transaction.commit();
                            } else {
                                displayNoInternet();
                            }
                            break;
                        case R.id.department:
                            boolean netOnDep = checkNetwork();
                            if (netOnDep) {
                                fragDept = new DepartmentListFragment();
                                setTitle("Change Department");
                                transaction = manager.beginTransaction();
                                transaction.replace(R.id.homePageLayout, fragDept, "fragDept");
                                transaction.commit();
                            } else {
                                displayNoInternet();
                            }
                            break;
                        case R.id.faqs:
                            fragFAQ = new FAQFragment();
                            setTitle("FAQs");
                            transaction = manager.beginTransaction();
                            transaction.replace(R.id.homePageLayout, fragFAQ, "fragFAQ");
                            transaction.commit();
                            break;
                        case R.id.aboutus:
                            abtusFrag = new AboutUsFragment();
                            setTitle("About Us");
                            transaction = manager.beginTransaction();
                            transaction.replace(R.id.homePageLayout, abtusFrag, "abtusFrag");
                            transaction.commit();
                            break;
                        case R.id.logout:
                            Intent login = new Intent(HomePage.this, MainActivity.class);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("userLoggedIn", false);
                            editor.commit();
                            startActivity(login);
                            finish();
                            break;
                    }
                    return false;
                }
            });

            boolean netOn = checkNetwork();
            if (netOn) {
                if (!fromLogin) {
                    Intent login = new Intent(this, MainActivity.class);
                    startActivity(login);
                    finish();
                } else {
                    manager = getFragmentManager();
                    storeDetails();
                    loadData();
                }
            } else {
                displayNoInternet();
            }
        } catch (Exception e) {
            displayNoInternet();
        }
    }

    public boolean checkNetwork() {
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (netInfo != null && ni != null && (netInfo.isConnected() || ni.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
    public void loadData() {
        loadFrag = new LoadQueriesFragment();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.homePageLayout, loadFrag, "loadFrag");
        transaction.commit();
    }

    public void displayNoInternet() {
        manager = getFragmentManager();
        internetFrag = new NoInternetFragment();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.homePageLayout, internetFrag, "internetFrag");
        transaction.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (netInfo != null && ni != null && (netInfo.isConnected() || ni.isConnected())) {
            if (!fromLogin) {
                Intent login = new Intent(this, MainActivity.class);
                startActivity(login);
                finish();
            } else {
                loadData();
            }
        }  else {
            Toast.makeText(this, "Please check network connection", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(selectedFragment == null || !selectedFragment.onBackPressed()) {
            // Selected fragment did not consume the back press event.
            super.onBackPressed();
        } else {
            loadData();
        }
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                loadData();
            } else {
                Toast.makeText(HomePage.this, "Until you grant the permission, we cannot display the questions posted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawListener.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawListener.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (netInfo != null && ni != null && (netInfo.isConnected() || ni.isConnected())) {
            if(fromLogin) {
                if(drawListener!= null) {
                    drawListener.syncState();
                }
            }
        } else {
            Toast.makeText(this, "Please check network connection", Toast.LENGTH_LONG).show();
        }

    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    public void storeDetails() {
        SharedPreferences sharedPrefPost = getSharedPreferences("LoggedUserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefPost.edit();
        editor.putString("LoggedUser", userID);
        editor.putString("PostDepartment", department);
        editor.putString("UserDepartment", userDepartment);
        editor.commit();
    }

    public void onPostQueryFragmentInteraction(boolean success) {
        if (success){
            setTitle("Home");
            Toast.makeText(this, "Question posted successfully", Toast.LENGTH_LONG).show();
        } else {
            setTitle("Home");
            Toast.makeText(this, "Post Query: Something went wrong please try again", Toast.LENGTH_LONG).show();
        }
        loadData();
    }

    public void onFragmentDepartmentListInteraction(String departmentResult) {
        department = departmentResult;
        setTitle("Home");
        storeDetails();
        loadData();
    }

    public void onReplyQueryFragmentInteraction(boolean success) {
        if (success){
            setTitle("Home");
            Toast.makeText(this, "Answer posted successfully", Toast.LENGTH_LONG).show();
            loadData();
        }
    }
}
