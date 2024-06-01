package com.example.firebasedemo;

import androidx.lifecycle.ViewModelProvider;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;
import java.io.Serializable;
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest.permission.*;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ConversationsActivity extends AppCompatActivity implements ConversationAdapter.ConversationClickListener, Serializable, FireBaseConvoDAO.DataObserver {
    private RecyclerView recyclerView;
    private ConversationAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAppDAO dao;
    private ActivityResultLauncher<Intent> chatLauncher;

    private static final int REQUEST_READ_CONTACTS_PERMISSION = 0;
    private static final int REQUEST_CONTACT = 1;
    private Button mContactPick;
    String selfID = "03237486891";

    String selfName = "Jack";



    ArrayList<ConversationItem> dataSet = new ArrayList<ConversationItem>();
    ConversationsViewModel vm;
    public void update(){
        dataSet = ConversationItem.load(dao);
        Log.i("update size: ", String.valueOf(dataSet.size()));
        mAdapter.changeDataSet(dataSet);
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list);

        Intent intent = getIntent();
        this.selfID=intent.getStringExtra("selfID");
        this.selfName=intent.getStringExtra("selfName");


        recyclerView = (RecyclerView) findViewById(R.id.convolist);
        recyclerView.setHasFixedSize(true);



        //dao = new ConvoDAO(this);
        vm = new ViewModelProvider(ConversationsActivity.this).get(ConversationsViewModel.class);
        dao = new FireBaseConvoDAO(this, selfID);
        /*new FireBaseMessagesDAO(new FireBaseMessagesDAO.DataObserver() {
            @Override
            public void update() {

            }
        }).load("03237486891");*/

        vm.setDao(dao);


        chatLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                    }
                });



        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //dataSet = ChatMsg.load(dao,conversationID,conversationName,this);
        // specify an adapter (see also next example)
        //dataSet = new ArrayList<ChatMsg>();
        dataSet=vm.getConvos(savedInstanceState,"conversations");
        mAdapter = new ConversationAdapter(dataSet,this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        //code for contacts app
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mContactPick = findViewById(R.id.contactapp);
        mContactPick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        requestContactsPermission();

        updateButton(hasContactsPermission());



    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_READ_CONTACTS_PERMISSION && grantResults.length > 0)
        {
            updateButton(grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }
    }

    public void updateButton(boolean enable)
    {
        mContactPick.setEnabled(enable);
    }
    private boolean hasContactsPermission()
    {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) ==
                PackageManager.PERMISSION_GRANTED;
    }

    // Request contact permission if it
    // has not been granted already
    private void requestContactsPermission()
    {
        if (!hasContactsPermission())
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS_PERMISSION);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_CONTACT && data != null)
        {
            Uri contactUri = data.getData();

            // Specify which fields you want
            // your query to return values for
            String[] queryFields = new String[]{ContactsContract.Contacts.DISPLAY_NAME,};

            // Perform your query - the contactUri
            // is like a "where" clause here
            Cursor cursor = this.getContentResolver()
                    .query(contactUri, queryFields, null, null, null);
            try
            {
                // Double-check that you
                // actually got results
                if (cursor.getCount() == 0) return;

                // Pull out the first column of
                // the first row of data
                // that is your contact's name
                cursor.moveToFirst();

                String name = cursor.getString(0);
                //((TextView)(findViewById(R.id.textinfo))).setText(name);
                ConversationItem c = new ConversationItem(dao, name, UUID.randomUUID().toString());
                c.save();
                dataSet.add(c);
                mAdapter.notifyDataSetChanged();
            }
            finally
            {
                cursor.close();
            }
        }
    }



    public void onClick(ConversationItem c){
        //Toast.makeText(this, c.getID()+"\n"+c.getName(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Id",c.getID());
        intent.putExtra("name",c.getName());
        intent.putExtra("selfID",selfID);
        intent.putExtra("selfName",selfName);
        //index.put(n.getId(),n);
        chatLauncher.launch(intent);

    }
    public void addContact(View v){
        if(v.getId() == R.id.addcontact){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Title");

// Set up the input
            final EditText input = new EditText(this);
            final EditText number = new EditText(this);
            input.setHint("Enter name...");
            number.setHint("Enter number...");
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            //input.setInputType(InputType.TYPE_CLASS_TEXT);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins( 20,20,20,20);
            LinearLayout lp=new LinearLayout(this);
            lp.setOrientation( LinearLayout.VERTICAL );
            lp.addView(input);
            lp.addView(number);
            builder.setView(lp);

// Set up the buttons
            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //m_Text = input.getText().toString();
                    ConversationItem c;
                    if(!input.getText().toString().equals("") && !number.getText().toString().equals("")) {
                        c = new ConversationItem(dao, input.getText().toString(),number.getText().toString());
                        c.save();
                        dataSet.add(c);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        }
    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        //state.putSerializable("conversations",dataSet);
        //state.putInt(DISPLAY_KEY,displayMode);
    }












}
