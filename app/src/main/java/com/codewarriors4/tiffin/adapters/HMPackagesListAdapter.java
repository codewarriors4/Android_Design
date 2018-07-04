package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.models.HMPackagesModel;

import java.util.List;

public class HMPackagesListAdapter extends RecyclerView.Adapter<HMPackagesListAdapter.HMPackagesViewHolder>{

    private Context HMPackCtxt;
    private List<HMPackagesModel> HMPackageList;

    public HMPackagesListAdapter(Context HMPackCtxt, List<HMPackagesModel> MPackageList) {
        this.HMPackCtxt = HMPackCtxt;
        this.HMPackageList = MPackageList;
    }

    @NonNull
    @Override
    public HMPackagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(HMPackCtxt);

        View view = inflater.inflate(R.layout.hm_packages_recycler_listview_layout, null);

        HMPackagesViewHolder holder = new HMPackagesViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HMPackagesViewHolder holder, int position) {
        HMPackagesModel hmPackage = HMPackageList.get(position);

        holder.HMPackTitle.setText(hmPackage.getPackTitle());
        holder.HMPackDesc.setText(hmPackage.getPackDesc());
        holder.HMPackCost.setText(String.valueOf(hmPackage.getPackCost()));
        holder.HMEditPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return HMPackageList.size();
    }

    class HMPackagesViewHolder extends RecyclerView.ViewHolder {

        public TextView HMPackTitle, HMPackDesc, HMPackCost, HMPackLabel;
        public Button HMEditPackage;
        public HMPackagesViewHolder(View itemView) {
            super(itemView);

            HMPackTitle = itemView.findViewById(R.id.hm_pack_title_listview);
            HMPackDesc = itemView.findViewById(R.id.hm_pack_desc_listview);
            HMPackCost = itemView.findViewById(R.id.hm_pack_cost);
            HMPackLabel = itemView.findViewById(R.id.hm_pack_cost_label);
            HMEditPackage = itemView.findViewById(R.id.hm_edit_pack);


        }
    }
}