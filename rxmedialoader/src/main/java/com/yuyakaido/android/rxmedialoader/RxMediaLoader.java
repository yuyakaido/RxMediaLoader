package com.yuyakaido.android.rxmedialoader;

import android.content.Context;
import androidx.loader.app.LoaderManager;

import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.entity.Folder;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.error.NeedPermissionException;
import com.yuyakaido.android.rxmedialoader.loader.FolderLoader;
import com.yuyakaido.android.rxmedialoader.loader.PhotoLoader;
import com.yuyakaido.android.rxmedialoader.loader.ScopedPhotoLoader;
import com.yuyakaido.android.rxmedialoader.loader.VideoLoader;
import com.yuyakaido.android.rxmedialoader.util.PermissionUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class RxMediaLoader {

    private RxMediaLoader() {}

    public static Single<List<Media>> scopedMedias(
            final Context context,
            final LoaderManager loaderManager
    ) {
        return Single.create(new SingleOnSubscribe<List<Media>>() {
            @Override
            public void subscribe(final SingleEmitter<List<Media>> emitter) throws Exception {
                new ScopedPhotoLoader(context, loaderManager, new ScopedPhotoLoader.Callback() {
                    @Override
                    public void onPhotoLoaded(List<Media> medias) {
                        emitter.onSuccess(medias);
                    }
                });
            }
        });
    }

    public static Single<List<Album>> medias(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager))
                .flatMap(videosFunc(context, loaderManager));
    }

    public static Single<Album> medias(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        return Single.just(Arrays.asList(folder))
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager))
                .flatMap(videosFunc(context, loaderManager))
                .map(new Function<List<Album>, Album>() {
                    @Override
                    public Album apply(@NonNull List<Album> albums) throws Exception {
                        return albums.get(0);
                    }
                });
    }

    public static Single<List<Album>> photos(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager));
    }

    public static Single<Album> photos(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        return Single.just(Arrays.asList(folder))
                .map(toAlbums())
                .flatMap(photosFunc(context, loaderManager))
                .map(new Function<List<Album>, Album>() {
                    @Override
                    public Album apply(@NonNull List<Album> albums) throws Exception {
                        return albums.get(0);
                    }
                });
    }

    public static Single<List<Album>> videos(
            final Context context, final LoaderManager loaderManager) {
        return folders(context, loaderManager)
                .map(toAlbums())
                .flatMap(videosFunc(context, loaderManager));
    }

    public static Single<Album> videos(
            final Context context,
            final LoaderManager loaderManager,
            final Folder folder) {
        return Single.just(Arrays.asList(folder))
                .map(toAlbums())
                .flatMap(videosFunc(context, loaderManager))
                .map(new Function<List<Album>, Album>() {
                    @Override
                    public Album apply(@NonNull List<Album> albums) throws Exception {
                        return albums.get(0);
                    }
                });
    }

    private static Single<List<Folder>> folders(
            final Context context, final LoaderManager loaderManager) {
        if (!PermissionUtil.hasReadExternalStoragePermission(context)) {
            return Single.error(new NeedPermissionException(
                    "This operation needs android.permission.READ_EXTERNAL_STORAGE"));
        }

        return Single.create(new SingleOnSubscribe<List<Folder>>() {
            @Override
            public void subscribe(@NonNull final SingleEmitter<List<Folder>> emitter) throws Exception {
                new FolderLoader(context, loaderManager, new FolderLoader.Callback() {
                    @Override
                    public void onFolderLoaded(List<Folder> folders) {
                        emitter.onSuccess(folders);
                    }
                });
            }
        });
    }

    private static Function<List<Folder>, List<Album>> toAlbums() {
        return new Function<List<Folder>, List<Album>>() {
            @Override
            public List<Album> apply(@NonNull List<Folder> folders) throws Exception {
                List<Album> albums = new ArrayList<>();
                for (Folder folder : folders) {
                    albums.add(new Album(folder));
                }
                return albums;
            }
        };
    }

    private static Function<List<Album>, Single<List<Album>>> photosFunc(
            final Context context, final LoaderManager loaderManager) {
        return new Function<List<Album>, Single<List<Album>>>() {
            @Override
            public Single<List<Album>> apply(@NonNull final List<Album> albums) throws Exception {
                return Single.create(new SingleOnSubscribe<List<Album>>() {
                    @Override
                    public void subscribe(@NonNull final SingleEmitter<List<Album>> emitter) throws Exception {
                        new PhotoLoader(context, loaderManager, albums, new PhotoLoader.Callback() {
                            @Override
                            public void onPhotoLoaded(List<Album> albums) {
                                emitter.onSuccess(albums);
                            }
                        });
                    }
                });
            }
        };
    }

    private static Function<List<Album>, Single<List<Album>>> videosFunc(
            final Context context, final LoaderManager loaderManager) {
        return new Function<List<Album>, Single<List<Album>>>() {
            @Override
            public Single<List<Album>> apply(@NonNull final List<Album> albums) throws Exception {
                return Single.create(new SingleOnSubscribe<List<Album>>() {
                    @Override
                    public void subscribe(@NonNull final SingleEmitter<List<Album>> emitter) throws Exception {
                        new VideoLoader(context, loaderManager, albums, new VideoLoader.Callback() {
                            @Override
                            public void onVideoLoaded(List<Album> albums) {
                                emitter.onSuccess(albums);
                            }
                        });
                    }
                });
            }
        };
    }

}
