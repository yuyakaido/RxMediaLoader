package com.yuyakaido.android.rxmedialoader.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.sample.R;

import java.util.List;

public class MediaGridAdapter extends ArrayAdapter<Media> {

    public MediaGridAdapter(Context context, List<Media> medias) {
        super(context, 0, medias);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_media_grid, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Media media = getItem(position);

        Glide.with(getContext()).load(media.uri).into(holder.image);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;

        public ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.item_media_grid_image);
        }
    }

}
