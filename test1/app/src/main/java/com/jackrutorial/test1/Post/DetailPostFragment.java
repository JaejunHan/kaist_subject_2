package com.jackrutorial.test1.Post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jackrutorial.test1.Adapter.CommentAdapter;
import com.jackrutorial.test1.Adapter.PreviewAdapter;
import com.jackrutorial.test1.Post.BoardActivity;
import com.jackrutorial.test1.Post.PostFragment;

import com.jackrutorial.test1.Data.Preview;
import com.jackrutorial.test1.Data.Comment;
import com.jackrutorial.test1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


public class DetailPostFragment extends Fragment {

    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";
    View view;
    private Context context;
    TextView title_tv, subtitle_tv, nickname_tv, date_tv;
    TextView content_tv;

    ListView listView;
    EditText comment_et;
    Button comm_reg_button;

    ImageButton edit_btn;

    CommentAdapter commentAdapter;
    Comment new_comment;
    ArrayList<Comment> commentList = new ArrayList<>();

    // bundle 로 받아올 값들
    Integer posting_position;
    String posting_title, posting_subtitle,posting_content, posting_date, posting_userName, curr_userName;

    ///////////////////////
    // 댓글 list, 댓글 어댑


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        view = inflater.inflate(R.layout.fragment_detail_post, container, false);
        context = container.getContext();


        // Post Fragment에서 게시글 정보 받아오기
        Bundle bundle = getArguments(); // 다른 곳에서 넘겨준 bundle 받아옴~~~
        posting_position = bundle.getInt("position");
        posting_title = bundle.getString("posting_title");
        posting_subtitle = bundle.getString("posting_subtitle");
        posting_content = bundle.getString("posting_content");
        posting_userName = bundle.getString("posting_userName");
        posting_date = bundle.getString("posting_date");
        curr_userName = bundle.getString("curr_userName");

        // 글 정보 view
        title_tv = view.findViewById(R.id.title_tv);
        subtitle_tv = view.findViewById(R.id.subtitle_tv);
        nickname_tv = view.findViewById(R.id.nickname_tv);
        date_tv = view.findViewById(R.id.date_tv);

        // 글 내용 view
        content_tv = view.findViewById(R.id.content_tv);

        // 댓글 등록 view
        listView = view.findViewById(R.id.listView);
        comment_et = view.findViewById(R.id.comment_et);
        comm_reg_button = view.findViewById(R.id.comm_reg_button);

        // 해당 user의 nickname이 해당 글의 nickname 과 같을 때만 보이도록
        edit_btn = view.findViewById(R.id.edit_btn);
        if (!curr_userName.equals(posting_userName)) // 일치하지 않다면 (= 내가 쓴글이 아니라면):
            edit_btn.setVisibility(View.GONE);

        ////////////////////////////////////
        // post 정보
        title_tv.setText(posting_title);
        subtitle_tv.setText(posting_subtitle);
        nickname_tv.setText(posting_userName);
        content_tv.setText(posting_content);
        date_tv.setText(posting_date);

        loadComment();

