package com.jackrutorial.test1.Chatting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.jackrutorial.test1.chat_list_shown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chatting_list extends Activity {
    private String localhost = "https://9504-192-249-18-214.jp.ngrok.io";
    private ArrayList<com.jackrutorial.test1.chat_list_shown> chat_room_list = new ArrayList<>();
    private chat_list_shown chat_list_shown;
    private String mynickname = "";

    CustomAdapter adapter = new CustomAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        mynickname = getIntent().getStringExtra("nickname");
        ListView list = findViewById(R.id.chat_list_view);
        try {
            getResponse();

        } catch (JSONException e) {
            System.out.println("getResponse 실패!!!!!!!!!!!");
            e.printStackTrace();
        }
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 상대방의 닉네임
                String other_nickname = chat_room_list.get(position).getNickname();
                // 채팅방 이름 (db 및 소켓 접속시 쓰임)
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("my_nickname", mynickname);
                intent.putExtra("other_nickname", other_nickname);

                startActivity(intent);
                // todo
                // db에 mynickname, other_nickname, room_name을 이용하여 채팅기록을 불러와서 화면에 띄워주고, 채팅방 소켓을 열음.
                //
            }
        });
        /*
        // list 꾹 누를 때 버튼
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @SuppressLint("Range")
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editIntent = new Intent(Intent.ACTION_VIEW);
                editIntent.setDataAndType(urllist.get(position), ContactsContract.Contacts.CONTENT_ITEM_TYPE);
                editIntent.putExtra("finishActivityOnSaveCompleted", true);
                startActivity(editIntent);
                return false;
            }
        });
         */
    }

    private class CustomAdapter extends BaseAdapter {
        Context context;

        public CustomAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return chat_room_list.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = View.inflate(context, R.layout.item, null);
            TextView nickname_view = itemView.findViewById(R.id.nickname);
            TextView last_message_view = itemView.findViewById(R.id.last_message);
            nickname_view.setText(chat_room_list.get(position).getNickname());
            last_message_view.setText(chat_room_list.get(position).getLast_message());
            TextView reg_date_view = itemView.findViewById(R.id.reg_date);
            // todo
            // 톡방의 시간을 현재 2207111459로 띄워주는데, 이거를
            //  07월 11일 14시 59분 이런식으로 프린트해주면 좋겠음.
            reg_date_view.setText(chat_room_list.get(position).getSend_date().substring(2, 12));


            // todo
            // 상대의 이미지도 여기에 띄워줘야함.

            return itemView;
        }
    }

    // ---------------------Volley Request 수행 함수들 ---------------------
    ////////// send req to Volley and get response to read Posting info ////////////
    public void getResponse() throws JSONException {
        System.out.println("getResponse 로 이동 성공");

        // url 지정
        String url = localhost + "/read_chatting_room";

        // 사용할 json obj 선언
        JSONObject readjson = new JSONObject();
        readjson.put("my_nickname", mynickname);
        // Volley 로 전송할 req를 담는 requestQueue 선언
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        // Volley 로 전송할 req !
        System.out.println("---------여기서 null request 보내기---------");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, readjson, new Response.Listener<JSONObject>(){

            // response 를 받아오는 listener !
            @Override
            public void onResponse(JSONObject response){
                try{
                    System.out.println("데이터전송 성공송123123");

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());

                    // string 형태의 json 을 받아와서
                    String jsonWholeArray = jsonObject.getString("jsonArray");

                    System.out.println("전체 array in string type");
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

    private void jsonParsing(String jsonStrData){  // 입력으로 string 형태olp의 json을 받아 온 후 array 로 변환 후 jsonObj로 파싱해주기
        try{
            JSONArray jsonArr = new JSONArray(jsonStrData);
            chat_room_list = new ArrayList<>();
            for (int i = 0; i < jsonArr.length(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String last_message = (String)jsonObj.get("last_message");
                String nickname = (String)jsonObj.get("nickname");
                String reg_date = (String)jsonObj.get("reg_date");

                System.out.println("jsonObj 에서 data를 받아옴");

                ////////////// image 도 추가해주기 //////////////

                chat_list_shown = new chat_list_shown();
                chat_list_shown.setLast_message(last_message);
                chat_list_shown.setNickname(nickname);
                chat_list_shown.setSend_date(reg_date);

                System.out.println("chat_list_shown 클래스에 값을 넣음");

                chat_room_list.add(chat_list_shown);
                System.out.println(chat_list_shown);

                ListView list = findViewById(R.id.chat_list_view);
                list.setAdapter(adapter);
            }


        }catch (JSONException e) {
            System.out.println("json parsing 에서 오류");
            e.printStackTrace();
        }
    }
}
