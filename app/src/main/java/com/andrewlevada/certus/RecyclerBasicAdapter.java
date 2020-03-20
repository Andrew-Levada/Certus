package com.andrewlevada.certus;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.certus.logic.User;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.tools.SimpleInflater;

import java.util.ArrayList;

public class RecyclerBasicAdapter extends RecyclerView.Adapter<RecyclerBasicAdapter.BasicViewHolder> {
    private ArrayList<RecyclableLesson> dataset;
    private Context context;
    private OnChatOpenRequest chatRequest;

    public RecyclerBasicAdapter(RecyclerView recyclerView, final ArrayList<RecyclableLesson> dataset,
                                OnChatOpenRequest chatRequest) {
        this.dataset = dataset;
        this.chatRequest = chatRequest;
        context = recyclerView.getContext();

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = viewHolder.getAdapterPosition();
                        dataset.remove(position);
                        notifyDataSetChanged();
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public BasicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = SimpleInflater.inflate(parent, R.layout.recyclable_lesson_template, false);

        return new BasicViewHolder(item, chatRequest);
    }

    @Override
    public void onBindViewHolder(@NonNull final BasicViewHolder holder, final int position) {
        if (context == null) return;

        ViewGroup item = (ViewGroup) holder.itemView;
        RecyclableLesson data = dataset.get(position);

        ((TextView) item.findViewById(R.id.recycler_title)).setText(data.getTitle());

        ((TextView) item.findViewById(R.id.recycler_status)).setText(data.getStatus().getStatusString(context));

        String subjectString = data.getSubject().getName().toUpperCase() + " " + data.getGrade().getName();
        ((TextView) item.findViewById(R.id.recycler_subject)).setText(subjectString);
        ((TextView) item.findViewById(R.id.recycler_subject)).setTextColor(data.getSubject().getColor());

        if (!data.getStatus().isChatable()) {
            item.findViewById(R.id.recycler_img_container).setVisibility(View.GONE);
        } else {
            item.findViewById(R.id.recycler_img_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.chatRequest.request(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public static class BasicViewHolder extends RecyclerView.ViewHolder {
        OnChatOpenRequest chatRequest;

        public BasicViewHolder(@NonNull View itemView, OnChatOpenRequest chatRequest) {
            super(itemView);
            this.chatRequest = chatRequest;
        }
    }

    public interface OnChatOpenRequest {
        void request(int index);
    }
}
