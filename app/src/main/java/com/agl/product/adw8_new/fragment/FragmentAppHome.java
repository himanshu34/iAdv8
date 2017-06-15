package com.agl.product.adw8_new.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.activity.DataActivity;
import com.agl.product.adw8_new.activity.LeadDashBoardActivity;
import com.agl.product.adw8_new.adapter.AdapterAppHome;
import com.agl.product.adw8_new.model.BeanAppHomeGrid;

import java.util.ArrayList;
import java.util.List;

public class FragmentAppHome extends Fragment {

    public static final String TAG = "FragmentAppHome";
    private RecyclerView gridViewAppHome;
    private RelativeLayout progress;
    private Activity activity;
    Dialog dialog;
    private ListView dialogListView;
    ArrayList<String> checkedValue;

    public static FragmentAppHome newInstance() {
        FragmentAppHome fragmentAppHome = new FragmentAppHome();
        return fragmentAppHome;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        try {
            AdapterAppHome adapterGridView = new AdapterAppHome(activity, getData());
            view = inflater.inflate(R.layout.fragment_apphome, container, false);
            gridViewAppHome = (RecyclerView) view.findViewById(R.id.grid_app_home);
            progress = (RelativeLayout) view.findViewById(R.id.progress);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 2);
            gridViewAppHome.setLayoutManager(gridLayoutManager);
            gridViewAppHome.setAdapter(adapterGridView);
            gridViewAppHome.addOnItemTouchListener(new RecyclerTouchListener(activity, gridViewAppHome, new ClickListener() {

                @Override
                public void onClick(View view, int position) {
//					Toast.makeText(activity, "Item Clicked postion : "+position, 0).show();
                    LaunchActivity(position);
                }

                @Override
                public void onLongClick(View view, int position) {
                    // TODO Auto-generated method stub
                }
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void LaunchActivity(int position) {
        switch (position) {
            case 0:
                Intent intentCampaign = new Intent(activity, DataActivity.class);
                startActivity(intentCampaign);
                break;

            case 1:
//                Intent intentLms = new Intent(activity, InsightsActivity.class);
//                startActivity(intentLms);
                break;

            case 2:
                Intent intentLeads = new Intent(activity, LeadDashBoardActivity.class);
                startActivity(intentLeads);
                break;

            case 3:
//                Intent intentTask = new Intent(activity, WebFormHome.class);
//                startActivity(intentTask);
                break;

            case 4:
//                Intent intentNotification = new Intent(activity, NotificationScreenActivity.class);
//                startActivity(intentNotification);
                break;
        }
    }


    private List<BeanAppHomeGrid> getData() {
        List<BeanAppHomeGrid> data = new ArrayList<BeanAppHomeGrid>();
        int[] arrImages = {R.drawable.btn_paid_dashboard, R.drawable.btn_analytics_dashboard,
                R.drawable.btn_leads_dashboard, R.drawable.btn_web_form, R.drawable.hrm};
        String[] arrTitles = {"Campaigns", "Insights", "Leads","Web Forms","Notification"};

        for (int i = 0; i < arrImages.length && i < arrTitles.length; i++) {
            BeanAppHomeGrid beanAppHomeGrid = new BeanAppHomeGrid();
            beanAppHomeGrid.setImage(arrImages[i]);
            beanAppHomeGrid.setTitle(arrTitles[i]);
            data.add(beanAppHomeGrid);
        }

        return data;
    }

    public void onCustomDialogItemClick(int position) {
        Log.e(TAG, "NOW position " + position);
    }

    public static interface ClickListener {
        public void onClick(View view, int position);
        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

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
                            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && clickListener != null) {
                                clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(motionEvent)) {
                clickListener.onClick(child, recyclerView.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }
}
