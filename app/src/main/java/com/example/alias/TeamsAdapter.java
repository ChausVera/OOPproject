package com.example.alias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamViewHolder> {

    private Context context;
    private ArrayList<Team> teams;
    private OnTeamClickListener listener;

    public TeamsAdapter(Context context, ArrayList<Team> teams) {
        this.context = context;
        this.teams = teams;
    }

    public interface OnTeamClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnTeamClickListener(OnTeamClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, final int position) {
        Team team = teams.get(position);
        holder.teamName.setText(team.getName());

        if (context instanceof GameSettingActivity) {
            holder.teamPoints.setVisibility(View.GONE);
        } else {
            holder.teamPoints.setVisibility(View.VISIBLE);
            holder.teamPoints.setText(String.valueOf(team.getPoints()));
        }

        holder.editTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onEditClick(adapterPosition);
                    }
                }
            }
        });

        holder.deleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int adapterPosition = holder.getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(adapterPosition);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

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
}
