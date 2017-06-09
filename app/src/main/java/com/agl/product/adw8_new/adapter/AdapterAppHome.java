package com.agl.product.adw8_new.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.model.BeanAppHomeGrid;

import java.util.ArrayList;
import java.util.List;

public class AdapterAppHome extends RecyclerView.Adapter<AdapterAppHome.MyViewHolder> {

	private static final String TAG = "AdapterAppHome :: ";
	private List<BeanAppHomeGrid> data = new ArrayList();
	private LayoutInflater inflater;
	private Context context;

	public AdapterAppHome(Context context, List<BeanAppHomeGrid> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		BeanAppHomeGrid current = data.get(position);
		holder.imgMain.setImageResource(current.getImage());
		holder.txtMain.setText(current.getTitle());
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
		View view = null;
		MyViewHolder myViewHolder = null;
		try {
			view = inflater.inflate(R.layout.grid_item_layout, parent, false);
			myViewHolder = new MyViewHolder(view);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return myViewHolder;
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView imgMain;
		TextView txtMain;

		public MyViewHolder(View view) {
			super(view);
			imgMain = (ImageView) view.findViewById(R.id.img_main);
			txtMain = (TextView) view.findViewById(R.id.txt_main);
		}
	}
}
