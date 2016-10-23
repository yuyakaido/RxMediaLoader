package com.yuyakaido.android.rxmedialoader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.List;

/**
 * Created by yuyakaido on 10/23/16.
 */
public class VideoLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        void onVideoLoaded();
    }

    private final Context context;
    private final List<Folder> folders;
    private final VideoLoader.Callback callback;

    public VideoLoader(
            Context context,
            LoaderManager loaderManager,
            List<Folder> folders,
            VideoLoader.Callback callback) {
        this.context = context;
        this.folders = folders;
        this.callback = callback;
        loaderManager.restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return VideoLoader.InternalVideoLoader.newInstance(context, folders);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        callback.onVideoLoaded();
    }

    private static class InternalVideoLoader extends CursorLoader {
        private static final String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_ID,
                MediaStore.Video.Media.DATE_TAKEN};
        private static final String ORDER_BY = MediaStore.Video.Media.DATE_TAKEN + " DESC";

        private final List<Folder> folders;

        public static CursorLoader newInstance(
                Context context, List<Folder> folders) {
            return new VideoLoader.InternalVideoLoader(
                    context,
                    folders,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    PROJECTION,
                    null,
                    null,
                    ORDER_BY);
        }

        private InternalVideoLoader(
                Context context,
                List<Folder> folders,
                Uri uri,
                String[] projection,
                String selection,
                String[] selectionArgs,
                String sortOrder) {
            super(context, uri, projection, selection, selectionArgs, sortOrder);
            this.folders = folders;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = super.loadInBackground();
            if (cursor.moveToFirst()) {
                do {
                    Media media = MediaUtil.video(cursor);
                    String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID));
                    Folder folder = getFolder(folders, id);
                    folder.medias.add(media);
                    if (folder.medias.size() == 1) {
                        folder.cover = media;
                    }
                } while (cursor.moveToNext());
            }
            return cursor;
        }

        private Folder getFolder(List<Folder> folders, String id) {
            for (Folder folder : folders) {
                if (folder.id.equals(id)) {
                    return folder;
                }
            }
            return null;
        }
    }

}
