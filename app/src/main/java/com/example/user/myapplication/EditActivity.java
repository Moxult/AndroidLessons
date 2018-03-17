package com.example.user.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;


public class EditActivity extends AppCompatActivity {

    private String redaction;
    private EditText et_title;
    private EditText et_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        redaction = getIntent().getExtras().getString(MainActivity.REDACTION_KEY);

        et_title = (EditText) findViewById(R.id.edit_title);
        et_description = (EditText) findViewById(R.id.edit_description);

        if (redaction.equals("1")) {
            Note selectionNote = (Note) getIntent().getParcelableExtra("NOTE");
            et_title.setText(selectionNote.getTitle());
            et_description.setText(selectionNote.getDescription());

        }
    }


    public void saveNote(View v) {

        String title = et_title.getText().toString();
        String description = et_description.getText().toString();

        if(title.trim().length() > 0 && title.trim().length() <= 20 && description.trim().length() <= 80) {

            Intent data = new Intent();
            data.putExtra(MainActivity.REDACTION_KEY, redaction);
            data.putExtra(MainActivity.TITLE_KEY, title.trim());
            data.putExtra(MainActivity.DESCRIPTION_KEY, description.trim().replaceAll("\n+", " "));

            setResult(RESULT_OK, data);
            finish();

        }
        else {
            if(title.trim().length() != 0  && description.trim().length() > 80 && title.trim().length() < 20 ) {
                Toast toastTitle_one = Toast.makeText(this, "Описание должно содержать до 80 символов", Toast.LENGTH_LONG);
                toastTitle_one.show();
            }
            if(title.trim().length() == 0 ) {
                Toast toastTitle_two = Toast.makeText(this, "Введите заголовок", Toast.LENGTH_LONG);
                toastTitle_two.show();

            }

            if(title.trim().length() >= 20) {
                et_title.getText().clear();
                Toast toastTitle_three = Toast.makeText(this, "Заголовок должен содержать до 20 символов", Toast.LENGTH_LONG);
                toastTitle_three.show();
            }
        }
    }
}
