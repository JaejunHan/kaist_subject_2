package com.jackrutorial.test1.Post;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jackrutorial.test1.Adapter.PhotoAddAdapter;
import com.jackrutorial.test1.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class
WritingPostFragment extends Fragment {
    private View view;
    private String nickname;
    EditText new_title, new_subtitle, new_content;
    private Context context;
    Button button;

    GridView gridView;
    PhotoAddAdapter adapter;
    int numPhoto = 0;

    public static void main(String[] args) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String dateToStr = dateFormat.format(date);
    }

    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";

    public WritingPostFragment(String name){
        this.nickname = nickname;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_writing_post, container, false);
        context = container.getContext();

        new_title = view.findViewById(R.id.new_title);
        new_subtitle = view.findViewById(R.id.new_subtitle);
        new_content = view.findViewById(R.id.new_content);
        button = view.findViewById(R.id.addPhotoButton);

        gridView = (GridView) view.findViewById(R.id.addPhoto);
        ArrayList<Integer> list = new ArrayList<>();

        // 작성 버튼 클릭 리스너
        view.findViewById(R.id.posting_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////// data 등록하는거 구현
                String title = new_title.getText().toString();
                String subtitle = new_subtitle.getText().toString();
                String content = new_content.getText().toString();

                postRequest("test_id", "test_nickname", title, subtitle, content, "0", "50");

                /////////
                //((BoardActivity) getActivity()).setFrag(3); // 다시 게시글 list 로 돌아감
            }
        });
        //사진추가
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo
                // list 자료형 integer로 되어있음 수정 필요
                if(numPhoto < 5){
                    list.add(R.drawable.delicious);
                    adapter.notifyDataSetChanged();
                }
                numPhoto = numPhoto + 1;
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                list.remove(i);
                return true;
            }
        });
        adapter = new PhotoAddAdapter(list);
        gridView.setAdapter(adapter);

        ((BoardActivity) getActivity()).setFrag(3); // 다시 게시글 list 로 돌아감
        return view;
    }







    public void postRequest(String nickname, String title ,String subtitle ,String content, String imgCnt, String score, String date){
        //########### url 지정
        String url = localhost + "/write_post";

        // 사용할 json obj 선언
        JSONObject writejson = new JSONObject();
        try{
            // writejson 을 통해 데이터 전달
            //writejson.put("user_id", user_id);
            writejson.put("nickname", nickname);
            writejson.put("title", title);
            writejson.put("sub_title", subtitle);
            writejson.put("contents", content);
            writejson.put("imgCnt", imgCnt);
            writejson.put("create_date", date);
            writejson.put("score", score);

            // Volley로 전송 ~~~~!
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

            System.out.println("---------여기서 request 수행---------");
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, writejson, new Response.Listener<JSONObject>() {


                // rqst 후 rsp 리스너
                @Override
                public void onResponse(JSONObject response) { // 서버에서 rsp
                    try{
                        System.out.println("---------데이터 전송 성공---------");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        // 받아온 응답을 key 에 따라 value 로 받아옴
                        //////////////// Response TRUE/FALSE
                        Boolean insert_ok = jsonObject.getBoolean("insert_ok");
                        if (insert_ok){  // Post 됨
                            String text = "글이 작성되었습니다.";
                            Toast toast;
                            int duration = Toast.LENGTH_SHORT;
                            toast = Toast.makeText(context, text, duration);
                            toast.show();
                            Intent intent = new Intent(context, BoardActivity.class); // 글이 작성되면
                            //intent.putExtra("resultId", user_id);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                        }
                        else{
                            String text = "글 작성에 실패하였습니다.";
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
}