        comm_reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createComment(curr_userName,posting_title, posting_subtitle, comment_et.getText().toString(),"0","20220711", "20220711" );
                loadComment();
            }
        });


        // 댓글 클릭시 해당 user 프로필로 연동가능하도록 ---------
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                System.out.println("프로필로 이동");

                Comment comment = commentList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("nickname", comment.getCommNickname());

                ((BoardActivity)getActivity()).profileFragment.setArguments(bundle);
                ((BoardActivity)getActivity()).setFrag(0);
            }
        });


        // 수정 하기 클릭시 ----------
        view.findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // EDIT Post Fragment 에 전달할 변수들
                Bundle bundle = new Bundle();
                bundle.putString("posting_title",posting_title );
                bundle.putString("posting_subtitle", posting_subtitle);

                ((BoardActivity)getActivity()).setFrag(5); // edit fragment 로 이동
            }
        });



        return view;
    }


    // ------------------------ DB 연동 함수 -----------------------

    public void loadComment(){
        System.out.println("loacComment 로 이동 성공");

        // url 지정
        String url = localhost + "/load_comment";

        // 사용할 json obj 선언
        JSONObject load_comment_json = new JSONObject();

        // Volley 로 전송할 req를 담는 requestQueue 선언
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());


        // Volley 로 전송할 req !
        System.out.println("---------여기서 null request 보내기---------");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, load_comment_json, new Response.Listener<JSONObject>(){

            // response 를 받아오는 listener !
            @Override
            public void onResponse(JSONObject response){
                try{
                    System.out.println("댓글 데이터 rsp 성공~~~₩");

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // string 형태의 json 을 받아와서
                    String jsonWholeArray = jsonObject.getString("jsonArray");

                    //System.out.println("전체 array in string type");
                    System.out.println(jsonWholeArray);

                    jsonCommentParsing(jsonWholeArray); // parsing 해서 preview 클래스와 Adapter에 적용

                }
                catch (Exception e) {
                    System.out.println("@@@@@@@@@@@@@@ RSP ERROR @@@@@@@@@@@@@@");
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley Error 발생ㅠ");
                error.printStackTrace();
                //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    public void createComment(String comment_nickname, String posting_title ,String posting_sub_title ,
                              String contents, String like_cnt, String create_date,String update_date )
    {
        //########### url 지정
        String url = localhost + "/create_comment";

        // 사용할 json obj 선언
        JSONObject comment_json = new JSONObject();
        try{
            // comment_json 을 통해 데이터 전달
            comment_json.put("comment_nickname", comment_nickname);
            comment_json.put("posting_title", posting_title);
            comment_json.put("posting_sub_title", posting_sub_title);
            comment_json.put("contents", contents);
            comment_json.put("like_cnt", like_cnt);
            comment_json.put("create_date", create_date);
            comment_json.put("update_date", update_date);

            // Volley로 전송 ~~~~!
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

            System.out.println("---------여기서 request 수행---------");
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, comment_json, new Response.Listener<JSONObject>() {

                // rqst 후 rsp 리스너
                @Override
                public void onResponse(JSONObject response) { // 서버에서 rsp
                    try{
                        System.out.println("---------데이터 전송 성공---------");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        // 받아온 응답을 key 에 따라 value 로 받아옴
                        //////////////// Response TRUE/FALSE
                        Boolean create_ok = jsonObject.getBoolean("create_ok");
                        if (create_ok){  // Post 됨
                            String text = "댓글이 작성되었습니다.";
                            Toast toast;
                            int duration = Toast.LENGTH_SHORT;
                            toast = Toast.makeText(context, text, duration);
                            toast.show();

                            Intent intent = new Intent(context, DetailPostFragment.class); // 글이 작성되면
                            intent.putExtra("nickname", comment_nickname);
                            intent.putExtra("contents", contents);
                            startActivity(intent);
                        }
                        else{
                            String text = "댓글 작성에 실패하였습니다.";
                            Toast toast;
                            int duration = Toast.LENGTH_SHORT;
                            toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }



                    }
                    catch(Exception e){
                        System.out.println("@@@@@@@@@@@@@@ RSP ERROR @@@@@@@@@@@@@@");
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Volley Error 발생ㅠ");
                    error.printStackTrace();
                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);

        } catch (JSONException e) {
            System.out.println("에러발생에러발생에러발생에러발생에러발생에러발생12312312312321");
            e.printStackTrace();
        }


    }


    private void jsonCommentParsing(String jsonStrData){  // 입력으로 string 형태의 json을 받아 온 후 array 로 변환 후 jsonObj로 파싱해주기
        try{
            JSONArray jsonArr = new JSONArray(jsonStrData);
            commentList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);
//                System.out.println("jsonObj를 출력");
//                System.out.println(jsonObj);

                //String comment_id = (String)jsonObj.get("comment_id");
                String comment_nickname = (String)jsonObj.get("comment_nickname");
                String posting_title = (String)jsonObj.get("posting_title");

                String posting_sub_title = (String)jsonObj.get("posting_sub_title");
                String contents = (String)jsonObj.get("contents");
                String like_cnt = (String)jsonObj.get("like_cnt");
                String create_date = (String)jsonObj.get("create_date");
                String update_date = (String)jsonObj.get("update_date");

                System.out.println("jsonObj 에서 data를 받아옴");

                ////////////// image 도 추가해주기 //////////////

                new_comment = new Comment();

                //new_comment.setCommId(comment_id);
                new_comment.setCommNickname(comment_nickname);
                new_comment.setCommTitle(posting_title);
                new_comment.setCommSubtitle(posting_subtitle);
                new_comment.setCommContent(contents);
                new_comment.setCommTime(create_date);
                new_comment.setCommUpdateTime(update_date);

                System.out.println("comment 클래스에 값을 넣음");
                System.out.println(new_comment.getCommContent());

                try{
                    commentList.add(new_comment);
                    System.out.println(commentList);

                }catch(NullPointerException n){
                    System.out.println("널 포인터...");
                    n.printStackTrace();
                }

                commentAdapter = new CommentAdapter(getContext(), commentList);
                listView.setAdapter(commentAdapter);
            }


        }catch (JSONException e) {
            System.out.println("json parsing 에서 오류");
            e.printStackTrace();
        }
    }

}