package com.jackrutorial.test1.Profile;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jackrutorial.test1.R;


public class ProfileFragment extends Fragment {

    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";
    View view;
    private Context context;
    // view 변수들
    TextView profile_name, profile_comment;
    TextView profile_career1,profile_career2,profile_career3, profile_group1, profile_group2, profile_group3;
    /////////// textview

    // bundle로 받아올 값들
     String nickname;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments(); // detailPostFrag 에서 넘겨준 bundle 받아옴 (user name)
        nickname = bundle.getString("nickname");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_post, container, false);
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }
}