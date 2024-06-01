package com.example.firebasedemo;
;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class MessagesDAO implements ChatAppDAO, Serializable {
    private Context context;
    public MessagesDAO(Context ctx){
        context = ctx;
    }


    public boolean save(Hashtable<String,String> row){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues content = new ContentValues();
        Enumeration<String> keys = row.keys();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            content.put(key,row.get(key));
        }


        db.insert("Message",null,content);
        return true;
    }

    public ArrayList<Hashtable<String,String>> load(){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT * FROM Message";
        Cursor cursor = db.rawQuery(query,null);



        ArrayList<Hashtable<String,String>> resultSet = new ArrayList<Hashtable<String,String>>();
        while(cursor.moveToNext()){
            String [] columns = cursor.getColumnNames();
            Hashtable<String,String> obj = new Hashtable<String, String>();
            for(String col : columns){
                int colIndex = cursor.getColumnIndex(col);
                if(colIndex > -1)
                  obj.put(col.toLowerCase(),cursor.getString(colIndex));
            }
            resultSet.add(obj);

        }
        cursor.close();

        return resultSet;
    }

    public ArrayList<Hashtable<String,String>> load(String convoWith){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT Content,CreationDateTime,status, conversation_with FROM Message WHERE conversation_with = ?";
        String[] arguments = new String[1];
        arguments[0] = convoWith;
        Cursor cursor = db.rawQuery(query,arguments);


        ArrayList<Hashtable<String,String>> resultSet = new ArrayList<Hashtable<String,String>>();
        while(cursor.moveToNext()){
            /*String [] columns = cursor.getColumnNames();
            Hashtable<String,String> obj = new Hashtable<String, String>();
            for(String col : columns){
                int colIndex = cursor.getColumnIndex(col);
                if(colIndex > -1)
                 obj.put(col.toLowerCase(),cursor.getString(colIndex));
            }*/
            Hashtable<String,String> obj = new Hashtable<String, String>();
            obj.put("Content",cursor.getString(0));
            obj.put("CreationDateTime",cursor.getString(1));
            obj.put("status",cursor.getString(2));
            obj.put("conversation_with",cursor.getString(3));
            resultSet.add(obj);

        }
        //cursor.close();

        return resultSet;

    }



}
