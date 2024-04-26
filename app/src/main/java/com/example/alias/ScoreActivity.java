package com.example.alias;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ScoreActivity extends AppCompatActivity {

    private TextView wordsGuessedTextView;
    private TextView wordsSkippedTextView;
    private TextView totalWordsTextView;
    private TextView currentTeamTextView;
    private TextView totalPointsTextView;
    private Game game; // Reference to the Game instance

    private BroadcastReceiver scoresUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals("SCORES_UPDATED")) {
                ArrayList<Integer> scores = intent.getIntegerArrayListExtra("scores");
                updateTotalPointsTextView(scores);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        game = Game.getInstance(this);

        List<Team> teams = game.getTeams();
        for (Team team : teams) {
            Log.d("Team Points", team.getName() + ": " + team.getPoints());
        }

        wordsGuessedTextView = findViewById(R.id.wordsGuessed);
        wordsSkippedTextView = findViewById(R.id.wordsSkipped);
        totalWordsTextView = findViewById(R.id.totalWordsText);
        currentTeamTextView = findViewById(R.id.textResults);
        totalPointsTextView = findViewById(R.id.scores);

        int wordsGuessed = getIntent().getIntExtra("wordsGuessed", 0);
        int wordsSkipped = getIntent().getIntExtra("wordsSkipped", 0);
        int totalWords = getIntent().getIntExtra("totalWords", 0);
        String teamName = getIntent().getStringExtra("teamName");

        wordsGuessedTextView.setText("Words guessed: " + wordsGuessed);
        wordsSkippedTextView.setText("Words skipped: " + wordsSkipped);
        totalWordsTextView.setText("Total words: " + totalWords);
        currentTeamTextView.setText("Current Team: " + teamName);

        StringBuilder pointsStringBuilder = new StringBuilder();
        boolean goToResult = false;
        for (Team team : teams) {
            pointsStringBuilder.append(team.getName()).append(": ").append(team.getPoints()).append(" ");
            if (team.getPoints() >= 100) {
                goToResult = true;
            }
        }
        totalPointsTextView.setText(pointsStringBuilder.toString().trim());

        Button nextTeamButton = findViewById(R.id.NextTeamButton);

        if (teams.isEmpty() || goToResult) {
            nextTeamButton.setVisibility(View.GONE);
            goToResultActivity();
        } else {
            nextTeamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startNextRound();
                }
            });
        }
    }


    private void startNextRound() {
        //Flag for ending the game
        List<Team> teams = game.getTeams();
        int currentTeamPoints = teams.isEmpty() ? 0 : teams.get(0).getPoints();

        if (currentTeamPoints < 100) {
            game.roundEnded();
            startActivity(new Intent(ScoreActivity.this, PlayingActivity.class));
            finish();
        } else {
            Toast.makeText(ScoreActivity.this, "Team has reached 100 points! Game over.", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToResultActivity() {
        startActivity(new Intent(ScoreActivity.this, ResultActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(scoresUpdateReceiver, new IntentFilter("SCORES_UPDATED"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(scoresUpdateReceiver);
    }

    private void updateTotalPointsTextView(ArrayList<Integer> scores) {
        StringBuilder pointsStringBuilder = new StringBuilder();
        for (int i = 0; i < scores.size(); i++) {
            pointsStringBuilder.append("Team ").append(i + 1).append(": ").append(scores.get(i)).append(" ");
        }
        totalPointsTextView.setText(pointsStringBuilder.toString().trim());
    }
}
