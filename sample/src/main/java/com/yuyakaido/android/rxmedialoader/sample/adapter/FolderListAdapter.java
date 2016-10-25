package com.yuyakaido.android.rxmedialoader.sample.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.sample.R;

import java.util.ArrayList;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class FolderListAdapter extends ArrayAdapter<Album> {

    public FolderListAdapter(Context context) {
        super(context, 0, new ArrayList<Album>());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.item_folter_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Album album = getItem(position);

        Glide.with(getContext()).load(album.cover.uri).into(holder.image);
        holder.name.setText(album.folder.name);

        return convertView;
    }

    private static class ViewHolder {
        public ImageView image;
        public TextView name;

        public ViewHolder(View view) {
            this.image = (ImageView) view.findViewById(R.id.item_folder_list_image);
            this.name = (TextView) view.findViewById(R.id.item_folder_list_name);
        }
    }

}
