package com.vacuum.app.cinema.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.bluzwong.swipeback.SwipeBackActivityHelper;
import com.vacuum.app.cinema.Activities.detailsActivity;
import com.vacuum.app.cinema.Model.Movie;
import com.vacuum.app.cinema.Model.Trailer;
import com.vacuum.app.cinema.R;

import java.util.List;

/**
 * Created by Home on 2/24/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailer;
    private Context mContext;


    public static class TrailerViewHolder extends RecyclerView.ViewHolder {
        TextView trailerTitle;
        TextView site;
        ImageView thumbnail;
        public TrailerViewHolder(View v) {
            super(v);
            trailerTitle = (TextView) v.findViewById(R.id.trailerTitle);
            site = (TextView) v.findViewById(R.id.site);
            thumbnail = (ImageView) v.findViewById(R.id.thumbnail);

        }
    }

    public TrailerAdapter(List<Trailer> trailer, Context mContext) {
        this.trailer = trailer;
        this.mContext = mContext;
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        return new TrailerAdapter.TrailerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, final int position) {

        holder.trailerTitle.setText(trailer.get(position).getName());
        holder.site.setText(trailer.get(position).getSite());

        Glide.with(mContext).load("https://img.youtube.com/vi/"+trailer.get(position).getKey()+"/hqdefault.jpg").into(holder.thumbnail);

        //onClick
        //==================================================================

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return trailer.size();
    }

}
