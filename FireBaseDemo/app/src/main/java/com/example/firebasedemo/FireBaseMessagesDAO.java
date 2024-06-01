package com.example.firebasedemo;

import android.util.Log;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;


public class FireBaseMessagesDAO implements ChatAppDAO, Serializable {

    public String selfID;
    public String selfName;
    ArrayList<Hashtable<String,String>> data;
    FirebaseDatabase database;
    public interface DataObserver{
        public void update();
    }
    public DataObserver observer;
    public static long countMsgs = 0;
    public FireBaseMessagesDAO(DataObserver obs){
        this.observer=obs;
        this.selfID = "3045424118";
    }
    public void setSelfID(String newID){
        this.selfID = newID;
    }
    public void setSelfName(String newName){
        this.selfName = newName;
    }

    public ArrayList<Hashtable<String,String>> load(String conversationID){
        if(data==null) {
            DatabaseReference myRef;
            database = FirebaseDatabase.getInstance();
            //database.setPersistenceEnabled(true);
            myRef = database.getReference("Chat").child("GlobalData").child(this.selfID).child("contacts").child(conversationID).child("msgs");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        data = new ArrayList<Hashtable<String,String>>();
                        countMsgs = dataSnapshot.getChildrenCount();
                        Log.i("codenew","yes");
                        if(countMsgs==0)
                            return;
                        for(DataSnapshot msgIDChild: dataSnapshot.getChildren()){
                            Hashtable<String,String> h = new Hashtable<String,String>();
                            for(DataSnapshot msgBodyChild : msgIDChild.getChildren()) {
                                if(String.valueOf(msgBodyChild.getKey()).equals("msg"))
                                    h.put("Content",String.valueOf(msgBodyChild.getValue()));
                                else if(String.valueOf(msgBodyChild.getKey()).equals("status"))
                                    h.put("status",String.valueOf(msgBodyChild.getValue()));
                                else if(String.valueOf(msgBodyChild.getKey()).equals("timestamp"))
                                    h.put("CreationDateTime",String.valueOf(msgBodyChild.getValue()));
                                h.put("conversation_with",conversationID);
                                Log.i(msgBodyChild.getKey(),String.valueOf(msgBodyChild.getValue()));
                            }
                            data.add(h);
                        }
                        observer.update();
                        //fillData();
                    }
                    catch (Exception ex) {
                        Log.e("firebasedb", ex.getMessage());
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Log.w("firebasedb", "Failed to read value.", error.toException());
                }
            });

            //execute callback
        }
        else return data;

        return new ArrayList<Hashtable<String,String>>();
    }
    public ArrayList<Hashtable<String,String>> load(){
        return new ArrayList<Hashtable<String,String>>();
    }

    public boolean save(Hashtable<String,String> h){
        //The fun part where ill finally have a working chatapplication i can show on my resume
        String conversationID = h.get("conversation_with");
        DatabaseReference ref1 = database.getReference("Chat").child("GlobalData").child(this.selfID).child("contacts").child(conversationID).child("msgs");
        DatabaseReference ref2 = database.getReference("Chat").child("GlobalData").child(conversationID).child("contacts").child(this.selfID).child("msgs");
        HashMap<String,String> h1 = new HashMap<String,String>();
        h1.put("timestamp",h.get("CreationDateTime"));
        h1.put("msg",h.get("Content"));
        h1.put("status",h.get("status"));
        String uuid = String.valueOf(UUID.randomUUID());
        ref1.child(String.valueOf(countMsgs+1)).setValue(h1);
        h1.put("status","recieved");
        uuid = String.valueOf(UUID.randomUUID());
        ref2.child(String.valueOf(countMsgs+1)).setValue(h1);
        Log.i("count",String.valueOf(countMsgs+1));

        return true;
    }


}
