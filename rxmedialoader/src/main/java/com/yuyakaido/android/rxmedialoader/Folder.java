package com.yuyakaido.android.rxmedialoader;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuyakaido on 10/22/16.
 */
public class Folder implements Parcelable {

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public String id;
    public String name;
    public Media cover;
    public List<Media> medias = new ArrayList<>();

    public Folder() {}

    public Folder(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.cover = in.readParcelable(Media.class.getClassLoader());
        this.medias = in.createTypedArrayList(Media.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.cover, flags);
        dest.writeTypedList(this.medias);
    }

}
