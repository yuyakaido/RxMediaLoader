package com.yuyakaido.android.rxmedialoader.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yuyakaido.android.rxmedialoader.Folder;
import com.yuyakaido.android.rxmedialoader.RxMediaLoader;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;
import rx.Subscriber;

@RuntimePermissions
public class MainActivity extends AppCompatActivity {

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
                Folder folder = adapter.getItem(i);
                startActivity(MediaGridActivity.createIntent(MainActivity.this, folder));
            }
        });

        MainActivityPermissionsDispatcher.loadWithCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void load() {
        RxMediaLoader.medias(this, getSupportLoaderManager())
                .subscribe(new Subscriber<List<Folder>>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onNext(List<Folder> folders) {
                        adapter.addAll(folders);
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}
