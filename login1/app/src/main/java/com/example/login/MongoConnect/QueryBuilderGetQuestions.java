package com.example.login.MongoConnect;

import android.util.Log;

import com.example.login.PostedQuestion;

import java.util.Calendar;

public class QueryBuilderGetQuestions {

    String department;
    Long questionID;
    String docID;

    public void setDepartment(String dep) {
        department = dep;
    }

    public String getDepartment() { return  department;}

    public Long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Long ID) {
        questionID = ID;
    }

    public String getDatabaseName() {
        return "hawksforum";
    }

    public String getApiKey() {
        return "xrJDpTroNZcfF3bRnVAnl8dGA05JEOcF";
    }

    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    public String docApiKeyUrl()
    {
        return "apiKey="+getApiKey();
    }

    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

    public String getCollection()
    {
        return "postedQuery";
    }

    public String getDocId() {return  docID;}

    public void setDocID(String doc) {
        docID = doc;
    }

    public String buildQuestionSaveURL()
    {
        return getBaseUrl()+getCollection()+"?"+docApiKeyUrl();
    }

    public String buildQuesAndAnsFilterURL()
    {
        return getBaseUrl()+getCollection()+"?q={'questionID':'"+getQuestionID()+"'}&"+docApiKeyUrl();
    }
    public String getMaxIDURL()
    {
       return getBaseUrl()+getCollection()+"?s={'questionID':-1}&l=1&"+docApiKeyUrl();
    }

    public String buildQuestionHomePageFilterURL()
    {
        return getBaseUrl()+getCollection()+"?q={'department':'"+getDepartment()+"', 'typeOfPost':'question'}&"+docApiKeyUrl();
    }

    public String buildQuesDeleteURL() {
        return getBaseUrl()+getCollection()+"/"+getDocId()+"?"+docApiKeyUrl();
    }

    public String createQuestion(PostedQuestion question) {
        java.util.Date curdate;
        Calendar cal = Calendar.getInstance();
        curdate = cal.getTime();

        return String.
                format("{\"userID\" : \"%s\", "
                                + "\"questionID\" : \"%s\", "
                                + "\"post\" : \"%s\", "
                                + "\"dateTime\" : \"%s\" ,"
                                + "\"typeOfPost\" : \"%s\" ,"
                                + "\"department\" : \"%s\" }",
                        question.getUserName(),
                        questionID,
                        question.getQuestion(),
                        curdate,
                        question.getType(),
                        question.getDepartment());

    }

    public String createAnswer(PostedQuestion question) {
        java.util.Date curdate;
        Calendar cal = Calendar.getInstance();
        curdate = cal.getTime();

        return String.
                format("{\"userID\" : \"%s\", "
                                + "\"questionID\" : \"%s\", "
                                + "\"post\" : \"%s\", "
                                + "\"dateTime\" : \"%s\" ,"
                                + "\"typeOfPost\" : \"%s\" ,"
                                + "\"department\" : \"%s\" }",
                        question.getUserName(),
                        question.getQuestionID(),
                        question.getQuestion(),
                        curdate,
                        question.getType(),
                        question.getDepartment());
    }
}
