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
import com.codewarriors4.tiffin.TSViewHMPackage;
import com.codewarriors4.tiffin.TSViewHMProfile;
import com.codewarriors4.tiffin.models.HomeMakerListItem;
import com.codewarriors4.tiffin.models.HomeMakerListItems;

import java.util.List;

public class HomeMakerListAdapter extends RecyclerView.Adapter<HomeMakerListAdapter.HomeMakerListItemViewHolder>
{
    private Context TSViewHMPackCtxt;
    private List<HomeMakerListItems> HomeMakerListItems;
    private TSViewSubsListAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(TSViewSubsListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public HomeMakerListAdapter(Context TSViewHMPackCtxt, List<com.codewarriors4.tiffin.models.HomeMakerListItems> homeMakerListItems) {

        this.TSViewHMPackCtxt = TSViewHMPackCtxt;
        this.HomeMakerListItems = homeMakerListItems;
    }

    public HomeMakerListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(TSViewHMPackCtxt);

        View view = inflater.inflate(R.layout.home_maker_item, null);

        HomeMakerListItemViewHolder holder = new HomeMakerListItemViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(HomeMakerListItemViewHolder holder, int position) {
        final HomeMakerListItems listIteam = HomeMakerListItems.get(position);
        holder.homeMakerFNameView.setText(getFullName(listIteam));
        holder.homeMakerAddressView.setText(getFullAddress(listIteam));
        holder.homeMakerRatingView.setText(listIteam.getHomeMakerRating());
        holder.homeMakerRatingWidget.setRating(Float.valueOf(listIteam.getHomeMakerRating()));
    }

    private String getFullAddress(HomeMakerListItems item){
        return item.getHomeMakerStreet() + "\n" + item.getHomeMakerCity() + "\n" + item.getHomeMakerZipCode() + "\n" + item.getHomeMakerPhone();
    }

    private String getFullName(HomeMakerListItems item){
        return item.getHomeMakerFirstName() + " " + item.getHomeMakerLastName();
    }


    @Override
    public int getItemCount() {
        return HomeMakerListItems.size();
    }

    class HomeMakerListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView homeMakerFNameView, homeMakerAddressView, homeMakerRatingView;
        public RatingBar homeMakerRatingWidget;

        public HomeMakerListItemViewHolder(View itemView, final TSViewSubsListAdapter.OnItemClickListener listener) {
            super(itemView);
            homeMakerFNameView = itemView.findViewById(R.id.home_maker_fName);
            homeMakerAddressView = itemView.findViewById(R.id.home_maker_address);
            homeMakerRatingView = itemView.findViewById(R.id.rating_field);
            homeMakerRatingWidget = itemView.findViewById(R.id.rating_view);
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
