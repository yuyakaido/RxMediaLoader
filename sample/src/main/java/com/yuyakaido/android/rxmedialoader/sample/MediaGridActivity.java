package com.yuyakaido.android.rxmedialoader.sample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.GridView;

import com.yuyakaido.android.rxmedialoader.Folder;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class MediaGridActivity extends AppCompatActivity {

    private static final String ARGS_FOLDER = "ARGS_FOLDER";

    public static Intent createIntent(Context context, Folder folder) {
        Intent intent = new Intent(context, MediaGridActivity.class);
        intent.putExtra(ARGS_FOLDER, folder);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_grid);

        Folder folder = getIntent().getParcelableExtra(ARGS_FOLDER);

        setTitle(folder.name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        MediaGridAdapter adapter = new MediaGridAdapter(this, folder.medias);
        GridView gridView = (GridView) findViewById(R.id.activity_media_grid_grid_view);
        gridView.setAdapter(adapter);
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
