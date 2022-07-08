package com.jackrutorial.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BoardActivity extends AppCompatActivity {

    // 사용할 변수와 객체 선언
    private BottomNavigationView bottomNavigationView;
    String resultId, nickname;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    /////////////////////////////////
    // BoardActivity 에서 불러올 fragment
    private ProfileFragment profileFragment;
    private PostFragment postFragment;
    private TmpFragment tmpFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        // intent 넘겨받기
        Intent intent = getIntent();
        resultId = intent.getStringExtra("resultId");
        nickname = intent.getStringExtra("nickname");

        // 하단 navigation - 탭 간 이동
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId())
                {
                    case R.id.Profile: // 하단 nav에서 profile 선택 시
                        setFrag(0);
                        break;
                    case R.id.Post:
                        setFrag(1);
                        break;
                    case R.id.Temp:
                        setFrag(2);
                        break;
                }
                return true;

            }
        });
        //////////////////// 불러올 fragment 들에 값 넣어주기!!!
        profileFragment = new ProfileFragment();
        postFragment = new PostFragment(); // resultId, nickname
        tmpFragment = new TmpFragment();

        // defualt fragment = Post
        setFrag(1);
    }

    /////////////////////////////// 분기 나누기!!!
    // Replace Fragment
    public void setFrag(int n){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        switch(n){
            case 0 :    // My profile
                fragmentTransaction.replace(R.id.main_frame, profileFragment); // repalce
                fragmentTransaction.commit();
                break;

            case 1 :    // 게시판 Fragment
                fragmentTransaction.replace(R.id.main_frame, postFragment);
                fragmentTransaction.commit();
                break;

            case 2 :    ///////////////////////// 미정 !
                fragmentTransaction.replace(R.id.main_frame, tmpFragment);
                fragmentTransaction.commit();
                break;
        }
    }
}