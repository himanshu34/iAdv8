package com.agl.product.adw8_new.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.FieldsData;
import com.agl.product.adw8_new.model.LmsLead;
import com.agl.product.adw8_new.utils.Utils;

import java.util.ArrayList;

public class AdapterLeads extends RecyclerView.Adapter<AdapterLeads.MyViewHolder> {
	
	private static final String TAG = "AdapterLeads :: ";
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<LmsLead> arrLmsLead = new ArrayList<LmsLead>();
	private String LeadType;
	
	public AdapterLeads(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	class MyViewHolder extends RecyclerView.ViewHolder{
		
		private TextView txtHeaderDate, txtStatus, txtSource;
		ViewGroup linChild;

		public MyViewHolder(View view) {
			super(view);
			txtHeaderDate = (TextView) view.findViewById(R.id.txt_lms_lead_date);
			txtStatus = (TextView) view.findViewById(R.id.txt_lms_lead_status);
			txtSource = (TextView) view.findViewById(R.id.txt_lms_lead_source);
			linChild = (ViewGroup) view.findViewById(R.id.lin_child);
		}
	}

	@Override
	public int getItemCount() {
		return arrLmsLead.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder viewHolder, int pos) {
		LmsLead currentLmsLead = arrLmsLead.get(pos);
		if(currentLmsLead.getLead_type().equalsIgnoreCase(Utils.TYPE_LEAD_WEB)) {
//			viewHolder.llCallingNo.setVisibility(View.GONE);
//			viewHolder.llRecording.setVisibility(View.GONE);
		}
		viewHolder.txtHeaderDate.setText(currentLmsLead.getSubmitted_on());
		viewHolder.txtStatus.setText(currentLmsLead.getLead_status());
		viewHolder.txtSource.setText(currentLmsLead.getLead_type());

		viewHolder.linChild.removeAllViews();
		for(FieldsData u: currentLmsLead.getFieldsDataList()) {
			View layout = inflater.inflate(R.layout.leads_item_row, viewHolder.linChild, false);
			TextView textLabel = (TextView) layout.findViewById(R.id.txt_label);
			TextView textValue = (TextView) layout.findViewById(R.id.txt_val);
			textLabel.setText(u.getLable()+":");
			textValue.setText(u.getValue());
			viewHolder.linChild.addView(layout);
		}

//		View layout_fixed = inflater.inflate(R.layout.leads_item_fixed, viewHolder.linChild, false);
//		TextView textLastFollowValue = (TextView) layout_fixed.findViewById(R.id.txt_last_follow_val);
//		TextView textNextFollowValue = (TextView) layout_fixed.findViewById(R.id.txt_next_follow_val);
//		textLastFollowValue.setText(currentLmsLead.getLast_followup());
//		textNextFollowValue.setText(currentLmsLead.getNext_followup());
//		viewHolder.linChild.addView(layout_fixed);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = null;
		MyViewHolder viewHolder = null;
		try {
			view = inflater.inflate(R.layout.leads_item, parent, false);
			viewHolder = new MyViewHolder(view);
		} catch (Exception e) {
			Log.e(TAG, e.toString());
		}
		
		return viewHolder;
	}

	public void addData(ArrayList<LmsLead> ArrLmsLead, boolean isLazyLoading) {
		if(isLazyLoading){
			arrLmsLead = ArrLmsLead;
            notifyDataSetChanged();
		} else {
			arrLmsLead = ArrLmsLead;
            notifyDataSetChanged();
		}
	}
}