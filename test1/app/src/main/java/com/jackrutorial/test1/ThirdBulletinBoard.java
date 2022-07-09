package com.jackrutorial.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.jackrutorial.test1.Adapter.BulletinBoardAdapter;
import com.jackrutorial.test1.Data.BulletenBoardData;

import java.util.ArrayList;

public class ThirdBulletinBoard extends AppCompatActivity {
    BulletinBoardAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_bulletin_board);

        listView = (ListView) findViewById(R.id.BulletinThirdListView);
//////////////////////////////////////////////////////////////////////////////////////////
        ArrayList<BulletenBoardData> list = new ArrayList<>();
        BulletenBoardData as = new BulletenBoardData();
        as.setTitle("333333333");
        as.setMainText("123254647586975645423123245673565456545654356543245654345654");
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
    }
}