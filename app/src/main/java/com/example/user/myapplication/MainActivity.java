package com.example.user.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final String REDACTION_KEY = "REDACTION";
    static final String NOTE_KEY = "NOTE";
    static final String TITLE_KEY = "TITLE";
    static final String DESCRIPTION_KEY = "DESCRIPTION";
    private static final int REQUEST_ACCESS_TYPE = 1;


    private ArrayList<Note> notes = new ArrayList();
    Animation animation;
    private int selectionElement;
    NoteAdapter adapter;
    ImageButton ib;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState != null)
            notes = (ArrayList<Note>) savedInstanceState.getSerializable(NOTE_KEY);

        ListView lw = (ListView) findViewById(R.id.my_list);
        animation = AnimationUtils.loadAnimation(this, R.anim.btn);

       // ib = (ImageButton) findViewById(R.id.add_btn);
        //ib.setColorFilter(Color.rgb(191, 107, 48));

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

        listView = (ListView) findViewById(R.id.my_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.attachToListView(listView);
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
                    adapter.add(new Note(data.getStringExtra(TITLE_KEY), data.getStringExtra(DESCRIPTION_KEY), date, data.getIntExtra(EditActivity.COLOR_KEY, 0)));
                    adapter.notifyDataSetChanged();

                }

                if(data.getStringExtra(REDACTION_KEY).equals("1")) {

                    Date currentDate = notes.get(selectionElement).getDate();
                    int currentColor = notes.get(selectionElement).getColor();
                    notes.set(selectionElement, new Note(data.getStringExtra(TITLE_KEY), data.getStringExtra(DESCRIPTION_KEY), currentDate, data.getIntExtra(EditActivity.COLOR_KEY, 0)));
                    adapter.notifyDataSetChanged();

                }
            }
        }
    }


    public void addNote (View v) {
        //ib.startAnimation(animation);
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(REDACTION_KEY, "0");
        startActivityForResult(intent, REQUEST_ACCESS_TYPE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
          outState.putSerializable(NOTE_KEY, notes);
    }
}
