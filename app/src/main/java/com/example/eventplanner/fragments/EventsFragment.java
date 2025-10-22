package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.adapters.EventAdapter;
import com.example.eventplanner.adapters.ServiceAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.clients.EventService;
import com.example.eventplanner.dto.Page;
import com.example.eventplanner.model.DisplayEvent;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceAndProductCategory;
import com.example.eventplanner.model.enums.DisplayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList = new ArrayList<>();

    ArrayList<String> categories;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        AppCompatImageButton servicesSearchButton = rootView.findViewById(R.id.searchButton);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Call<ArrayList<String>> findCategories = ClientUtils.eventService.getEventTypes();
        findCategories.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                categories = (ArrayList<String>) response.body();

                ArrayList<String> categoriesStrings = new ArrayList<>();

                categoriesStrings.add("");
                for (int i = 0; i < categories.size(); i++) {
                    categoriesStrings.add(categories.get(i));
                }

                Spinner categorySpinner = rootView.findViewById(R.id.eventSpinnerType);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoriesStrings);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "ERROR");
            }
        });

        ArrayList<String> statusStrings = new ArrayList<>();
        statusStrings.add("");
        statusStrings.add("open");
        statusStrings.add("closed");

        Spinner statusSpinner = rootView.findViewById(R.id.eventSpinnerPrivacy);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, statusStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        servicesSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText servicesSearchName = rootView.findViewById(R.id.searchEntry);
                Spinner servicesSearchCategory = rootView.findViewById(R.id.eventSpinnerType);
                Spinner servicesSearchStatus = rootView.findViewById(R.id.eventSpinnerPrivacy);
                EditText servicesSearchPriceMin = rootView.findViewById(R.id.startDateEntry);
                EditText servicesSearchPriceMax = rootView.findViewById(R.id.endDateEntry);

                String name = servicesSearchName.getText().toString();
                String type = servicesSearchCategory.getSelectedItem().toString();
                String privacy = servicesSearchStatus.getSelectedItem().toString();
                String startDate = servicesSearchPriceMin.getText().toString();
                String endDate = servicesSearchPriceMax.getText().toString();

                Map<String, String> filters = new HashMap<String, String>();

                if (!name.isEmpty()) filters.put("name", name);
                if (!type.equals("") || !type.isEmpty()) filters.put("type", type);
                if (!privacy.equals("") || !privacy.isEmpty()) filters.put("privacyType", privacy);
                if (!startDate.isEmpty()) filters.put("minDate", startDate);
                if (!endDate.isEmpty()) filters.put("maxDate", endDate);
                filters.put("sort", "id");
                filters.put("page", "0");
                filters.put("size", "10");

                Call<Page> call = ClientUtils.eventService.search(filters);
                call.enqueue(new Callback<Page>() {
                    @Override
                    public void onResponse(Call<Page> call, Response<Page> response) {
                        String jsonString = gson.toJson(response.body());
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            Page javaObject = mapper.readValue(jsonString, Page.class);
                            List<Event> services = new ArrayList<>();

                            for (Object o : javaObject.getContent()) {
                                Event service = mapper.convertValue(o, Event.class);
                                services.add(service);
                                Log.i(String.format("Event ID=%d", service.getId()), service.toString());
                            }

                            List<Event> displayServices = new ArrayList<>();

                            /*
                            for (Event s : services) {
                                Event ds = new Event();
                                ds.setId(s.getId());
                                ds.setCategory(s.getServiceAndProductCategory().getName());
                                ds.setDescription(s.getDescription());
                                ds.setPrice(Float.toString(s.getPrice()));
                                ds.setProviderName(s.getServiceAndProductProvider().getFirstName() + " " + s.getServiceAndProductProvider().getLastName());
                                ds.setName(s.getName());
                                displayServices.add(ds);
                            }
                             */

                            RecyclerView servicesRecyclerView = rootView.findViewById(R.id.recyclerViewEvents);
                            servicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            EventAdapter adapterk = new EventAdapter(services);
                            servicesRecyclerView.setAdapter(adapterk);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void onFailure(Call<Page> call, Throwable t) {
                        Log.d("REZ", t.getMessage() != null ? t.getMessage() : "ERROR");
                    }
                });
            }
        });

        return rootView;
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
