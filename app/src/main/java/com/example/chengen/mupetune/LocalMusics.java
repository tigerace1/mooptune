package com.example.chengen.mupetune;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class LocalMusics extends Fragment implements View.OnClickListener {
    View v;
    private  ArrayList<File> defaultSongs;
    private static final String path = "/storage/extSdCard/music";
    private ListView musicList;
    private static int MODE_CODE;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        v = inflater.inflate(R.layout.local_music, container, false);
        musicList = (ListView)v.findViewById(R.id.listMusic);
        ImageButton Default = (ImageButton) v.findViewById(R.id.ibDefault);
        ImageButton love = (ImageButton) v.findViewById(R.id.ibLove);
        ImageButton singer = (ImageButton) v.findViewById(R.id.ibSingers);
        ImageButton recent = (ImageButton) v.findViewById(R.id.ibRecent);
        ImageButton albums = (ImageButton) v.findViewById(R.id.ibAlbums);
        ImageButton custom = (ImageButton) v.findViewById(R.id.ibCustom);
        updatePlayList();
        musicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), MusicPlayer.class)
                        .putExtra("pos", position).putExtra("songlist", defaultSongs));
            }
        });
        Default.setOnClickListener(this);
        love.setOnClickListener(this);
        singer.setOnClickListener(this);
        recent.setOnClickListener(this);
        albums.setOnClickListener(this);
        custom.setOnClickListener(this);
        return v;
    }
    private void updatePlayList(){
        defaultSongs=new ArrayList<>();
        ArrayList<Bitmap> mySongPhotos = new ArrayList<>();
        File home = new File(path);
        if (home.listFiles(new Mp3Filter()).length > 0) {
            Collections.addAll(defaultSongs, home.listFiles(new Mp3Filter()));
        }
        Collections.sort(defaultSongs);
        SongDataAdapter adapter = new SongDataAdapter(getActivity(),
                R.layout.activity_song_data_adapter);
        Bitmap bitmap;
        for(int i=0;i<defaultSongs.size();i++) {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(defaultSongs.get(i).getPath());
            byte[] artBytes = mmr.getEmbeddedPicture();
            String albumName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            String singer = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            String songName = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (artBytes != null) {
                InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
                bitmap = BitmapFactory.decodeStream(is);
            } else
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.musics);
            mySongPhotos.add(resizeBitmap(bitmap, 80, 80));
            SongDataProvider provider = new SongDataProvider(new BitmapDrawable(getResources(),mySongPhotos.get(i))
                    ,songName, singer);
            adapter.add(provider);
        }
        musicList.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    class Mp3Filter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String filename) {
            return (filename.endsWith(".mp3") || filename.endsWith(".wav")||filename.endsWith("m4a"));
        }
    }
    public Bitmap resizeBitmap(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }
}
