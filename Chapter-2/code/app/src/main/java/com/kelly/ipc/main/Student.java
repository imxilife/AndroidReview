package com.kelly.ipc.main;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {


    private String name;
    private int age;
    private String favorite;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeString(this.favorite);
    }

    public Student() {
    }

    protected Student(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.favorite = in.readString();
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
