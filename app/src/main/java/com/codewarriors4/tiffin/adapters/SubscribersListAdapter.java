package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.models.HomeMakerListItems;
import com.codewarriors4.tiffin.models.SubscribersListModel;

import java.util.List;


public class SubscribersListAdapter extends RecyclerView.Adapter<SubscribersListAdapter.SubScribersListViewHolder
        >
{
    public SubscribersListAdapter(Context subscribersLayoutContext, List<SubscribersListModel> subscribersListModelArray) {
        this.subscribersLayoutContext = subscribersLayoutContext;
        this.subscribersListModelArray = subscribersListModelArray;
    }

    private Context subscribersLayoutContext;
    private List<com.codewarriors4.tiffin.models.SubscribersListModel> subscribersListModelArray;

    public SubScribersListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(subscribersLayoutContext);

        View view = inflater.inflate(R.layout.subscriptions_list_item_recycle, null);

        SubScribersListViewHolder holder = new SubScribersListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SubScribersListViewHolder holder, int position) {
        final SubscribersListModel listIteam = subscribersListModelArray.get(position);
        holder.homemakeEmail.setText(listIteam.getUserName());
        holder.homeMakerPostCode.setText(listIteam.getUserStreet());
    }

    @Override
    public int getItemCount() {
        return subscribersListModelArray.size();
    }

    class SubScribersListViewHolder extends RecyclerView.ViewHolder {

        public TextView homemakeEmail, homeMakerPostCode;

        public SubScribersListViewHolder(View itemView) {
            super(itemView);
            homemakeEmail = itemView.findViewById(R.id.email_id_tf);
            homeMakerPostCode = itemView.findViewById(R.id.street_address_tf);
        }
    }
}
