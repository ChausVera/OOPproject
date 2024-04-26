package com.example.alias;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

// Class is responsible for the Game Setting UI and its functionality

public class GameSettingActivity extends AppCompatActivity {

    private TeamsAdapter adapter;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        game = Game.getInstance(this);

        SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS_NAME, MODE_PRIVATE);
        String selectedLanguage = prefs.getBoolean(MainActivity.IS_ENGLISH_KEY, true) ? Game.LANGUAGE_ENGLISH : Game.LANGUAGE_FINNISH;
        game.setLanguage(selectedLanguage);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button addTeam = findViewById(R.id.addTeamButton);
        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameSettingActivity.this, TeamSettingActivity.class);
                startActivity(intent);
            }
        });

        Button start = findViewById(R.id.StartButton);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (game.getTeams().size() >= 2) {
                    game.saveTeams();
                    game.startGame();

                    Intent intent = new Intent(GameSettingActivity.this, PlayingActivity.class);
                    startActivity(intent);
                } else {
                    // exception
                }
            }
        });

        RecyclerView recyclerView = findViewById(R.id.teamsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TeamsAdapter(this, game.getTeams());
        recyclerView.setAdapter(adapter);
        game.setAdapter(adapter);

        adapter.setOnTeamClickListener(new TeamsAdapter.OnTeamClickListener() {
            @Override
            public void onEditClick(int position) {
                // Handle edit click here
                Intent intent = new Intent(GameSettingActivity.this, TeamSettingActivity.class);
                intent.putExtra("teamPosition", position);
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                game.getTeams().remove(position);
                adapter.notifyItemRemoved(position);
                // Save the changes after deleting the team
                game.saveTeams();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}