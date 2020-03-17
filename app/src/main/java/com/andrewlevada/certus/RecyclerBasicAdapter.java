package com.andrewlevada.certus;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerBasicAdapter extends RecyclerView.Adapter<RecyclerBasicAdapter.BasicViewHolder> {
    
    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder {
        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
