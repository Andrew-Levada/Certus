package com.andrewlevada.certus.logic.chat;

import android.view.View;

public interface RecyclableChatElement {
    int getType();

    void fillViewContent(View view);
}
