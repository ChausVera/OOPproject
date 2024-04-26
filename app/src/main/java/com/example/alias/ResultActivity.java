package com.example.alias;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Responsible for calculating and showing game results

public class ResultActivity extends AppCompatActivity {
    BarChart barChart;
    BarData barData; // variable for our bar data.
    BarDataSet barDataSet; // variable for our bar data set.
    ArrayList barEntriesArrayList; // array list for storing entries.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        barChart = findViewById(R.id.idBarChart);
        getBarEntries();
        barDataSet = new BarDataSet(barEntriesArrayList, "Team Points");

        barData = new BarData(barDataSet);

        barChart.setData(barData);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        barDataSet.setValueTextColor(Color.BLACK);

        barDataSet.setValueTextSize(16f);

        barChart.getDescription().setEnabled(false);

        barChart.invalidate();
        MediaPlayer music = MediaPlayer.create(ResultActivity.this, R.raw.music);
        music.start();

        Button homeButton = findViewById(R.id.homePageButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPoints(); // Reset points to zero
                // starting a new intent for the main activity.
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        customizeXAxisLabels();
        writeResultsToFile();
    }


    private void getBarEntries() {
        barEntriesArrayList = new ArrayList<>();
        List<Team> teams = Game.getInstance(ResultActivity.this).getTeams();
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            barEntriesArrayList.add(new BarEntry(i, team.getPoints()));
        }
    }

    private void customizeXAxisLabels() {
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getTeamNames()));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
    }

    private List<String> getTeamNames() {
        List<Team> teams = Game.getInstance(ResultActivity.this).getTeams();
        List<String> teamNames = new ArrayList<>();
        for (Team team : teams) {
            teamNames.add(team.getName());
        }
        return teamNames;
    }

    private void resetPoints() {
        List<Team> teams = Game.getInstance(ResultActivity.this).getTeams();
        for (Team team : teams) {
            team.setPoints(0);
        }
    }


    private void writeResultsToFile() {
        List<Team> teams = Game.getInstance(ResultActivity.this).getTeams();

        String fileName = "game_results.txt";
        Context context = getApplicationContext();

        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            Map<String, Integer> existingData = readExistingDataFromFile();

            for (Team team : teams) {
                String teamName = team.getName();
                int points = team.getPoints();
                if (existingData.containsKey(teamName)) {
                    int existingPoints = existingData.get(teamName);
                    if (points > existingPoints) {
                        existingData.put(teamName, points);
                    }
                } else {
                    existingData.put(teamName, points);
                }
            }

            for (Map.Entry<String, Integer> entry : existingData.entrySet()) {
                String line = entry.getKey() + ": " + entry.getValue() + "\n";
                outputStreamWriter.write(line);
            }

            outputStreamWriter.close();
            Toast.makeText(context, "Results saved to " + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle error
        }
    }

    private Map<String, Integer> readExistingDataFromFile() {
        Map<String, Integer> existingData = new HashMap<>();
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
                    existingData.put(teamName, points);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return existingData;
    }


}