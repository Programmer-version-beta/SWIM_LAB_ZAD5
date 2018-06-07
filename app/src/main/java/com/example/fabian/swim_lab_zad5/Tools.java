package com.example.fabian.swim_lab_zad5;

import android.media.MediaMetadataRetriever;

import java.io.File;
import java.io.FilenameFilter;


class Tools {
    String getSongDuration(String path){
        String result;
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
        long dur = Long.parseLong(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        String seconds = String.valueOf((dur % 60000) / 1000);
        String minutes = String.valueOf(dur / 60000);
        if (seconds.length() == 1) {
            result = "0" + minutes + ":0" + seconds;
        }else {
            result = "0" + minutes + ":" + seconds;
        }
        metaRetriever.release();

        return result;
    }


    String getSongTitle(String path) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
        return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
    }


    String getSongAuthor(String path) {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(path);
        return metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }


    class MP3Filter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));
        }
    }


    void createPlaylist(){
        File home = new File(MainActivity.PATH);
        MP3Filter filter = new MP3Filter();
        if(home.listFiles().length > 0){
            for(File file: home.listFiles()){
                if(filter.accept(file, file.getName()))
                    MainActivity.playlist.add(file.getName());
            }
        }
    }
}
