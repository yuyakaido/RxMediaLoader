package com.yuyakaido.android.rxmedialoader.entity;

import android.os.Parcel;
import android.os.Parcelable;

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

    public Folder() {}

    public Folder(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

}
