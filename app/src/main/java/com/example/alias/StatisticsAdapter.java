package com.example.alias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Map;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsHolder>{
    private Map<Integer, String> teamPointMap;
    public StatisticsAdapter(Map<Integer, String> teamPointMap) {
        this.teamPointMap = teamPointMap;
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticsHolder holder, int position) {
        Integer points = (Integer) teamPointMap.keySet().toArray()[position];
        String teamName = teamPointMap.get(points);
        holder.bind(points, teamName);
    }


    @NonNull
    @Override
    public StatisticsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ranking_item, parent, false);
        return new StatisticsHolder(view);
    }

    @Override
    public int getItemCount() {
        return teamPointMap.size();
    }

}
