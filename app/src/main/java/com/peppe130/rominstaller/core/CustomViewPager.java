package com.peppe130.rominstaller.core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.peppe130.rominstaller.Utils;


@SuppressWarnings("unused")
public class CustomViewPager extends ViewPager {

    private static final int ON_ILLEGALLY_REQUESTED_NEXT_PAGE_MAX_INTERVAL = 1000;
    private boolean pagingEnabled;
    private boolean nextPagingEnabled;
    private float currentTouchDownX;
    private long illegallyRequestedNextPageLastCalled;
    private OnPageChangeListener pageChangeListener;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        pagingEnabled = !Utils.BUTTON_UI;
        nextPagingEnabled = true;
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        super.addOnPageChangeListener(listener);
        this.pageChangeListener = listener;
    }

    @Override
    public void setCurrentItem(int item) {
        boolean invokeMeLater = false;
        if (super.getCurrentItem() == 0 && item == 0)
            invokeMeLater = true;
        super.setCurrentItem(item);
        if (invokeMeLater && pageChangeListener != null)
            pageChangeListener.onPageSelected(0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            currentTouchDownX = event.getX();
            return super.onInterceptTouchEvent(event);
        } else if (checkPagingState(event)) {
            checkIllegallyRequestedNextPage(event);
            return false;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            currentTouchDownX = event.getX();
            return super.onTouchEvent(event);
        } else if (checkPagingState(event)) {
            checkIllegallyRequestedNextPage(event);
            return false;
        }

        return super.onTouchEvent(event);
    }



    private boolean checkPagingState(MotionEvent event) {
        if (!pagingEnabled) {
            return true;
        }
        if (!nextPagingEnabled) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                currentTouchDownX = event.getX();
            }
            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (detectSwipeToRight(event)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkIllegallyRequestedNextPage(MotionEvent event) {
        int swipeThreshold = 25;
        if (event.getAction() == MotionEvent.ACTION_MOVE && Math.abs(event.getX() - currentTouchDownX) >= swipeThreshold) {
            if(System.currentTimeMillis() - illegallyRequestedNextPageLastCalled >= ON_ILLEGALLY_REQUESTED_NEXT_PAGE_MAX_INTERVAL) {
                illegallyRequestedNextPageLastCalled = System.currentTimeMillis();
            }
        }
    }

    private boolean detectSwipeToRight(MotionEvent event) {
        final int SWIPE_THRESHOLD = 0;
        boolean result = false;
        try {
            float diffX = event.getX() - currentTouchDownX;
            if (Math.abs(diffX) > SWIPE_THRESHOLD) {
                if (diffX < 0) {
                    result = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        this.pagingEnabled = pagingEnabled;
    }
}