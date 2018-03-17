package com.example.user.myapplication;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 13.03.2018.
 */

public class Note implements Parcelable{

    private String title;
    private String description;
    private Date date;
    private int color;

    public Note(String title, String description, Date date, int color) {

        this.title = title;
        this.description = description;
        this.date = date;
        this.color = color;

    }

    public Note(Parcel in)  {

        String[] data = new String[2];
        in.readStringArray(data);
        title = data[0];
        description = data[1];

    }


    public String getTitle() {

        return this.title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDescription() {

        return this.description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public Date getDate() {

        return this.date;
    }

    public void setDate(Date date) {

        this.date = date;
    }
    public int getColor() {

        return this.color;
    }

    public void setColor(int color) {

        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] { title, description });
    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {

            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
