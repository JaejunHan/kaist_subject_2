package com.jackrutorial.test1.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jackrutorial.test1.Data.Comment;
import com.jackrutorial.test1.R;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends BaseAdapter {

    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    List<Comment> commentList;

    TextView commentUserName, commentContent;

    public CommentAdapter(Context context, ArrayList<Comment> data) {
        mContext = context;
        commentList = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // inflator를 통해 각 View 들을 객체화, 데이터 값 지정
        View view = mLayoutInflater.inflate(R.layout.comment_list_item, null);

        TextView commentUserName = (TextView) view.findViewById(R.id.comment_username);
        TextView commentContent = (TextView) view.findViewById(R.id.comment_content);

        commentUserName.setText(commentList.get(position).getCommNickname());
        commentContent.setText(commentList.get(position).getCommContent());

        return view;
    }

}
