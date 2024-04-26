package com.example.alias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

//Responsible for the Main page UI and functionality

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyPrefs";
    public static final String IS_ENGLISH_KEY = "IsEnglish";
    private boolean isEnglish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        isEnglish = prefs.getBoolean(IS_ENGLISH_KEY, true);
        Button newGameButton = findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameSettingActivity.class);
                startActivity(intent);
            }
        });

        Button languageButton = findViewById(R.id.languageButton);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLanguage();
                updateView();
            }
        });

        updateLanguageButton();

        ImageView rules = findViewById(R.id.infoView);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the RulesActivity
                Intent intent = new Intent(MainActivity.this, RulesActivity.class);
                startActivity(intent);
            }
        });

        Button showRank = findViewById(R.id.rankingButton);
        showRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RankActivity.class);
                startActivity(intent);
            }
        });
    }


    private void switchLanguage() {
        isEnglish = !isEnglish;
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(IS_ENGLISH_KEY, isEnglish);
        editor.apply();
    }

    private void updateView() {
        recreate();
    }

    private void updateLanguageButton() {
        Button languageButton = findViewById(R.id.languageButton);
        if (isEnglish) {
            languageButton.setText("EN");
        } else {
            languageButton.setText("FI");
        }
    }
}