package com.example.fabian.swim_lab_zad5;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static final int TIME_TO_WIND = 10000;
    private static final int MEMORY_ACCESS_KEY = 4;
    static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/Music/";
    static ArrayList <String> playlist = new ArrayList <> ();
    RecyclerView songs;
    TextView tvTitle;
    TextView tvLength;
    SeekBar seekBar;
    static MediaPlayer mediaPlayer;
    static boolean playInTurn = false;
    static boolean looping = false;
    static boolean isDark = false;
    static int currentIndex = 0;
    private Handler handler;
    Runnable runnable;
    CoordinatorLayout layout;
    MyViewModel model;
    Tools tools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE))
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MEMORY_ACCESS_KEY);
        tools = new Tools();
        if(playlist.size() == 0)
            tools.createPlaylist();
        initViews();
        initRecycleView();
        initMediaPlayer();
        setViews();
    }


    @Override
    protected void onStop() {
        super.onStop();
        model.setMediaPlayer(mediaPlayer);
        if(mediaPlayer != null) {
            mediaPlayer.pause();
            model.setCurrentProgress(mediaPlayer.getCurrentPosition());
        }
        model.setCurrentTitle((String) tvTitle.getText());
        model.setCurrentLength((String) tvLength.getText());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case MEMORY_ACCESS_KEY:
                if(!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(getApplicationContext(), R.string.no_access, Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }

        if(id == R.id.about_author){
            Intent intent = new Intent(this, InfoAboutAuthor.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    private void initViews(){
        tvTitle = findViewById(R.id.title_song);
        tvLength = findViewById(R.id.length_song);
        seekBar = findViewById(R.id.seekBar);
        layout = findViewById(R.id.main_layout);
        if(isDark)
            layout.setBackgroundColor(Color.GRAY);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    mediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    private void setOnCompletion(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(playInTurn){
                    mediaPlayer.reset();
                    currentIndex++;
                    if(currentIndex == playlist.size())
                        currentIndex = 0;
                    try {
                        mediaPlayer.setDataSource(PATH + playlist.get(currentIndex));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    setViewsForNewSong(tools.getSongTitle(PATH + playlist.get(currentIndex)),
                            tools.getSongDuration(PATH + playlist.get(currentIndex)),
                            mediaPlayer.getDuration());
                    mediaPlayer.start();
                }
            }
        });
    }

    private void setViews(){
        model = ViewModelProviders.of(this).get(MyViewModel.class);
        tvTitle.setText(model.getCurrentTitle().getValue());
        tvLength.setText(model.getCurrentLength().getValue());
        mediaPlayer = model.getMediaPlayer().getValue();
        if(mediaPlayer != null) {
            mediaPlayer.seekTo(model.getCurrentProgress().getValue());
            seekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            playCycle();
        }
    }


    private void initRecycleView(){
        songs = findViewById(R.id.list_of_songs);
        songs.setHasFixedSize(true);
        songs.setLayoutManager(new LinearLayoutManager(this));
        songs.setItemAnimator(new DefaultItemAnimator());
        songs.setAdapter(new RecycleViewAdapter(songs, getApplicationContext(), tvTitle, tvLength,
                seekBar));
    }


    private void initMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this, R.raw.singlesoundfull);
        handler = new Handler();
    }


    public void playMusic(View view){
        if(mediaPlayer != null) {
            mediaPlayer.setLooping(looping);
            setOnCompletion();
            if (!mediaPlayer.isPlaying())
                mediaPlayer.start();
            else
                mediaPlayer.pause();
            playCycle();
        }
    }


    public void playCycle(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }


    public void fastForward(View view){
        if(mediaPlayer != null)
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + TIME_TO_WIND);
    }


    public void rewind(View view) {
        if(mediaPlayer != null)
            mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - TIME_TO_WIND);
    }

    private void setViewsForNewSong(String title, String length, int duration){
        tvTitle.setText(title);
        tvLength.setText(length);
        seekBar.setMax(duration);
    }
}