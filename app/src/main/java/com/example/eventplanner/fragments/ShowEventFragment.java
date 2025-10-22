package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;

public class ShowEventFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_DATE = "date";
    private static final String ARG_PARTICIPANTS = "participants";

    private String eventName;
    private String eventDescription;
    private String eventDate;
    private int eventParticipants;

    public ShowEventFragment() {
        // Required empty public constructor
    }

    // 🔹 Factory metoda za kreiranje novog fragmenta sa podacima
    public static ShowEventFragment newInstance(String name, String description, String date, int participants) {
        ShowEventFragment fragment = new ShowEventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_DESCRIPTION, description);
        args.putString(ARG_DATE, date);
        args.putInt(ARG_PARTICIPANTS, participants);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventName = getArguments().getString(ARG_NAME);
            eventDescription = getArguments().getString(ARG_DESCRIPTION);
            eventDate = getArguments().getString(ARG_DATE);
            eventParticipants = getArguments().getInt(ARG_PARTICIPANTS);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_event, container, false);

        TextView nameText = view.findViewById(R.id.showEventName);
        TextView descriptionText = view.findViewById(R.id.showEventDescription);
        TextView dateText = view.findViewById(R.id.showEventDate);
        TextView participantsText = view.findViewById(R.id.showEventParticipants);

        nameText.setText(eventName != null ? eventName : "Unknown Event");
        descriptionText.setText(eventDescription != null ? eventDescription : "No description available");
        dateText.setText(eventDate != null ? "📅 " + eventDate : "");
        participantsText.setText("👥 Participants: " + eventParticipants);

        return view;
    }
}
