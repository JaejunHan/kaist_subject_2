package com.jackrutorial.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BoardActivity extends AppCompatActivity {

    // 사용할 변수와 객체 선언
    private BottomNavigationView bottomNavigationView;
    String resultId, nickname;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    // BoardActivity 에서 불러올 fragment
    private ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);
    }
}