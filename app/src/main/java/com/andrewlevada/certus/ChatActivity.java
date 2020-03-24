package com.andrewlevada.certus;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.certus.logic.chat.ChatMessage;
import com.andrewlevada.certus.logic.chat.RecyclableChatElement;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    ArrayList<RecyclableChatElement> chatElements;
    EditText textInputView;
    MaterialToolbar toolbarView;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Find views by ids
        textInputView = findViewById(R.id.chat_text_input);
        toolbarView = findViewById(R.id.chat_toolbar);
        recyclerView = findViewById(R.id.chat_recycler);

        // Get chat data
        // TODO: Request from server
        chatElements = getFaceDataset();

        setupRecyclerView();

        // Process new message sending
        textInputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO: Send new message to server

                return false;
            }
        });

        // Setup back button
        toolbarView.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerChatAdapter adapter = new RecyclerChatAdapter(chatElements);
        recyclerView.setAdapter(adapter);
    }

    @Deprecated
    private ArrayList<RecyclableChatElement> getFaceDataset() {
        ArrayList<RecyclableChatElement> list = new ArrayList<>();

        list.add(new ChatMessage("Котики молодцы."));
        list.add(new ChatMessage("Я знаю", "Инокентий"));

        return list;
    }
}
