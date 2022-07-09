package com.jackrutorial.test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jackrutorial.test1.Data.BulletenBoardData;
import com.jackrutorial.test1.R;

import java.util.ArrayList;
import java.util.List;

public class BulletinBoardAdapter extends BaseAdapter {

    List<BulletenBoardData> bulletenBoardData;

    public BulletinBoardAdapter(ArrayList<BulletenBoardData> data){
        bulletenBoardData = data;
    }
    @Override
    public int getCount() {
        return bulletenBoardData.size();
    }

    @Override
    public Object getItem(int i) {
        return bulletenBoardData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bulletinboardlayout, parent, false);
        }


        TextView boardTitle = (TextView)convertView.findViewById(R.id.bulletinTitle);
        TextView boardMainText = (TextView)convertView.findViewById(R.id.bulletinText);

        boardTitle.setText(bulletenBoardData.get(position).getTitle());
        boardMainText.setText(bulletenBoardData.get(position).getMainText());

        return convertView;
    }
}
