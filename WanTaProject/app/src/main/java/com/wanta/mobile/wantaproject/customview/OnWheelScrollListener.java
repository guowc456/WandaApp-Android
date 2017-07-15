package com.wanta.mobile.wantaproject.customview;

/**
 * Created by WangYongqiang on 2016/11/26.
 */
/**
 * Wheel scrolled listener interface.
 */
public interface OnWheelScrollListener {
    /**
     * Callback method to be invoked when scrolling started.
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingStarted(MultiWheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingFinished(MultiWheelView wheel);
}
