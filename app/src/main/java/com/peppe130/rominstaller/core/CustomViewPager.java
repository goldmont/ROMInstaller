/*

    Copyright © 2016, Giuseppe Montuoro and Paolo Rotolo.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

package com.peppe130.rominstaller.core;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.peppe130.rominstaller.ControlCenter;


public class CustomViewPager extends ViewPager {

    private boolean pagingEnabled;
    private float currentTouchDownX;
    private boolean nextPagingEnabled;
    private OnPageChangeListener pageChangeListener;
    private long illegallyRequestedNextPageLastCalled;
    private static final int ON_ILLEGALLY_REQUESTED_NEXT_PAGE_MAX_INTERVAL = 1000;

    public CustomViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);

        pagingEnabled = !ControlCenter.BUTTON_UI;

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

        if (super.getCurrentItem() == 0 && item == 0) {

            invokeMeLater = true;

        }

        super.setCurrentItem(item);

        if (invokeMeLater && pageChangeListener != null) {

            pageChangeListener.onPageSelected(0);

        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

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

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

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

}