package com.v7ench.kiyo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vicky on 22/11/16.
 */

public class Biolistnew implements Parcelable{
    private String biodqr;
    private String sdate;
    private String stime;

    public String getBiodqr() {
        return biodqr;
    }

    public void setBiodqr(String biodqr) {
        this.biodqr = biodqr;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public static Creator<Biolistnew> getCREATOR() {
        return CREATOR;
    }

    protected Biolistnew(Parcel in) {
        biodqr = in.readString();
        sdate = in.readString();
        stime = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(biodqr);
        dest.writeString(sdate);
        dest.writeString(stime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Biolistnew> CREATOR = new Creator<Biolistnew>() {
        @Override
        public Biolistnew createFromParcel(Parcel in) {
            return new Biolistnew(in);
        }

        @Override
        public Biolistnew[] newArray(int size) {
            return new Biolistnew[size];
        }
    };
}
