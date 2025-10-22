package com.example.eventplanner.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.ShowEventActivity;
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
        holder.cityText.setText(event.getCity() != null ? event.getCity() : "");

        // 🔹 Dodaj klik listener za svaku karticu
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ShowEventActivity.class);
            intent.putExtra("event_name", event.getName());
            intent.putExtra("event_description", event.getDescription());
            intent.putExtra("event_date", event.getDateTime());
            intent.putExtra("event_participants", event.getParticipants());
            intent.putExtra("organizer_id", event.getOrganizerId());
            v.getContext().startActivity(intent);

            // (opciono: animacija pri prelazu)
            if (v.getContext() instanceof Activity) {
                ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
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
