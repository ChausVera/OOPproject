package com.example.alias;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

//Responsible for UI and functionality during playing mode

public class PlayingActivity extends AppCompatActivity {

    private List<String> wordsList = new ArrayList<>();
    private TextView currentWordTextView;
    private TextView currentTeamTextView;
    private RecyclerView pastWordsRecyclerView;
    private PastWordsAdapter pastWordsAdapter;
    private List<String> pastWords = new ArrayList<>();
    private Map<String, String> wordStatus = new HashMap<>();
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 60000; // 60 second timer
    private boolean timerRunning;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_mode);

        currentWordTextView = findViewById(R.id.currentWord);
        currentTeamTextView = findViewById(R.id.CurrentTeam);
        pastWordsRecyclerView = findViewById(R.id.WordsrecyclerView);
        timerTextView = findViewById(R.id.timer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        pastWordsRecyclerView.setLayoutManager(layoutManager);

        pastWordsAdapter = new PastWordsAdapter(pastWords, wordStatus);
        pastWordsRecyclerView.setAdapter(pastWordsAdapter);

        Button skipButton = findViewById(R.id.skipButton);
        Button nextButton = findViewById(R.id.Nextbutton);

        game = Game.getInstance(this);

        String lang = game.getLanguage();
        String sep = "";
        if ("EN".equals(lang)) {
            sep = ";taburetka;";
        } else if ("FI".equals(lang)){
            sep = ";pussikatu;";
        } else {
            //
        }

        readWordsFromFile(lang, sep);
        setRandomWord();
        updateCurrentTeamName();

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipWord();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessWord();
            }
        });

        startTimer();
        game.startGame();
    }

    private void readWordsFromFile(String language, String separator) {
        //Reading of words from file depending on language settings
        int fileId;
        if ("EN".equals(language)) {
            fileId = R.raw.alias_english_words;
        } else {
            fileId = R.raw.alias_finnish_words;
        }

        InputStream inputStream = getResources().openRawResource(fileId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(separator);
                for (String word : words) {
                    wordsList.add(word.trim());
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRandomWord() {
        if (!wordsList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(wordsList.size());
            String randomWord = wordsList.get(randomIndex);
            currentWordTextView.setText(randomWord);
            wordsList.remove(randomIndex);
        } else {
            // Exception
        }
    }

    private void skipWord() {
        String currentWord = currentWordTextView.getText().toString();
        pastWords.add(0, currentWord);
        wordStatus.put(currentWord, "Skipped");
        pastWordsAdapter.notifyDataSetChanged();
        setRandomWord();
        scrollToBottom();
    }

    private void guessWord() {
        String currentWord = currentWordTextView.getText().toString();
        pastWords.add(0, currentWord);
        wordStatus.put(currentWord, "Guessed");
        pastWordsAdapter.notifyDataSetChanged();
        setRandomWord();
        scrollToBottom();
    }

    private void scrollToBottom() {
        pastWordsRecyclerView.scrollToPosition(0);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateTimerText();
                switchToScoreActivity();
            }
        }.start();

        timerRunning = true;
    }

    private void updateTimerText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d", seconds);
        timerTextView.setText(timeLeftFormatted);
    }

    private void switchToScoreActivity() {
        int wordsGuessed = getWordsGuessedCount();
        int wordsSkipped = getWordsSkippedCount();

        updateScores(wordsGuessed);

        Intent intent = new Intent(PlayingActivity.this, ScoreActivity.class);
        intent.putExtra("wordsGuessed", wordsGuessed);
        intent.putExtra("wordsSkipped", wordsSkipped);
        intent.putExtra("totalWords", getTotalWordsCount());
        intent.putExtra("teamName", getCurrentTeamName());
        startActivity(intent);
        finish();
    }

    private void updateScores(int wordsGuessed) {
        if (!game.getTeams().isEmpty()) {
            // Get the current team
            Team currentTeam = game.getTeams().get(game.getCurrentTeamIndex());

            // Update the points of the current team
            currentTeam.setPoints(currentTeam.getPoints() + wordsGuessed);
        }
    }

    private int getWordsGuessedCount() {
        int count = 0;
        for (Map.Entry<String, String> entry : wordStatus.entrySet()) {
            if (entry.getValue().equals("Guessed")) {
                count++;
            }
        }
        return count;
    }

    private int getWordsSkippedCount() {
        int count = 0;
        for (Map.Entry<String, String> entry : wordStatus.entrySet()) {
            if (entry.getValue().equals("Skipped")) {
                count++;
            }
        }
        return count;
    }

    private String getCurrentTeamName() {
        if (!game.getTeams().isEmpty()) {
            Team currentTeam = game.getTeams().get(game.getCurrentTeamIndex());
            return currentTeam.getName();
        }
        return ""; // Return empty string if no team is available
    }

    private int getTotalWordsCount() {
        return pastWords.size();
    }

    private void updateCurrentTeamName() {
        if (!game.getTeams().isEmpty()) {
            Team currentTeam = game.getTeams().get(game.getCurrentTeamIndex());
            currentTeamTextView.setText(currentTeam.getName());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timerRunning) {
            countDownTimer.cancel();
            timerRunning = false;
        }
    }

    public void setCurrentTeamName(String teamName) {
        currentTeamTextView.setText(teamName);
    }
}