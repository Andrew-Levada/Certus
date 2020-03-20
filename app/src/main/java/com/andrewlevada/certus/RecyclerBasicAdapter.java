package com.andrewlevada.certus;

import android.content.Context;
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
    private Context context;

    public RecyclerBasicAdapter(RecyclerView recyclerView, final ArrayList<RecyclableLesson> dataset) {
        this.dataset = dataset;
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

        return new BasicViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull BasicViewHolder holder, int position) {
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
