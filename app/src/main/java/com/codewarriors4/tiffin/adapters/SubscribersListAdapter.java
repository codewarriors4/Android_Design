package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.models.HomeMakerListItems;
import com.codewarriors4.tiffin.models.SubscribersListModel;

import java.util.List;


public class SubscribersListAdapter extends RecyclerView.Adapter<SubscribersListAdapter.SubScribersListViewHolder>
{
    public SubscribersListAdapter(Context subscribersLayoutContext, List<SubscribersListModel> subscribersListModelArray) {
        this.subscribersLayoutContext = subscribersLayoutContext;
        this.subscribersListModelArray = subscribersListModelArray;
    }

    private Context subscribersLayoutContext;
    private List<com.codewarriors4.tiffin.models.SubscribersListModel> subscribersListModelArray;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    public SubScribersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(subscribersLayoutContext);

        View view = inflater.inflate(R.layout.subscriptions_list_item_recycle, null);

        SubScribersListViewHolder holder = new SubScribersListViewHolder(view, mListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubScribersListViewHolder holder, int position) {
        final SubscribersListModel listIteam = subscribersListModelArray.get(position);
        holder.firstAndLastView.setText(listIteam.getUserName());
        holder.descriptionView.setText(listIteam.getUserStreet() +"\n" +
            listIteam.getPhoneNumber());
        holder.costView.setText(listIteam.getPackageCost() + "CAD");
    }

    @Override
    public int getItemCount() {
        return subscribersListModelArray.size();
    }

    class SubScribersListViewHolder extends RecyclerView.ViewHolder {

        public TextView firstAndLastView, descriptionView, costView;

        public SubScribersListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            firstAndLastView = itemView.findViewById(R.id.first_last_name);
            descriptionView = itemView.findViewById(R.id.street_address_tf);
            costView = itemView.findViewById(R.id.cost_view);

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
