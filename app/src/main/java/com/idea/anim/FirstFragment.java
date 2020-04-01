package com.idea.anim;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.idea.anim.view.CircleMotionView;
import com.idea.anim.view.CircleMotionDrawable;

public class FirstFragment extends Fragment {

    private CircleMotionView mCircleMotionView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCircleMotionView = view.findViewById(R.id.anim_round);

        showView(view);
        hideView(view);
        startAnim(view);
        stopAnim(view);


    }

    private void showView(View view) {
        view.findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircleMotionView.smoothToShow();
            }
        });

    }

    private void hideView(View view) {

        view.findViewById(R.id.btn_hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircleMotionView.smoothToHide();
            }
        });

    }

    private void startAnim(View view) {
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircleMotionView.startAnimation();
            }
        });

    }

    private void stopAnim(View view) {

        view.findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCircleMotionView.stopAnimation();
            }
        });

    }
}
