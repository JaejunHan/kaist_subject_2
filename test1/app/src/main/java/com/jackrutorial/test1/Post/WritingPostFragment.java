package com.jackrutorial.test1.Post;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jackrutorial.test1.Adapter.PhotoAddAdapter;
import com.jackrutorial.test1.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class
WritingPostFragment extends Fragment {
    private View view;
    private String nickname;
    EditText new_title, new_subtitle, new_content;
    private Context context;

    ApiService apiService;
    Uri picUri;
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    private final static int IMAGE_RESULT = 200;
    ArrayList<Bitmap> list = new ArrayList<>();
    FloatingActionButton fabCamera, fabUpload;
    Bitmap mBitmap;
    TextView textView;

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


        askPermissions();///////////////////////////////////
        initRetrofitClient();///////////////////////////////////

        // 작성 버튼 클릭 리스너
        view.findViewById(R.id.posting_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //////// data 등록하는거 구현
                String title = new_title.getText().toString();
                String subtitle = new_subtitle.getText().toString();
                String content = new_content.getText().toString();

                // 날짜
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date date = new Date();
                String dateStr = dateFormat.format(date);

                postRequest(nickname, title, subtitle, content, "0", "50", dateStr);


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
                    startActivityForResult(getPickImageChooserIntent(), IMAGE_RESULT);

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
        System.out.println(nickname);
        System.out.println(title);
        System.out.println(subtitle);
        System.out.println("ㅇㄹㅇㄹㅇㄹㅇㄹㅇ");
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
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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
            if(requestCode == IMAGE_RESULT){
                String filePath = getImageFilePath(data);
                if (filePath != null) {
                    mBitmap = BitmapFactory.decodeFile(filePath);
                    list.add(mBitmap);
                    if (mBitmap != null)
                        multipartImageUpload();
                    else {
                        Toast.makeText(getActivity().getApplicationContext(), "Bitmap is null. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

}