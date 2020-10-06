package com.yuyakaido.android.rxmedialoader.sample.activity;

import android.Manifest;
import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.yuyakaido.android.rxmedialoader.RxMediaLoader;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.sample.R;
import com.yuyakaido.android.rxmedialoader.sample.adapter.MediaGridAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ScopedMediaGridActivity extends AppCompatActivity {

    private CompositeDisposable disposables = new CompositeDisposable();
    private MediaGridAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_grid);

        adapter = new MediaGridAdapter(this, new ArrayList<Media>());
        GridView gridView = findViewById(R.id.activity_media_grid_grid_view);
        gridView.setAdapter(adapter);

        ScopedMediaGridActivityPermissionsDispatcher.loadWithPermissionCheck(this);
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ScopedMediaGridActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void load() {
        disposables.add(RxMediaLoader.scopedMedias(this, getSupportLoaderManager())
                .subscribe(new BiConsumer<List<Media>, Throwable>() {
                    @Override
                    public void accept(List<Media> medias, Throwable throwable) throws Exception {
                        adapter.addAll(medias);
                        adapter.notifyDataSetChanged();
                    }
                }));
    }

}
