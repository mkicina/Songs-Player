package com.example.kicinamartin.songsplayer;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FeedAdapter  extends ArrayAdapter {
    private static final String TAG = "FeedAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<FeedEntry> applications;

    FeedAdapter(@NonNull Context context, int resource, List<FeedEntry> applications) {
        super(context, resource);
        this.layoutResource = resource;
        this.layoutInflater = LayoutInflater.from(context);
        this.applications = applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            Log.d(TAG, "getView: called with null convertView");
            convertView = layoutInflater.inflate(layoutResource, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            Log.d(TAG, "getView: provided a convertView");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        FeedEntry currentApp = applications.get(position);
        viewHolder.name.setText(currentApp.getName());
        viewHolder.releaseDate.setText(currentApp.getReleaseDate());
        viewHolder.views.setText(currentApp.getViews());
        currentApp.setImage(currentApp.getImage());
        viewHolder.name.setContentDescription(currentApp.getSongURL());
        viewHolder.image.setContentDescription(currentApp.getImageURL());
        Picasso.get().load(currentApp.getImageURL()).into(viewHolder.image);

        return convertView;
    }
    ViewHolder getViewHolder(View v){
        return new ViewHolder(v);
    }
    String getTitle(){
        return applications.get(0).getTitle();
    }
    public static class ViewHolder {
        final TextView name;
        final TextView releaseDate;
        final TextView views;
        final ImageView image;


        ViewHolder (View v){
            this.name = v.findViewById(R.id.name);
            this.releaseDate = v.findViewById(R.id.rlsDate);
            this.views = v.findViewById(R.id.views);
            this.image = v.findViewById(R.id.imgSource);
        }

    }
}
