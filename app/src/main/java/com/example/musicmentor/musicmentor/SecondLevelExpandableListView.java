package com.example.musicmentor.musicmentor;

import android.content.Context;
import android.widget.ExpandableListView;

/**
 * Created by serajam on 10/22/2017.
 */

public class SecondLevelExpandableListView extends ExpandableListView {

    public SecondLevelExpandableListView(Context context) {
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(999999, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}