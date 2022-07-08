package com.jackrutorial.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Example extends AppCompatActivity {

    // Main 에서 받아오는 data 목록
    String resultId, nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);

        Button board_button = findViewById(R.id.board_btn);

        // button click 시 이동
        board_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 이전 activity 에서 데이터 받아오기
                resultId = getIntent().getStringExtra("user_id_in");
                nickname = getIntent().getStringExtra("name_in");

                // 게시판 Activity로 이동 맟 data 전달
                Intent board_intent = new Intent(getApplicationContext(), BoardActivity.class);
                board_intent.putExtra("user_id", resultId);
                board_intent.putExtra("name", nickname);
                //////////////pic_uri ? 등 여기에 다른 정보 추가 가능

                startActivity(board_intent);

            }
        });


    }
}
