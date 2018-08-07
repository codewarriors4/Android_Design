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
    private TSViewSubsListAdapter.OnItemClickListener mListener;

    public TSViewHMPackagesListAdapter(Context TSViewHMPackCtxt, List<HMPackagesModel> MPackageList) {
        this.TSViewHMPackCtxt = TSViewHMPackCtxt;
        this.HMPackageList = MPackageList;
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(TSViewSubsListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public TSViewHMPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(TSViewHMPackCtxt);

        View view = inflater.inflate(R.layout.ts_view_hm_packages_recycler, null);

        TSViewHMPackagesViewHolder holder = new TSViewHMPackagesViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TSViewHMPackagesViewHolder holder, int position) {
        final HMPackagesModel hmPackage = HMPackageList.get(position);



        holder.HMPackTitle.setText(hmPackage.getPackTitle());
        holder.HMPackDesc.setText(hmPackage.getPackDesc());
        holder.HMPackCost.setText(String.valueOf(hmPackage.getPackCost())+" CAD");

    }

    @Override
    public int getItemCount() {
        return HMPackageList.size();
    }

    class TSViewHMPackagesViewHolder extends RecyclerView.ViewHolder {

        public TextView HMPackTitle, HMPackDesc, HMPackCost;

        public TSViewHMPackagesViewHolder(View itemView, final TSViewSubsListAdapter.OnItemClickListener listener) {
            super(itemView);

            HMPackTitle = itemView.findViewById(R.id.hm_name);
            HMPackDesc = itemView.findViewById(R.id.package_cost);
            HMPackCost = itemView.findViewById(R.id.hm_pack_cost);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }
}
