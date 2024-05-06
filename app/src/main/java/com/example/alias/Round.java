package com.example.alias;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Responsible for round attributes
public class Round {
    private Team team;
    private List<String> pastWords;
    private Map<String, String> wordStatus;

    public Round(Team team) {
        this.team = team;
        pastWords = new ArrayList<>();
        wordStatus = new HashMap<>();
    }

    public void startRound() {
        pastWords.clear();
        wordStatus.clear();
    }

    public int getWordsGuessedCount() {
        int count = 0;
        for (Map.Entry<String, String> entry : wordStatus.entrySet()) {
            if (entry.getValue().equals("Guessed")) {
                count++;
            }
        }
        return count;
    }

    public Team getTeam() {
        return team;
    }
}
