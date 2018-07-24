package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.TSReviewHM;
import com.codewarriors4.tiffin.TSViewTSSubscription;
import com.codewarriors4.tiffin.models.TSSubscriptionsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TSViewSubsListAdapter extends RecyclerView.Adapter<TSViewSubsListAdapter.TSSubscriptionsViewHolder>{

    private Context TSSubscriptionsCtxt;
    private List<TSSubscriptionsModel> TSSubsList;

    public TSViewSubsListAdapter(Context TSSubscriptionsCtxt, List<TSSubscriptionsModel> MPackageList) {
        this.TSSubscriptionsCtxt = TSSubscriptionsCtxt;
        this.TSSubsList = MPackageList;
    }

    @NonNull
    @Override
    public TSSubscriptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(TSSubscriptionsCtxt);
        View view = inflater.inflate(R.layout.ts_view_subs_recyclerview, null);
        TSSubscriptionsViewHolder holder = new TSSubscriptionsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TSSubscriptionsViewHolder holder, int position) {
        final TSSubscriptionsModel TSSubscription = TSSubsList.get(position);



        holder.hm_name.setText(TSSubscription.getHmName());
        holder.package_title.setText(TSSubscription.getPackTitle());
        holder.package_cost.setText(String.valueOf(TSSubscription.getPackage_cost())+" CAD");
        holder.ratingBar.setRating(TSSubscription.getRatingCount());


        String subStartDate =  TSSubscription.getSubStartDate();
        String subEndDate =  TSSubscription.getSubEndDate();
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date tempsubStartDate = formatIn.parse(subStartDate);
            Date tempsubEndDate = formatIn.parse(subEndDate);

            formatIn = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
            String subscription_start_date = formatIn.format(tempsubStartDate);
            String subscription_end_date = formatIn.format(tempsubEndDate);
            holder.subscription_start_date.setText(subscription_start_date);
            holder.subscription_end_date.setText(subscription_end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.hm_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSSubscriptionsCtxt, TSViewTSSubscription.class);
                i.putExtra("HomeMakerId", TSSubscription.getHmID());
                i.putExtra("package_id", String.valueOf(TSSubscription.getPackID()));
                i.putExtra("sub_id", String.valueOf(TSSubscription.getSubID()));
                TSSubscriptionsCtxt.startActivity(i);
            }
        });

        holder.package_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(TSSubscriptionsCtxt, TSViewTSSubscription.class);
                i.putExtra("HomeMakerId", TSSubscription.getHmID());
                i.putExtra("package_id", String.valueOf(TSSubscription.getPackID()));
                i.putExtra("sub_id", String.valueOf(TSSubscription.getSubID()));

                TSSubscriptionsCtxt.startActivity(i);
            }
        });

        holder.package_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSSubscriptionsCtxt, TSViewTSSubscription.class);
                i.putExtra("HomeMakerId", TSSubscription.getHmID());
                i.putExtra("package_id", String.valueOf(TSSubscription.getPackID()));
                i.putExtra("sub_id", String.valueOf(TSSubscription.getSubID()));
                TSSubscriptionsCtxt.startActivity(i);
            }
        });

        holder.subscription_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSSubscriptionsCtxt, TSViewTSSubscription.class);
                i.putExtra("HomeMakerId", TSSubscription.getHmID());
                i.putExtra("package_id", String.valueOf(TSSubscription.getPackID()));
                i.putExtra("sub_id", String.valueOf(TSSubscription.getSubID()));
                TSSubscriptionsCtxt.startActivity(i);
            }
        });

        holder.subscription_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSSubscriptionsCtxt, TSViewTSSubscription.class);
                i.putExtra("HomeMakerId", TSSubscription.getHmID());
                i.putExtra("package_id", String.valueOf(TSSubscription.getPackID()));
                i.putExtra("sub_id", String.valueOf(TSSubscription.getSubID()));
                TSSubscriptionsCtxt.startActivity(i);
            }
        });

        holder.ratingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TSSubscriptionsCtxt, TSReviewHM.class);
                i.putExtra("HomeMakerId", TSSubscription.getHmID());
                i.putExtra("HMName", TSSubscription.getHmName());
                TSSubscriptionsCtxt.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return TSSubsList.size();
    }

    class TSSubscriptionsViewHolder extends RecyclerView.ViewHolder {

        public TextView hm_name, package_title, package_cost,subscription_start_date, subscription_end_date;
        public RatingBar ratingBar;
        public TSSubscriptionsViewHolder(View itemView) {
            super(itemView);

            hm_name = itemView.findViewById(R.id.hm_name);
            package_title = itemView.findViewById(R.id.package_title);
            package_cost = itemView.findViewById(R.id.package_cost);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            subscription_start_date = itemView.findViewById(R.id.subscription_start_date);
            subscription_end_date = itemView.findViewById(R.id.subscription_end_date);





        }
    }
}
