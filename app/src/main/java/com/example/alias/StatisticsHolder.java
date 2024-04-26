package com.example.alias;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatisticsHolder extends RecyclerView.ViewHolder {

    private TextView teamNameTextView;
    private TextView pointsTextView;

    public StatisticsHolder(@NonNull View itemView) {
        super(itemView);
        teamNameTextView = itemView.findViewById(R.id.teamName);
        pointsTextView = itemView.findViewById(R.id.points);
    }

    public void bind(int points, String teamName) {
        teamNameTextView.setText(teamName);
        pointsTextView.setText(String.valueOf(points));
    }
}