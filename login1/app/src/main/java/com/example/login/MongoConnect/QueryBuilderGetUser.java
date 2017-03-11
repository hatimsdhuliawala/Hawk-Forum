package com.example.login.MongoConnect;

import com.example.login.UserDetails;

public class QueryBuilderGetUser {

    String useremail;

    public String getEmail(){
        return useremail;
    }

    public void setEmailid(String emailid){
        useremail=emailid;
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

    public String getCollections()
    {
        return "userDetails";
    }

    public String buildContactsSaveURL()
    {
        return getBaseUrl()+ getCollections()+"?"+docApiKeyUrl();
    }

    public String createContact(UserDetails contact)
    {
        return String
                .format("{\"firstName\": \"%s\", "
                                + "\"lastName\": \"%s\", "
                                + "\"type\": \"%s\", "
                                + "\"email\": \"%s\", "
                                + "\"userID\": \"%s\",  "
                                + "\"password\": \"%s\",  "
                                + "\"secretQuestion\": \"%s\",  "
                                + "\"secretAnswer\": \"%s\",  "
                                + "\"department\": \"%s\"}",
                        contact.getFirst_name(), contact.getLast_name(), contact.getType(), contact.getEmail(), contact.getUserID(),
                        contact.getPassword(), contact.getSecretQuestion(), contact.getSecretAnswer(), contact.getDepartment());
    }
    public String userDetailsFilterURL()
    {
        return getBaseUrl()+ getCollections() +"?q={\'email\':'"+getEmail()+"'}&"+docApiKeyUrl();
    }

}
