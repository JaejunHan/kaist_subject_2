package com.jackrutorial.test1.Post;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.jackrutorial.test1.Adapter.PreviewAdapter;
import com.jackrutorial.test1.Data.Preview;
import com.jackrutorial.test1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

public class PostFragment extends Fragment {

    /////////// 객체 내 변수와 내부 객체들
    private String nickname;
    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";
    private View view;
    PreviewAdapter previewAdapter;
    Preview new_prev;
    ArrayList<Preview> previewList = new ArrayList<>();
    public ListView listView;
    private Context context;
    EditText search_bar;
    Button searching_btn;

    Bundle bundle;
    ///////// recycler view ??

    public PostFragment(String resultId, String nickname){
        //this.resultId = resultId;
        System.out.println("여기엥ㅅㅇ여려녈ㄹ");
        System.out.println(nickname);
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

        getResponse(); // db에서 게시글들을 가져옴

        EditText search_bar = view.findViewById(R.id.search_bar);
        Button searching_btn = view.findViewById(R.id.searching_btn);

        // --------------------
        // 검색 버튼 클릭 리스너 : 원하는 결과 검색
        searching_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_title = search_bar.getText().toString();
                searchResponse(search_title);
            }
        });


        // ---------------------
        // 게시글 클릭 리스너 -> detail fragment 로 이동
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                // 토스트 메세지
                //Toast.makeText(getActivity(), adapterView.getItemAtPosition(position) + " 클릭", Toast.LENGTH_SHORT).show();

                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = new Date();
                String dateToStr = dateFormat.format(date);

                // Detail Post Fragment 에 전달할 변수들
                Preview preview = previewList.get(position);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("posting_title", preview.getTitle());
                bundle.putString("posting_subtitle", preview.getSubtitle());
                bundle.putString("posting_content", preview.getContent());
                bundle.putString("posting_date", dateToStr);
                bundle.putString("posting_userName", preview.getName()); // 글의 작성자
                bundle.putString("curr_userName", nickname); // 현재 사용 유저 name

                ((BoardActivity)getActivity()).detailPostFragment.setArguments(bundle); // detail Frag 로 bundle 을 넘겨줍니당
                ((BoardActivity)getActivity()).setFrag(4); // detail Post 로 이동
            }
        });


        // ---------------------
        /////// 게시글 longClick 시 삭제 : call back 함수로 adapter listener 필요
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l){
                Log.d("LONG CLICK", "OnLongClickListener");
                // 해당 게시물 정보
                Preview preview = previewList.get(position);
                Log.i("체크1", "print"+preview.getName());
                Log.i("체크2", "print"+nickname);
                System.out.println(preview.getName().equals(nickname));
                System.out.println("집가고싶어");
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
                Bundle bundle = new Bundle();
                bundle.putString("nickname", nickname);
                ((BoardActivity)getActivity()).writingPostFragment.setArguments(bundle); //
                ((BoardActivity) getActivity()).setFrag(3);
            }
        });
        return view;
    }


    // ---------------------Volley Request 수행 함수들 ---------------------
    ////////// send req to Volley and get response to read Posting info ////////////
    public void searchResponse(String title){
        System.out.println("searchResponse 로 이동 성공");

        // url 지정
        String url = localhost + "/search_post";

        // 사용할 json obj 선언
        JSONObject searchjson = new JSONObject();
        try{
            searchjson.put("title", title);

            // Volley 로 전송할 req를 담는 requestQueue 선언
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());


            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, searchjson, new Response.Listener<JSONObject>() {
                // rqst 후 rsp 리스너
                @Override
                public void onResponse(JSONObject response) { // 서버에서 rsp
                    try{
                        System.out.println("---------데이터 전송 성공---------");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        // string 형태의 json 을 받아와서
                        String jsonWholeArray = jsonObject.getString("jsonArray");
                        System.out.println("여기에요~~~~~~~~~");
                        System.out.println(jsonWholeArray);
                        jsonParsingforSearching(jsonWholeArray); // parsing 해서 preview 클래스와 Adapter에 적용


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

        }catch (JSONException e){
            e.printStackTrace();
            System.out.println("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ");
        }

    }


    public void getResponse(){
        System.out.println("getResponse 로 이동 성공");

        // url 지정
        String url = localhost + "/read_post";

        // 사용할 json obj 선언
        JSONObject readjson = new JSONObject();

        // Volley 로 전송할 req를 담는 requestQueue 선언
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());


        // Volley 로 전송할 req !
        System.out.println("---------여기서 null request 보내기---------");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, readjson, new Response.Listener<JSONObject>(){

            // response 를 받아오는 listener !
            @Override
            public void onResponse(JSONObject response){
                try{
                    System.out.println("검색 데이터 전송 성공!!!!");

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // string 형태의 json 을 받아와서
                    String jsonWholeArray = jsonObject.getString("jsonArray");

                    System.out.println(jsonWholeArray);
                    jsonParsing(jsonWholeArray); // parsing 해서 preview 클래스와 Adapter에 적용

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
                        System.out.println("예예예예예예예예예예예예예예예");
                        deleteRequest(preview);
                        getResponse();
                        ((BoardActivity) getActivity()).setFrag(1); // 다시 Board list 로 돌아가기
                    }
                })
                .setCancelable(false) // 백버튼으로 팝업창이 닫히지 않도록 한다.
                .show();
    }

    public void deleteRequest(Preview preview){
        //previewList.remove(preview);

        // url 지정
        String url = localhost + "/delete_post";
        System.out.println("---------여기에요여가얼나라널--ㅣㅑ----");
        // 사용할 json obj 선언
        JSONObject deljson = new JSONObject();

        try{
            System.out.println("---------여기서 del request 수행1212121212121---------");
            // 삭제 시 필요한 data 들 전달
            deljson.put("nickname", preview.getName());
            deljson.put("title", preview.getTitle());
            deljson.put("sub_title", preview.getSubtitle());

            // Volley 로 전송할 req를 담는 requestQueue 선언
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());

            // Volley 로 전송할 req !
            System.out.println("---------여기서 del request 수행1212121212121---------");
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, deljson, new Response.Listener<JSONObject>(){

                // callback
                // response 를 받아오는 listener !
                @Override
                public void onResponse(JSONObject response){
                    try{
                        System.out.println("삭제할 데이터 전송 완료");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        // delete 되었는지 체크
                        Boolean is_deleted = jsonObject.getBoolean("is_delete");
                        System.out.println("is_deleted: " + is_deleted);
                        if (is_deleted){ // 삭제 됨
                            previewList.remove(preview);
                            Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show();
                        }
                        else{ // 삭제 안됨
                            Toast.makeText(context, "삭제가 되지 않았습니다", Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch(Exception e){
                        System.out.println("delete 할 onResponse Listener 에서 error");
                        e.printStackTrace();
                    }
//
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("삭제 시 Volley Error 발생");
                    error.printStackTrace();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);


        }catch (JSONException e){
            System.out.println("JSon Exception 발생...");
            e.printStackTrace();
        }

    }

    private void jsonParsing(String jsonStrData){  // 입력으로 string 형태olp의 json을 받아 온 후 array 로 변환 후 jsonObj로 파싱해주기
        try{
            JSONArray jsonArr = new JSONArray(jsonStrData);
            previewList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);
//                System.out.println("jsonObj를 출력");
//                System.out.println(jsonObj);

                //String user_id = (String)jsonObj.get("user_id");
                String nickname = (String)jsonObj.get("nickname");
                String title = (String)jsonObj.get("title");

//                System.out.println(title);

                String sub_title = (String)jsonObj.get("sub_title");
                String contents = (String)jsonObj.get("contents");
                String score = (String)jsonObj.get("score");

                System.out.println("jsonObj 에서 data를 받아옴");

                ////////////// image 도 추가해주기 //////////////

                new_prev = new Preview();

                //new_prev.setId(user_id);
                new_prev.setName(nickname);
                new_prev.setTitle(title);
                new_prev.setSubtitle(sub_title);
                new_prev.setContent(contents);
                new_prev.setScore(score);

                System.out.println("preview 클래스에 값을 넣음");
                System.out.println(new_prev.getTitle());

                try{
                    previewList.add(new_prev);
                    System.out.println(previewList);

                }catch(NullPointerException n){
                    System.out.println("널 포인터...");
                    n.printStackTrace();
                }

                previewAdapter = new PreviewAdapter(getContext(), previewList);
                listView.setAdapter(previewAdapter);
            }


        }catch (JSONException e) {
            System.out.println("json parsing 에서 오류");
            e.printStackTrace();
        }
    }

    private void jsonParsingforSearching(String jsonStrData){  // 입력으로 string 형태olp의 json을 받아 온 후 array 로 변환 후 jsonObj로 파싱해주기
        try{
            JSONArray jsonArr = new JSONArray(jsonStrData);
            previewList = new ArrayList<>();

            for (int i = 0; i < jsonArr.length(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);
//                System.out.println("jsonObj를 출력");
//                System.out.println(jsonObj);

                String title = (String)jsonObj.get("title");
                String nickname = "INFP";
                String contents = "뭐행";
                String score = "50";
                String sub_title = (String)jsonObj.get("sub_title");


                System.out.println("jsonObj 에서 data를 받아옴");

                ////////////// image 도 추가해주기 //////////////

                new_prev = new Preview();

                //new_prev.setId(user_id);
                new_prev.setName(nickname);
                new_prev.setTitle(title);
                new_prev.setSubtitle(sub_title);
                new_prev.setContent(contents);
                new_prev.setScore(score);

                System.out.println("preview 클래스에 값을 넣음");
                System.out.println(new_prev.getTitle());

                try{
                    previewList.add(new_prev);
                    System.out.println(previewList);

                }catch(NullPointerException n){
                    System.out.println("널 포인터...");
                    n.printStackTrace();
                }

                previewAdapter = new PreviewAdapter(getContext(), previewList);
                listView.setAdapter(previewAdapter);
            }


        }catch (JSONException e) {
            System.out.println("json parsing 에서 오류");
            e.printStackTrace();
        }
    }

}