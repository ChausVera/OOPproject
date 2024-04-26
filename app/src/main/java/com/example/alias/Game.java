package com.example.alias;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

// Game class is responsible for permanent game settings
public class Game {
    private static Game instance;
    private List<Team> teams;
    private TeamsAdapter adapter;
    private int currentTeamIndex = 0;
    private Round currentRound;
    private Context context;
    private List<Integer> points;
    private String language;
    protected static final String LANGUAGE_ENGLISH = "EN";
    protected static final String LANGUAGE_FINNISH = "FI";

    private Game(Context context) {
        teams = new ArrayList<>();
        this.context = context;
    }

    public static Game getInstance(Context context) {
        if (instance == null) {
            instance = new Game(context);
        }
        return instance;
    }

    public ArrayList<Team> getTeams() {
        return (ArrayList<Team>) teams;
    }

    public void setAdapter(TeamsAdapter adapter) {
        this.adapter = adapter;
    }

    public void addTeam(Team team) {
        teams.add(team);
        adapter.notifyItemInserted(teams.size() - 1);
    }

    public void updateTeamName(int position, String newName) {
        Team team = teams.get(position);
        team.setName(newName);
        adapter.notifyItemChanged(position);
    }

    public void startGame() {
        if (currentRound == null) {
            startNextRound();
        }
    }

    private void goToResultActivity() {
        Intent intent = new Intent(context, ScoreActivity.class);
        context.startActivity(intent);
    }

    public void saveTeams() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(context.openFileOutput("statistics.data", Context.MODE_PRIVATE));
            objectOutputStream.writeObject(teams);
            objectOutputStream.close();
        } catch (IOException e) {
            Log.e("Game", "Saving teams failed", e);
        }
    }


    public int getCurrentTeamIndex() {
        return currentTeamIndex;
    }


    public void roundEnded() {
        if (currentRound != null) {
            int guessedWords = currentRound.getWordsGuessedCount();
            Team team = currentRound.getTeam();
            team.setPoints(team.getPoints() + guessedWords);

            if (team.getPoints() >= 100) {
                goToResultActivity();
            } else {
                startNextRound();
            }
        }
    }

    private void startNextRound() {
        if (!teams.isEmpty()) {
            // Increment the team index for the next round
            currentTeamIndex++;
            if (currentTeamIndex >= teams.size()) {
                currentTeamIndex = 0;
            }
            Team currentTeam = teams.get(currentTeamIndex);
            currentRound = new Round(currentTeam);
            currentRound.startRound();
        } else {
            // exception
        }
    }

    public void setLanguage(String language) {
        if (language.equals(LANGUAGE_ENGLISH) || language.equals(LANGUAGE_FINNISH)) {
            this.language = language;
        } else {
            Log.e("Game", "Invalid language code");
        }
    }

    public String getLanguage() {
        return language;
    }
}