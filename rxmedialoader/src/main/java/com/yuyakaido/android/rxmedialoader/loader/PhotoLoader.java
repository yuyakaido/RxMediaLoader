package com.yuyakaido.android.rxmedialoader.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.yuyakaido.android.rxmedialoader.entity.Album;
import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.util.MediaUtil;

import java.util.List;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class PhotoLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        void onPhotoLoaded(List<Album> albums);
    }

    private final Context context;
    private final List<Album> albums;
    private final Callback callback;

    public PhotoLoader(
            Context context,
            LoaderManager loaderManager,
            List<Album> albums,
            Callback callback) {
        this.context = context;
        this.albums = albums;
        this.callback = callback;
        loaderManager.restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return InternalPhotoLoader.newInstance(context, albums);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        callback.onPhotoLoaded(albums);
    }

    private static class InternalPhotoLoader extends CursorLoader {
        private static final String[] PROJECTION = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DATE_TAKEN};
        private static final String ORDER_BY = MediaStore.Images.Media.DATE_TAKEN + " DESC";

        private final List<Album> albums;

        public static CursorLoader newInstance(
                Context context, List<Album> albums) {
            return new InternalPhotoLoader(
                    context,
                    albums,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    ORDER_BY);
        }

        private InternalPhotoLoader(
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
                    Media media = MediaUtil.photo(cursor);
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));

                    Album album = getAlbum(id);
                    album.medias.add(media);
                    if (album.medias.size() == 1) {
                        album.cover = media;
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
