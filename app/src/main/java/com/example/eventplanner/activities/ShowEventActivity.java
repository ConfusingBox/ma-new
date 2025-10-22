package com.example.eventplanner.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.eventplanner.R;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.databinding.ActivityShowEventBinding;
import com.example.eventplanner.fragments.ShowEventFragment;
import com.example.eventplanner.fragments.UserDetailsFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowEventActivity extends AppCompatActivity {

    private int organizerId = 1; // 🔹 privremeni ID dok ne dobijemo pravi iz adaptera

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShowEventBinding binding = ActivityShowEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 🔹 Preuzmi podatke iz Intenta koje je poslao EventAdapter
        String name = getIntent().getStringExtra("event_name");
        String description = getIntent().getStringExtra("event_description");
        String date = getIntent().getStringExtra("event_date");
        int participants = getIntent().getIntExtra("event_participants", 0);

        // 🔹 Učitaj fragment sa prosleđenim podacima
        replaceFragment(ShowEventFragment.newInstance(name, description, date, participants));

        // 🔹 Postavi navigacioni meni (navbar)
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigationBarHome) {
                finish(); // vraća se nazad na HomeActivity
            }

            return true;
        });

        Log.d("Katenda", "ShowEventActivity onCreate()");
        Log.d("SHOW_EVENT", "🔍 Organizer ID = " + organizerId);

        // ============================================
        // 🔹 DEBUG POZIV — prikaz rezervisanih servisa
        // ============================================

        Call<List<Object>> call = ClientUtils.serviceService.getReservedServicesBy(organizerId);

        call.enqueue(new Callback<List<Object>>() {
            @Override
            public void onResponse(Call<List<Object>> call, Response<List<Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Object> reservedList = response.body();
                    Log.d("SHOW_EVENT", "✅ Received " + reservedList.size() + " reserved services.");
                    for (Object item : reservedList) {
                        Log.d("SHOW_EVENT", "Reserved: " + item.toString());
                    }
                } else {
                    Log.e("SHOW_EVENT", "⚠️ Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Object>> call, Throwable t) {
                Log.e("SHOW_EVENT", "💥 Retrofit error: " + t.getMessage(), t);
            }
        });

//        // ==================================================
//        // 🔹 Dugme koje vodi na UserDetailsFragment (chat)
//        // ==================================================
//        Button actionButton = findViewById(R.id.showEventActionButton);
//        Log.d("SHOW_EVENT", "🔹 Dugme je " + (actionButton == null ? "null" : "pronađeno"));
//
//
//        if (actionButton != null) {
//            actionButton.setOnClickListener(v -> {
//                Fragment userDetailsFragment = UserDetailsFragment.newInstance(organizerId);
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container_show_event, userDetailsFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//            });
//        }
    }

    private void replaceFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_show_event, newFragment);
        fragmentTransaction.commit();
    }
}
