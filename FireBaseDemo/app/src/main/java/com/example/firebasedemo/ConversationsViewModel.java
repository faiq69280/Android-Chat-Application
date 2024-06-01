package com.example.firebasedemo;


import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import android.os.Bundle;
import java.io.File;
import android.content.Context;
import androidx.lifecycle.ViewModel;

public class ConversationsViewModel extends ViewModel{
    private ArrayList<ConversationItem> notes;
    ChatAppDAO dao;
    //ChatAppDAO dao,String conversationID, String conversationName, Context ctx
    public ArrayList<ConversationItem> getConvos(Bundle savedInstanceState, String key){
        if (notes == null){
            if (savedInstanceState == null) {
                if (dao != null){
                    notes = ConversationItem.load(dao);
                }
                else notes = new ArrayList<ConversationItem>();
            }
            else{
                notes = (ArrayList<ConversationItem>) savedInstanceState.get(key);
            }
        }
        return notes;
    }

    public void setDao(ChatAppDAO d){
        dao = d;
    }



}
