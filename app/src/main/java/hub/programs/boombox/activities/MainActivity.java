package hub.programs.boombox.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

import hub.programs.boombox.R;

public class    MainActivity extends AppCompatActivity {

    MediaPlayer theMediaPlayer;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theMediaPlayer = MediaPlayer.create(MainActivity.this.getBaseContext(), R.raw.traveling);
        theMediaPlayer.start();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        if (theMediaPlayer != null)
            theMediaPlayer.release();
    }

    // Release resources clinged onto by MediaPlayer when Activity is invisible/stopped - no longer
    // interacting with user. Background playback later
    @Override
    protected void onStop () {
        super.onStop();
        theMediaPlayer.release();
        theMediaPlayer = null;
    }
}