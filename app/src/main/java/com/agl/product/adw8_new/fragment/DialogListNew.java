package com.agl.product.adw8_new.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.Owner;
import com.agl.product.adw8_new.utils.CustomDialogClickListner;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;

public class DialogListNew extends DialogFragment implements AdapterView.OnItemClickListener {

    private static final String TAG = null;
    private Context context;
    private ArrayList<String> arrData;
    private Activity activity;
    private String titleText = null;

    public static DialogListNew newInstance(ArrayList<String> arrData, Context context, String titleText) {
        DialogListNew dialogList = new DialogListNew();
        dialogList.context = context;
        dialogList.arrData = arrData;
        dialogList.titleText = titleText;
        return dialogList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Utils.isEmptyString(titleText)) {
            setStyle(STYLE_NO_TITLE, 0);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ListView listView = null;
        if(!Utils.isEmptyString(titleText)) {
            getDialog().setTitle(titleText);
        }
        try {
            listView = new ListView(getActivity());
            listView.setBackgroundColor(context.getResources().getColor(R.color.white));
            listView.setOnItemClickListener(this);
            listView.setAdapter(new BaseAdapter() {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = convertView;
                    if(view == null){
                        view = inflater.inflate(R.layout.owner_list_item, parent, false);
                        ViewHolder viewHolder = new ViewHolder(view);
                        view.setTag(viewHolder);
                    }

                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.txtName.setText(arrData.get(position));

                    return view;
                }

                @Override
                public long getItemId(int position) {
                    return 0;
                }

                @Override
                public Object getItem(int position) {
                    return null;
                }

                @Override
                public int getCount() {
                    return arrData.size();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

        return listView;
    }

    class ViewHolder {
        protected TextView txtName;
        public ViewHolder(View view) {
            txtName = (TextView) view.findViewById(R.id.txt_name);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CustomDialogClickListner customDialogClickListner = (CustomDialogClickListner) activity;
        customDialogClickListner.onCustomDialogClick(position);
        getDialog().dismiss();
    }
}
