package com.v7ench.kiyo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vicky on 17/11/16.
 */

public class Categorieslist implements Parcelable {
    private String content;
    private String sdate;
    private String stme;
    private String dqr;
    private String sterlizer;
    private String pack;
    private String sload;
   private String type;
    private String postcolor;
    private String tresult;
    private String pre_colr;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSdate() {
        return sdate;
    }

    public void setSdate(String sdate) {
        this.sdate = sdate;
    }

    public String getStme() {
        return stme;
    }

    public void setStme(String stme) {
        this.stme = stme;
    }

    public String getDqr() {
        return dqr;
    }

    public void setDqr(String dqr) {
        this.dqr = dqr;
    }

    public String getSterlizer() {
        return sterlizer;
    }

    public void setSterlizer(String sterlizer) {
        this.sterlizer = sterlizer;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getSload() {
        return sload;
    }

    public void setSload(String sload) {
        this.sload = sload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostcolor() {
        return postcolor;
    }

    public void setPostcolor(String postcolor) {
        this.postcolor = postcolor;
    }

    public String getTresult() {
        return tresult;
    }

    public void setTresult(String tresult) {
        this.tresult = tresult;
    }

    public String getPre_colr() {
        return pre_colr;
    }

    public void setPre_colr(String pre_colr) {
        this.pre_colr = pre_colr;
    }

    public static Creator<Categorieslist> getCREATOR() {
        return CREATOR;
    }

    protected Categorieslist(Parcel in) {
        content = in.readString();
        sdate = in.readString();
        stme = in.readString();
        dqr = in.readString();
        sterlizer = in.readString();
        pack = in.readString();
        sload = in.readString();
        type = in.readString();
        postcolor = in.readString();
        tresult = in.readString();
        pre_colr = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeString(sdate);
        dest.writeString(stme);
        dest.writeString(dqr);
        dest.writeString(sterlizer);
        dest.writeString(pack);
        dest.writeString(sload);
        dest.writeString(type);
        dest.writeString(postcolor);
        dest.writeString(tresult);
        dest.writeString(pre_colr);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Categorieslist> CREATOR = new Creator<Categorieslist>() {
        @Override
        public Categorieslist createFromParcel(Parcel in) {
            return new Categorieslist(in);
        }

        @Override
        public Categorieslist[] newArray(int size) {
            return new Categorieslist[size];
        }
    };
}
