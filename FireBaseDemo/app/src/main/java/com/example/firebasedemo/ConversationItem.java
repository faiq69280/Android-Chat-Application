package com.example.firebasedemo;

import java.util.Date;
import java.util.UUID;
import java.text.DateFormat;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import android.content.Context;
import java.util.Hashtable;
import java.text.ParseException;
import java.io.Serializable;
public class ConversationItem implements Serializable  {
    private String Id;
    private String name;
    private Context ctx; //this is being used just in case
    private ChatMsg chMsg;
    private String lastMsg;
    private ChatAppDAO dao;
    public void init(){
        //for now, don't do anything with convoID, this for
        //fetching the last msg
        this.Id = UUID.randomUUID().toString();
    }
    public ConversationItem(ChatAppDAO newDao, String newName){
        dao = newDao;
        name = newName;
        init();
    }
    public ConversationItem(ChatAppDAO newDAO, String newName, String newID){
        dao = newDAO;
        name = newName;
        Id = newID;
    }

    public String getLastMsg(){
        return lastMsg;
    }
    public void setLastMsg(String msg){
        this.lastMsg = msg;
    }

    public String getID(){
        return this.Id;
    }
    public String getName(){
        return this.name;
    }
    public void save(){
        Hashtable<String,String> row = new Hashtable<String,String>();
        row.put("Id",Id);
        row.put("name",name);
        dao.save(row);
    }
    public static ArrayList<ConversationItem> load(ChatAppDAO dao){
        ArrayList<Hashtable<String,String>> rows = dao.load();
        ArrayList<ConversationItem> conversationSubjects = new ArrayList<ConversationItem>();

        for(Hashtable<String,String> item : rows){
            conversationSubjects.add(new ConversationItem(dao,item.get("name"), item.get("Id")));
        }
        return conversationSubjects;
    }

}
