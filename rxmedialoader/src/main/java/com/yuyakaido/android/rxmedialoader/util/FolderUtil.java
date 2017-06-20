package com.yuyakaido.android.rxmedialoader.util;

import android.database.Cursor;
import android.provider.MediaStore;

import com.yuyakaido.android.rxmedialoader.entity.Folder;

public class FolderUtil {

    private FolderUtil() {}

    public static Folder valueOf(Cursor cursor) {
        Folder folder = new Folder();
        folder.id = cursor.getString(cursor.getColumnIndex(
                MediaStore.Images.Media.BUCKET_ID));
        folder.name = cursor.getString(cursor.getColumnIndex(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
        return folder;
    }

}
