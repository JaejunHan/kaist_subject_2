package com.jackrutorial.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.jackrutorial.test1.FirstBulletinBoard;

public class BulletinBoard extends AppCompatActivity {

    Button button1;
    Button button2;
    Button button3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_board);
        button1 = findViewById(R.id.buttonFirst);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FirstBulletinBoard.class);
                startActivity(intent);
            }
        });
        button2 = findViewById(R.id.buttonSecond);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SecondBulletinBoard.class);
                startActivity(intent);
            }
        });
        button3 = findViewById(R.id.buttonThird);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThirdBulletinBoard.class);
                startActivity(intent);
            }
        });
    }
}