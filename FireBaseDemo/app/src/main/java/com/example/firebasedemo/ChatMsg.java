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
public class ChatMsg implements Serializable{
    private ChatAppDAO dao;
    private String id;
    private String content;
    private String sender_type;
    private Date creationDateTime;
    private String sender; //"me" or some other name

    public ChatMsg(String sender_name,String msg_content, String type,Context ctx){
        sender = sender_name;
        content = msg_content;
        sender_type = type;
        init(ctx);
    }

    public ChatMsg(String sender_name,String type,Context ctx){
        sender = sender_name;
        sender_type = type;
        content = "";
        init(ctx);
    }
    public void init(Context ctx){
        this.id = UUID.randomUUID().toString();
        this.creationDateTime = new Date();
        //dao = new MessagesDAO(ctx);
    }

    public Date getCreationDateTime(){
        return this.creationDateTime;
    }
    public void setDao(ChatAppDAO d){
        this.dao = d;
    }
    public String getContent(){
        return content;
    }
    public String getSenderName(){
        return sender;
    }
    public String getTimeStamp(){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        return dateFormat.format(creationDateTime);
    }


    public String getType(){
        return sender_type;
    }
    public boolean save(String with){
        if (dao != null){

            Hashtable<String,String> data = new Hashtable<String, String>();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ");

            data.put("Id",id);
            data.put("Content",content);
            data.put("CreationDateTime",getTimeStamp());
            if(getType().toLowerCase().equals("me"))
                data.put("status","sent");
            else
                data.put("status","recieved");
            data.put("conversation_with",with);

            dao.save(data);
            return true;
        }
        return false;


    }
    public void setDate(Date d){
        this.creationDateTime = d;
    }

    public static ArrayList<ChatMsg> load(ChatAppDAO dao,String conversationID, String conversationName, Context ctx){
          ArrayList<Hashtable<String,String>> obj = dao.load(conversationID);
          ArrayList<ChatMsg> msgs = new ArrayList<ChatMsg>();

          for(Hashtable<String,String> h : obj){
              String content = h.get("Content");
              String dateTime = h.get("CreationDateTime");
              String status = h.get("status");
              String sender = conversationName;
              if(status.toLowerCase().equals("sent")) {
                 status = "me";
                 //sender = "Faiq";
                 sender = ((FireBaseMessagesDAO)dao).selfName;
              }
              DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
              Date d = new Date();
              try {
                  d = dateFormat.parse(dateTime);
              }catch(ParseException ex) {

              }
              ChatMsg c = new ChatMsg(sender,content,status,ctx);
              c.setDate(d);
              c.setDao(dao);
              //setDao needs to be called here
              msgs.add(c);
          }


          return msgs;
    }
}
