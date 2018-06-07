package com.example.fabian.swim_lab_zad5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;


public class Settings extends AppCompatActivity {

    Switch backgroundColor;
    CheckBox playInTurn;
    CheckBox looping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
    }

    public void initViews(){
        looping = findViewById(R.id.checkBox);
        playInTurn = findViewById(R.id.checkBox2);
        backgroundColor = findViewById(R.id.switch1);
        looping.setChecked(MainActivity.looping);
        playInTurn.setChecked(MainActivity.playInTurn);
        backgroundColor.setChecked(MainActivity.isDark);

        looping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.looping = looping.isChecked();
            }
        });

        playInTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.playInTurn = playInTurn.isChecked();
            }
        });

        backgroundColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.isDark = backgroundColor.isChecked();
            }
        });
    }

}
