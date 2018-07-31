package com.codewarriors4.tiffin.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codewarriors4.tiffin.R;

import java.util.regex.Pattern;

public class PaymentSucessDialog extends DialogFragment {

    public interface PaymentSucessListner {
        public void onClickListner();
    }

    PaymentSucessDialog mListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (PaymentSucessDialog) context;
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
        View inflate = inflater.inflate(R.layout.payment_sucess_popup, null);
        builder.setView(inflate);
        final Button finishPaymentButton = inflate.findViewById(R.id.finish_payment_btn);
        finishPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Started", Toast)
            }
        });

        return builder.create();
    }

}
