package com.andrewlevada.certus.logic.chat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrewlevada.certus.R;
import com.andrewlevada.certus.logic.User;
import com.andrewlevada.certus.tools.SimpleInflater;

import java.util.Calendar;

public class ChatMessage implements RecyclableChatElement {
    public static final int TYPE_USER = 1;
    public static final int TYPE_OTHER = 2;

    private String text;
    private String name;
    private Calendar time;
    private boolean isUser;

    @Override
    public int getType() {
        if (isUser) return TYPE_USER;
        else return TYPE_OTHER;
    }

    public static View getBaseView(ViewGroup parent, int type) {
        View view;

        if (type == TYPE_USER)
            view = SimpleInflater.inflate(parent, R.layout.chat_message_user_layout, false);
        else view = SimpleInflater.inflate(parent, R.layout.chat_message_other_layout, false);

        return view;
    }

    @Override
    public void fillViewContent(View view) {
        ((TextView) view.findViewById(R.id.chat_message_text)).setText(text);
    }

    public ChatMessage(String text) {
        this.text = text;
        isUser = true;
        name = User.getInstance().getFirebaseUser().getDisplayName();
        time = Calendar.getInstance();
    }

    @Deprecated
    public ChatMessage(String text, String name) {
        this.text = text;
        isUser = false;
        this.name = name;
        time = Calendar.getInstance();
    }
}
