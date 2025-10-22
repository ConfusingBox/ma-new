package com.example.eventplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.R;
import com.example.eventplanner.model.DisplayProduct;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<DisplayProduct> products;

    public ProductAdapter(List<DisplayProduct> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DisplayProduct product = products.get(position);

        holder.nameText.setText(product.getName());
        holder.descriptionText.setText(product.getDescription());

        if (product.getPrice() != null) {
            holder.priceText.setText("Price: " + product.getPrice() + " RSD");
        } else {
            holder.priceText.setText("Price: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, descriptionText, priceText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.productName);
            descriptionText = itemView.findViewById(R.id.productDescription);
            priceText = itemView.findViewById(R.id.productPrice);
        }
    }
}
