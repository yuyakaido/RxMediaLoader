package com.yuyakaido.android.rxmedialoader.sample.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.sample.R;
import com.yuyakaido.android.rxmedialoader.sample.adapter.MediaGridAdapter;

public class MediaGridActivity extends AppCompatActivity {

    private static final String ARGS_ALBUM = "ARGS_ALBUM";

    public static Intent createIntent(Context context, Album album) {
        Intent intent = new Intent(context, MediaGridActivity.class);
        intent.putExtra(ARGS_ALBUM, album);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_grid);

        final Album album = getIntent().getParcelableExtra(ARGS_ALBUM);

        setTitle(album.folder.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MediaGridAdapter adapter = new MediaGridAdapter(this, album.medias);
        GridView gridView = (GridView) findViewById(R.id.activity_media_grid_grid_view);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(MediaPagerActivity.createIntent(
                        MediaGridActivity.this, album, i));
            }
        });
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
