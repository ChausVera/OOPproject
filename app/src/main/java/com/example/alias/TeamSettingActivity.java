package com.example.alias;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class TeamSettingActivity extends AppCompatActivity {

    private Game game;
    private int teamPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_settings);

        final EditText teamNameEditText = findViewById(R.id.editName);
        Button saveButton = findViewById(R.id.saveButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        game = Game.getInstance(this);
        teamPosition = getIntent().getIntExtra("teamPosition", -1);
        final boolean isNewTeam = teamPosition == -1;

        if (!isNewTeam) {
            Team team = game.getTeams().get(teamPosition);
            teamNameEditText.setText(team.getName());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = teamNameEditText.getText().toString().trim();

                if (isNewTeam) {
                    Team newTeam = new Team(newName, 0);
                    game.addTeam(newTeam);
                } else {
                    game.updateTeamName(teamPosition, newName);
                }
                game.saveTeams();
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
