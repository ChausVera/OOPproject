package com.example.alias;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

//Representation of the last game statistics

public class RankActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StatisticsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking);

        recyclerView = findViewById(R.id.statisticsView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayRanking();
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void displayRanking() {
        Map<Integer, String> teamPointsMap = readStatisticsFromFile();
        adapter = new StatisticsAdapter(teamPointsMap);
        recyclerView.setAdapter(adapter);
    }

    private Map<Integer, String> readStatisticsFromFile() {
        // reading from file
        Map<Integer, String> teamPointsMap = new HashMap<>();
        try {
            FileInputStream fis = openFileInput("game_results.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into team name and points
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String teamName = parts[0];
                    int points = Integer.parseInt(parts[1]);
                    teamPointsMap.put(points, teamName);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teamPointsMap;
    }
}
