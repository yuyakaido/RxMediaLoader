package com.yuyakaido.android.rxmedialoader.sample.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.sample.R;

import java.util.List;

public class MediaPagerAdapter extends PagerAdapter {

    private Context context;
    private List<Media> medias;

    public MediaPagerAdapter(Context context, List<Media> medias) {
        this.context = context;
        this.medias = medias;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.item_media_pager, null);

        Media media = medias.get(position);

        ImageView image = (ImageView) view.findViewById(R.id.item_media_pager_image);
        Glide.with(context).load(media.uri).into(image);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return medias.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

}
