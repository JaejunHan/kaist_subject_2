package com.jackrutorial.test1;

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

import java.util.ArrayList;

public class Chatting_list extends Activity {
    private ArrayList<chat_list_shown> chat_room_list = new ArrayList<>();
    private String mynickname = "";
    private ArrayList<Uri> urllist = new ArrayList<>();
    Button btnShowContacts;

    CustomAdapter adapter = new CustomAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        ListView list = findViewById(R.id.chat_list_view);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // 상대방의 닉네임
                String other_nickname = chat_room_list.get(position).getNickname();
                // 채팅방 이름 (db 및 소켓 접속시 쓰임)
                String room_name = mynickname + other_nickname;

                // todo
                // db에 mynickname, other_nickname, room_name을 이용하여 채팅기록을 불러와서 화면에 띄워주고, 채팅방 소켓을 열음.
                //

            }
        });

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
            /*
            TextView nameItem = itemView.findViewById(R.id.nameItem);
            TextView numItem = itemView.findViewById(R.id.numItem);
            TextView emailItem = itemView.findViewById(R.id.emailItem);
            nameItem.setText(information_list.get(position).getName());
            numItem.setText(information_list.get(position).getNum());
            emailItem.setText(information_list.get(position).getEmail());
            */
            return itemView;
        }
        */
    }
}
