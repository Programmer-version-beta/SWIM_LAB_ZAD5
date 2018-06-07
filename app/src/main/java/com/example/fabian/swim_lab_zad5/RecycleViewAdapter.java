package com.example.fabian.swim_lab_zad5;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class RecycleViewAdapter extends RecyclerView.Adapter {
    private RecyclerView recyclerView;
    private Context context;
    private TextView tvTitleFromMain;
    private TextView tvLengthFromMain;
    private SeekBar seekBarFromMain;
    private Tools tools;


    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvLength;
        ImageButton playsong;

        MyViewHolder(View pItem) {
            super(pItem);
            tvTitle = pItem.findViewById(R.id.title);
            tvAuthor = pItem.findViewById(R.id.author);
            tvLength = pItem.findViewById(R.id.length);
            playsong = pItem.findViewById(R.id.button_play_song);
            tools = new Tools();
        }
    }

    RecycleViewAdapter(RecyclerView recyclerView, Context context, TextView tvTitleFromMain,
            TextView tvLengthFromMain, SeekBar seekBarFromMain){
        this.recyclerView = recyclerView;
        this.context = context;
        this.tvTitleFromMain = tvTitleFromMain;
        this.tvLengthFromMain = tvLengthFromMain;
        this.seekBarFromMain = seekBarFromMain;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_item, parent, false);

        return new RecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final String length = tools.getSongDuration(MainActivity.PATH + MainActivity.playlist.get(position));
        final String author = tools.getSongAuthor(MainActivity.PATH + MainActivity.playlist.get(position));
        final String title = tools.getSongTitle(MainActivity.PATH + MainActivity.playlist.get(position));
        ((MyViewHolder) holder).tvTitle.setText(title);
        ((MyViewHolder) holder).tvAuthor.setText(author);
        ((MyViewHolder) holder).tvLength.setText(length);
        ((MyViewHolder) holder).playsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvTitleFromMain.setText(title);
                try {
                    if(MainActivity.mediaPlayer == null)
                        MainActivity.mediaPlayer = new MediaPlayer();
                    else
                        MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(MainActivity.PATH + MainActivity.playlist.get(position));
                    MainActivity.mediaPlayer.prepare();
                    MainActivity.currentIndex = position;
                }catch (IOException e){}
                seekBarFromMain.setMax(MainActivity.mediaPlayer.getDuration());
                tvLengthFromMain.setText(length);
            }
        });
    }

    @Override
    public int getItemCount() { return MainActivity.playlist.size(); }
}
