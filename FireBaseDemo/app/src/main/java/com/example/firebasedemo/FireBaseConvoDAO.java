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

public class FireBaseConvoDAO implements ChatAppDAO, Serializable{
    public interface DataObserver{
        public void update();
    }
    private ArrayList<Hashtable<String,String>> data;
    private ArrayList<String> ids;
    DataObserver observer;
    FirebaseDatabase database;
    String selfID;
    public static long countUsers;
    private void fillData(){
        //FirebaseDatabase database;
        DatabaseReference myRef;
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        myRef = database2.getReference("Chat").child("Users");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    countUsers = dataSnapshot.getChildrenCount();

                    data = new ArrayList<Hashtable<String,String>>();
                    for(String id : ids){
                        Hashtable<String,String> h = new Hashtable<String,String>();
                        h.put("Id",id);
                        Log.i("id fillData: ",id);
                        for(DataSnapshot d : dataSnapshot.getChildren()) {
                            Hashtable<String,String> h1= new Hashtable<String,String>();
                            for(DataSnapshot d1 : d.getChildren()){
                                h1.put(d1.getKey(),String.valueOf(d1.getValue()));
                                Log.i(d1.getKey(),String.valueOf(d1.getValue()));
                            }
                            if(h1.get("id").equals(id)) {
                                h.put("name",h1.get("name"));
                                break;
                            }

                        }
                        data.add(h);

                    }
                    Log.i("size: ",String.valueOf(data.size()));

                    //here you can notify the observer
                    observer.update();

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


    }


    //reason why constraint is being added in constructor is because conversation id won't change
    //for a particular application instance
    public FireBaseConvoDAO(DataObserver obs, String id){
        this.observer = obs;
        this.selfID = id;

        DatabaseReference myRef;
        database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        myRef = database.getReference("Chat").child("GlobalData");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    ids = new ArrayList<String>();

                    for(DataSnapshot d : dataSnapshot.child(id).child("contacts").getChildren()){
                         ids.add(String.valueOf(d.getKey()));
                         Log.i("id: ",String.valueOf(d.getKey()));
                    }
                    fillData();
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



    }

    public ArrayList<Hashtable<String,String>> load(String id){
        if(data == null)
            return new ArrayList<Hashtable<String,String>>();

        return data;
    }

    @Override
    public ArrayList<Hashtable<String, String>> load() {
        if(data == null)
            return new ArrayList<Hashtable<String,String>>();

        return data;
    }
    public boolean save(Hashtable<String,String> h){
        //something needs to be implemented here
        //write to both users and create a new node in id.contacts
        DatabaseReference myRef1;
        //database = FirebaseDatabase.getInstance();
        //database.setPersistenceEnabled(true);
        myRef1 = database.getReference("Chat").child("Users");
        HashMap<String,String> obj = new HashMap<String,String>();
        obj.put("id",h.get("Id"));
        obj.put("name",h.get("name"));
        myRef1.child(String.valueOf(countUsers+1)).setValue(obj);

        database.getReference("Chat").child("GlobalData")
                .child(selfID).child("contacts")
                .child(h.get("Id")).child("msgs").setValue("");

        database.getReference("Chat").
                child("GlobalData").
                child(h.get("Id")).child("contacts").child(selfID).
                child("msgs").setValue("");


        return true;
    }


}
