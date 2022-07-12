package com.jackrutorial.test1.Post;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jackrutorial.test1.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditPostFragment extends Fragment {

    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";
    private View view;
    //private String name, tableId;
    private ListView listView;
    private Context context;
    EditText edit_title, edit_subtitle,edit_content;
    String berfore_title, berfore_subtitle;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_post, container, false);
        context = container.getContext();

        // 수정해서 업뎃할 내용할 -------
        edit_title = view.findViewById(R.id.edit_title);
        edit_subtitle = view.findViewById(R.id.edit_subtitle);
        edit_content = view.findViewById(R.id.edit_content);

        view.findViewById(R.id.post_edit_btn).setOnClickListener(new View.OnClickListener() { // 수정하기를 눌러 db로 전송시
            @Override
            public void onClick(View v) {
                String after_title = edit_title.getText().toString();
                String after_subtitle = edit_subtitle.getText().toString();
                String after_content = edit_content.getText().toString();

                // 날짜
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                String dateStr = dateFormat.format(date);


                Bundle bundle = getArguments(); // 다른 곳에서 넘겨준 bundle 받아옴
                berfore_title = bundle.getString("position_title");
                berfore_subtitle = bundle.getString("posting_subtitle");

                // (berfore_title, berfore_subtitle) 을 수정할 게시물의 pk로 사용하여 조회하기
                editRequest("test_nickname", berfore_title, berfore_subtitle, after_title, after_subtitle, after_content, "0", "50",  dateStr);
                ((BoardActivity) getActivity()).setFrag(1);  // 게시판 화면으로 돌아감
            }
        });

        return view;
    }

    public void editRequest(String nickname, String berfore_title, String berfore_subtitle, String title ,String subtitle ,String content, String imgCnt, String score, String date){
        //########### url 지정
        String url = localhost + "/edit_post";

        // 사용할 json obj 선언
        JSONObject editjson = new JSONObject();
        try{
            // editjson  을 통해 데이터 전달
            // (berfore_title, berfore_subtitle) 을 수정할 게시물의 pk로 사용하여 조회하기 -> update 쿼리 수행
            editjson.put("nickname", nickname);
            editjson.put("title", title);
            editjson.put("sub_title", subtitle);
            editjson.put("contents", content);
            editjson.put("imgCnt", imgCnt);
            editjson.put("update_date", date);
            editjson.put("score", score);

            // Volley로 전송 ~~~~!
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

            System.out.println("---------여기서 edit request 수행---------");
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, editjson, new Response.Listener<JSONObject>() {


                // rqst 후 rsp 리스너
                @Override
                public void onResponse(JSONObject response) { // 서버에서 rsp
                    try{
                        System.out.println("---------수정 데이터 전송 성공---------");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        // 받아온 응답을 key 에 따라 value 로 받아옴
                        //////////////// Response TRUE/FALSE
                        Boolean edit_ok = jsonObject.getBoolean("edit_ok");
                        if (edit_ok){  // Post 됨
                            String text = "글이 수정되었습니다.";
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
                            String text = "글 수정에 실패하였습니다.";
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