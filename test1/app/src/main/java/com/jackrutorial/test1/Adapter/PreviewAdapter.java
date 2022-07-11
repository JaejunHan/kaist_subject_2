package com.jackrutorial.test1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jackrutorial.test1.Data.Preview;
import com.jackrutorial.test1.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class PreviewAdapter extends BaseAdapter{

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    List<Preview> previewList;

    public PreviewAdapter(Context context, ArrayList<Preview> data) {
        mContext = context;
        previewList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return previewList.size();
    }

    @Override
    public Object getItem(int position) {
        return previewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // inflator를 통해 각 View 들을 객체화, 데이터 값 지정
        View view = mLayoutInflater.inflate(R.layout.listview_preview, null);

        TextView previewTitle = (TextView)view.findViewById(R.id.preview_title);
        TextView previewSubtitle = (TextView)view.findViewById(R.id.preview_subtitle);
        TextView previewScore = (TextView)view.findViewById(R.id.preview_bounty);

        previewTitle.setText(previewList.get(position).getTitle());
        previewSubtitle.setText(previewList.get(position).getSubtitle());
        previewScore.setText(previewList.get(position).getScore());

        return view;
    }

}
