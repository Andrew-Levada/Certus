package com.andrewlevada.certus;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andrewlevada.certus.logic.User;
import com.andrewlevada.certus.logic.lessons.RecyclableLesson;
import com.andrewlevada.certus.tools.SimpleInflater;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass which contains only recycler view.
 */
public abstract class RecyclerFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<RecyclableLesson> list;

    private HomeActivity hostActivity;

    private Handler afterAnimHandler;
    private Runnable afterAnimRunnable;

    @LayoutRes
    private int backdropContentLayout;

    public RecyclerFragment() {
        this.hostActivity = (HomeActivity) getActivity();
        list = new ArrayList<>();
    }

    public RecyclerFragment(HomeActivity hostActivity, @LayoutRes int backdropContentLayout) {
        this.hostActivity = hostActivity;
        this.backdropContentLayout = backdropContentLayout;
        list = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate fragment view
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_recycler, container, false);

        // Get data
        list = getFakeDataset();
        // TODO: Request to server to update list array

        // Setup recycler view
        recyclerView = (RecyclerView) layout.findViewById(R.id.learn_home_recycler);
        setupRecyclerView(new RecyclerBasicAdapter.OnChatOpenRequest() {
            @Override
            public void request(int index) {
                openChatRequest(index);
            }
        });

        // Add actions after fragment switching is finished
        afterAnimRunnable = new Runnable() {
            @Override
            public void run() {
                if (hostActivity == null) return;

                // Request fab from activity
                hostActivity.requestFAB(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        hostActivity.updateBackdrop(true);
                    }
                });

                // Request to fill backdrop
                hostActivity.fillBackdrop(backdropContentLayout, new SimpleInflater.OnViewInflated() {
                    @Override
                    public void inflated(View view) {
                        fillBackdrop((ViewGroup) view);
                    }
                });
            }
        };

        // Setup actions after fragment switching is finished
        afterAnimHandler = new Handler();
        afterAnimHandler.postDelayed(afterAnimRunnable, 250);

        return layout;
    }

    @Override
    public void onDestroy() {
        // If anim is not finished cancel after actions
        afterAnimHandler.removeCallbacks(afterAnimRunnable);

        super.onDestroy();
    }

    private void setupRecyclerView(RecyclerBasicAdapter.OnChatOpenRequest chatRequest) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RecyclerBasicAdapter adapter = new RecyclerBasicAdapter(recyclerView, list, chatRequest);
        recyclerView.setAdapter(adapter);
    }

    @Deprecated
    abstract ArrayList<RecyclableLesson> getFakeDataset();

    abstract void fillBackdrop(ViewGroup backdrop);

    private void openChatRequest(int index) {
        User user = User.getInstance();

        // Require auth
        if (!user.isAuthed()) {
            if (hostActivity == null) return;
            hostActivity.requestAuth();
        }

        // TODO: Open chat
    }
}
