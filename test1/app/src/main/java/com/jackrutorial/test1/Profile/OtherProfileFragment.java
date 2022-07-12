package com.jackrutorial.test1.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.jackrutorial.test1.Chatting.ChatActivity;
import com.jackrutorial.test1.R;
import com.jackrutorial.test1.ThirdBulletinBoard;

import org.json.JSONException;
import org.json.JSONObject;


public class OtherProfileFragment extends Fragment {

    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";
    View view;
    ImageView profile_other;
    private Context context;
    // view 변수들

    TextView profile_email;
    TextView profile_name, profile_comment;
    TextView profile_career1,profile_career2,profile_career3, profile_career4, profile_career5, profile_group1, profile_group2, profile_group3;
    /////////// textview

    ImageView profile_dm_btn;

    // bundle로 받아올 값들
     String my_nickname, other_nickname;

    public OtherProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments(); // detailPostFrag 에서 넘겨준 bundle 받아옴 (user name)
        my_nickname = bundle.getString("my_nickname");
        other_nickname = bundle.getString("other_nickname");

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_other_profile, container, false);
        profile_other = (ImageView) view.findViewById(R.id.profile_other);
        profile_career1 = (TextView) view.findViewById(R.id.profile_career1);
        profile_career2 = (TextView) view.findViewById(R.id.profile_career2);
        profile_career3 = (TextView) view.findViewById(R.id.profile_career3);
        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_email = (TextView) view.findViewById(R.id.profile_email);
        profile_comment = (TextView) view.findViewById(R.id.profile_comment);
        profile_group1 = (TextView) view.findViewById(R.id.profile_group1);
        profile_dm_btn = (ImageButton) view.findViewById(R.id.profile_dm_btn) ;

        // dm btn
        profile_dm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("my_nickname", my_nickname);
                intent.putExtra("other_nickname", other_nickname);
                startActivity(intent);
            }
        });


        context = container.getContext();

        request(other_nickname);
        return view;
    }

    public void request(String nickname){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = localhost + "/get_profile";

        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            testjson.put("nickname", nickname);
            String jsonString = testjson.toString(); //완성된 json 포맷

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
            // 이부분 fragment로 바꾸면 오류뜰텐데
            // getApplicationContext() -> getContext().getApplicationContext()으로 바꾸면 됨.

            System.out.println("zxcvasfsfasdfsdf");
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,testjson, new com.android.volley.Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("데이터전송 성공");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        String nickname = jsonObject.getString("nickname");
                        String profile_img = jsonObject.getString("profile_img");
                        String phone_number = jsonObject.getString("phone_number");
                        String email = jsonObject.getString("email");
                        String stack = jsonObject.getString("stack");
                        String one_talk = jsonObject.getString("one_talk");
                        String careerCnt = jsonObject.getString("carrerCnt");
                        String career1 = jsonObject.getString("career1");
                        String career2 = jsonObject.getString("career2");
                        String career3 = jsonObject.getString("career3");
                        String career4 = jsonObject.getString("career4");
                        String career5 = jsonObject.getString("career5");
                        System.out.println("ㅇㄹㅇㄹㅇㄹㅇㄹ");
                        System.out.println(profile_other);
                        if (!profile_img.equals("")){ // 이미지 url이 존재한다면
                            // todo
                            // url에서 이미지 불러오기
                            Glide.with(getActivity()).load(profile_img).into(profile_other);
                        }

                        System.out.println("ㅇㄹㅇㄹㅇㄹㅇㄹ");
                        System.out.println(nickname);
                        System.out.println(profile_name);
                        profile_name.setText(nickname);
                        profile_email.setText(email);
                        profile_comment.setText(one_talk);
                        profile_group1.setText(stack);


                        int num_career = Integer.parseInt(careerCnt);
                        System.out.println(num_career);
                        System.out.println("sakdmskmdksmdksmdkm");
                        if (num_career == 1){
                            profile_career1.setText(career1); //경력에 추가
                        }else if (num_career == 2){
                            profile_career1.setText(career1);
                            profile_career2.setText(career2);
                        }else if (num_career == 3){
                            profile_career1.setText(career1);
                            profile_career2.setText(career2);
                            profile_career2.setText(career3);
                        }else if (num_career == 4){
                            profile_career1.setText(career1);
                            profile_career2.setText(career2);
                            profile_career2.setText(career3);
                        }else if (num_career == 5){
                            profile_career1.setText(career1);
                            profile_career2.setText(career2);
                            profile_career2.setText(career3);
                        }
                        System.out.println("main nickname nickname nickname nickname nickname");
                        System.out.println(nickname);
                    } catch (Exception e) {
                        System.out.println("에러발생에러발생에러발생에러발생에러발생에러발생");
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("뭔가 이상해요");
                    error.printStackTrace();
                    System.out.println(error);
                    System.out.println("오아아ㅘ아ㅏ아아ㅏㅏㅇㅇㅇㅇㅇㅇㅇㅇ앙");

                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            //
        } catch (JSONException e) {
            System.out.println("에러발생에러발생에러발생에러발생에러발생에러발생12312312312321");
            e.printStackTrace();
        }
    }
}