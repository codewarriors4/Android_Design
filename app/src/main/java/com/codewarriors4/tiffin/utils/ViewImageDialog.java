package com.codewarriors4.tiffin.utils;

import android.app.Activity;
import android.app.DialogFragment;

public class ViewImageDialog extends DialogFragment {
    public interface LocationDiglogActivityListner {
        public void onDialogPostiveClick(String postalCode);

        public void onDialogNegativeClick();
    }

    LocationDialog.LocationDiglogActivityListner mListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LocationDialog.LocationDiglogActivityListner) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }
}
