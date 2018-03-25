package com.example.user.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.user.myapplication.MainActivity.NOTE_KEY;


public class ColorPicker extends AppCompatActivity {

    static final String MAIN_COLORS_KEY = "MAIN_COLORS";
    static final String CURRENT_COLOR_KEY = "CURRENT_COLOR";
    static final String BEST_COLOR_KEY = "BEST_COLOR";

    RelativeLayout relativeLayout;
    MyScrollView scrollView;
    LinearLayout scroll_layout;
    LinearLayout best_colors_layout;
    TextView currentColorView;
    TextView rgbValue;
    TextView hsvValue;
    boolean isLongPressHandlerActivated = false;
    boolean isSignal = true;
    float x1;
    int saveColor;
    Bitmap bitmap;
    View.OnTouchListener onTouchListener_main;
    View.OnTouchListener onTouchListener_inbest;
    View.OnTouchListener onTouchListener_outbest;
    float firstY;
    ArrayList<ImageView> bestList = new ArrayList<>();
    ArrayList<Integer> mainColorsList = new ArrayList<>();
    ArrayList<Integer> bestColorsList = new ArrayList<>();
    long lastClickTime = 0;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_color);
        scroll_layout = (LinearLayout) findViewById(R.id.scroll_linear);
        currentColorView = (TextView) findViewById(R.id.current_color);
        if(savedInstanceState != null) {
            mainColorsList = (ArrayList<Integer>) savedInstanceState.getIntegerArrayList(MAIN_COLORS_KEY);
            bestColorsList = (ArrayList<Integer>) savedInstanceState.getIntegerArrayList(BEST_COLOR_KEY);
            currentColorView.setBackgroundColor(savedInstanceState.getInt(CURRENT_COLOR_KEY));
        }

        relativeLayout = (RelativeLayout) findViewById(R.id.edit_color_view);
        scrollView = (MyScrollView) findViewById(R.id.scroll);
        rgbValue = (TextView) findViewById(R.id.rgb_value);
        hsvValue = (TextView) findViewById(R.id.hsv_value);
        best_colors_layout = (LinearLayout) findViewById(R.id.best_colors);
        setDrawableBackground();

        ViewTreeObserver vto = scrollView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    relativeLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                else
                    relativeLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                scroll_layout.setDrawingCacheEnabled(true);
                scroll_layout.buildDrawingCache();
                bitmap = scroll_layout.getDrawingCache();
                currentColorView.setOnTouchListener(onTouchListener_inbest);

                    for (int i = 1; i <= 16; i++) {
                        int id = getResources().getIdentifier("color" + i, "id", getPackageName());
                        setDefaultColor(id);
                        Button bt = (Button) findViewById(id);
                        if(savedInstanceState != null)
                            bt.setBackgroundColor(mainColorsList.get(i - 1));
                    }

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                layoutParams.setMargins(8,8,8,8);
                for(int i = 0; i < 5; i++){
                    ImageView iv = new ImageView(ColorPicker.this);
                    iv.setLayoutParams(layoutParams);
                    iv.setId(100 +i);
                    if(savedInstanceState != null)
                        iv.setBackgroundColor(bestColorsList.get(i));
                    else
                        iv.setBackgroundColor(Color.rgb(255,255,255));
                    best_colors_layout.addView(iv);
                    bestList.add(iv);
                    iv.setOnTouchListener(onTouchListener_outbest);
                }
            }
        });

        onTouchListener_main = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int MAX_DISTANCE = v.getWidth();
                Rect myViewRect = new Rect();
                v.getGlobalVisibleRect(myViewRect);
                float cx = myViewRect.exactCenterX();
                int LONG_PRESS_TIMEOUT = ViewConfiguration.getLongPressTimeout();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        saveColor = ((ColorDrawable) currentColorView.getBackground()).getColor();
                        firstY = event.getRawY();
                        isLongPressHandlerActivated = false;
                        x1 = event.getX();
                        scrollView.setScrollingEnabled(false);
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (event.getEventTime() - event.getDownTime() > LONG_PRESS_TIMEOUT && Math.abs(event.getRawX() - cx) <= MAX_DISTANCE) {
                            if (isSignal) {
                                long mills = 50L;
                                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(mills);
                            }

                            isSignal = false;
                            isLongPressHandlerActivated = true;
                            int pixel = bitmap.getPixel((int) event.getRawX() + scrollView.getScrollX(), (int) 10);
                            if (Math.abs(0.5 + (firstY - event.getRawY()) / 500) <= 1 && (0.5 + (firstY - event.getRawY()) / 500) >= 0) {
                                float[] hsv = new float[3];
                                Color.colorToHSV(pixel, hsv);
                                hsv[2] = (float) (0.5 + (firstY - event.getRawY()) / 500);
                                v.setBackgroundColor(Color.HSVToColor(hsv));
                                int color = Color.HSVToColor(hsv);

                                currentColorView.setBackgroundColor(color);
                                rgbValue.setVisibility(View.INVISIBLE);
                                hsvValue.setVisibility(View.INVISIBLE);
                            }
                        }
                        if (event.getEventTime() - event.getDownTime() > LONG_PRESS_TIMEOUT && Math.abs(event.getRawX() - cx) > MAX_DISTANCE) {
                            long mills = 50L;
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(mills);
                        }
                        if (event.getEventTime() - event.getDownTime() > LONG_PRESS_TIMEOUT && (Math.abs(0.5 + (firstY - event.getRawY()) / 500) > 1 || (0.5 + (firstY - event.getRawY()) / 500) < 0)) {

                            long mills = 50L;
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(mills);
                        }
                        break;

                    case MotionEvent.ACTION_UP:
                        isSignal = true;
                        long clickTime = System.currentTimeMillis();
                        if (clickTime - lastClickTime < ViewConfiguration.getDoubleTapTimeout()){
                            setDefaultColor(v.getId());
                            int color = ((ColorDrawable) v.getBackground()).getColor();
                            currentColorView.setBackgroundColor(color);
                            rgbValue.setText("RGB: " + Color.red(color) + " " + Color.green(color) + " " + Color.blue(color));
                            float[] hsv = new float[3];
                            Color.colorToHSV(color, hsv);
                            hsvValue.setText("HSV:" + hsv[0] + " " + hsv[1] + " " + hsv[2]);
                            lastClickTime = 0;
                        } else {
                            currentColorView.setBackgroundColor(saveColor);
                            rgbValue.setVisibility(View.VISIBLE);
                            hsvValue.setVisibility(View.VISIBLE);
                            if (!isLongPressHandlerActivated) {
                                int color = ((ColorDrawable) v.getBackground()).getColor();
                                currentColorView.setBackgroundColor(color);
                                rgbValue.setText("RGB: " + Color.red(color) + " " + Color.green(color) + " " + Color.blue(color));
                                float[] hsv = new float[3];
                                Color.colorToHSV(color, hsv);
                                hsvValue.setText("HSV:" + hsv[0] + " " + hsv[1] + " " + hsv[2]);
                            }
                        }
                        lastClickTime = clickTime;
                        scrollView.setScrollingEnabled(true);
                        break;

                }
                    return  true;
            }
        };

        onTouchListener_inbest = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent events) {

                switch (events.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        int curColor = ((ColorDrawable) view.getBackground()).getColor();
                        ImageView iv = bestList.get(0);
                        best_colors_layout.removeViewAt(0);
                        bestList.remove(0);
                        iv.setBackgroundColor(curColor);
                        bestList.add(iv);
                        iv.setOnTouchListener(onTouchListener_outbest);
                        best_colors_layout.addView(iv);
                        break;
                }

                return false;
            }
        };

        onTouchListener_outbest = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent events) {

                switch (events.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.d("123", "1231");
                        int curColor = ((ColorDrawable) view.getBackground()).getColor();
                        currentColorView.setBackgroundColor(curColor);
                        break;
                }
                return false;
            }
        };
    }



    private void setDrawableBackground() {
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.RED});
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        drawable.setStroke(2, Color.BLACK);
        scroll_layout.setBackground(drawable);
    }

    private void setDefaultColor(int id) {
        Rect myViewRect = new Rect();
        Button iv;
        iv = (Button) findViewById(id);
        iv.setOnTouchListener(onTouchListener_main);
        iv.getGlobalVisibleRect(myViewRect);
        float cx = myViewRect.exactCenterX();
        float cy = myViewRect.exactCenterY();
        int pixel = bitmap.getPixel((int) cx + scrollView.getScrollX(), (int) 50);
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        iv.setBackgroundColor(Color.rgb(r, g, b));
    }

    public void changeColor (View v) {
        Intent data = new Intent();
        data.putExtra(EditActivity.COLOR_KEY, ((ColorDrawable) currentColorView.getBackground()).getColor());
        setResult(RESULT_OK, data);
        finish();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainColorsList.clear();
        bestColorsList.clear();
        ArrayList<View> allViews;
        allViews = scroll_layout.getTouchables();

        for (int i = 0; i < allViews.size(); i++) {
            int curColor = ((ColorDrawable) allViews.get(i).getBackground()).getColor();
            mainColorsList.add(curColor);
        }

        for (int i = 0; i < bestList.size(); i++) {
            int curColor = ((ColorDrawable) bestList.get(i).getBackground()).getColor();
            bestColorsList.add(curColor);
        }
        outState.putIntegerArrayList(MAIN_COLORS_KEY, mainColorsList);
        outState.putInt(CURRENT_COLOR_KEY, ((ColorDrawable) currentColorView.getBackground()).getColor());
        outState.putIntegerArrayList(BEST_COLOR_KEY, bestColorsList);
    }
}
