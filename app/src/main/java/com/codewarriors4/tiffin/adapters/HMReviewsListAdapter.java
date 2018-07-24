package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.TSReviewHM;
import com.codewarriors4.tiffin.models.HMReviewsModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HMReviewsListAdapter extends RecyclerView.Adapter<HMReviewsListAdapter.HMReviewsListViewHolder>{

    private Context HMReviewsListCtxt;
    private List<HMReviewsModel> HMReviewsList;

    public HMReviewsListAdapter(Context HMReviewsListCtxt, List<HMReviewsModel> MPackageList) {
        this.HMReviewsListCtxt = HMReviewsListCtxt;
        this.HMReviewsList = MPackageList;
    }

    @NonNull
    @Override
    public HMReviewsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(HMReviewsListCtxt);
        View view = inflater.inflate(R.layout.hm_view_reviews_recycler, null);
        HMReviewsListViewHolder holder = new HMReviewsListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HMReviewsListViewHolder holder, int position) {
        final HMReviewsModel HMReview = HMReviewsList.get(position);



        holder.hm_name.setText(HMReview.getTsName());

        holder.review_updated_date .setText(HMReview.getReviewUpdateDate());
        holder.view_review_desc .setText(HMReview.getRatingDesc());
        holder.ratingBar.setRating(HMReview.getRatingCount());


        String reviewUpdatedDate =  HMReview.getReviewUpdateDate();
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date tempreviewUpdatedDate = formatIn.parse(reviewUpdatedDate);

            formatIn = new SimpleDateFormat("MMM dd, yyyy");
            String review_update_date = formatIn.format(tempreviewUpdatedDate);
            holder.review_updated_date.setText(review_update_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }


      /*  holder.hm_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HMReviewsListCtxt, TSViewHMReview.class);
                i.putExtra("HomeMakerId", HMReview.getHmID());
                i.putExtra("package_id", String.valueOf(HMReview.getPackID()));
                i.putExtra("sub_id", String.valueOf(HMReview.getSubID()));
                HMReviewsListCtxt.startActivity(i);
            }
        });

        holder.package_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(HMReviewsListCtxt, TSViewHMReview.class);
                i.putExtra("HomeMakerId", HMReview.getHmID());
                i.putExtra("package_id", String.valueOf(HMReview.getPackID()));
                i.putExtra("sub_id", String.valueOf(HMReview.getSubID()));

                HMReviewsListCtxt.startActivity(i);
            }
        });

        holder.package_cost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HMReviewsListCtxt, TSViewHMReview.class);
                i.putExtra("HomeMakerId", HMReview.getHmID());
                i.putExtra("package_id", String.valueOf(HMReview.getPackID()));
                i.putExtra("sub_id", String.valueOf(HMReview.getSubID()));
                HMReviewsListCtxt.startActivity(i);
            }
        });

        holder.subscription_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HMReviewsListCtxt, TSViewHMReview.class);
                i.putExtra("HomeMakerId", HMReview.getHmID());
                i.putExtra("package_id", String.valueOf(HMReview.getPackID()));
                i.putExtra("sub_id", String.valueOf(HMReview.getSubID()));
                HMReviewsListCtxt.startActivity(i);
            }
        });

        holder.subscription_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HMReviewsListCtxt, TSViewHMReview.class);
                i.putExtra("HomeMakerId", HMReview.getHmID());
                i.putExtra("package_id", String.valueOf(HMReview.getPackID()));
                i.putExtra("sub_id", String.valueOf(HMReview.getSubID()));
                HMReviewsListCtxt.startActivity(i);
            }
        });

        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Intent i = new Intent(HMReviewsListCtxt, TSReviewHM.class);
                    i.putExtra("HomeMakerId", String.valueOf(HMReview.getHmID()));
                    i.putExtra("HMName", HMReview.getHmName());
                    HMReviewsListCtxt.startActivity(i);
                }
                return true;
            }
        });
*/

    }

    @Override
    public int getItemCount() {
        return HMReviewsList.size();
    }

    class HMReviewsListViewHolder extends RecyclerView.ViewHolder {

        public TextView hm_name,review_updated_date,view_review_desc;
        public RatingBar ratingBar;
        public HMReviewsListViewHolder(View itemView) {
            super(itemView);

            hm_name = itemView.findViewById(R.id.hm_name);
            review_updated_date = itemView.findViewById(R.id.review_updated_date);
            view_review_desc = itemView.findViewById(R.id.view_review_desc);
            ratingBar = itemView.findViewById(R.id.view_ratingBar);





        }
    }
}
