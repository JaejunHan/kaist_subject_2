package com.jackrutorial.test1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class EditMyInfo extends AppCompatActivity {

    Button editName;
    Button editNum;
    Button editMail;
    Button gosuTalk;

    TextView myName;
    TextView myNumber;
    TextView myEmail;
    TextView gosuTalkWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_info);

        editName = (Button) findViewById(R.id.editName);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 0);
            }
        });
        editNum = (Button) findViewById(R.id.editNum);
        editNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 1);
            }
        });
        editMail = (Button) findViewById(R.id.editMail);
        editMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 2);
            }
        });
        gosuTalk = (Button) findViewById(R.id.gosuTalk);
        gosuTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 3);
            }
        });


        myName = (TextView) findViewById(R.id.myName);
        myNumber = (TextView) findViewById(R.id.myNum);
        myEmail = (TextView) findViewById(R.id.myEmail);
        gosuTalkWrite = (TextView) findViewById(R.id.gosuTalkWrite);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(requestCode == 0){
                myName.setText(data.getStringExtra("changeThing"));
            } else if(requestCode == 1){
                myNumber.setText(data.getStringExtra("changeThing"));
            } else if(requestCode == 2){
                myEmail.setText(data.getStringExtra("changeThing"));
            }else if(requestCode == 3){
                gosuTalkWrite.setText(data.getStringExtra("changeThing"));
            }
        }
    }
}