package com.kelly.ipc.calc;

import android.os.Parcel;
import android.os.Parcelable;

public class Param implements Parcelable {

    private int a;
    private int b;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.a);
        dest.writeInt(this.b);
    }

    public Param() {
    }

    protected Param(Parcel in) {
        this.a = in.readInt();
        this.b = in.readInt();
    }

    public static final Creator<Param> CREATOR = new Creator<Param>() {
        @Override
        public Param createFromParcel(Parcel source) {
            return new Param(source);
        }

        @Override
        public Param[] newArray(int size) {
            return new Param[size];
        }
    };

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
