package com.example.user.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by User on 13.03.2018.
 */

public class NoteAdapter extends ArrayAdapter<Note> {

    private LayoutInflater inflater;
    private int layout;
    private List<Note> notes;

    public NoteAdapter(Context context, int resource, List<Note> notes) {
        super(context, resource, notes);
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);


        TextView titleView = (TextView) view.findViewById(R.id.title);
        TextView descriptionView = (TextView) view.findViewById(R.id.description);
        TextView dateView = (TextView) view.findViewById(R.id.date);

        Note note = notes.get(position);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.list_item);
        relativeLayout.setBackgroundColor(note.getColor());

        SimpleDateFormat formatForDateNow = new SimpleDateFormat(" dd-MM-yy hh:mm");

        titleView.setText(note.getTitle());
        descriptionView.setText(note.getDescription());
        dateView.setText(formatForDateNow.format(note.getDate()).toString());

        return view;
    }
}
