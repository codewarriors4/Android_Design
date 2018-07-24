package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.TSViewHMPackage;
import com.codewarriors4.tiffin.models.HMPackagesModel;

import java.util.List;

public class TSViewHMPackagesListAdapter extends RecyclerView.Adapter<TSViewHMPackagesListAdapter.TSViewHMPackagesViewHolder>{

    private Context TSViewHMPackCtxt;
    private List<HMPackagesModel> HMPackageList;

    public TSViewHMPackagesListAdapter(Context TSViewHMPackCtxt, List<HMPackagesModel> MPackageList) {
        this.TSViewHMPackCtxt = TSViewHMPackCtxt;
        this.HMPackageList = MPackageList;
    }

    @NonNull
    @Override
    public TSViewHMPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(TSViewHMPackCtxt);

        View view = inflater.inflate(R.layout.ts_view_hm_packages_recycler, null);

        TSViewHMPackagesViewHolder holder = new TSViewHMPackagesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TSViewHMPackagesViewHolder holder, int position) {
        final HMPackagesModel hmPackage = HMPackageList.get(position);



        holder.HMPackTitle.setText(hmPackage.getPackTitle());
        holder.HMPackDesc.setText(hmPackage.getPackDesc());
        holder.HMPackCost.setText(String.valueOf(hmPackage.getPackCost())+" CAD");

        holder.HMPackTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSViewHMPackCtxt, TSViewHMPackage.class);
                i.putExtra("HomeMakerId", "35");
                i.putExtra("package_id", String.valueOf(hmPackage.getPackID()));
                TSViewHMPackCtxt.startActivity(i);
            }
        });

        holder.HMPackDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TSViewHMPackCtxt, TSViewHMPackage.class);
                i.putExtra("HomeMakerId", "35");
                i.putExtra("package_id", String.valueOf(hmPackage.getPackID()));
                TSViewHMPackCtxt.startActivity(i);
            }
        });

        holder.HMPackCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSViewHMPackCtxt, TSViewHMPackage.class);
                i.putExtra("HomeMakerId", "35");
                i.putExtra("package_id", String.valueOf(hmPackage.getPackID()));
                TSViewHMPackCtxt.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return HMPackageList.size();
    }

    class TSViewHMPackagesViewHolder extends RecyclerView.ViewHolder {

        public TextView HMPackTitle, HMPackDesc, HMPackCost;

        public TSViewHMPackagesViewHolder(View itemView) {
            super(itemView);

            HMPackTitle = itemView.findViewById(R.id.hm_name);
            HMPackDesc = itemView.findViewById(R.id.package_cost);
            HMPackCost = itemView.findViewById(R.id.hm_pack_cost);



        }
    }
}
