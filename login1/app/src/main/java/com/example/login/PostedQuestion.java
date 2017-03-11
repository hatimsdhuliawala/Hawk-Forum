package com.example.login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class PostedQuestion {

    String uniqueID;
    Long questionID;
    String question;
    String department;
    String userName;
    java.util.Date dateTime;
    String typeOfPost;
    String date;
    SimpleDateFormat sdf = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);

    public PostedQuestion() {

    }

    public PostedQuestion(String uID, String q, String dep, String dt, String type) {
        userName = uID;
        question = q;
        department = dep;
        date = dt;
        typeOfPost = type;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String u) {
        uniqueID = u;
    }

    public Long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String q) {
        question = q;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public java.util.Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(String d) {
        this.date = d;
        try {
            dateTime = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return typeOfPost;
    }

    public void setType(String type) {
        this.typeOfPost = type;
    }

}
