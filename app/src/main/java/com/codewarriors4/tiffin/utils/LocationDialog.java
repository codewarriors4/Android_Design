package com.codewarriors4.tiffin.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;

import java.util.regex.Pattern;

public class LocationDialog extends DialogFragment {

    public interface LocationDiglogActivityListner {
        public void onDialogPostiveClick(String postalCode);

        public void onDialogNegativeClick();
    }

    LocationDiglogActivityListner mListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LocationDiglogActivityListner) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.location_diglog_box, null);
        builder.setView(inflate);
        final TextView postalUserInput = (TextView) inflate.findViewById(R.id.location_postal_code);
        builder.setMessage("Hello")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        mListener.onDialogPostiveClick(stringValidation(postalUserInput.getEditableText().toString()));
                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick();
                    }
                });
        return builder.create();
    }

    private String stringValidation(String str) {
        String strAllCaps = str.toUpperCase();
        Pattern pattern = Pattern.compile(Constants.POSTALCODEREGEX);
        if (pattern.matcher(strAllCaps).matches()) {
            return strAllCaps;
        } else {
            if (str.length() == 6) {
                String finalStr = strAllCaps.substring(0, 3) + " " + strAllCaps.substring(3, 6);
                if (pattern.matcher(finalStr).matches()) {
                    return finalStr;
                } else {
                    return "";
                }
            } else {
                return "";
            }
        }
    }


}
