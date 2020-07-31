package hub.programs.boombox;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    MediaPlayer theMediaPlayer;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        theMediaPlayer = MediaPlayer.create(MainActivity.this.getBaseContext(), R.raw.traveling);
        theMediaPlayer.start();
    }
}