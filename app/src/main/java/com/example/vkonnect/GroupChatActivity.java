package com.example.vkonnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

public class GroupChatActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText userMsgInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;
    private ImageButton sendMessageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        initializeFields();


    }

    private void initializeFields() {
        mToolbar=(Toolbar)findViewById(R.id.group_page_tool);
        userMsgInput=(EditText)findViewById(R.id.input_group_msg);
        mScrollView=(ScrollView)findViewById(R.id.scroll_bar);
        sendMessageButton=(ImageButton)findViewById(R.id.send_msg_button);
        displayTextMessages=(TextView)findViewById(R.id.group_chat_text_display);

    }
}
