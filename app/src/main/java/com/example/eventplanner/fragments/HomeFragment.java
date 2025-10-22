package com.example.eventplanner.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.eventplanner.R;
import com.example.eventplanner.activities.ActivityAgendaActivity;
import com.example.eventplanner.activities.DisplayAllEventsActivity;
import com.example.eventplanner.activities.DisplayCategoriesForSppActivity;
import com.example.eventplanner.activities.DisplayCompanyInfoActivity;
import com.example.eventplanner.activities.DisplayEventTypesForSppActivity;
import com.example.eventplanner.activities.DisplayPersonalInfoActivity;
import com.example.eventplanner.activities.DisplayProductsForSppActivity;
import com.example.eventplanner.activities.EventCreationActivity;
import com.example.eventplanner.activities.EventTypeCreationActivity;
import com.example.eventplanner.activities.EventTypeUpdateActivity;
import com.example.eventplanner.activities.LoginActivity;
import com.example.eventplanner.activities.ProductCreationActivity;
import com.example.eventplanner.activities.UpdateCompanyActivity;
import com.example.eventplanner.adapters.ProductAdapter;
import com.example.eventplanner.adapters.ServiceAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.databinding.ActivityDisplayAllEventsBinding;
import com.example.eventplanner.model.DisplayProduct;
import com.example.eventplanner.model.EventOrganizer;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.adapters.EventAdapter;
import com.example.eventplanner.clients.EventService;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.model.Event;
import com.example.eventplanner.model.enums.DisplayService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //requireActivity().getIntent().putExtra("user",requireActivity().getIntent().getStringExtra("user") + " null");
        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_home, container, false);

        Button logInButton = rootView.findViewById(R.id.logInButton);
        if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN")) {
            if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                logInButton.setText(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("USERNAME", null));
            }
            else{
                logInButton.setText("Log in");
            }
        }
        else{
            logInButton.setText("Log in");
        }
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        Button etCreationButton = rootView.findViewById(R.id.etCreation);

        etCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("SERVICE_AND_PRODUCT_PROVIDER")) {
                            Intent intent = new Intent(getActivity(), EventTypeCreationActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be SERVICE AND PRODUCT PROVIDER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Button eCreationButton = rootView.findViewById(R.id.eCreation);

        eCreationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("EVENT_ORGANIZER")) {
                            Intent intent = new Intent(getActivity(), EventCreationActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be EVENT ORGANIZER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }

            }

        });

        Button displayPersonalButton = rootView.findViewById(R.id.displayPersonal);

        displayPersonalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(requireActivity().getIntent().hasExtra("user")) {
                    if(!requireActivity().getIntent().getStringExtra("user").equals("")) {
                        //JWT decodedJWT = new JWT(requireActivity().getIntent().getStringExtra("user"));
                        Intent intent = new Intent(getActivity(), DisplayPersonalInfoActivity.class);
                        intent.putExtra("user", requireActivity().getIntent().getStringExtra("user"));
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }*/
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("EVENT_ORGANIZER")) {
                            Intent intent = new Intent(getActivity(), DisplayPersonalInfoActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be EVENT ORGANIZER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        Button displayCompanyButton = rootView.findViewById(R.id.displayCompany);

        displayCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(requireActivity().getIntent().hasExtra("user")) {
                    if(!requireActivity().getIntent().getStringExtra("user").equals("")) {
                        Intent intent = new Intent(getActivity(), DisplayCompanyInfoActivity.class);
                        intent.putExtra("user", requireActivity().getIntent().getStringExtra("user"));
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }*/

                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("SERVICE_AND_PRODUCT_PROVIDER")) {
                            Intent intent = new Intent(getActivity(), DisplayCompanyInfoActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be SERVICE AND PRODUCT PROVIDER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button displayEtForSpp = rootView.findViewById(R.id.displayEtForSpp);

        displayEtForSpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("SERVICE_AND_PRODUCT_PROVIDER")) {
                            Intent intent = new Intent(getActivity(), DisplayEventTypesForSppActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be SERVICE AND PRODUCT PROVIDER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button displayCatForSpp = rootView.findViewById(R.id.displayCatForSpp);

        displayCatForSpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("SERVICE_AND_PRODUCT_PROVIDER")) {
                            Intent intent = new Intent(getActivity(), DisplayCategoriesForSppActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be SERVICE AND PRODUCT PROVIDER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button createProduct = rootView.findViewById(R.id.createProduct);

        createProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("SERVICE_AND_PRODUCT_PROVIDER")) {
                            Intent intent = new Intent(getActivity(), ProductCreationActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be SERVICE AND PRODUCT PROVIDER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button displayProduct = rootView.findViewById(R.id.displayProduct);

        displayProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("SERVICE_AND_PRODUCT_PROVIDER")) {
                            Intent intent = new Intent(getActivity(), DisplayProductsForSppActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be SERVICE AND PRODUCT PROVIDER to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button displayAllEventsButton = rootView.findViewById(R.id.allEvents);

        displayAllEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN"))  {
                    if(!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                        String jwtToken = requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null);
                        JWT decodedJWT = new JWT(jwtToken);
                        List<Map> authorities = decodedJWT.getClaim("role").asList(Map.class);
                        String authority=authorities.get(0).get("authority").toString();
                        if(authority.equals("ADMIN")) {
                            Intent intent = new Intent(getActivity(), DisplayAllEventsActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(requireContext(), "User must be ADMIN to use this feature.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(requireContext(), "User must be logged in for using this feature.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // --- TOP 5 EVENTS (RecyclerView) ---
        RecyclerView recyclerViewTop = rootView.findViewById(R.id.recyclerViewTopEvents);
        if (recyclerViewTop != null) {
            recyclerViewTop.setLayoutManager(new LinearLayoutManager(getContext()));
            loadTop5Events(recyclerViewTop);
        }
        recyclerViewTop.setAdapter(new EventAdapter(new ArrayList<>()));

///////////////////////////////////////////
        RecyclerView productRecyclerView = rootView.findViewById(R.id.topProductsRecyclerView);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTop5Products(productRecyclerView);


/////////////////////////////////////////
        RecyclerView topServicesRecyclerView = rootView.findViewById(R.id.topServicesRecyclerView);
        topServicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadTop5Services(topServicesRecyclerView);




        return rootView;

    }

    private void loadTop5Events(RecyclerView recyclerView) {
        EventService eventService = ClientUtils.getEventService();
        Call<ArrayList<Event>> call = eventService.getTop5Events();

        call.enqueue(new Callback<ArrayList<Event>>() {
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Event> events = response.body();
                    android.util.Log.d("TOP_EVENTS", "✅ Received " + events.size() + " events from backend");

                    EventAdapter adapter = new EventAdapter(events);
                    recyclerView.setAdapter(adapter);
                } else {
                    android.util.Log.e("TOP_EVENTS", "❌ Response failed. Code: " + response.code());
                    Toast.makeText(getContext(), "No events found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Event>> call, Throwable t) {
                android.util.Log.e("TOP_EVENTS", "💥 Retrofit call failed: " + t.getMessage(), t);
                Toast.makeText(getContext(), "Failed to load events: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadTop5Products(RecyclerView recyclerView) {
        android.util.Log.d("TOP_PRODUCTS", "📡 Sending request to backend...");

        Call<com.google.gson.JsonObject> call = ClientUtils.productService.getTop5Products();

        call.enqueue(new Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(Call<com.google.gson.JsonObject> call, Response<com.google.gson.JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.google.gson.JsonObject responseBody = response.body();

                    // ✅ ručno izvučemo "content" polje
                    com.google.gson.JsonArray contentArray = responseBody.getAsJsonArray("content");

                    List<DisplayProduct> products = new ArrayList<>();

                    if (contentArray != null) {
                        for (int i = 0; i < contentArray.size(); i++) {
                            com.google.gson.JsonObject obj = contentArray.get(i).getAsJsonObject();

                            DisplayProduct p = new DisplayProduct();
                            p.setId(obj.get("id").getAsInt());
                            p.setName(obj.get("name").getAsString());
                            p.setDescription(obj.has("description") && !obj.get("description").isJsonNull()
                                    ? obj.get("description").getAsString() : "");
                            if (obj.has("price") && !obj.get("price").isJsonNull()) {
                                p.setPrice(String.valueOf(obj.get("price").getAsDouble()));
                            }

                            products.add(p);
                        }
                    }

                    android.util.Log.d("TOP_PRODUCTS", "✅ Parsed " + products.size() + " products");

                    ProductAdapter adapter = new ProductAdapter(products);
                    recyclerView.setAdapter(adapter);
                } else {
                    android.util.Log.e("TOP_PRODUCTS", "❌ Response unsuccessful or body null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.google.gson.JsonObject> call, Throwable t) {
                android.util.Log.e("TOP_PRODUCTS", "💥 Retrofit call failed: " + t.getMessage(), t);
            }
        });
    }

    private void loadTop5Services(RecyclerView recyclerView) {
        android.util.Log.d("TOP_SERVICES", "📡 Sending request to backend...");

        Call<com.google.gson.JsonObject> call = ClientUtils.serviceService.getTop5Services();

        call.enqueue(new Callback<com.google.gson.JsonObject>() {
            @Override
            public void onResponse(Call<com.google.gson.JsonObject> call, Response<com.google.gson.JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.google.gson.JsonObject responseBody = response.body();
                    com.google.gson.JsonArray contentArray = responseBody.getAsJsonArray("content");

                    List<DisplayService> services = new ArrayList<>();

                    if (contentArray != null) {
                        for (int i = 0; i < contentArray.size(); i++) {
                            com.google.gson.JsonObject obj = contentArray.get(i).getAsJsonObject();

                            DisplayService s = new DisplayService();
                            s.setId(obj.get("id").getAsInt());
                            s.setName(obj.get("name").getAsString());
                            s.setDescription(obj.has("description") && !obj.get("description").isJsonNull()
                                    ? obj.get("description").getAsString() : "");

                            if (obj.has("price") && !obj.get("price").isJsonNull()) {
                                s.setPrice(String.valueOf(obj.get("price").getAsDouble()));
                            } else {
                                s.setPrice("N/A");
                            }

                            services.add(s);
                        }
                    }

                    android.util.Log.d("TOP_SERVICES", "✅ Parsed " + services.size() + " services");

                    ServiceAdapter adapter = new ServiceAdapter(services);
                    recyclerView.setAdapter(adapter);
                } else {
                    android.util.Log.e("TOP_SERVICES", "❌ Response unsuccessful or body null. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<com.google.gson.JsonObject> call, Throwable t) {
                android.util.Log.e("TOP_SERVICES", "💥 Retrofit call failed: " + t.getMessage(), t);
            }
        });
    }



}