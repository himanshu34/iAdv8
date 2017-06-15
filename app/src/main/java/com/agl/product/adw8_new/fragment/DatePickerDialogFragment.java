package com.agl.product.adw8_new.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.utils.OnCustomDateDialogClick;

public class DatePickerDialogFragment extends DialogFragment implements OnClickListener {

	private static final String TAG = "DatePickerDialog";
	private Button btnOk, btnCancel;
	private DatePicker datepickerStart;
	private OnCustomDateDialogClick mCallBack;

	public static DatePickerDialogFragment newInstance(){
		DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
		return datePickerDialogFragment;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallBack = (OnCustomDateDialogClick) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+ " must implements OnCustomDateDialogClick");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.datepicker, container);
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		datepickerStart = (DatePicker) view .findViewById(R.id.datePicker_start);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ok:
			Log.e(TAG,"btn_ok clciked");
			int day = datepickerStart.getDayOfMonth();
			int month  = datepickerStart.getMonth()+1;
			int year = datepickerStart.getYear();
			mCallBack.onCustomDatePickerSelected(new int[]{day, month, year}, Integer.valueOf(getTag()));
			getDialog().dismiss();
			break;
		case R.id.btn_cancel:
			getDialog().dismiss();
			break;

		default:
			break;
		}
	}

}