package com.example.fabian.swim_lab_zad5;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.media.MediaPlayer;

class MyViewModel extends ViewModel {
    private MutableLiveData<MediaPlayer> media_player;
    private MutableLiveData<String> current_title;
    private MutableLiveData<String> current_length;
    private MutableLiveData<Integer> current_progress;

    void setMediaPlayer(MediaPlayer media_player) {
        if(this.media_player == null){
            this.media_player = new MutableLiveData<>();
        }
        this.media_player.setValue(media_player);
    }

    LiveData<MediaPlayer> getMediaPlayer(){
        if(media_player == null){
            media_player = new MutableLiveData<>();
            setMediaPlayer(null);
        }
        return media_player;
    }

    LiveData<String> getCurrentTitle() {
        if (current_title == null) {
            current_title = new MutableLiveData<>();
            setCurrentTitle("No title");
        }
        return current_title;
    }

    void setCurrentTitle(String current_title) {
        if (this.current_title == null) {
            this.current_title = new MutableLiveData<>();
        }
        this.current_title.setValue(current_title);
    }

    void setCurrentProgress(Integer current_progress) {
        if(this.current_progress == null){
            this.current_progress = new MutableLiveData<>();
        }
        this.current_progress.setValue(current_progress);
    }

    void setCurrentLength(String current_length) {
        if(this.current_length == null) {
            this.current_length = new MutableLiveData<>();
        }
        this.current_length.setValue(current_length);
    }

    LiveData<String> getCurrentLength(){
        if(current_length == null) {
            current_length = new MutableLiveData<>();
            setCurrentLength("0");
        }
        return current_length;
    }

    LiveData<Integer> getCurrentProgress(){
        if(current_progress == null) {
            current_progress = new MutableLiveData<>();
            setCurrentProgress(0);
        }
        return current_progress;
    }
}
