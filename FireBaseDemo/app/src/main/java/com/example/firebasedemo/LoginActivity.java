package com.example.firebasedemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity implements Serializable {
    EditText name;
    EditText number;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginlayout);
        number = (EditText) findViewById(R.id.number);
        name = (EditText) findViewById(R.id.name);
    }
    public void loginButtonClick(View v){
        if(v.getId()==R.id.loginbutton){
            Intent intent = new Intent(this,ConversationsActivity.class);
            intent.putExtra("selfName",name.getText().toString());
            intent.putExtra("selfID",number.getText().toString());
            startActivity(intent);
        }
    }
}
