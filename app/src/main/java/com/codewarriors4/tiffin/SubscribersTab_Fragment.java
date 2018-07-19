package com.codewarriors4.tiffin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

public class SubscribersTab_Fragment extends android.support.v4.app.Fragment
{
    private String str;
    public static final String KEY = "key_args";
    private View inflate;
    public SubscribersTab_Fragment(){

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.subscribers_tab_fragment, container, false);
        initString();
        return inflate;

    }

    private void initString() {

    }
}
