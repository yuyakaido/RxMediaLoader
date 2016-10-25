package com.yuyakaido.android.rxmedialoader.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyakaido on 10/25/16.
 */
public class Album implements Parcelable {

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public Folder folder;
    public Media cover;
    public List<Media> medias = new ArrayList<>();

    public Album(Folder folder) {
        this.folder = folder;
    }

    public Album(Parcel in) {
        this.folder = in.readParcelable(Folder.class.getClassLoader());
        this.cover = in.readParcelable(Media.class.getClassLoader());
        this.medias = in.createTypedArrayList(Media.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.folder, flags);
        dest.writeParcelable(this.cover, flags);
        dest.writeTypedList(this.medias);
    }

}
