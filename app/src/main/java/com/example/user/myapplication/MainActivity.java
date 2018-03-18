package com.example.user.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private List<Note> notes = new ArrayList();

    static final String REDACTION_KEY = "REDACTION";
    static final String NOTE_KEY = "NOTE";
    static final String TITLE_KEY = "TITLE";
    static final String DESCRIPTION_KEY = "DESCRIPTION";

    private static final int REQUEST_ACCESS_TYPE = 1;
    Animation animation;
    private int selectionElement;
    NoteAdapter adapter;
    ImageButton ib;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (savedInstanceState != null) {
            notes = savedInstanceState.getParcelable(NOTE_KEY);
            Log.d("234", notes.get(0).getTitle().toString());
        }*/
        ListView lw = (ListView) findViewById(R.id.my_list);
        animation = AnimationUtils.loadAnimation(this, R.anim.btn);

        ib = (ImageButton) findViewById(R.id.add_btn);
        ib.setColorFilter(Color.rgb(191, 107, 48));

        adapter = new NoteAdapter(this, R.layout.list_item, notes);

        lw.setAdapter(adapter);

        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               selectionElement = i;
               Note selection = notes.get(selectionElement);
               startEditActivity(selection);
           }

       });




    }

    public void startEditActivity(Note selection)  {

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(NOTE_KEY, selection );
        intent.putExtra(REDACTION_KEY, "1");
        startActivityForResult(intent, REQUEST_ACCESS_TYPE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ACCESS_TYPE) {
            if (resultCode == RESULT_OK) {
                if(data.getStringExtra(REDACTION_KEY).equals("0")) {


                    Date date = new Date();
                    adapter.add(new Note(data.getStringExtra(TITLE_KEY), data.getStringExtra(DESCRIPTION_KEY), date, getRandomColor()));
                    adapter.notifyDataSetChanged();

                }

                if(data.getStringExtra(REDACTION_KEY).equals("1")) {

                    Date currentDate = notes.get(selectionElement).getDate();
                    int currentColor = notes.get(selectionElement).getColor();
                    notes.set(selectionElement, new Note(data.getStringExtra(TITLE_KEY), data.getStringExtra(DESCRIPTION_KEY), currentDate, currentColor));
                    adapter.notifyDataSetChanged();

                }
            }
        }
    }


    public void addNote (View v) {
        ib.startAnimation(animation);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(REDACTION_KEY, "0");
        startActivityForResult(intent, REQUEST_ACCESS_TYPE);
    }

    private int getRandomColor() {

        Random rand = new Random();
        int r = rand.nextInt(255 );
        int g = rand.nextInt(255 );
        int b = rand.nextInt(255);

        return Color.argb(150, r, g, b);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        /*notes = (ArrayList<Note>) savedInstanceState.getParcelable(NOTE_KEY);
        for(int i = 0; i < notes.size(); i++) {

            Log*.d("234", notes.get(i).getTitle());
        }*/
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
          //  outState.putParcelable(NOTE_KEY, (Parcelable) notes);

    }
}
