package com.jackrutorial.test1.Chatting;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jackrutorial.test1.R;
import com.jackrutorial.test1.chat_list_shown;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity implements TextWatcher {
    private String localhost = "https://9504-192-249-18-214.jp.ngrok.io";
    private String name;
    private String room;
    private WebSocket webSocket;
    private String SERVER_PATH = "https://9504-192-249-18-214.jp.ngrok.io";
    private EditText messageEdit;
    private View sendBtn, pickImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;

    ImageButton goback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        name = getIntent().getStringExtra("name");
        room = getIntent().getStringExtra("room");
        initiateSocketConnection();

        goback = findViewById(R.id.gobackchat);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        String url = SERVER_PATH + "?user="+ name + "&room=" + room; ;
        Request request = new Request.Builder().url(url).build();
        System.out.println(request);

        System.out.println("여기여기여기여기여기여기여기여기여기여기여기여기");
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(ChatActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();

                try {
                    initializeView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    // 받는 메시지
                    jsonObject.put("isSent", false);

                    messageAdapter.addItem(jsonObject);

                    recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    private void initializeView() throws JSONException {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);

        recyclerView = findViewById(R.id.recyclerView);

        messageAdapter = new MessageAdapter(getLayoutInflater());

        

        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        messageEdit.addTextChangedListener(this);
        getResponse();
        
        // 메시지 보내기 버튼
        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", name);
                jsonObject.put("message", messageEdit.getText().toString());

                webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });

        // 이미지 보내기 버튼
        pickImgBtn.setOnClickListener(v -> {

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            startActivityForResult(Intent.createChooser(intent, "Pick image"),
                    IMAGE_REQUEST_ID);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", name);
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // ---------------------Volley Request 수행 함수들 ---------------------
    ////////// send req to Volley and get response to read Posting info ////////////
    public void getResponse() throws JSONException {
        System.out.println("getResponse 로 이동 성공");

        // url 지정
        String url = localhost + "/load_chat";

        // 사용할 json obj 선언
        JSONObject readjson = new JSONObject();
        readjson.put("room", room);
        // Volley 로 전송할 req를 담는 requestQueue 선언
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


        // Volley 로 전송할 req !
        System.out.println("---------여기서 null request 보내기---------");
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, url, readjson, new com.android.volley.Response.Listener<JSONObject>(){

            // response 를 받아오는 listener !
            @Override
            public void onResponse(JSONObject response){
                try{
                    System.out.println("데이터전송 성공송123123");

                    //받은 json형식의 응답을 받아
                    JSONObject jsonObject = new JSONObject(response.toString());
                    System.out.println(jsonObject);
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

        }, new com.android.volley.Response.ErrorListener() {
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
            for (int i = 0; i < jsonArr.length(); i++){
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                String from_nickname = (String)jsonObj.get("from_nickname");
                String to_nickname = (String)jsonObj.get("to_nickname");
                System.out.println(jsonObj.get("mes"));
                System.out.println(jsonObj.get("mes").getClass().getName());
                System.out.println("sfsafsdfasdfasfsdfegkgnmfkn");
                String message = (String)jsonObj.get("mes");
                System.out.println("메시지불러오기ㅅ");
                System.out.println(message);

                System.out.println("jsonObj 에서 data를 받아옴");
                JSONObject jsonObject = new JSONObject();
                ////////////// image 도 추가해주기 //////////////

                jsonObject.put("name", from_nickname);
                jsonObject.put("message", message);
                jsonObject.put("isSent", !(to_nickname.equals(name)));

                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
            }


        }catch (JSONException e) {
            System.out.println("json parsing 에서 오류");
            e.printStackTrace();
        }
    }
}
