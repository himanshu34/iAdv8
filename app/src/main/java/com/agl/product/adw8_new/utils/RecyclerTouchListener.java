package com.agl.product.adw8_new.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.agl.product.adw8_new.fragment.FragmentAppHome.ClickListener;

public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

	private GestureDetector gestureDetector;
	private ClickListener clickListener;

	public RecyclerTouchListener(Context context,
								 final RecyclerView recyclerView,
								 final ClickListener clickListener) {

		this.clickListener = clickListener;
		gestureDetector = new GestureDetector(context,
				new GestureDetector.SimpleOnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return true;
					}

					@Override
					public void onLongPress(MotionEvent e) {
						View child = recyclerView.findChildViewUnder(
								e.getX(), e.getY());
						if (child != null && clickListener != null) {
							clickListener.onLongClick(child,
									recyclerView.getChildPosition(child));
						}
					}

				});

	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView recyclerView,
										 MotionEvent motionEvent) {
		View child = recyclerView.findChildViewUnder(motionEvent.getX(),
				motionEvent.getY());
		if (child != null && clickListener != null
				&& gestureDetector.onTouchEvent(motionEvent)) {
			clickListener.onClick(child,
					recyclerView.getChildPosition(child));
		}
		return false;
	}

	@Override
	public void onTouchEvent(RecyclerView recyclerView,
							 MotionEvent motionEvent) {
// TODO Auto-generated method stub

	}

	@Override
	public void onRequestDisallowInterceptTouchEvent(boolean b) {

	}

	/*@Override
	public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

	}*/

}