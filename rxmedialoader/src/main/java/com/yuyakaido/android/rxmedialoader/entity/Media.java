package com.yuyakaido.android.rxmedialoader.entity;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Media implements Parcelable {

    public static final Creator<Media> CREATOR = new Creator<Media>() {
        @Override
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        @Override
        public Media[] newArray(int size) {
            return new Media[size];
        }
    };

    public enum Type {
        Photo, Video
    }

    public long id;
    public Type type;
    public Uri uri;

    public Media() {}

    public Media(Parcel in) {
        this.id = in.readLong();
        this.type = (Type) in.readSerializable();
        this.uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeSerializable(this.type);
        dest.writeParcelable(this.uri, flags);
    }

}
