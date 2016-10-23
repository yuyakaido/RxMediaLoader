package com.yuyakaido.android.rxmedialoader;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.PermissionChecker;

/**
 * Created by yuyakaido on 10/23/16.
 */
public class PermissionUtil {

    public static boolean hasReadExternalStoragePermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = PermissionChecker.checkSelfPermission(
                    context, Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

}
