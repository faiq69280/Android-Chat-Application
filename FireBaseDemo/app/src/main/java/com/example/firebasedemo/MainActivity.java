package com.example.firebasedemo;

import androidx.lifecycle.ViewModelProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.content.Intent;


import java.io.Serializable;
import java.util.ArrayList;
import android.os.Handler;
import android.os.Looper;
import java.io.Serializable;
public class MainActivity extends AppCompatActivity implements Serializable,FireBaseMessagesDAO.DataObserver {
    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    String conversationName;
    String conversationID;

    ArrayList<ChatMsg> dataSet = new ArrayList<ChatMsg>();

    EditText msgBox;
    ChatAppDAO dao;

    public void update(){
        dataSet = ChatMsg.load(dao,this.conversationID,this.conversationName,this);
        /*dataSet.sort((o1, o2) -> o1.getCreationDateTime().compareTo(
                o2.getCreationDateTime()));*/
        mAdapter.changeData(dataSet);
    }




    String selfID;
    String selfName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        Intent intent = getIntent();
        this.selfID = intent.getStringExtra("selfID");
        this.selfName = intent.getStringExtra("selfName");


        //dao = new MessagesDAO(this);
        dao = new FireBaseMessagesDAO(this);
        ((FireBaseMessagesDAO)dao).setSelfID(selfID);
        ((FireBaseMessagesDAO)dao).setSelfName(selfName);
        ChatViewModel vm = new ViewModelProvider(MainActivity.this).get(ChatViewModel.class);
        vm.setDao(dao);
        //Start of intent logic

        conversationName = intent.getStringExtra("name");
        //textArea.setText(content);
        conversationID = intent.getStringExtra("Id");



        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //dataSet = ChatMsg.load(dao,conversationID,conversationName,this);
        // specify an adapter (see also next example)
        //dataSet = new ArrayList<ChatMsg>();
        dataSet=vm.getMsgs(savedInstanceState,"msgs",conversationID,conversationName,this);
        mAdapter = new ChatAdapter(dataSet);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);



        //dao = new MessagesDAO(this);


        msgBox = (EditText) findViewById(R.id.msgtype);
    }

    public void sendMessage(View v){
        if (v.getId() == R.id.button_send){
            ChatMsg msg = new ChatMsg(this.selfName,msgBox.getText().toString(),"me",this);
            dataSet.add(msg);
            //call setDAO here
            msg.setDao(dao);

            msg.save(conversationID);
            mAdapter.notifyDataSetChanged();
            /*final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    ChatMsg ms2 = new ChatMsg("Johan Seong","yeah sure,lets talk","not_me",this);
                    dataSet.add(ms2);
                    //msg.save(conversationID as foreignkey)
                    mAdapter.notifyDataSetChanged();
                }
            }, 500);

             */
            /*ChatMsg ms2 = new ChatMsg(conversationName,"yeah sure,lets talk","not_me",this);
            dataSet.add(ms2);
            ms2.save(conversationID);
            mAdapter.notifyDataSetChanged();

             */

        }

    }


    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        state.putSerializable("msgs",dataSet);
        //state.putInt(DISPLAY_KEY,displayMode);
    }


}