package com.example.alias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Shows rules

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules);
        TextView rulesTextView = findViewById(R.id.rulesText);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RulesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        boolean isEnglish = prefs.getBoolean(MainActivity.IS_ENGLISH_KEY, true);

        if (isEnglish) {
            rulesTextView.setText("1. Teams take turns guessing while one member gives clues.\n" +
                    "2. Each team has 60 seconds per round to guess as many words as possible.\n" +
                    "3. The clue-giver cannot use the word itself or any part of it in their clues.\n" +
                    "4. If a team guesses the word correctly, they get a point.\n" +
                    "5. If a team cannot guess the word, they can skip it, but no points are awarded.\n" +
                    "6. Teams take turns until all words are guessed or time runs out.\n" +
                    "7. Scoring: Each correctly guessed word earns one point.\n" +
                    "8. The team with the most points at the end wins.");
        } else {
            rulesTextView.setText("1. Joukkueet vuorottelevat arvaamassa, kun yksi joukkueen jäsen antaa vihjeitä.\n" +
                    "2. Jokaisella joukkueella on 60 sekuntia kierrosta kohden arvata mahdollisimman monta sanaa.\n" +
                    "3. Vihjeiden antaja ei saa käyttää itse sanaa tai sen osaa vihjeissään.\n" +
                    "4. Jos joukkue arvaa sanan oikein, he saavat pisteen.\n" +
                    "5. Jos joukkue ei osaa arvata sanaa, he voivat jättää sen väliin, mutta pisteitä ei anneta.\n" +
                    "6. Joukkueet vuorottelevat, kunnes kaikki sanat on arvattu tai aika loppuu.\n" +
                    "7. Pisteiden laskenta: Jokainen oikein arvattu sana ansaitsee yhden pisteen.\n" +
                    "8. Joukkue, jolla on eniten pisteitä lopussa, voittaa.");
        }
    }
}
