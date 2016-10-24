package com.yuyakaido.android.rxmedialoader.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.yuyakaido.android.rxmedialoader.entity.Folder;

/**
 * Created by yuyakaido on 10/24/16.
 */
public class MediaPagerActivity extends AppCompatActivity {

    private static final String ARGS_FOLDER = "ARGS_FOLDER";
    private static final String ARGS_POSITION = "ARGS_POSITION";
    private static final int DEFAULT_POSITION = 0;

    public static Intent createIntent(Context context, Folder folder, int position) {
        Intent intent = new Intent(context, MediaPagerActivity.class);
        intent.putExtra(ARGS_FOLDER, folder);
        intent.putExtra(ARGS_POSITION, position);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_pager);

        Intent intent = getIntent();
        Folder folder = intent.getParcelableExtra(ARGS_FOLDER);
        int position = intent.getIntExtra(ARGS_POSITION, DEFAULT_POSITION);

        setTitle(folder.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_media_pager_view_pager);
        viewPager.setAdapter(new MediaPagerAdapter(this, folder.medias));
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
