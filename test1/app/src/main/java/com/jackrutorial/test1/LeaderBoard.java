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
        first.setId("장병규");
        first.setNum(String.valueOf(1));
        LeaderBoardData second = new LeaderBoardData();
        second.setId("ENTPLOve");
        second.setNum(String.valueOf(2));
        LeaderBoardData third = new LeaderBoardData();
        third.setId("LOVEME");
        third.setNum(String.valueOf(3));
        LeaderBoardData fourth = new LeaderBoardData();
        fourth.setId("LONELY");
        fourth.setNum(String.valueOf(4));
        LeaderBoardData fifth = new LeaderBoardData();
        fifth.setId("Happy");
        fifth.setNum(String.valueOf(5));
        LeaderBoardData six = new LeaderBoardData();
        six.setId("MABCAMP");
        six.setNum(String.valueOf(6));
        LeaderBoardData seven = new LeaderBoardData();
        seven.setId("istp");
        seven.setNum(String.valueOf(7));
        LeaderBoardData seven_1 = new LeaderBoardData();
        seven_1.setId("COFFEE");
        seven_1.setNum(String.valueOf(8));
        LeaderBoardData seven_12 = new LeaderBoardData();
        seven_12.setId("ENFP");
        seven_12.setNum(String.valueOf(9));
        LeaderBoardData seven_123 = new LeaderBoardData();
        seven_123.setId("장병규love");
        seven_123.setNum(String.valueOf(10));
        LeaderBoardData seven_1234 = new LeaderBoardData();
        seven_1234.setId("MAD병규");
        seven_1234.setNum(String.valueOf(11));
        LeaderBoardData seven_1235 = new LeaderBoardData();
        seven_1235.setId("사랑해요");
        seven_1235.setNum(String.valueOf(12));
        LeaderBoardData seven_1236 = new LeaderBoardData();
        seven_1236.setId("LOvePLease");
        seven_1236.setNum(String.valueOf(13));
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