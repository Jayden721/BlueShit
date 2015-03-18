package com.blueshit.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blueshit.R;


public class FirstFragment extends BaseFragment {
    private ImageView ivFirst;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first, container, false);
        findView();
        init();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void findView() {
        ivFirst = (ImageView) root.findViewById(R.id.iv_first);
    }

    private void init() {
    }
}
