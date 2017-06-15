package com.agl.product.adw8_new.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.agl.product.adw8_new.R;
import com.agl.product.adw8_new.utils.DateHelper;
import com.agl.product.adw8_new.utils.OnCustomDateDialogClick;
import com.agl.product.adw8_new.utils.Utils;

public class CustomDateSelectorDialog extends DialogFragment implements OnClickListener, AdapterView.OnItemSelectedListener {
	private static final String TAG = "CustomDateSelector ::";
	private Button btnFromStart, btnFromEnd, btnToStart, btnToEnd, btnCancel, btnApply;
	private Spinner spinnerDate;
	private CheckBox chkCampare;
	private String campareArray[] = {"Previous Period", "Custom" };
	private ArrayAdapter<String> campareAdapter;
	private String toDate, fromDate, toCmpDate, fromCmpDate = "";
	private OnCustomDateDialogClick mCallBack;
	private boolean isCompareDialog = true;
	private RelativeLayout rlMiddle, rlEnd;

	public static CustomDateSelectorDialog newInstance(boolean isCompareDialog) {
		CustomDateSelectorDialog customDateSelectorDialog = new CustomDateSelectorDialog();
		customDateSelectorDialog.isCompareDialog = isCompareDialog;
		return customDateSelectorDialog;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			Log.e(TAG,"on attched of customDateSeletor");
			mCallBack = (OnCustomDateDialogClick) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+ " must implements OnCustomDateDialogClick");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = null;
		try {
			view = inflater.inflate(R.layout.custom_date_widget, container, false);
			getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			rlMiddle = (RelativeLayout) view.findViewById(R.id.rl_middle);
			rlEnd = (RelativeLayout) view.findViewById(R.id.rl_end);
			
			if(isCompareDialog){
				rlMiddle.setVisibility(View.VISIBLE);
				rlEnd.setVisibility(View.VISIBLE);
			}else{
				rlMiddle.setVisibility(View.GONE);
				rlEnd.setVisibility(View.GONE);
			}
			
			campareAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, campareArray);
			campareAdapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerDate = (Spinner) view.findViewById(R.id.spin_date);
			spinnerDate.setAdapter(campareAdapter);
			chkCampare = (CheckBox) view.findViewById(R.id.chkCampare);
			btnFromStart = (Button) view.findViewById(R.id.btn_from_start);
			btnFromEnd = (Button) view.findViewById(R.id.btn_from_end);
			btnToStart = (Button) view.findViewById(R.id.btn_to_start);
			btnToEnd = (Button) view.findViewById(R.id.btn_to_end);
			btnApply = (Button) view.findViewById(R.id.btn_apply);
			btnCancel = (Button) view.findViewById(R.id.btn_cancel);
			btnFromStart.setOnClickListener(this);
			btnFromEnd.setOnClickListener(this);
			btnToStart.setOnClickListener(this);
			btnApply.setOnClickListener(this);
			btnCancel.setOnClickListener(this);
			setDefaultPreviousWeek();
			spinnerDate.setEnabled(false);
			chkCampare
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if (chkCampare.isChecked()) {
								spinnerDate.setEnabled(true);
								btnToStart.setEnabled(true);
								setDefaultToDates();
							} else {
								spinnerDate.setEnabled(false);
								btnToStart.setEnabled(false);
							}
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_apply:
//			For custom date only
			Log.e(TAG, "fromDate "+ fromDate);
			Log.e(TAG, "toDate "+ toDate);
			Log.e(TAG, "fromCmpDate "+ fromCmpDate);
			Log.e(TAG, "toCmpDate "+ toCmpDate);
			mCallBack.onCustomDateSelectorSelected(toDate, fromDate, toCmpDate, fromCmpDate);
			dismiss();
			break;
		case R.id.btn_cancel:
			dismiss();
			break;
		case R.id.btn_from_start:
			startDateWidget(Utils.BTN_FROM_START);
			break;
		case R.id.btn_from_end:
			startDateWidget(Utils.BTN_FROM_END);
			break;
		case R.id.btn_to_start:
			startDateWidget(Utils.BTN_TO_START);
			break;

		default:
			break;
		}
	}

	private void startDateWidget(int startTag) {
		FragmentManager fragmentManager = getActivity() .getSupportFragmentManager();
		DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
		datePickerDialogFragment .show(fragmentManager, String.valueOf(startTag));
	}

	public void setFromDate(int[] date, int mTAG) {
		
		int day = date[0];
		int month = date[1];
		int year = date[2];
		
		String strDateDisplayFormat = DateHelper.getDateDisplayFormat(day, month, year);
		String strDateUrlFormat = DateHelper.getDateUrlFormat(day, month, year);
		
		switch (mTAG) {
		case Utils.BTN_FROM_START:
			fromDate = strDateUrlFormat;
			btnFromStart.setText(strDateDisplayFormat);
			break;
			
		case Utils.BTN_FROM_END:
			toDate = strDateUrlFormat;
			btnFromEnd.setText(strDateDisplayFormat);
			break;
			
		case Utils.BTN_TO_START:
			System.out.println("spinnerDate.getSelectedItemPosition() " + spinnerDate.getSelectedItemPosition());
			fromCmpDate = strDateUrlFormat;
			btnToStart.setText(strDateDisplayFormat);
			if (spinnerDate.getSelectedItemPosition() == 1) {
				setCustomBtnToEndTxt();
			}
			else if (spinnerDate.getSelectedItemPosition() == 0) {
				setPreviousBtnToEndTxt();
			}
			break;

		default:
			break;
		}
	}

	private void setPreviousBtnToEndTxt() {
		long daysDifference = 6;
		long toDate = DateHelper.fromStringToDate(btnToStart.getText()
				.toString());
		String setBtnToEndDatetxt = DateHelper.setBtnToEndDate(toDate, daysDifference);
		btnToEnd.setText(setBtnToEndDatetxt);
		toCmpDate = DateHelper.setBtnToEndDateUrlFormat(toDate, daysDifference);
	}

	private void setCustomBtnToEndTxt() {
		long daysDifference = getDateDifference();
		long toDate = DateHelper.fromStringToDate(btnToStart.getText() .toString());
		String setBtnToEndDatetxt = DateHelper.setBtnToEndDate(toDate, daysDifference);
		toCmpDate = DateHelper.setBtnToEndDateUrlFormat(toDate, daysDifference);
		btnToEnd.setText(setBtnToEndDatetxt);
	}

	private void setDefaultPreviousWeek() {
		btnFromStart.setText(DateHelper.getLastSevenDays());
		btnFromEnd.setText(DateHelper.yesterday());
		
		fromDate = DateHelper.getLastSevenDaysUrlFormat();
		toDate = DateHelper.yesterdayUrlFormat();
	}

	private void setDefaultToDates() {
		btnToStart.setText(DateHelper.getLast15Days());
		btnToEnd.setText(DateHelper.getLast8Days());
		
		fromCmpDate = DateHelper.getLast15DaysUrlFormat(); 
		toCmpDate = DateHelper.getLast8DaysUrlFormat();
		
	}

	private long getDateDifference() {
		long mDateDiffernce = DateHelper.fromStringToDate(btnFromEnd.getText().toString()) - DateHelper.fromStringToDate(btnFromStart.getText().toString());
		long seconds = mDateDiffernce / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;
		Log.e(TAG, "days" + days);
		return days;
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
		switch (position) {
			case 0:
				setPreviousBtnToEndTxt();
				break;
			case 1:
				setCustomBtnToEndTxt();
				break;

			default:
				break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}
}
