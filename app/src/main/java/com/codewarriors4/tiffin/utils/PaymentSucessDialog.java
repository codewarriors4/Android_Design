package com.codewarriors4.tiffin.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;

public class PaymentSucessDialog extends DialogFragment {
    private int pId;
    private String pAmt;

    public void setValues(int pId, String pAmt) {
        this.pId = pId;
        this.pAmt = pAmt;
    }

    public interface PaymentSuccessListener extends View.OnClickListener {
        @Override
        void onClick(View v);
        void getMainActivity();
    }

    PaymentSuccessListener mListener;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (PaymentSuccessListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflate = inflater.inflate(R.layout.payment_sucess_popup, null);
        builder.setView(inflate);
        TextView tidView = inflate.findViewById(R.id.transaction_id_field);
        TextView costView = inflate.findViewById(R.id.package_cost_field);
        tidView.setText(pId + "");
        costView.setText(pAmt);
        final Button finishPaymentButton = inflate.findViewById(R.id.finish_payment_btn);
        finishPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.getMainActivity();
            }
        });
        return builder.create();
    }

}
