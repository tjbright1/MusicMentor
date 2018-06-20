package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Logic borrowed from a tutorial at https://dzone.com/articles/android-tips
 * */
public class MainActivity extends AppCompatActivity {

    TextView task_1;
    TextView video_1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task_1 = (TextView) findViewById(R.id.task_1);

        video_1 = (TextView) findViewById(R.id.video_1);

        task_1.setVisibility(View.GONE);    // hide until lesson is clicked
        video_1.setVisibility(View.GONE);   // hide until task is clicked
    }
    /**
     * onClick handler
     */
    public void toggle_lesson(View v){
        task_1.setVisibility( task_1.isShown()
                ? View.GONE
                : View.VISIBLE );
        video_1.setVisibility( View.GONE );
    }

    public void toggle_task(View v){
        video_1.setVisibility( video_1.isShown()
                ? View.GONE
                : View.VISIBLE );
    }
}
