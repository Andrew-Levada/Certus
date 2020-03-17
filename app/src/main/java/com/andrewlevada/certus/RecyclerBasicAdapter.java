package com.andrewlevada.certus;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.tools.SimpleInflater;

import java.util.ArrayList;

public class RecyclerBasicAdapter extends RecyclerView.Adapter<RecyclerBasicAdapter.BasicViewHolder> {
    private ArrayList<RecyclableLesson> dataset;

    public RecyclerBasicAdapter(RecyclerView recyclerView, final ArrayList<RecyclableLesson> dataset) {
        this.dataset = dataset;

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

        return new BasicViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
        ViewGroup item = (ViewGroup) holder.itemView;
        RecyclableLesson data = dataset.get(position);

        ((TextView) item.findViewById(R.id.recycler_title)).setText(data.getTitle());

        ((TextView) item.findViewById(R.id.recycler_status)).setText(data.getStatus().getStatusString());

        ((TextView) item.findViewById(R.id.recycler_subject)).setText(data.getSubject().getName().toUpperCase());
        ((TextView) item.findViewById(R.id.recycler_subject)).setTextColor(data.getSubject().getColor());

        if (!data.getStatus().isChatable()) {
            item.findViewById(R.id.recycler_img_container).setVisibility(View.GONE);
        } else {
            //TODO: Chat open on click listener
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class BasicViewHolder extends RecyclerView.ViewHolder {
        public BasicViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
