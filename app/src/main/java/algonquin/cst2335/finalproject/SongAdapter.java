package algonquin.cst2335.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private List<Song> songs;
    private OnSongInteractionListener listener;

    public SongAdapter(List<Song> songs, OnSongInteractionListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Context can be obtained from the parent
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.textViewTitle.setText(song.getTitle());
        holder.textViewDuration.setText(String.valueOf(song.getDuration())); // Assuming getDuration() returns an int

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSongClick(song);
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSongDelete(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs != null ? songs.size() : 0;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged(); // Notify any registered observers that the data set has changed.
    }

    public void removeSong(Song song) {
        int position = songs.indexOf(song);
        if (position > -1) {
            songs.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, songs.size()); // Only necessary if you have position-dependent data in your items.
        }
    }

    public List<Song> getAllSongs() {
        return null;
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDuration;
        ImageButton deleteButton;

        SongViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDuration = itemView.findViewById(R.id.textViewDuration);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnSongInteractionListener {
        void onSongClick(Song song);
        void onSongDelete(Song song);
    }

    // The setOnSongInteractionListener seems redundant if you're setting the listener via the constructor. Consider removing it if not used.


    public void setOnSongInteractionListener(OnSongInteractionListener listener) {
        this.listener = listener;
    }
}

