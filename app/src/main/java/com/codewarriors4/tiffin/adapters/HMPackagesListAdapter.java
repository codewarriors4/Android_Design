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
        final HMPackagesModel hmPackage = HMPackageList.get(position);



        holder.HMPackTitle.setText(hmPackage.getPackTitle());
        holder.HMPackDesc.setText(hmPackage.getPackDesc());
        holder.HMPackCost.setText(String.valueOf(hmPackage.getPackCost())+" CAD");
        holder.HMEditPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HMPackCtxt, HomemakerUpdatePackage.class);
                i.putExtra("package_id", String.valueOf(hmPackage.getPackID()));
                HMPackCtxt.startActivity(i);
            }
        });
        holder.HMViewPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HMPackCtxt, HomemakerViewPackage.class);
                i.putExtra("package_id", String.valueOf(hmPackage.getPackID()));
                HMPackCtxt.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return HMPackageList.size();
    }

    class HMPackagesViewHolder extends RecyclerView.ViewHolder {

        public TextView HMPackTitle, HMPackDesc, HMPackCost;
        public Button HMEditPackage, HMViewPackage;
        public HMPackagesViewHolder(View itemView) {
            super(itemView);

            HMPackTitle = itemView.findViewById(R.id.hm_pack_title_listview);
            HMPackDesc = itemView.findViewById(R.id.hm_pack_desc_listview);
            HMPackCost = itemView.findViewById(R.id.hm_pack_cost);
            HMEditPackage = itemView.findViewById(R.id.hm_edit_pack);
            HMViewPackage = itemView.findViewById(R.id.view_package_detail_btn);


        }
    }
}
