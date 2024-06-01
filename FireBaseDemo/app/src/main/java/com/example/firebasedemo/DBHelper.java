package com.example.firebasedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ChatDB.db";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String msgsTable = "CREATE TABLE Message(Id TEXT PRIMARY KEY, " +
                "Content TEXT," +
                "CreationDateTime TEXT,"+ "status TEXT," + "conversation_with TEXT," +
                " FOREIGN KEY(conversation_with) REFERENCES Conversation(Id))";

        String conversationTable = "CREATE TABLE Conversation(Id TEXT PRIMARY KEY, name TEXT)";
        db.execSQL(conversationTable);
        db.execSQL(msgsTable);
        ContentValues cv = new ContentValues();
        cv.put("Id","1");
        cv.put("name","Johan Seong");
        db.insert("Conversation",null,cv);

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }




}
