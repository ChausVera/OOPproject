// WordViewHolder.java
package com.example.alias;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WordViewHolder extends RecyclerView.ViewHolder {
    TextView wordTextView;
    TextView statusTextView;

    public WordViewHolder(@NonNull View itemView) {
        super(itemView);
        wordTextView = itemView.findViewById(R.id.textWord);
        statusTextView = itemView.findViewById(R.id.textStatus);
    }
}
