package com.jackrutorial.test1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jackrutorial.test1.Adapter.BulletinBoardAdapter;
import com.jackrutorial.test1.Data.BulletenBoardData;

import java.util.ArrayList;

public class FirstBulletinBoard extends AppCompatActivity {
    BulletinBoardAdapter adapter;
    ListView listView;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_bulletin_board);
        button = (ImageButton) findViewById(R.id.firstBulletinButton);
        listView = (ListView) findViewById(R.id.BulletinFirstListView);
//////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<BulletenBoardData> list = new ArrayList<>();
        BulletenBoardData as = new BulletenBoardData();
        as.setTitle("11111111111");
        as.setMainText("12325464758697564542312324567");
        BulletenBoardData as1 = new BulletenBoardData();
        as1.setTitle("1");
        as1.setMainText("1");
        BulletenBoardData as2 = new BulletenBoardData();
        as2.setTitle("1");
        as2.setMainText("1");
        BulletenBoardData as3 = new BulletenBoardData();
        as3.setTitle("1");
        as3.setMainText("1");
        BulletenBoardData as4 = new BulletenBoardData();
        as4.setTitle("1");
        as4.setMainText("1");
        BulletenBoardData as5 = new BulletenBoardData();
        as5.setTitle("1");
        as5.setMainText("1");
        list.add(as);
        list.add(as1);
        list.add(as2);
        list.add(as3);
        list.add(as4);
        list.add(as5);
//////////////////////////////////////////////////////////////////////////////////////////
        adapter = new BulletinBoardAdapter(list);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }
}