package com.jackrutorial.test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jackrutorial.test1.Data.LeaderBoardData;
import com.jackrutorial.test1.LeaderBoard;
import com.jackrutorial.test1.R;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardListViewAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    List<LeaderBoardData> leaderBoardAdapter;

    public LeaderBoardListViewAdapter(ArrayList<LeaderBoardData> data){
        leaderBoardAdapter = data;
    }

    @Override
    public int getCount() {
        return leaderBoardAdapter.size();
    }

    @Override
    public Object getItem(int i) {
        return leaderBoardAdapter.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.leaderboard, parent, false);
        }


        TextView leaderboardNum = (TextView)convertView.findViewById(R.id.leaderboardNum);
        TextView leaderboardId = (TextView)convertView.findViewById(R.id.leaderboardId);

        leaderboardNum.setText(leaderBoardAdapter.get(position).getNum());
        leaderboardId.setText(leaderBoardAdapter.get(position).getId());

        return convertView;
    }
}
