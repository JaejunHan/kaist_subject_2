package com.jackrutorial.test1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup  extends AppCompatActivity {
    private RadioGroup radioGroup;

    private char sex = ' ';
    private boolean is_id_valid = false;
    private boolean is_password_valid = false;
    private boolean is_nickname_valid = false;
    private boolean is_sex_valid = false;

    Button id_confirm;
    Button password_confirm;
    Button nickname_confirm;
    Button sign_up_submit;
    TextView id_input;
    TextView password_input;
    TextView nickname_input;
    private String localhost = "https://ae25-192-249-18-214.jp.ngrok.io";

    Button birthdayText;
    String year;
    String month;
    String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        id_confirm = findViewById(R.id.id_confirm);
        password_confirm = (Button) findViewById(R.id.password_confirm);
        nickname_confirm = (Button) findViewById(R.id.nickname_confirm);
        sign_up_submit = (Button) findViewById(R.id.sign_up_submit);
        id_input = findViewById(R.id.sign_up_id);
        password_input = findViewById(R.id.sign_up_password);
        nickname_input = findViewById(R.id.sign_up_nickname);

        // radio button
        // https://yoo-hyeok.tistory.com/55
        //라디오 그룹 설정
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);


        //라디오 그룹 클릭 리스너
        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(i == R.id.r_btn1){ //남자
                    sex = 'm';
                    is_sex_valid = true;
                    System.out.println("111여기요여기요여기요여기요여기요");
                    System.out.println(is_sex_valid);
                }
                else if(i == R.id.r_btn2){
                    sex = 'f';
                    is_sex_valid = true;
                    System.out.println(is_sex_valid);
                    System.out.println("222여기요여기요여기요여기요여기요");
                }else if(i == R.id.r_btn3){
                    sex = 'e';
                    System.out.println("여기요여기요여기요여기요여기요");
                    is_sex_valid = true;
                    System.out.println(is_sex_valid);
                }
            }
        };

        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        /*
        //라디오 버튼 클릭 리스너
        RadioButton.OnClickListener radioButtonClickListener = new RadioButton.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("눌렸어요");
                if (r_btn1.isChecked()){    // 남자
                    sex = 'm';
                    is_sex_valid = true;
                    System.out.println("111여기요여기요여기요여기요여기요");
                    System.out.println(is_sex_valid);
                } else if (r_btn2.isChecked()){     //여자
                    sex = 'f';
                    is_sex_valid = true;
                    System.out.println(is_sex_valid);
                    System.out.println("222여기요여기요여기요여기요여기요");
                } else if (r_btn3.isChecked()){     //기타
                    sex = 'e';
                    System.out.println("여기요여기요여기요여기요여기요");
                    is_sex_valid = true;
                    System.out.println(is_sex_valid);
                }
            }
        };
        */

        //id 확인 버튼
        id_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("id check button");
                String id = id_input.getText().toString();
                System.out.println(id);
                if (id.length() >= 6 && check_alpha_or_digit(id)) { //주어진 조건을 만족하면
                    //id가 db에서 중복이 되었는지 확인
                    System.out.println("ㅋㅌㅊㅋㅌㅊㅋㅊㅋㅊㅋㅌㅊㅋㅊ");
                    request_id_valid(id);
                } else {
                    System.out.println("여기여기여깅누란우라ㅜㅏ");
                    String text = "주어진 조건에 맞게 입력해주세요.";
                    toast_text(text);
                }
            }
        });

        //비밀번호 확인 버튼
        password_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = id_input.getText().toString();
                String password = password_input.getText().toString();
                String text = checkPassword(password, id);
                if (!text.equals("")){
                    toast_text(text);
                } else {
                    toast_text("가능한 비밀번호입니다.");
                    is_password_valid = true;
                }
            }
        });

        //닉네임 중복 확인 버튼
        nickname_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickname = nickname_input.getText().toString();
                if (check_alpha_or_digit(nickname)) { //주어진 조건을 만족하면
                    //id가 db에서 중복이 되었는지 확인
                    request_nickname_valid(nickname);
                } else {
                    toast_text("주어진 조건에 맞게 입력해주세요.");
                }
            }
        });

        //회원가입 버튼
        sign_up_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_id_valid){
                    if (is_password_valid){
                        if (is_nickname_valid){
                            if (is_sex_valid){
                                String id = id_input.getText().toString();
                                String password = password_input.getText().toString();
                                String nickname = nickname_input.getText().toString();
                                add_user_row_to_db(id, password, nickname, sex);
                                toast_text("회원가입 성공!");
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else{
                                toast_text("성별을 선택해주세요.");
                            }
                        } else {
                            toast_text("닉네임을 올바르게 입력해주세요.");
                        }
                    } else {
                        toast_text("비밀번호를 올바르게 입력해주세요.");
                    }
                }else {
                    toast_text("아이디를 올바르게 입력해주세요.");
                }
            }
        });

        Date today = new Date();      // birthday 버튼의 초기화를 위해 date 객체와 SimpleDataFormat 사용
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String result = dateFormat.format(today);

        birthdayText = ((Button) findViewById(R.id.birthday));
        birthdayText.setText(result);       // 오늘 날짜로 birthday 버튼 텍스트 초기화
    }
    public void toast_text(String text){
        Toast toast;
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();
    }
    public boolean check_alpha_or_digit(String s){
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (!(c >= 'A' && c <= 'Z') && !(c >= 'a' && c <= 'z') && !(c >= '0' && c <= '9')) {
                return false;
            }
        }
        return true;
    }

    private String checkPassword(String pwd, String id){

        // 비밀번호 포맷 확인(영문, 특수문자, 숫자 포함 8자 이상)
        Pattern passPattern1 = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
        Matcher passMatcher1 = passPattern1.matcher(pwd);

        if(!passMatcher1.find()){
            return "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다.";
        }

        // 반복된 문자 확인
        Pattern passPattern2 = Pattern.compile("(\\w)\\1\\1\\1");
        Matcher passMatcher2 = passPattern2.matcher(pwd);

        if(passMatcher2.find()){
            return "비밀번호에 동일한 문자를 과도하게 연속해서 사용할 수 없습니다.";
        }

        // 아이디 포함 확인
        if(pwd.contains(id)){
            return "비밀번호에 ID를 포함할 수 없습니다.";
        }

        // 특수문자 확인
        Pattern passPattern3 = Pattern.compile("\\W");
        Pattern passPattern4 = Pattern.compile("[!@#$%^*+=-]");

        for(int i = 0; i < pwd.length(); i++){
            String s = String.valueOf(pwd.charAt(i));
            Matcher passMatcher3 = passPattern3.matcher(s);

            if(passMatcher3.find()){
                Matcher passMatcher4 = passPattern4.matcher(s);
                if(!passMatcher4.find()){
                    return "비밀번호에 특수문자는 !@#$^*+=-만 사용 가능합니다.";
                }
            }
        }

        //연속된 문자 확인
        int ascSeqCharCnt = 0; // 오름차순 연속 문자 카운트
        int descSeqCharCnt = 0; // 내림차순 연속 문자 카운트

        char char_0;
        char char_1;
        char char_2;

        int diff_0_1;
        int diff_1_2;

        for(int i = 0; i < pwd.length()-2; i++){
            char_0 = pwd.charAt(i);
            char_1 = pwd.charAt(i+1);
            char_2 = pwd.charAt(i+2);

            diff_0_1 = char_0 - char_1;
            diff_1_2 = char_1 - char_2;

            if(diff_0_1 == 1 && diff_1_2 == 1){
                ascSeqCharCnt += 1;
            }

            if(diff_0_1 == -1 && diff_1_2 == -1){
                descSeqCharCnt -= 1;
            }
        }

        if(ascSeqCharCnt > 1 || descSeqCharCnt > 1){
            return "비밀번호에 연속된 문자열을 사용할 수 없습니다.";
        }

        return "";
    }


    public void request_id_valid(String id){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = localhost + "/id_valid";;

        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            testjson.put("id", id);
            String jsonString = testjson.toString(); //완성된 json 포맷

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                        boolean is_valid = jsonObject.getBoolean("id_valid");

                        if (!is_valid){  // 가능한 아이디
                            String text = "가능한 아이디입니다.";
                            is_id_valid = true;
                            toast_text(text);
                        } else {    //불가능한 아이디
                            String text = "중복된 아이디입니다. 다른 아이디를 입력해주세요.";
                            toast_text(text);
                        }
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
                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            //
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void request_nickname_valid(String nickname){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = localhost + "/nickname_valid";;

        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            testjson.put("nickname", nickname);
            String jsonString = testjson.toString(); //완성된 json 포맷

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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
                        boolean is_valid = jsonObject.getBoolean("nickname_valid");

                        if (!is_valid){  // 가능한 닉네임
                            String text = "가능한 닉네임입니다.";
                            is_nickname_valid = true;
                            toast_text(text);
                        } else {    //불가능한 닉네임
                            String text = "중복된 닉네임입니다. 다른 닉네임를 입력해주세요.";
                            toast_text(text);
                        }
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
                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            //
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void add_user_row_to_db(String id, String password, String nickname, char sex){
        //url 요청주소 넣는 editText를 받아 url만들기
        String url = localhost + "/sign_up";;
        String sex_to_send = "";
        sex_to_send += sex;
        //JSON형식으로 데이터 통신을 진행합니다!
        JSONObject testjson = new JSONObject();
        try {
            //입력해둔 edittext의 id와 pw값을 받아와 put해줍니다 : 데이터를 json형식으로 바꿔 넣어주었습니다.
            testjson.put("id", id);
            testjson.put("password", password);
            testjson.put("nickname", nickname);
            testjson.put("sex", sex_to_send);
            String jsonString = testjson.toString(); //완성된 json 포맷

            //이제 전송해볼까요?
            final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

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

    ///////////////

    public void onBirthdayClicked (View v) {
        DataPickerFragment newFragment = new DataPickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
    }

    public  void getBirthday (int year, int month, int day){
        this.year = String.valueOf(year);
        this.month = String.valueOf(month);
        this.day = String.valueOf(day);
        System.out.println("year" + year);
        System.out.println("month" + month);
        System.out.println("day" + day);
    }
    ///////////////
}
