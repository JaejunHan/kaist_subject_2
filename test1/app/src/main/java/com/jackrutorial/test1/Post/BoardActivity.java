package com.jackrutorial.test1.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jackrutorial.test1.Profile.ProfileFragment;
import com.jackrutorial.test1.R;
import com.jackrutorial.test1.TmpFragment;

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
    WritingPostFragment writingPostFragment;
    DetailPostFragment detailPostFragment;


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
        postFragment = new PostFragment(resultId, nickname); // resultId, nickname
        tmpFragment = new TmpFragment();
        writingPostFragment = new WritingPostFragment(nickname);
        detailPostFragment = new DetailPostFragment();

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

            case 3 :    // 글 등록 구현하기~~~!
                fragmentTransaction.replace(R.id.main_frame, writingPostFragment);
                fragmentTransaction.commit();
                break;

            case 4 :    // 글 세부 정보 보여주는 fragment
                fragmentTransaction.replace(R.id.main_frame, detailPostFragment);
                fragmentTransaction.commit();
                break;

        }
    }
}