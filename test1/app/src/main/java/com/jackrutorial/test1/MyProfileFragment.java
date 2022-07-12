package com.jackrutorial.test1;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MyProfileFragment extends Fragment {

    private String localhost = "https://7db1-192-249-18-214.jp.ngrok.io";
    //todo
    // nickname을 ""로 수정하기
    private String nickname = "hello123";
    Button editImage;
    Button editName;
    Button editNum;
    Button editMail;
    Button techStack;
    Button gosuTalk;
    Button addCareer;
    Button showLocation; //등수보기

    TextView myName;
    TextView myNumber;
    TextView myEmail;
    TextView gosuTalkWrite;
    TextView techStackText;

    ListView career;
    ScrollView scrollView;

    ApiService apiService;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    FloatingActionButton fabCamera, fabUpload;
    Bitmap mBitmap;
    TextView textView;

    ImageView profile;


    ArrayAdapter<String> adpater;

    public MyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview =  inflater.inflate(R.layout.fragment_profile, container, false);

        // todo
        // 이거를 fragment로 바꿔야함.
        //


        // todo
        // nickname을 받아와서 request 해줌.
        // db서버와 연결해서 프로필 사진을 불러옴.
        request(nickname);///////////////////////////////////


        askPermissions();///////////////////////////////////
        initRetrofitClient();///////////////////////////////////

        profile = (ImageView) rootview.findViewById(R.id.my_profile);

        editImage = (Button) rootview.findViewById(R.id.EditProfileImage);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);///////////////////////////////////
            }
        });

        editName = (Button) rootview.findViewById(R.id.editName);
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 0);
            }
        });
        editNum = (Button) rootview.findViewById(R.id.editNum);
        editNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 1);
            }
        });
        editMail = (Button) rootview.findViewById(R.id.editMail);
        editMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 2);
            }
        });
        techStack = (Button) rootview.findViewById(R.id.techStack);
        techStack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                intent.putExtra("originalText", techStackText.getText().toString());
                startActivityForResult(intent, 5);
            }
        });
        gosuTalk = (Button) rootview.findViewById(R.id.gosuTalk);
        gosuTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), gosuTalkEdit.class);
                intent.putExtra("originalText", gosuTalkWrite.getText().toString());
                startActivityForResult(intent, 3);
                ;
            }
        });
        addCareer = (Button) rootview.findViewById(R.id.addCareer);
        addCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), dialogMake.class);
                startActivityForResult(intent, 4);
            }
        });
        showLocation = (Button) rootview.findViewById(R.id.showLocation);
        showLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LeaderBoard.class);
                startActivity(intent);
            }
        });


        myName = (TextView) rootview.findViewById(R.id.myName);
        myNumber = (TextView) rootview.findViewById(R.id.myNum);
        myEmail = (TextView) rootview.findViewById(R.id.myEmail);
        gosuTalkWrite = (TextView) rootview.findViewById(R.id.gosuTalkWrite);
        techStackText = (TextView) rootview.findViewById(R.id.techStackText);
        career = (ListView) rootview.findViewById(R.id.career);

        List<String> list = new ArrayList<>();
//        list.add("카이스트");
//        list.add("고려대");
//        list.add("포스텍");
        adpater = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        career.setAdapter(adpater);

        scrollView = (ScrollView) rootview.findViewById(R.id.scrollView);
        career.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        return rootview;
    }


    private void askPermissions() {
        permissions.add(CAMERA);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissionsToRequest = findUnAskedPermissions(permissions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private void initRetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        apiService = new Retrofit.Builder().baseUrl(localhost).client(client).build().create(ApiService.class);
    }

    public Intent getPickImageChooserIntent() {

        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getActivity().getPackageManager();/////////////////////////////////////

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalFilesDir("");
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }
    private String getImageFromFilePath(Intent data) {
        boolean isCamera = data == null || data.getData() == null;

        if (isCamera) return getCaptureImageOutputUri().getPath();
        else return getPathFromURI(data.getData());

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("pic_uri", picUri);
    }
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        picUri = savedInstanceState.getParcelable("pic_uri");
//    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (ActivityCompat.checkSelfPermission(getActivity() ,permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private void multipartImageUpload() {
        try {
            File filesDir = getActivity().getApplicationContext().getFilesDir();
            File file = new File(filesDir, "image" + ".png");
            System.out.println(filesDir);
            System.out.println(file);
            System.out.println("파일파일파일파일파일파일파일파일파일");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();


            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            System.out.println(reqFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
            System.out.println("sdfasdfsadfsdafdsfsdfsdf");
            System.out.println(body);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "upload");
            System.out.println(name);
            // todo
            // 닉네임 부분을 바꿔야함.
            Call<ResponseBody> req = apiService.postImage(body, name, nickname);
            System.out.println("req: " + req);
            req.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    System.out.println(name);
                    System.out.println(body);
                    System.out.println("여기여기여기123123123123");
                    if (response.code() == 200) {
                        System.out.println("프로필 업로드 성공");
                    }

                    Toast.makeText(getActivity().getApplicationContext(), response.code() + " ", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("프로필 업로드 실패");
                    Toast.makeText(getContext().getApplicationContext(), "Request failed", Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }


    public String getImageFilePath(Intent data) {
        return getImageFromFilePath(data);
    }

    private String getPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK){
            if(requestCode == 0){
                myName.setText(data.getStringExtra("changeThing"));
            } else if(requestCode == 1){
                myNumber.setText(data.getStringExtra("changeThing"));
            } else if(requestCode == 2){
                myEmail.setText(data.getStringExtra("changeThing"));
            }else if(requestCode == 3){
                gosuTalkWrite.setText(data.getStringExtra("gosuchangeThing"));
            }else if(requestCode == 4){
                adpater.add(data.getStringExtra("changeThing")); //경력에 추가
            }else if(requestCode == 5){
                techStackText.setText(data.getStringExtra("changeThing"));
            }else if(requestCode == IMAGE_RESULT){
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    profile.setImageBitmap(mBitmap);
                    if (mBitmap != null)
                        multipartImageUpload();
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
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
            final RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
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

                        if (!profile_img.equals("")){ // 이미지 url이 존재한다면
                            // todo
                            // url에서 이미지 불러오기
                            Glide.with(getActivity().getApplicationContext()).load(profile_img).into(profile);
                        }
                        myName.setText(nickname);
                        myNumber.setText(phone_number);
                        myEmail.setText(email);
                        techStackText.setText(stack);
                        gosuTalkWrite.setText(one_talk);
                        int num_career = Integer.parseInt(careerCnt);
                        System.out.println(num_career);
                        System.out.println("sakdmskmdksmdksmdkm");
                        if (num_career == 1){
                            adpater.add(career1); //경력에 추가
                        }else if (num_career == 2){
                            adpater.add(career1);
                            adpater.add(career2);
                        }else if (num_career == 3){
                            adpater.add(career1);
                            adpater.add(career2);
                            adpater.add(career3);
                        }else if (num_career == 4){
                            adpater.add(career1);
                            adpater.add(career2);
                            adpater.add(career3);
                            adpater.add(career4);
                        }else if (num_career == 5){
                            adpater.add(career1);
                            adpater.add(career2);
                            adpater.add(career3);
                            adpater.add(career4);
                            adpater.add(career5);
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