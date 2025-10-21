package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private final List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);

        holder.nameText.setText(event.getName() != null ? event.getName() : "Unnamed event");
        holder.descriptionText.setText(event.getDescription() != null ? event.getDescription() : "");
        try {
            // ako postoji polje city direktno u Event modelu
            holder.cityText.setText(event.getCity());
        } catch (Exception e) {
            holder.cityText.setText("");
        }

    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, descriptionText, cityText;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.eventName);
            descriptionText = itemView.findViewById(R.id.eventDescription);
            cityText = itemView.findViewById(R.id.eventCity);
        }
    }
}
