package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.EventService;
import com.example.eventplanner.model.Event;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewEvents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EventAdapter(eventList);
        recyclerView.setAdapter(adapter);

        fetchEvents();

        return view;
    }

    private void fetchEvents() {
        String fullUrl = "http://192.168.1.2:8080/api/events/search?sort=id,desc&page=0&size=10";

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("http://192.168.1.2:8080/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build();

        EventService tempService = retrofit.create(EventService.class);
        retrofit2.Call<Object> call = tempService.getPagedEvents();

        call.enqueue(new retrofit2.Callback<Object>() {
            @Override
            public void onResponse(retrofit2.Call<Object> call, retrofit2.Response<Object> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.google.gson.JsonObject jsonResponse =
                            com.google.gson.JsonParser.parseString(new com.google.gson.Gson().toJson(response.body())).getAsJsonObject();

                    com.google.gson.JsonArray content = jsonResponse.getAsJsonArray("content");
                    eventList.clear();

                    com.google.gson.Gson gson = new com.google.gson.Gson();
                    for (com.google.gson.JsonElement element : content) {
                        try {
                            Event event = gson.fromJson(element, Event.class);
                            eventList.add(event);
                        } catch (Exception e) {
                            android.util.Log.e("EVENTS", "⚠️ Error parsing event: " + e.getMessage());
                        }
                    }

                    android.util.Log.d("EVENTS", "✅ Loaded " + eventList.size() + " events");
                    adapter.notifyDataSetChanged();
                } else {
                    android.util.Log.e("EVENTS", "⚠️ Response failed: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Object> call, Throwable t) {
                android.util.Log.e("EVENTS", "💥 Retrofit call failed: " + t.getMessage(), t);
            }
        });
    }

}
