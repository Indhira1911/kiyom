package com.v7ench.kiyo.global;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vicky on 10/11/16.
 */

public class Tstres  implements Parcelable{
    private String content;
    private String stime;
    private String sdate;

    protected Tstres(Parcel in) {
        content = in.readString();
        stime = in.readString();
        sdate = in.readString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public static Creator<Tstres> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<Tstres> CREATOR = new Creator<Tstres>() {
        @Override
        public Tstres createFromParcel(Parcel in) {
            return new Tstres(in);
        }

        @Override
        public Tstres[] newArray(int size) {
            return new Tstres[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(stime);
        dest.writeString(sdate);
    }
}
