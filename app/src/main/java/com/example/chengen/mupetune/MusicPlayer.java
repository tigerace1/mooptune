package com.example.chengen.mupetune;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity implements View.OnClickListener,MediaPlayer.OnCompletionListener {
    private ArrayList<File> mySongs;
    private int position;
    private static MediaPlayer mp;
    private Bitmap loop,repeat,random,bitmap;;
    private SeekBar seekBar;
    private ImageButton mode;
    private ImageView photo;
    private TextView currentTime, totalTime, name;
    private ToggleButton playAndStop;
    private static int MODE_CODE=0;
    private Handler myHandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
        loop = BitmapFactory.decodeResource(getResources(),R.drawable.loop);
        repeat = BitmapFactory.decodeResource(getResources(),R.drawable.repeat);
        random = BitmapFactory.decodeResource(getResources(),R.drawable.randomtwo);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        photo = (ImageView)findViewById(R.id.ivSongImage);
        mode = (ImageButton)findViewById(R.id.ibMode);
        ImageButton foreward = (ImageButton) findViewById(R.id.iBForward);
        ImageButton backward = (ImageButton) findViewById(R.id.iBBackward);
        playAndStop = (ToggleButton) findViewById(R.id.tBPlayStop);
        currentTime = (TextView) findViewById(R.id.tvCurrentTime);
        totalTime = (TextView) findViewById(R.id.tvTotalTime);
        name = (TextView) findViewById(R.id.tvSongsN);
        foreward.setOnClickListener(this);
        backward.setOnClickListener(this);
        playAndStop.setOnClickListener(this);
        mode.setOnClickListener(this);
        if(MODE_CODE==0){
            mode.setBackground(new BitmapDrawable(getResources(), loop));
        }else if(MODE_CODE==1){
            mode.setBackground(new BitmapDrawable(getResources(), repeat));
        }else if(MODE_CODE==2){
            mode.setBackground(new BitmapDrawable(getResources(), random));
        }
        final Intent music = getIntent();
        Bundle bundle = music.getExtras();
        if (bundle != null) {
            if(mySongs!=null)
               mySongs.clear();
            mySongs = (ArrayList) bundle.getParcelableArrayList("songlist");
            position = bundle.getInt("pos", 0);
        }
        playSongs(position);
    }
    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            long startTime = mp.getCurrentPosition();
            currentTime.setText(getTimeString(startTime));
            seekBar.setProgress((int) startTime);
            myHandler.postDelayed(UpdateSongTime, 100);
        }
    };
    private void playSongs(int position) {
        Uri uri = Uri.parse(mySongs.get(position).toString());
        mp = MediaPlayer.create(getApplicationContext(), uri);
        mp.setOnCompletionListener(this);
        mp.start();
        myHandler.postDelayed(UpdateSongTime, 100);
        seekBar.setMax(mp.getDuration());
        totalTime.setText("| " + getTimeString(mp.getDuration()));
        name.setText(mySongs.get(position).getName().replace(".mp3", "").replace(".wav", "").replace("m4a", ""));
        changePhoto(position);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tBPlayStop:
                if (mp.isPlaying()) {
                    mp.pause();
                    myHandler.removeCallbacks(UpdateSongTime);
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.play);
                    playAndStop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newpause);
                    playAndStop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
                    mp.start();
                    myHandler.postDelayed(UpdateSongTime,100);
                }
                break;
            case R.id.iBForward:
                mp.stop();
                mp.release();
                myHandler.removeCallbacks(UpdateSongTime);
                position = (position + 1) % mySongs.size();
                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.newpause);
                playAndStop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap2));
                playSongs(position);
                break;
            case R.id.iBBackward:
                mp.stop();
                mp.release();
                myHandler.removeCallbacks(UpdateSongTime);
                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.newpause);
                playAndStop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap3));
                playSongs(position);
                break;
            case R.id.ibMode:
                PopupMenu popup2 = new PopupMenu(this,mode);
                popup2.getMenuInflater().inflate(R.menu.playing_mode, popup2.getMenu());
                popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Loop")) {
                            MODE_CODE = 0;
                            mode.setBackground(new BitmapDrawable(getResources(), loop));
                        } else if(item.getTitle().equals("Repeat")) {
                            MODE_CODE = 1;
                            mode.setBackground(new BitmapDrawable(getResources(), repeat));
                        }else if(item.getTitle().equals("Random")) {
                            MODE_CODE = 2;
                            mode.setBackground(new BitmapDrawable(getResources(), random));
                        }
                        return true;
                     }
                    });
                popup2.show();
                break;
        }
    }
    @Override
    public void onCompletion(MediaPlayer player) {
        mp.stop();
        mp.release();
        myHandler.removeCallbacks(UpdateSongTime);
        if(MODE_CODE==0) {
            position = (position + 1) % mySongs.size();
            playSongs(position);
        }else if(MODE_CODE==1){
            playSongs(position);
        }else if(MODE_CODE==2){
            position = (int)(Math.random()*mySongs.size());
            playSongs(position);
        }
    }
    private String getTimeString(long millis) {
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);
        return (String.format("%02d", hours))+":"+String.format("%02d", minutes)+":"+
                String.format("%02d", seconds);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, NavigationLayout.class);
                intent.putExtra("songsback", mySongs);
                loop.recycle();
                repeat.recycle();
                random.recycle();
                bitmap.recycle();
                myHandler.removeCallbacks(UpdateSongTime);
                startActivity(intent);
                if(photo.getBackground()!=null)
                    ((BitmapDrawable)photo.getBackground()).getBitmap().recycle();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NavigationLayout.class);
        intent.putExtra("songsback", mySongs);
        loop.recycle();
        repeat.recycle();
        random.recycle();
        bitmap.recycle();
        startActivity(intent);
        myHandler.removeCallbacks(UpdateSongTime);
        if(photo.getBackground()!=null)
            ((BitmapDrawable)photo.getBackground()).getBitmap().recycle();
        finish();
    }
    private void changePhoto(int position){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mySongs.get(position).getPath());
        byte[] artBytes =  mmr.getEmbeddedPicture();
        if(artBytes != null) {
            InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
            bitmap = BitmapFactory.decodeStream(is);
        } else
            bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.musics);
        photo.setBackground(null);
        photo.setBackground(new BitmapDrawable(getResources(),bitmap));
    }
    public static MediaPlayer getMediaPlayer(){
        return mp;
    }
    public static int getMode(){
        return MODE_CODE;
    }
}
