package com.jackrutorial.test1.BulletinBoard;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jackrutorial.test1.R;

public class BullentinBoardFragment extends Fragment {
    View view;

    Button button1;
    Button button2;
    Button button3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_bulletin_board, container, false);
        button1 = view.findViewById(R.id.buttonFirst);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FirstBulletinBoard.class);
                startActivity(intent);
            }
        });
        button2 = view.findViewById(R.id.buttonSecond);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SecondBulletinBoard.class);
                startActivity(intent);
            }
        });
        button3 = view.findViewById(R.id.buttonThird);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ThirdBulletinBoard.class);
                startActivity(intent);
            }
        });
        return view;
    }
}