package com.jackrutorial.test1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class WritingPostFragment extends Fragment {
    private View view;
    private String nickname;
    EditText new_title, new_content;

    private String localhost = "https://e805-192-249-19-234.jp.ngrok.io";

    public WritingPostFragment(String name){
        this.nickname = nickname;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_writing_post, container, false);

        new_title = view.findViewById(R.id.new_title);
        new_content = view.findViewById(R.id.new_content);

        ///////////////// DB

        // 작성 버튼 클릭 리스너
        view.findViewById(R.id.posting_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////// data 등록하는거 구현
                String title = new_title.getText().toString();
                String content = new_content.getText().toString();

                postRequest(title, content);

                /////////
                ((BoardActivity) getActivity()).setFrag(3); // 다시 게시글 list 로 돌아감
            }
        });


        return view;
    }

    public void postRequest(String id, String password){

    }
}