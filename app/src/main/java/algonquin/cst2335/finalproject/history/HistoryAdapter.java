package algonquin.cst2335.finalproject.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import algonquin.cst2335.finalproject.R;

import java.util.List;
import algonquin.cst2335.finalproject.ui.DictionaryEntry;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<DictionaryEntry> historyList;

    public HistoryAdapter(List<DictionaryEntry> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        DictionaryEntry historyEntry = historyList.get(position);
        holder.bind(historyEntry);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        private TextView wordTextView;
        private TextView definitionsTextView;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.word_text);
            definitionsTextView = itemView.findViewById(R.id.definitions_text);
        }

        public void bind(DictionaryEntry historyEntry) {
            wordTextView.setText("Word: " + historyEntry.getWord());
            definitionsTextView.setText("Definition: " + historyEntry.getDefinitions());
        }
    }
}




