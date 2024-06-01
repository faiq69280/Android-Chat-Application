package com.example.firebasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class ConvoDAO implements ChatAppDAO, Serializable {
    private Context ctx;
    public ConvoDAO(Context newContext){
        ctx = newContext;
    }
    public boolean save(Hashtable<String,String> row){
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        Enumeration<String> keys = row.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            content.put(key,row.get(key));
        }


        db.insert("Conversation",null,content);

        return true;
    }
    public ArrayList<Hashtable<String,String>> load(){
        DBHelper dbHelper = new DBHelper(ctx);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT Id,name FROM Conversation";
        Cursor cursor = db.rawQuery(query,null);



        ArrayList<Hashtable<String,String>> resultSet = new ArrayList<Hashtable<String,String>>();
        while(cursor.moveToNext()){
            String [] columns = cursor.getColumnNames();
            Hashtable<String,String> obj = new Hashtable<String, String>();
            obj.put("Id",cursor.getString(0));
            obj.put("name", cursor.getString(1));
            resultSet.add(obj);

        }
        cursor.close();

        return resultSet;

    }

    public ArrayList<Hashtable<String,String>> load(String conversationID){
        //note : for now, this is a dummy method we don't need atm, but will be
        //needed in the future
        return new ArrayList<Hashtable<String,String>>();
    }

}
