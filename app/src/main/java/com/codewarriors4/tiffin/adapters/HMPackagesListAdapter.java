package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codewarriors4.tiffin.HomemakerUpdatePackage;
import com.codewarriors4.tiffin.HomemakerViewPackage;
import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.models.HMPackagesModel;

import java.util.List;

public class HMPackagesListAdapter extends RecyclerView.Adapter<HMPackagesListAdapter.HMPackagesViewHolder>{

    private Context HMPackCtxt;
    private List<HMPackagesModel> HMPackageList;
    private HMPackagesListAdapter.OnItemClickListener mListener;

    public HMPackagesListAdapter(Context HMPackCtxt, List<HMPackagesModel> MPackageList) {
        this.HMPackCtxt = HMPackCtxt;
        this.HMPackageList = MPackageList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(HMPackagesListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public HMPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(HMPackCtxt);

        View view = inflater.inflate(R.layout.hm_packages_recycler_listview_layout, null);

        HMPackagesViewHolder holder = new HMPackagesViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HMPackagesViewHolder holder, int position) {
        final HMPackagesModel hmPackage = HMPackageList.get(position);

        holder.HMPackTitle.setText(hmPackage.getPackTitle());
        holder.HMPackDesc.setText(hmPackage.getPackDesc());
        holder.HMPackCost.setText(String.valueOf(hmPackage.getPackCost())+" CAD");


    }

    @Override
    public int getItemCount() {
        return HMPackageList.size();
    }

    class HMPackagesViewHolder extends RecyclerView.ViewHolder {

        public TextView HMPackTitle, HMPackDesc, HMPackCost;
        public Button HMEditPackage, HMViewPackage;
        public HMPackagesViewHolder(View itemView, final OnItemClickListener listener) {
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
