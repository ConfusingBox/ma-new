package com.example.eventplanner.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.eventplanner.R;
import com.example.eventplanner.activities.LoginActivity;
import com.example.eventplanner.adapters.ProductAdapter;
import com.example.eventplanner.adapters.ServiceAdapter;
import com.example.eventplanner.clients.ClientUtils;
import com.example.eventplanner.dto.Page;
import com.example.eventplanner.model.DisplayProduct;
import com.example.eventplanner.model.Product;
import com.example.eventplanner.model.Service;
import com.example.eventplanner.model.ServiceAndProductCategory;
import com.example.eventplanner.model.enums.DisplayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList<ServiceAndProductCategory> categories;

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);

        Button logInButton = rootView.findViewById(R.id.logInButton);
        AppCompatImageButton productsSearchButton = rootView.findViewById(R.id.productsSearchButton);

        if (requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).contains("JWT_TOKEN")) {
            if (!requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("JWT_TOKEN", null).equals("")) {
                logInButton.setText(requireContext().getSharedPreferences("Preferences", Context.MODE_PRIVATE).getString("USERNAME", null));
            } else {
                logInButton.setText("Log in");
            }
        } else {
            logInButton.setText("Log in");
        }

        Call<Page> findCategories = ClientUtils.serviceService.findAllCategories();
        findCategories.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                categories = (ArrayList<ServiceAndProductCategory>) response.body().getContent();

                Gson gson = new Gson();
                ArrayList<String> categoriesStrings = new ArrayList<>();

                categoriesStrings.add("");
                for (int i = 0; i < categories.size(); i++) {
                    JsonObject jsonObject = gson.toJsonTree(categories.get(i)).getAsJsonObject();
                    ServiceAndProductCategory object = gson.fromJson(jsonObject, ServiceAndProductCategory.class);
                    categoriesStrings.add(object.getName());
                }

                Spinner categorySpinner = rootView.findViewById(R.id.productsCategorySpinner);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categoriesStrings);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Log.d("REZ", t.getMessage() != null ? t.getMessage() : "ERROR");
            }
        });

        ArrayList<String> statusStrings = new ArrayList<>();
        statusStrings.add("");
        statusStrings.add("visible");
        statusStrings.add("invisible");
        statusStrings.add("deleted");
        statusStrings.add("pending");
        statusStrings.add("unavailable");

        Spinner statusSpinner = rootView.findViewById(R.id.productsStatusSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, statusStrings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        productsSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText productsSearchName = rootView.findViewById(R.id.productsSearchEntry);
                Spinner productsSearchCategory = rootView.findViewById(R.id.productsCategorySpinner);
                Spinner productsSearchStatus = rootView.findViewById(R.id.productsStatusSpinner);
                EditText productsSearchPriceMin = rootView.findViewById(R.id.priceFromEntry);
                EditText productsSearchPriceMax = rootView.findViewById(R.id.priceToEntry);

                String name = productsSearchName.getText().toString();
                String categoryName = productsSearchCategory.getSelectedItem().toString();
                String status = productsSearchStatus.getSelectedItem().toString();
                String priceLow = productsSearchPriceMin.getText().toString();
                String priceHigh = productsSearchPriceMax.getText().toString();

                Map<String, String> filters = new HashMap<String, String>();

                if (!name.isEmpty()) filters.put("name", name);
                if (!categoryName.equals("") || !categoryName.isEmpty()) filters.put("category", categoryName);
                if (!status.equals("") || !status.isEmpty()) filters.put("status", status);
                if (!priceLow.isEmpty()) filters.put("priceLow", priceLow);
                if (!priceHigh.isEmpty()) filters.put("priceHigh", priceHigh);
                filters.put("sort", "id");
                filters.put("page", "0");
                filters.put("size", "10");

                Call<Page> call = ClientUtils.productService.search(filters);
                call.enqueue(new Callback<Page>() {
                    @Override
                    public void onResponse(Call<Page> call, Response<Page> response) {
                        String jsonString = gson.toJson(response.body());
                        ObjectMapper mapper = new ObjectMapper();

                        try {
                            Page javaObject = mapper.readValue(jsonString, Page.class);
                            List<Product> products = new ArrayList<>();

                            if (javaObject == null) return;

                            for (Object o : javaObject.getContent()) {
                                Log.i("CCCCCCCCCCCCCCCCCCCCCCCCCC", o.toString());
                                Product product = mapper.convertValue(o, Product.class);
                                products.add(product);
                                Log.i(String.format("Service ID=%d", product.getId()), product.toString());
                            }

                            List<DisplayProduct> displayProducts = new ArrayList<>();

                            for (Product s : products) {
                                DisplayProduct ds = new DisplayProduct();
                                ds.setId(s.getId());
                                ds.setDiscount(s.getDiscount());
                                ds.setServiceAndProductCategory(s.getServiceAndProductCategory().getName());
                                ds.setDescription(s.getDescription());
                                ds.setPrice(s.getPrice());
                                ds.setName(s.getName());
                                displayProducts.add(ds);
                            }

                            RecyclerView productsRecyclerView = rootView.findViewById(R.id.productsRecyclerView);
                            productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            ProductAdapter adapter = new ProductAdapter(displayProducts);
                            productsRecyclerView.setAdapter(adapter);
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
}