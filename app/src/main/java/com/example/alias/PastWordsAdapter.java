// PastWordsAdapter.java
package com.example.alias;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PastWordsAdapter extends RecyclerView.Adapter<WordViewHolder> {

    private List<String> pastWords;
    private Map<String, String> wordStatus;
    public PastWordsAdapter(List<String> pastWords, Map<String, String> wordStatus) {
        this.pastWords = pastWords;
        this.wordStatus = wordStatus;
        Collections.reverse(this.pastWords);
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_item, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        String word = pastWords.get(position);
        String status = wordStatus.get(word);

        holder.wordTextView.setText(word);
        holder.statusTextView.setText(status);
    }

    @Override
    public int getItemCount() {
        return pastWords.size();
    }
}
