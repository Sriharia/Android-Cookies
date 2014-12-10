package com.srihary.myapplications;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

public class MultiStateImageView extends ImageView {

	public MultiStateImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		density = getResources().getDisplayMetrics().density;
	}

	public MultiStateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = getResources().getDisplayMetrics().density;
	}

	public MultiStateImageView(Context context) {
		super(context);
		density = getResources().getDisplayMetrics().density;
	}

	private static final int MAX_CLICK_DISTANCE = 15;

	private long pressStartTime;
	private float pressedX;
	private float pressedY;

	private static final int MAX_CLICK_DURATION = 500;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (null == getDrawable())
			return super.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			pressStartTime = System.currentTimeMillis();
			pressedX = event.getX();
			pressedY = event.getY();
			// overlay is white with transparency of 0xAC (172)
			getDrawable().setColorFilter(0xACffffff, PorterDuff.Mode.SRC_ATOP);
			invalidate();
			break;
		}
		case MotionEvent.ACTION_UP:
			long pressDuration = System.currentTimeMillis() - pressStartTime;
			if (pressDuration < MAX_CLICK_DURATION
					&& distance(pressedX, pressedY, event.getX(), event.getY()) < MAX_CLICK_DISTANCE) {
				performClick();
			}
		case MotionEvent.ACTION_CANCEL: {
			// clear the overlay
			getDrawable().clearColorFilter();
			invalidate();
			break;
		}
		}
		return true;
	}

	private static float distance(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
		return pxToDp(distanceInPx);
	}

	private static float density;

	private static float pxToDp(float px) {
		return px / density;
	}

}
