package com.jackrutorial.test1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jackrutorial.test1.Adapter.ListViewAdapter;
import com.jackrutorial.test1.Data.Preview;

import java.util.List;

public class PostFragment extends Fragment {

    /////////// 객체 내 변수와 내부 객체들
    private String resultId, nickname;
    private String localhost = "https://e805-192-249-19-234.jp.ngrok.io";
    private View view;
    ListViewAdapter previewAdapter;
    public static List<Preview> previewList;
    public ListView listView;
    ///////// recylcer view ??

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
    }

    public PostFragment(String resultId, String nickname){
        this.resultId = resultId;
        this.nickname = nickname;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_post, container, false);
        listView = (ListView) view.findViewById(R.id.post_listview);


        /////////////////// DB connection

        // 게시글 클릭 리스너 -> detail fragment 로 이동 /////////////////// 하는 거 구현하기 (지금은 toast만)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                // 토스트 메세지
                Toast.makeText(getActivity(), adapterView.getItemAtPosition(position) + " 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        ///////// 게시글 longClick 삭제 : call back 함수로 adapter listener 필요
        //listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){});

        // 글 작성 버튼 : 글 작성 fragment로 넘어가도록
        view.findViewById(R.id.fab_write).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((BoardActivity) getActivity()).setFrag(3);
            }
        });



        return view;

    }
}