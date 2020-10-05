package com.yuyakaido.android.rxmedialoader.sample.activity;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yuyakaido.android.rxmedialoader.RxMediaLoader;
import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.sample.R;
import com.yuyakaido.android.rxmedialoader.sample.adapter.FolderListAdapter;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

    private CompositeDisposable disposables = new CompositeDisposable();
    private FolderListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new FolderListAdapter(this);
        ListView listView = (ListView) findViewById(R.id.activity_main_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Album album = adapter.getItem(i);
                startActivity(MediaGridActivity.createIntent(MainActivity.this, album));
            }
        });

        MainActivityPermissionsDispatcher.loadWithPermissionCheck(this);
    }

    @Override
    protected void onDestroy() {
        disposables.dispose();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void load() {
        disposables.add(RxMediaLoader.medias(this, getSupportLoaderManager())
                .subscribe(new BiConsumer<List<Album>, Throwable>() {
                    @Override
                    public void accept(List<Album> albums, Throwable throwable) throws Exception {
                        adapter.addAll(albums);
                        adapter.notifyDataSetChanged();
                    }
                }));
    }

}
