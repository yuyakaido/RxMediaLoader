package com.yuyakaido.android.rxmedialoader.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.sample.R;
import com.yuyakaido.android.rxmedialoader.sample.adapter.MediaPagerAdapter;

/**
 * Created by yuyakaido on 10/24/16.
 */
public class MediaPagerActivity extends AppCompatActivity {

    private static final String ARGS_ALBUM = "ARGS_ALBUM";
    private static final String ARGS_POSITION = "ARGS_POSITION";
    private static final int DEFAULT_POSITION = 0;

    public static Intent createIntent(Context context, Album album, int position) {
        Intent intent = new Intent(context, MediaPagerActivity.class);
        intent.putExtra(ARGS_ALBUM, album);
        intent.putExtra(ARGS_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_pager);

        Intent intent = getIntent();
        Album album = intent.getParcelableExtra(ARGS_ALBUM);
        int position = intent.getIntExtra(ARGS_POSITION, DEFAULT_POSITION);

        setTitle(album.folder.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_media_pager_view_pager);
        viewPager.setAdapter(new MediaPagerAdapter(this, album.medias));
        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
