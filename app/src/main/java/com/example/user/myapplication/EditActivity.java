package com.example.user.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;


public class EditActivity extends AppCompatActivity {

    private String redaction;
    private EditText et_title;
    private EditText et_description;
    ImageView iv;
    int curColor;
    private static final int REQUEST_ACCESS_TYPE = 2;
    static final String COLOR_KEY = "COLOR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        redaction = getIntent().getExtras().getString(MainActivity.REDACTION_KEY);

        et_title = (EditText) findViewById(R.id.edit_title);
        et_description = (EditText) findViewById(R.id.edit_description);
        iv = (ImageView) findViewById(R.id.icon_repeater);
        if(redaction.equals("0")) {
            curColor = getRandomColor();
            iv.setColorFilter(curColor);
        }
        if (redaction.equals("1")) {
            Note selectionNote = (Note) getIntent().getSerializableExtra("NOTE");
            et_title.setText(selectionNote.getTitle());
            et_description.setText(selectionNote.getDescription());
            curColor = selectionNote.getColor();
            iv.setColorFilter(selectionNote.getColor());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ACCESS_TYPE) {
            if (resultCode == RESULT_OK) {
                iv.setColorFilter(data.getIntExtra(COLOR_KEY, 0));
                curColor = data.getIntExtra(COLOR_KEY, 0);
            }
        }
    }

    public void saveNote(View v) {

        String title = et_title.getText().toString();
        String description = et_description.getText().toString();

        if(title.trim().length() > 0) {

            Intent data = new Intent();
            data.putExtra(MainActivity.REDACTION_KEY, redaction);
            data.putExtra(MainActivity.TITLE_KEY, title.trim());
            data.putExtra(MainActivity.DESCRIPTION_KEY, description.trim());
            Log.d("11111", String.valueOf(curColor));
            data.putExtra(COLOR_KEY, curColor);
            setResult(RESULT_OK, data);
            finish();

        }
        else {
            if(title.trim().length() == 0 ) {
                Toast toastTitle_two = Toast.makeText(this, "Введите заголовок", Toast.LENGTH_LONG);
                toastTitle_two.show();

            }

           /* if(title.trim().length() >= 20) {
                et_title.getText().clear();
                Toast toastTitle_three = Toast.makeText(this, "Заголовок должен содержать до 20 символов", Toast.LENGTH_LONG);
                toastTitle_three.show();
            }*/
        }
    }

    public void editColor (View v) {

        Intent intent = new Intent(this, ColorPicker.class);
        startActivityForResult(intent, REQUEST_ACCESS_TYPE);
    }


    private int getRandomColor() {

        Random rand = new Random();
        int r = rand.nextInt(255 );
        int g = rand.nextInt(255 );
        int b = rand.nextInt(255);

        return Color.rgb(r, g, b);
    }
}
