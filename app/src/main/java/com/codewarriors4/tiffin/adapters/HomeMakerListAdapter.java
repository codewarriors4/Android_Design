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
import com.codewarriors4.tiffin.TSViewHMProfile;
import com.codewarriors4.tiffin.models.HomeMakerListItems;

import java.util.List;

public class HomeMakerListAdapter extends RecyclerView.Adapter<HomeMakerListAdapter.HomeMakerListItemViewHolder>
{
    private Context TSViewHMPackCtxt;
    private List<HomeMakerListItems> HomeMakerListItems;

    public HomeMakerListAdapter(Context TSViewHMPackCtxt, List<com.codewarriors4.tiffin.models.HomeMakerListItems> homeMakerListItems) {

        this.TSViewHMPackCtxt = TSViewHMPackCtxt;
        this.HomeMakerListItems = homeMakerListItems;
    }

    public HomeMakerListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(TSViewHMPackCtxt);

        View view = inflater.inflate(R.layout.home_maker_item, null);

        HomeMakerListItemViewHolder holder = new HomeMakerListItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(HomeMakerListItemViewHolder holder, int position) {
        final HomeMakerListItems listIteam = HomeMakerListItems.get(position);
        holder.homemakeEmail.setText(listIteam.getHomeMakerEmail());
        holder.homeMakerPostCode.setText(listIteam.getHomeMakerPostCode());
        holder.homemakeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSViewHMPackCtxt, TSViewHMProfile.class);
                i.putExtra("Id", listIteam.getId()+"");
                i.putExtra("HMId", listIteam.getHmId()+"");
                TSViewHMPackCtxt.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return HomeMakerListItems.size();
    }

    class HomeMakerListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView homemakeEmail, homeMakerPostCode;

        public HomeMakerListItemViewHolder(View itemView) {
            super(itemView);
            homemakeEmail = itemView.findViewById(R.id.home_maker_email);
            homeMakerPostCode = itemView.findViewById(R.id.home_maker_postalcode);
        }
    }

}
