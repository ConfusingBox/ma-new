package com.example.eventplanner.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.Page;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowEventFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_DATE = "date";
    private static final String ARG_PARTICIPANTS = "participants";

    private String eventName;
    private String eventDescription;
    private String eventDate;
    private int eventParticipants;

    public ShowEventFragment() {}

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

        // 🔹 Prikaz osnovnih informacija o eventu
        TextView nameText = view.findViewById(R.id.showEventName);
        TextView descriptionText = view.findViewById(R.id.showEventDescription);
        TextView dateText = view.findViewById(R.id.showEventDate);
        TextView participantsText = view.findViewById(R.id.showEventParticipants);

        nameText.setText(eventName != null ? eventName : "Unknown Event");
        descriptionText.setText(eventDescription != null ? eventDescription : "No description available");
        dateText.setText(eventDate != null ? "📅 " + eventDate : "");
        participantsText.setText("👥 Participants: " + eventParticipants);

        // 🔹 Dugme za prikaz organizatora
        Button organizerButton = view.findViewById(R.id.showEventActionButton);
        if (organizerButton != null) {
            organizerButton.setOnClickListener(v -> {
                Fragment userDetailsFragment = UserDetailsFragment.newInstance(0);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_show_event, userDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }

        // ======================
        // 🔸 Dobavljanje servisa
        // ======================
        LinearLayout servicesContainer = view.findViewById(R.id.servicesContainer);
        ClientUtils.serviceService.getAllServices().enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement rootEl = JsonParser.parseString(new Gson().toJson(response.body()));
                    JsonArray items = extractContentArray(rootEl);
                    if (items == null) return;

                    servicesContainer.removeAllViews();
                    for (JsonElement el : items) {
                        if (!el.isJsonObject()) continue;
                        JsonObject obj = el.getAsJsonObject();

                        String name = getStringSafe(obj, "name",
                                getStringSafe(obj, "serviceName", "Service"));
                        String priceStr = getStringSafe(obj, "price", "");

                        LinearLayout row = new LinearLayout(requireContext());
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        row.setPadding(dp(8), dp(8), dp(8), dp(8));

                        TextView tv = new TextView(requireContext());
                        tv.setText(priceStr.isEmpty() ? name : (name + " • " + priceStr));
                        tv.setLayoutParams(new LinearLayout.LayoutParams(
                                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                        Button reserveBtn = new Button(requireContext());
                        reserveBtn.setText("RESERVE");
                        reserveBtn.setOnClickListener(v ->
                                Toast.makeText(requireContext(),
                                        "Service reserved: " + name, Toast.LENGTH_SHORT).show());

                        row.addView(tv);
                        row.addView(reserveBtn);
                        servicesContainer.addView(row);
                    }
                    Log.d("SHOW_EVENT", "✅ Services rendered: " + items.size());
                } else {
                    Log.e("SHOW_EVENT", "⚠️ Failed to load services: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Log.e("SHOW_EVENT", "💥 Retrofit error (services): " + t.getMessage(), t);
            }
        });

        // ======================
        // 🔸 Dobavljanje proizvoda
        // ======================
        LinearLayout productsContainer = view.findViewById(R.id.productsContainer);
        TextView productsTitle = new TextView(requireContext());
        productsTitle.setText("Available Products:");
        productsTitle.setTextSize(18);
        productsTitle.setPadding(0, dp(16), 0, dp(8));
        productsContainer.addView(productsTitle);

        Map<String, String> filters = new HashMap<>();
        filters.put("sort", "id,desc");
//        filters.put("page", "0");
//        filters.put("size", "10");

        ClientUtils.productService.search(filters).enqueue(new Callback<Page>() {
            @Override
            public void onResponse(@NonNull Call<Page> call, @NonNull Response<Page> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement rootEl = JsonParser.parseString(new Gson().toJson(response.body()));
                    JsonArray items = extractContentArray(rootEl);
                    if (items == null) return;

                    for (JsonElement el : items) {
                        if (!el.isJsonObject()) continue;
                        JsonObject obj = el.getAsJsonObject();

                        String name = getStringSafe(obj, "name",
                                getStringSafe(obj, "productName", "Product"));
                        String priceStr = getStringSafe(obj, "price", "");

                        LinearLayout row = new LinearLayout(requireContext());
                        row.setOrientation(LinearLayout.HORIZONTAL);
                        row.setPadding(dp(8), dp(8), dp(8), dp(8));

                        TextView tv = new TextView(requireContext());
                        tv.setText(priceStr.isEmpty() ? name : (name + " • " + priceStr));
                        tv.setLayoutParams(new LinearLayout.LayoutParams(
                                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

                        Button reserveBtn = new Button(requireContext());
                        reserveBtn.setText("RESERVE");
                        reserveBtn.setOnClickListener(v ->
                                Toast.makeText(requireContext(),
                                        "Product reserved: " + name, Toast.LENGTH_SHORT).show());

                        row.addView(tv);
                        row.addView(reserveBtn);
                        productsContainer.addView(row);
                    }

                    Log.d("SHOW_EVENT", "✅ Products rendered: " + items.size());
                } else {
                    Log.e("SHOW_EVENT", "⚠️ Failed to load products: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Page> call, @NonNull Throwable t) {
                if (!isAdded()) return;
                Log.e("SHOW_EVENT", "💥 Retrofit error (products): " + t.getMessage(), t);
            }
        });

        return view;
    }

    // ======================
    // Helper metode
    // ======================

    private JsonArray extractContentArray(JsonElement rootEl) {
        if (rootEl.isJsonObject() && rootEl.getAsJsonObject().has("content")) {
            return rootEl.getAsJsonObject().getAsJsonArray("content");
        } else if (rootEl.isJsonArray()) {
            return rootEl.getAsJsonArray();
        }
        Log.e("SHOW_EVENT", "Unexpected JSON format: " + rootEl);
        return null;
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density);
    }

    private String getStringSafe(JsonObject obj, String key, String fallback) {
        try {
            if (obj.has(key) && !obj.get(key).isJsonNull()) {
                return obj.get(key).getAsString();
            }
        } catch (Exception ignored) {}
        return fallback;
    }
}
