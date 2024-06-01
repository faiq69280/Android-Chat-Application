package com.example.firebasedemo;

import android.os.Bundle;

import java.io.File;
import java.util.ArrayList;
import android.content.Context;
import androidx.lifecycle.ViewModel;
public class ChatViewModel extends ViewModel{
    private ArrayList<ChatMsg> notes;
    ChatAppDAO dao;
     //ChatAppDAO dao,String conversationID, String conversationName, Context ctx
    public ArrayList<ChatMsg> getMsgs(Bundle savedInstanceState, String key,String conversationID, String conversationName, Context ctx){
        if (notes == null){
            if (savedInstanceState == null) {
                if (dao != null){
                    notes = ChatMsg.load(dao,conversationID,conversationName,ctx);
                }
                else notes = new ArrayList<ChatMsg>();
            }
            else{
                notes = (ArrayList<ChatMsg>) savedInstanceState.get(key);
            }
        }
        return notes;
    }

    public void setDao(ChatAppDAO d){
        dao = d;
    }

}
