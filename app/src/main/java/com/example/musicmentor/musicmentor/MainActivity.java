package com.example.musicmentor.musicmentor;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

/**
 * Logic borrowed from a tutorial at https://dzone.com/articles/android-tips
 * */
public class MainActivity extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;

    TextView task_1;
    TextView video_1;
    Button button;
    VideoView resultVideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        task_1 = (TextView) findViewById(R.id.task_1);

        video_1 = (TextView) findViewById(R.id.video_1);

        button = (Button) findViewById(R.id.button);
        resultVideo = (VideoView) findViewById(R.id.videoView);

        // Hide until menu "parents" are clicked
        task_1.setVisibility(View.GONE);
        video_1.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        resultVideo.setVisibility(View.GONE);
    }

    /**
     * begin onClick handlers
     */
    public void toggle_lesson(View v) {
        task_1.setVisibility( task_1.isShown()
                ? View.GONE
                : View.VISIBLE );
        video_1.setVisibility( View.GONE );
        resultVideo.setVisibility( View.GONE );
        button.setVisibility( View.GONE );
    }

    public void toggle_task(View v) {
        video_1.setVisibility( video_1.isShown()
                ? View.GONE
                : View.VISIBLE );
        resultVideo.setVisibility( View.GONE );
        button.setVisibility( View.GONE );
    }

    public void toggle_video(View v) {
        resultVideo.setVisibility( resultVideo.isShown()
            ? View.GONE
            : View.VISIBLE);

        button.setVisibility( button.isShown()
            ? View.GONE
            : View.VISIBLE);
    }

    /**
     * End onClick handlers (sorta)
     */

    /**
     * Record video
     */
    public void dispatchTakeVideoIntent(View videoView) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    /**
     * Handle resultant video
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            resultVideo.setVideoURI(videoUri);
        }
    }
}
