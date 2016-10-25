package com.yuyakaido.android.rxmedialoader;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.entity.Folder;
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

    public static Observable<List<Album>> medias(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager))
                .flatMap(videosFunc(context, loaderManager));
    }

    public static Observable<Album> medias(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        return Observable.just(Arrays.asList(folder))
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager))
                .flatMap(videosFunc(context, loaderManager))
                .map(new Func1<List<Album>, Album>() {
                    @Override
                    public Album call(List<Album> albums) {
                        return albums.get(0);
                    }
                });
    }

    public static Observable<List<Album>> photos(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager));
    }

    public static Observable<Album> photos(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        return Observable.just(Arrays.asList(folder))
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager))
                .map(new Func1<List<Album>, Album>() {
                    @Override
                    public Album call(List<Album> albums) {
                        return albums.get(0);
                    }
                });
    }

    public static Observable<List<Album>> videos(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .map(toAlbums())
                .flatMap(videosFunc(context, loaderManager));
    }

    public static Observable<Album> videos(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        return Observable.just(Arrays.asList(folder))
                .map(toAlbums())
                .flatMap(videosFunc(context, loaderManager))
                .map(new Func1<List<Album>, Album>() {
                    @Override
                    public Album call(List<Album> albums) {
                        return albums.get(0);
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

    private static Func1<List<Folder>, List<Album>> toAlbums() {
        return new Func1<List<Folder>, List<Album>>() {
            @Override
            public List<Album> call(List<Folder> folders) {
                List<Album> albums = new ArrayList<>();
                for (Folder folder : folders) {
                    albums.add(new Album(folder));
                }
                return albums;
            }
        };
    }

    private static Func1<List<Album>, Observable<List<Album>>> photosFunc(
            final Context context, final LoaderManager loaderManager) {
        return new Func1<List<Album>, Observable<List<Album>>>() {
            @Override
            public Observable<List<Album>> call(final List<Album> albums) {
                return Observable.create(new Observable.OnSubscribe<List<Album>>() {
                    @Override
                    public void call(final Subscriber<? super List<Album>> subscriber) {
                        new PhotoLoader(context, loaderManager, albums, new PhotoLoader.Callback() {
                            @Override
                            public void onPhotoLoaded(List<Album> albums) {
                                subscriber.onNext(albums);
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
            }
        };
    }

    private static Func1<List<Album>, Observable<List<Album>>> videosFunc(
            final Context context, final LoaderManager loaderManager) {
        return new Func1<List<Album>, Observable<List<Album>>>() {
            @Override
            public Observable<List<Album>> call(final List<Album> albums) {
                return Observable.create(new Observable.OnSubscribe<List<Album>>() {
                    @Override
                    public void call(final Subscriber<? super List<Album>> subscriber) {
                        new VideoLoader(context, loaderManager, albums, new VideoLoader.Callback() {
                            @Override
                            public void onVideoLoaded(List<Album> albums) {
                                subscriber.onNext(albums);
                                subscriber.onCompleted();
                            }
                        });
                    }
                });
            }
        };
    }

}
