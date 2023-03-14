package com.spongedev.rateapp;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

public class MainFragment extends Fragment {

    private ReviewInfo reviewInfo;
    private ReviewManager manager;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rateDialog();
        rateApp();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(view);
    }

    private void initUI(View rootView) {

    }

    private void rateDialog(){

        View view = View.inflate(getContext(), R.layout.rate_layout, null);

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setView(view);

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        Button later = view.findViewById(R.id.later);
        Button rate = view.findViewById(R.id.rate);

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startReviewFlow();
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

    }

    private void rateApp(){

        manager = ReviewManagerFactory.create(getContext());
        Task<ReviewInfo> managerInfoTask = manager.requestReviewFlow();

        managerInfoTask.addOnCompleteListener((task -> {
            if(task.isSuccessful()){
                reviewInfo = task.getResult();
            }
            else{
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }));

    }

    private void startReviewFlow(){
        if(reviewInfo != null){
            Task<Void> flow = manager.launchReviewFlow((Activity) getContext(), reviewInfo);
            flow.addOnCompleteListener(task -> {
                Toast.makeText(getContext(), "Completed", Toast.LENGTH_SHORT).show();
            });
        }
    }

}