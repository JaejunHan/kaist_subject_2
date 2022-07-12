package com.jackrutorial.test1.Post;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackrutorial.test1.R;


public class DetailPostFragment extends Fragment {

    private String localhost = "https://8cd5-192-249-18-214.jp.ngrok.io";
    View view;
    TextView title_tv, content_tv, date_tv, name_tv;
    EditText comment_et;
    Button reg_button;
    ListView listView;
    Integer position;
    String table_id, name, tableOwner;
    ImageView update;

    ///////////////////////
    // 댓글 list, 댓글 어댑


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        /////////////
        return view;
    }
}