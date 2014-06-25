package tw.tasker.babysitter.view.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ListDialogFragment extends DialogFragment {
	final String[] ListStr ;
	private OnClickListener mListener;
	public ListDialogFragment(String[] phones, OnClickListener onClickListener) {
		ListStr = phones;
		mListener = onClickListener;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
        .setTitle("請選擇要撥打的電話")
        .setItems(ListStr, mListener)
        .create();
	}
}
