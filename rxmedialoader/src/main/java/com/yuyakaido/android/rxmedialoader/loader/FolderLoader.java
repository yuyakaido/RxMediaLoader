package com.yuyakaido.android.rxmedialoader.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.yuyakaido.android.rxmedialoader.entity.Folder;
import com.yuyakaido.android.rxmedialoader.util.FolderUtil;

import java.util.ArrayList;
import java.util.List;

public class FolderLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    public interface Callback {
        void onFolderLoaded(List<Folder> folders);
    }

    private final Context context;
    private final Callback callback;

    public FolderLoader(Context context, LoaderManager loaderManager, Callback callback) {
        this.context = context;
        this.callback = callback;
        loaderManager.restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new InternalFolderLoader(context);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int count = cursor.getCount();
        List<Folder> folders = new ArrayList<>(count);
        if (cursor.moveToFirst()) {
            do {
                folders.add(FolderUtil.valueOf(cursor));
            } while (cursor.moveToNext());
        }
        callback.onFolderLoaded(folders);
    }

    private static class InternalFolderLoader extends CursorLoader {
        private static final String[] PROJECTION = {
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media._ID};

        // https://android.googlesource.com/platform/packages/apps/Gallery2/+/master/src/com/android/gallery3d/data/BucketHelper.java
        //
        // We want to order the albums by reverse chronological order.
        // We abuse the "WHERE" parameter to insert a "GROUP BY" clause into the SQL statement.
        // The template for "WHERE" parameter is like:
        //     SELECT ... FROM ... WHERE (%s)
        // and we make it look like:
        //     SELECT ... FROM ... WHERE (1) GROUP BY (1)
        // The first "(1)" means true. The second "(1)" means the first column specified after SELECT.
        // Note that because there is a ")" in the template, we use "(1" to match it.
        private static final String BUCKET_GROUP_BY = "1) GROUP BY (1";
        private static final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        public InternalFolderLoader(Context context) {
            super(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);
        }
    }

}
