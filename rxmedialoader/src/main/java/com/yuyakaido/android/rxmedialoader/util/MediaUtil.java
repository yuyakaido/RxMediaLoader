package com.yuyakaido.android.rxmedialoader.util;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.yuyakaido.android.rxmedialoader.entity.Media;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class MediaUtil {

    private MediaUtil() {}

    public static Media photo(Cursor cursor) {
        Media media = new Media();
        media.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
        media.type = Media.Type.Photo;
        media.uri = MediaUtil.uri(media.id, media.type);
        return media;
    }

    public static Media video(Cursor cursor) {
        Media media = new Media();
        media.id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));
        media.type = Media.Type.Video;
        media.uri = MediaUtil.uri(media.id, media.type);
        return media;
    }

    public static Uri uri(long id, Media.Type type) {
        if (type == Media.Type.Photo) {
            return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        }
        return ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
    }

}
