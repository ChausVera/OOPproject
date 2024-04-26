package com.example.alias;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TeamViewHolder extends RecyclerView.ViewHolder {
    TextView teamName;
    TextView teamPoints;
    ImageView deleteTeam, editTeam;

    public TeamViewHolder(@NonNull View itemView) {
        super(itemView);
        teamName = itemView.findViewById(R.id.textNoteTitle);
        teamPoints = itemView.findViewById(R.id.textNoteText);
        deleteTeam = itemView.findViewById(R.id.deleteTeam);
        editTeam = itemView.findViewById(R.id.editTeam);

    }
}
