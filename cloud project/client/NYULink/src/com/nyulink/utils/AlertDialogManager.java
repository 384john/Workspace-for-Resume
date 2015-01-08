package com.nyulink.utils;

import android.app.AlertDialog;
import android.content.Context;

public class AlertDialogManager {

	// one-button alert dialog
	public static AlertDialog showAlert(Context context, String title, String message, String button) {
		return new AlertDialog.Builder(context)
					.setTitle(title)
					.setMessage(message)
					.setPositiveButton(button, null)
					.show();
	}

}
