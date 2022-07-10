package com.jackrutorial.test1;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.jackrutorial.test1.Adapter.LeaderBoardListViewAdapter;
import com.jackrutorial.test1.Data.LeaderBoardData;

import java.util.ArrayList;

public class LeaderBoard extends AppCompatActivity {
    LeaderBoardListViewAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        listView = (ListView) findViewById(R.id.leaderboardListView);

        ArrayList<LeaderBoardData> list = new ArrayList<>();
        LeaderBoardData first = new LeaderBoardData();
        first.setId("오렌지");
        first.setNum(String.valueOf(1));
        LeaderBoardData second = new LeaderBoardData();
        second.setId("망고");
        second.setNum(String.valueOf(2));
        LeaderBoardData third = new LeaderBoardData();
        third.setId("망고");
        third.setNum(String.valueOf(2));
        LeaderBoardData fourth = new LeaderBoardData();
        fourth.setId("망고");
        fourth.setNum(String.valueOf(2));
        LeaderBoardData fifth = new LeaderBoardData();
        fifth.setId("망고");
        fifth.setNum(String.valueOf(2));
        LeaderBoardData six = new LeaderBoardData();
        six.setId("망고");
        six.setNum(String.valueOf(2));
        LeaderBoardData seven = new LeaderBoardData();
        seven.setId("망고");
        seven.setNum(String.valueOf(2));
        LeaderBoardData seven_1 = new LeaderBoardData();
        seven_1.setId("망고");
        seven_1.setNum(String.valueOf(2));
        LeaderBoardData seven_12 = new LeaderBoardData();
        seven_12.setId("망고");
        seven_12.setNum(String.valueOf(2));
        LeaderBoardData seven_123 = new LeaderBoardData();
        seven_123.setId("망고");
        seven_123.setNum(String.valueOf(2));
        LeaderBoardData seven_1234 = new LeaderBoardData();
        seven_1234.setId("망고");
        seven_1234.setNum(String.valueOf(2));
        LeaderBoardData seven_1235 = new LeaderBoardData();
        seven_1235.setId("망고");
        seven_1235.setNum(String.valueOf(2));
        LeaderBoardData seven_1236 = new LeaderBoardData();
        seven_1236.setId("망고");
        seven_1236.setNum(String.valueOf(2));
        list.add(first);
        list.add(second);
        list.add(fourth);
        list.add(fifth);
        list.add(six);
        list.add(seven);
        list.add(seven_1);
        list.add(seven_12);
        list.add(seven_123);
        list.add(seven_1234);
        list.add(seven_1235);
        list.add(seven_1236);

        adapter = new LeaderBoardListViewAdapter(list);
        listView.setAdapter(adapter);
    }
}