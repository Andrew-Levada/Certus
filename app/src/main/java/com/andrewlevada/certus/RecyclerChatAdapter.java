package com.andrewlevada.certus;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.certus.logic.chat.ChatMessage;
import com.andrewlevada.certus.logic.chat.RecyclableChatElement;

import java.util.ArrayList;

public class RecyclerChatAdapter extends RecyclerView.Adapter<RecyclerChatAdapter.ChatViewHolder> {
    private ArrayList<RecyclableChatElement> dataset;

    @Override
    public int getItemViewType(int position) {
        return dataset.get(dataset.size() - position - 1).getType();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == ChatMessage.TYPE_USER || viewType == ChatMessage.TYPE_OTHER) {
            view = ChatMessage.getBaseView(parent, viewType);
        } else {
            view = new LinearLayout(parent.getContext());
        }

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        dataset.get(dataset.size() - position - 1).fillViewContent(holder.itemView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public RecyclerChatAdapter(ArrayList<RecyclableChatElement> dataset) {
        super();
        this.dataset = dataset;
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
