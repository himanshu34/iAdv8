package com.agl.product.adw8_new.utils;

public interface OnCustomDateDialogClick {
	
	public void onCustomDatePickerSelected(int[] date, Integer tag);
	
	public void onCustomDateSelectorSelected(String toDate, String fromDate, String toCmpDate, String fromCmpDate);
	
}
