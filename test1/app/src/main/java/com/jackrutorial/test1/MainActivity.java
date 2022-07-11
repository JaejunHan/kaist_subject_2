package com.jackrutorial.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    String result;
    TextView login_id;
    TextView login_password;
    TextView sign_up;
    Button submit;
    private String localhost = "https://9504-192-249-18-214.jp.ngrok.io";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_id = findViewById(R.id.login_id);
        login_password = findViewById(R.id.login_password);
        submit = (Button) findViewById(R.id.login_submit);
        sign_up = findViewById(R.id.sign_up);

        //로그인 버튼
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = login_id.getText().toString();
                String password = login_password.getText().toString();
                // db서버와 연결해서 request
                request(id, password);

            }
        });

        //회원가입 버튼
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditMyInfo.class);
                startActivity(intent);

            }
        });

        // 무시해도 됨
        String url = "https://093f-192-249-19-234.jp.ngrok.io/users";
        JSONObject testjson = new JSONObject();
        try {
            testjson.put("id", "test_id");
            testjson.put("password", "test_password");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*
        String html = "https://d4e6-192-249-19-234.jp.ngrok.io/users";
        StringBuilder urlBuilder = new StringBuilder(html); // URL
        System.out.println("urlBuilder : " + urlBuilder);
        URL url = null;
        try {
            url = new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setRequestProperty("Content-type", "application/json");

        HttpURLConnection finalConn = conn;
        new Thread(() -> {
            try {
                BufferedReader bf;
                if (finalConn.getResponseCode() >= 200 && finalConn.getResponseCode() <= 300) {
                    bf = new BufferedReader(new InputStreamReader(finalConn.getInputStream()));
                } else {
                    bf = new BufferedReader(new InputStreamReader(finalConn.getErrorStream()));
                }
                result = bf.readLine();
                System.out.println("result : " + result);
            } catch (Exception e) {
                System.out.println("오류올오ㅠㄹㅇㄹ유로유로유로유로유로유류ㅗ");
            }
        }).start();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }
    public void request(String id, String password){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = localhost + "/users";

        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            testjson.put("id", id);
            testjson.put("password", password);
            String jsonString = testjson.toString(); //완성된 json 포맷

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

            System.out.println("zxcvasfsfasdfsdf");
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,testjson, new Response.Listener<JSONObject>() {

                //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println("데이터전송 성공");

                        //받은 json형식의 응답을 받아
                        JSONObject jsonObject = new JSONObject(response.toString());

                        //key값에 따라 value값을 쪼개 받아옵니다.
                        String resultId = jsonObject.getString("id");
                        String resultPassword = jsonObject.getString("password");
                        String nickname = jsonObject.getString("nickname");
                        System.out.println("main nickname nickname nickname nickname nickname");
                        System.out.println(nickname);
                        String sex = jsonObject.getString("sex");
                        String Api_Token = jsonObject.getString("Api_Token");
                        if (Api_Token.equals("")){  // 로그인 실패
                            String text = "아이디, 비밀번호를 확인해주세요.";
                            Toast toast;
                            int duration = Toast.LENGTH_SHORT;
                            toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                        } else {    //로그인 성공!
                            String text = "로그인 성공!";
                            Toast toast;
                            int duration = Toast.LENGTH_SHORT;
                            toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();
                            Intent intent = new Intent(getApplicationContext(), Example.class);
                            intent.putExtra("resultId", resultId);
                            intent.putExtra("nickname", nickname);
                            startActivity(intent);
                        }
                        /*
                        //만약 그 값이 같다면 로그인에 성공한 것입니다.
                        if(resultId.equals("OK") & resultPassword.equals("OK")){

                            //이 곳에 성공 시 화면이동을 하는 등의 코드를 입력하시면 됩니다.
                        }else{
                            //로그인에 실패했을 경우 실행할 코드를 입력하시면 됩니다.
                        }
                         */

                    } catch (Exception e) {

                        System.out.println("에러발생에러발생에러발생에러발생에러발생에러발생");
                        e.printStackTrace();
                    }
                }
                //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            }, new Response.ErrorListener() {
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