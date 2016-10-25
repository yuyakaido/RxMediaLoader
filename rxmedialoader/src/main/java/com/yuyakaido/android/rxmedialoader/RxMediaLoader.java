package com.yuyakaido.android.rxmedialoader;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.yuyakaido.android.rxmedialoader.entity.Folder;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.error.NeedPermissionException;
import com.yuyakaido.android.rxmedialoader.loader.FolderLoader;
import com.yuyakaido.android.rxmedialoader.loader.PhotoLoader;
import com.yuyakaido.android.rxmedialoader.loader.VideoLoader;
import com.yuyakaido.android.rxmedialoader.util.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class RxMediaLoader {

    private RxMediaLoader() {}

    public static Observable<List<Folder>> medias(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .flatMap(photosFunc(context, loaderManager))
                .flatMap(videosFunc(context, loaderManager));
    }

    public static Observable<List<Media>> medias(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        folder.medias = new ArrayList<>();
        return Observable.just(Arrays.asList(folder))
                .flatMap(photosFunc(context, loaderManager))
                .flatMap(videosFunc(context, loaderManager))
                .map(new Func1<List<Folder>, List<Media>>() {
                    @Override
                    public List<Media> call(List<Folder> folders) {
                        return folders.get(0).medias;
                    }
                });
    }

    public static Observable<List<Folder>> photos(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .flatMap(photosFunc(context, loaderManager));
    }

    public static Observable<List<Media>> photos(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        folder.medias = new ArrayList<>();
        return Observable.just(Arrays.asList(folder))
                .flatMap(photosFunc(context, loaderManager))
                .map(new Func1<List<Folder>, List<Media>>() {
                    @Override
                    public List<Media> call(List<Folder> folders) {
                        return folders.get(0).medias;
                    }
                });
    }

    public static Observable<List<Folder>> videos(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .flatMap(videosFunc(context, loaderManager));
    }

    public static Observable<List<Media>> videos(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        folder.medias = new ArrayList<>();
        return Observable.just(Arrays.asList(folder))
                .flatMap(videosFunc(context, loaderManager))
                .map(new Func1<List<Folder>, List<Media>>() {
                    @Override
                    public List<Media> call(List<Folder> folders) {
                        return folders.get(0).medias;
                    }
                });
    }

    private static Observable<List<Folder>> folders(
            final Context context, final LoaderManager loaderManager) {
        if (!PermissionUtil.hasReadExternalStoragePermission(context)) {
            return Observable.error(new NeedPermissionException(
                    "This operation needs android.permission.READ_EXTERNAL_STORAGE"));
        }

        return Observable.create(new Observable.OnSubscribe<List<Folder>>() {
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
        });
    }

    private static Func1<List<Folder>, Observable<List<Folder>>> photosFunc(
            final Context context, final LoaderManager loaderManager) {
        return new Func1<List<Folder>, Observable<List<Folder>>>() {
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
        };
    }

    private static Func1<List<Folder>, Observable<List<Folder>>> videosFunc(
            final Context context, final LoaderManager loaderManager) {
        return new Func1<List<Folder>, Observable<List<Folder>>>() {
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
        };
    }

}
