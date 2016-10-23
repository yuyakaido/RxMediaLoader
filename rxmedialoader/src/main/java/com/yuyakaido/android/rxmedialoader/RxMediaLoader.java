package com.yuyakaido.android.rxmedialoader;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class RxMediaLoader {

    public static Observable<List<Folder>> medias(
            final Context context, final LoaderManager loaderManager) {
        if (!PermissionUtil.hasReadExternalStoragePermission(context)) {
            return Observable.error(new NeedPermissionException(
                    "This operation needs android.permission.READ_EXTERNAL_STORAGE"));
        }

        return Observable.create(
                new Observable.OnSubscribe<List<Folder>>() {
                    @Override
                    public void call(final Subscriber<? super List<Folder>> subscriber) {
                        new FolderLoader(context, loaderManager, new FolderLoader.Callback() {
                            @Override
                            public void onFolderLoaded(List<Folder> folders) {
                                subscriber.onNext(folders);
                                subscriber.onCompleted();
                            }
                        });
                    }
                })
                .flatMap(new Func1<List<Folder>, Observable<List<Folder>>>() {
                    @Override
                    public Observable<List<Folder>> call(final List<Folder> folders) {
                        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
                            @Override
                            public void call(final Subscriber<? super List<Folder>> subscriber) {
                                new PhotoLoader(context, loaderManager, folders, new PhotoLoader.Callback() {
                                    @Override
                                    public void onPhotoLoaded() {
                                        subscriber.onNext(folders);
                                        subscriber.onCompleted();
                                    }
                                });
                            }
                        });
                    }
                })
                .flatMap(new Func1<List<Folder>, Observable<List<Folder>>>() {
                    @Override
                    public Observable<List<Folder>> call(final List<Folder> folders) {
                        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
                            @Override
                            public void call(final Subscriber<? super List<Folder>> subscriber) {
                                new VideoLoader(context, loaderManager, folders, new VideoLoader.Callback() {
                                    @Override
                                    public void onVideoLoaded() {
                                        subscriber.onNext(folders);
                                        subscriber.onCompleted();
                                    }
                                });
                            }
                        });
                    }
                });
    }

}
