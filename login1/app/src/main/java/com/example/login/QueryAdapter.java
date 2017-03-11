package com.example.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.MongoConnect.DeleteQuestionAsyncTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class QueryAdapter  extends BaseAdapter {

    ArrayList<PostedQuestion> list;
    Context context;
    int REPLY_QUERY_REQUEST_CODE = 2705;
    String userID;
    boolean replyEnabled;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public QueryAdapter(Context c) {
        context = c;
    }

    public QueryAdapter(Context c, ArrayList<PostedQuestion> questions) {
        context = c;
        list = questions;
        replyEnabled = false;
    }

    public QueryAdapter(Context c, ArrayList<PostedQuestion> questions, String uName, boolean reply) {
        context = c;
        list = questions;
        userID = uName;
        replyEnabled = reply;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_query_item,viewgroup,false);
        TextView userID = (TextView) row.findViewById(R.id.userIDText);
        TextView dateTime = (TextView) row.findViewById(R.id.dateTime);
        TextView query = (TextView) row.findViewById(R.id.queryText);
        Button rq = (Button) row.findViewById(R.id.buttonReply);

        final PostedQuestion temp = list.get(position);
        query.setText(temp.getQuestion());
        userID.setText(temp.getUserName());
        dateTime.setText(sdf.format(temp.getDateTime()));

        if(!replyEnabled) {
            rq.setVisibility(View.GONE);
            query.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            rq.setVisibility(View.VISIBLE);
        }


        rq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netOn = ((HomePage)context).checkNetwork();
                if(netOn) {
                    writeToSharedPref(temp);
                    ReplyQueryFragment replyFrag = new ReplyQueryFragment();
                    ((HomePage) context).transaction = ((HomePage) context).manager.beginTransaction();
                    ((HomePage) context).transaction.replace(R.id.homePageLayout, replyFrag, "replyFrag");
                    ((HomePage) context).transaction.commit();
                    ((HomePage) context).setTitle("Reply A Question");
                } else {
                    ((HomePage)context).displayNoInternet();
                }
            }
        });

        query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean netOn = ((HomePage)context).checkNetwork();
                if(netOn) {
                    if (temp.getType().equals("question")) {
                        writeToSharedPref(temp);
                        QuestionAndAnswerFragment quesFrag = new QuestionAndAnswerFragment();
                        ((HomePage) context).transaction = ((HomePage) context).manager.beginTransaction();
                        ((HomePage) context).transaction.replace(R.id.homePageLayout, quesFrag, "quesFrag");
                        ((HomePage) context).transaction.commit();
                        ((HomePage) context).setTitle("Question and Answers");
                    }
                }  else {
                    ((HomePage)context).displayNoInternet();
                }
            }
        });

        query.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean netOn = ((HomePage)context).checkNetwork();
                if(netOn) {
                    if (temp.getUserName().equals(QueryAdapter.this.userID)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete Alert");
                        builder.setMessage("Delete is permanent. Do you wish to continue?");
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteQuestionAsyncTask task = new DeleteQuestionAsyncTask(temp);
                                try {
                                    boolean success = task.execute().get();
                                    if (success) {
                                        Toast.makeText(context, "The question deleted successfully", Toast.LENGTH_LONG).show();
                                        ((HomePage) context).loadData();
                                    } else {
                                        Toast.makeText(context, "The question was not deleted", Toast.LENGTH_LONG).show();
                                    }
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        Dialog dialog = builder.create();
                        dialog.setCancelable(false);
                        dialog.show();

                    } else {
                        Toast.makeText(context, "You can only delete a question that you posted!!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    ((HomePage)context).displayNoInternet();
                }

                return true;
            }
        });
        return row;
    }

    public void writeToSharedPref(PostedQuestion temp) {
        sharedPref = context.getSharedPreferences("QueryDetails", 0);
        editor = sharedPref.edit();
        editor.putString("question", temp.getQuestion());
        editor.putString("department", temp.getDepartment());
        editor.putString("userID", QueryAdapter.this.userID);
        editor.putLong("questionID", temp.getQuestionID());
        editor.commit();
    }
}

