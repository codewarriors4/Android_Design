package com.codewarriors4.tiffin;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private RecyclerView recyclerView;

    public SubscribersTab_Fragment(){

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.subscribers_tab_fragment, container, false);
        recyclerView = (RecyclerView) inflate.findViewById(R.id.search_home_maker_list_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return inflate;

    }

    private void initString() {

    }
}
