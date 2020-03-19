package com.example.groceryshop;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DynamicGridLayoutManager extends GridLayoutManager {

    private int spanCount = 1;

    public DynamicGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount, VERTICAL, false);

        this.spanCount = spanCount;
    }

    public DynamicGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    private int[] mMeasuredDimension = new int[2];

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {

        Log.i("onMeasure"," item:"+getItemCount()+" "+(0<(double)getItemCount())+" measuredDimension:"+mMeasuredDimension[0]
        +" span Count:"+spanCount+" widthspec "+widthSpec+" heighspec "+heightSpec
        );
        Log.i("measure shmuck"," "+View.MeasureSpec.getMode(widthSpec)+" "+View.MeasureSpec.getSize(heightSpec)+" "
                +View.MeasureSpec.getMode(heightSpec)+" "+View.MeasureSpec.getSize(widthSpec)
        );
        setMeasuredDimension(View.MeasureSpec.UNSPECIFIED, heightSpec);
        RecyclerView view;

    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {
        Log.i("recycler"," "+recycler.getScrapList()+" "+" "+recycler.getClass()+" ");
        View view = recycler.getViewForPosition(position);
        recycler.bindViewToPosition(view, position);
        if (view != null) {
            RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
            int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                    getPaddingLeft() + getPaddingRight(), p.width);
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                    getPaddingTop() + getPaddingBottom(), p.height);
            view.measure(childWidthSpec, childHeightSpec);
            measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
            measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
            recycler.recycleView(view);
        }
    }
}