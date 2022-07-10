package com.jackrutorial.test1.Post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jackrutorial.test1.Adapter.ListViewAdapter;
import com.jackrutorial.test1.Data.Preview;
import com.jackrutorial.test1.Post.BoardActivity;
import com.jackrutorial.test1.R;

import org.json.JSONObject;

import java.util.List;

public class PostFragment extends Fragment {

    /////////// 객체 내 변수와 내부 객체들
    private String resultId, nickname;
    private String localhost = "https://ae25-192-249-18-214.jp.ngrok.io";
    private View view;
    ListViewAdapter previewAdapter;
    public static List<Preview> previewList;
    public ListView listView;
    private Context context;
    ///////// recycler view ??

    public PostFragment(String resultId, String nickname){
        this.resultId = resultId;
        this.nickname = nickname;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_post, container, false);
        listView = (ListView) view.findViewById(R.id.post_listview);
        context = container.getContext();

        getResponse();

        // ---------------------
        // 게시글 클릭 리스너 -> detail fragment 로 이동 /////////////////// 하는 거 구현하기 (지금은 toast만)
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                // 토스트 메세지
                Toast.makeText(getActivity(), adapterView.getItemAtPosition(position) + " 클릭", Toast.LENGTH_SHORT).show();
            }
        });


        // ---------------------
        ///////// 게시글 longClick 시 삭제 : call back 함수로 adapter listener 필요
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l){
                Log.d("LONG CLICK", "OnLongClickListener");
                // 해당 게시물 정보
                Preview preview = previewList.get(position);
                Log.i("체크1", "print"+preview.getName());
                Log.i("체크2", "print"+nickname);
                if (preview.getName().equals(nickname))  // 게시글의 작성자와 현재 접속한 user가 같은지 체크
                    deleteNoticeDialog(preview);
                return true;
            }
        });


        // ---------------------
        // 글 작성 버튼 : 글 작성 fragment로 넘어가도록
        view.findViewById(R.id.fab_write).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((BoardActivity) getActivity()).setFrag(3);
            }
        });
        return view;
    }


    // ---------------------Volley Request 수행 함수들 ---------------------
    ////////// send req to Volley and get response to read Posting info ////////////
    public void getResponse(){
        // url 지정
        String url = localhost + "/read_post";
        System.out.println("게시판전체조회게시판전체조회게시판전체조회게시판전체조회게시판전체조회");

        // 사용할 json obj 선언
        JSONObject readjson = new JSONObject();

        // Volley 로 전송할 req를 담는 requestQueue 선언
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());


        // Volley 로 전송할 req !
        System.out.println("---------여기서 request 수행 PostFragment---------");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, readjson, new Response.Listener<JSONObject>(){

            // response 를 받아오는 listener !
            @Override
            public void onResponse(JSONObject response){
                try{
                    System.out.println("데이터전송 성공 PostFragment");

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());

                    //key값에 따라 value값을 쪼개 받아옵니다.
                    String nickname = jsonObject.getString("jsonArray");
                    String title = jsonObject.getString("title");
                    String sub_title = jsonObject.getString("sub_title");
                    String content = jsonObject.getString("content");
                    String score = jsonObject.getString("score");
                    String create_date = jsonObject.getString("create_date");
                    String update_date = jsonObject.getString("update_date");

                    ////////////// image 도 추가해주기 //////////////
                }
                catch (Exception e) {
                    System.out.println("@@@@@@@@@@@@@@ RSP ERROR PostFragment @@@@@@@@@@@@@@");
                    e.printStackTrace();
                }
            }

            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley Error 발생ㅠ PostFragment");
                error.printStackTrace();
                //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjectRequest);
    }

    private void deleteNoticeDialog(Preview preview){
        AlertDialog.Builder del_dialog = new AlertDialog.Builder(getContext(),
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        del_dialog.setMessage("삭제하겠습니까?")
                .setTitle("게시물 삭제")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Log.i("Dialog", "취소");
                    }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        deleteRequest(preview);
                        ((BoardActivity) getActivity()).setFrag(0); // 다시 Board list 로 돌아가기
                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    public void deleteRequest(Preview preview){
        previewList.remove(preview);

        // url 지정
        String url = localhost + "/delete_post";

        // Volley 로 전송할 req를 담는 requestQueue 선언
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

        // Volley 로 전송할 req !
        System.out.println("---------여기서 request 수행---------");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, new Response.Listener<JSONObject>(){

            // callback
            // response 를 받아오는 listener !
            @Override
            public void onResponse(JSONObject response){
                Toast.makeText(context, "삭제", Toast.LENGTH_SHORT).show();

//                try{
//                    System.out.println("데이터전송 성공");
//
//                    //받은 json형식의 응답을 받아
//                    JSONObject jsonObject = new JSONObject(response.toString());
//
//                    //key값에 따라 value값을 쪼개 받아옵니다.
//                    String nickname = jsonObject.getString("nickname");
//                    String title = jsonObject.getString("title");
//                    String sub_title = jsonObject.getString("sub_title");
//                    String content = jsonObject.getString("content");
//                    String score = jsonObject.getString("score");
//                    String create_date = jsonObject.getString("create_date");
//                    String update_date = jsonObject.getString("update_date");
//
//                    ////////////// image 도 추가해주기 //////////////
//                }
//                catch (Exception e) {
//                    System.out.println("@@@@@@@@@@@@@@ RSP ERROR @@@@@@@@@@@@@@");
//                    e.printStackTrace();
//                }
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
}