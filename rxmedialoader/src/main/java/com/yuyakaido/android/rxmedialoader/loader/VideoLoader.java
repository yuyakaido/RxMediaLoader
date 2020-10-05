package com.yuyakaido.android.rxmedialoader.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.util.MediaUtil;

import java.util.List;

public class VideoLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        void onVideoLoaded(List<Album> albums);
    }

    private final Context context;
    private final List<Album> albums;
    private final VideoLoader.Callback callback;

    public VideoLoader(
            Context context,
            LoaderManager loaderManager,
            List<Album> albums,
            VideoLoader.Callback callback) {
        this.context = context;
        this.albums = albums;
        this.callback = callback;
        loaderManager.restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return VideoLoader.InternalVideoLoader.newInstance(context, albums);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        callback.onVideoLoaded(albums);
    }

    private static class InternalVideoLoader extends CursorLoader {
        private static final String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.DATE_TAKEN};
        private static final String ORDER_BY = MediaStore.Video.Media.DATE_TAKEN + " DESC";

        private final List<Album> albums;

        public static CursorLoader newInstance(
                Context context, List<Album> albums) {
            return new VideoLoader.InternalVideoLoader(
                    context,
                    albums,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    ORDER_BY);
        }

        private InternalVideoLoader(
                Context context,
                List<Album> albums,
                Uri uri,
                String[] projection,
                String selection,
                String[] selectionArgs,
                String sortOrder) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
            this.albums = albums;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = super.loadInBackground();
            if (cursor.moveToFirst()) {
                do {
                    Media media = MediaUtil.video(cursor);
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));

                    Album album = getAlbum(id);
                    if (album != null) {
                        if (album.medias.isEmpty()) {
                            album.cover = media;
                        }
                        album.medias.add(media);
                    }
                } while (cursor.moveToNext());
            }

            return cursor;
        }

        private Album getAlbum(String id) {
            for (Album album : albums) {
                if (album.folder.id.equals(id)) {
                    return album;
                }
            }
            return null;
        }
    }

}
