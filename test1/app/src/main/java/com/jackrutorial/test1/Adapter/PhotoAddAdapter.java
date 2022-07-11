package com.jackrutorial.test1.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jackrutorial.test1.R;

import java.util.ArrayList;
import java.util.List;

public class PhotoAddAdapter extends BaseAdapter {

    List<Integer> imageList;

    public PhotoAddAdapter(ArrayList<Integer> data){
        imageList = data;
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageList.get(i);
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
            convertView = inflater.inflate(R.layout.show_photo, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.gridImageView);

        imageView.setImageResource(imageList.get(position));
        return convertView;
    }
}
