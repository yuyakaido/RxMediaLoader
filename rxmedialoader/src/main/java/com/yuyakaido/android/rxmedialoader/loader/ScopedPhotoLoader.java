package com.yuyakaido.android.rxmedialoader.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.yuyakaido.android.rxmedialoader.entity.Media;
import com.yuyakaido.android.rxmedialoader.util.MediaUtil;

import java.util.ArrayList;
import java.util.List;

public class ScopedPhotoLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        void onPhotoLoaded(List<Media> medias);
    }

    private final Context context;
    private final Callback callback;
    private final List<Media> medias = new ArrayList<>();

    public ScopedPhotoLoader(
            Context context,
            LoaderManager loaderManager,
            Callback callback
    ) {
        this.context = context;
        this.callback = callback;
        loaderManager.restartLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return InternalMediaLoader.newInstance(context, medias);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {}

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        callback.onPhotoLoaded(medias);
    }

    private static class InternalMediaLoader extends CursorLoader {
        private static final String[] PROJECTION = {
                MediaStore.Images.Media._ID
        };

        private final List<Media> medias;

        public static CursorLoader newInstance(
                Context context,
                List<Media> medias
        ) {
            return new InternalMediaLoader(
                    context,
                    medias,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    null
            );
        }

        private InternalMediaLoader(
                Context context,
                List<Media> medias,
                Uri uri,
                String[] projection,
                String selection,
                String[] selectionArgs,
                String sortOrder
        ) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
            this.medias = medias;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = super.loadInBackground();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Media media = MediaUtil.photo(cursor);
                    medias.add(media);
                } while (cursor.moveToNext());
            }
            return cursor;
        }
    }

}
