package com.codewarriors4.tiffin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codewarriors4.tiffin.R;
import com.codewarriors4.tiffin.models.HomeMakerListItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeMakerListAdapter extends RecyclerView.Adapter<HomeMakerListAdapter.HomeMakerListHolder>
{
    private LayoutInflater layoutInflater;
    private List<HomeMakerListItem> items = new ArrayList<>();

    public HomeMakerListAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
        items.add(new HomeMakerListItem("Home Maker Title 1", "Description 1"));
        items.add(new HomeMakerListItem("Home Maker Title 2", "Description 2"));
        items.add(new HomeMakerListItem("Home Maker Title 3", "Description 3"));
        items.add(new HomeMakerListItem("Home Maker Title 4", "Description 4"));

    }





    @Override
    public HomeMakerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = layoutInflater.inflate(R.layout.home_maker_list_item_layout, parent, false);
        return new HomeMakerListHolder(item);
    }

    @Override
    public void onBindViewHolder(HomeMakerListHolder holder, int position) {
        HomeMakerListItem listIteam= items.get(position);
        holder.title.setText(listIteam.getHomeMakerListItemTitle());
        holder.desc.setText(listIteam.getHomeMakerListItemContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HomeMakerListHolder extends RecyclerView.ViewHolder{

        TextView title;
        TextView desc;
        public HomeMakerListHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.home_maker_list_title);
            desc = itemView.findViewById(R.id.home_maker_list_desc);
        }
    }

}
