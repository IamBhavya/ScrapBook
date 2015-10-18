package com.example.bhavya.myapplication;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bhavya.myapplication.R;
import com.example.bhavya.myapplication.TwoWayView;


public class HorizontalPhotoGalleryFragment extends SimplePhotoGalleryListFragment {

    private TwoWayView mHorizontalListView;


    public static HorizontalPhotoGalleryFragment newInstance(int sectionNumber) {
        HorizontalPhotoGalleryFragment fragment = new HorizontalPhotoGalleryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
    /**
     * Create View!
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.fragment_horizontal_gallery, container, false);

// Set the mAdapter
        mEmptyTextView = (TextView)view.findViewById(R.id.empty);
        mHorizontalListView = (TwoWayView) view.findViewById(R.id.horizontalList);
        mHorizontalListView.setAdapter(mAdapter);
        mHorizontalListView.setItemMargin(10);
        resolveEmptyText();
        return view;
    }
}

