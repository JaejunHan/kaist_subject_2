package com.jackrutorial.test1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EditMyInfo extends AppCompatActivity {

    Button editName;
    Button editNum;
    Button editMail;
    Button techStack;
    Button gosuTalk;
    Button addCareer;

    TextView myName;
    TextView myNumber;
    TextView myEmail;
    TextView gosuTalkWrite;
    TextView techStackText;

    ListView career;

    ArrayAdapter<String> adpater;
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
        techStack = (Button) findViewById(R.id.techStack);
        techStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                intent.putExtra("originalText", techStackText.getText().toString());
                startActivityForResult(intent, 5);
            }
        });
        gosuTalk = (Button) findViewById(R.id.gosuTalk);
        gosuTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), gosuTalkEdit.class);
                intent.putExtra("originalText", gosuTalkWrite.getText().toString());
                startActivityForResult(intent, 3);
                ;
            }
        });
        addCareer = (Button) findViewById(R.id.addCareer);
        addCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 4);
            }
        });


        myName = (TextView) findViewById(R.id.myName);
        myNumber = (TextView) findViewById(R.id.myNum);
        myEmail = (TextView) findViewById(R.id.myEmail);
        gosuTalkWrite = (TextView) findViewById(R.id.gosuTalkWrite);
        techStackText = (TextView) findViewById(R.id.techStackText);
        career = (ListView) findViewById(R.id.career);

        List<String> list = new ArrayList<>();
//        list.add("카이스트");
//        list.add("고려대");
//        list.add("포스텍");
        adpater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        career.setAdapter(adpater);
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
                gosuTalkWrite.setText(data.getStringExtra("gosuchangeThing"));
            }else if(requestCode == 4){
                adpater.add(data.getStringExtra("changeThing")); //경력에 추가
            }else if(requestCode == 5){
                techStackText.setText(data.getStringExtra("changeThing"));
            }
        }
    }
}