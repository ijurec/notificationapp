package com.task.notificationapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener {

    public interface OnButtonClickedListener {
        void onButtonClicked(View view);
    }

    private ImageView mDecreaseFragmentBtn;

    private OnButtonClickedListener mListener;
    private int mFragmentNumber;
    private boolean isRemoveBtnAvailable;

    public ScreenSlidePageFragment(int mFragmentNumber, boolean isRemoveBtnAvailable) {
        this.mFragmentNumber = mFragmentNumber;
        this.isRemoveBtnAvailable = isRemoveBtnAvailable;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnButtonClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnButtonClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        TextView fragmentNumber = rootView.findViewById(R.id.textView_fragment_number);
        ImageView notificationBtn = rootView.findViewById(R.id.button_notification);
        mDecreaseFragmentBtn = rootView.findViewById(R.id.button_decrease);
        ImageView increaseFragmentBtn = rootView.findViewById(R.id.button_increase);

        if (savedInstanceState != null && savedInstanceState.containsKey("fragment_number")) {
            mFragmentNumber = savedInstanceState.getInt("fragment_number");
        }

        fragmentNumber.setText(String.valueOf(mFragmentNumber));
        notificationBtn.setOnClickListener(this);
        mDecreaseFragmentBtn.setOnClickListener(this);
        increaseFragmentBtn.setOnClickListener(this);

        if (!isRemoveBtnAvailable) {
            mDecreaseFragmentBtn.setVisibility(View.INVISIBLE);
        } else {
            mDecreaseFragmentBtn.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        mListener.onButtonClicked(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("fragment_number", mFragmentNumber);
        super.onSaveInstanceState(outState);
    }

    public void setRemoveBtnAvailable(boolean isRemoveBtnAvailable) {
        if (!isRemoveBtnAvailable) {
            mDecreaseFragmentBtn.setVisibility(View.INVISIBLE);
        } else {
            mDecreaseFragmentBtn.setVisibility(View.VISIBLE);
        }
    }
}