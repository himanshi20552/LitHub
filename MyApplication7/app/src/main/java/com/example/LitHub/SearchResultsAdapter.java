package com.example.LitHub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ResourceViewHolder> {
    private List<Resource> resources;
    private Context context;
    private static final int ITEMS_PER_ROW = 2;

    public SearchResultsAdapter(List<Resource> resources, Context context) {
        this.resources = resources;
        this.context = context;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        Resource resource = resources.get(position);
        holder.titleText.setText(resource.getTitle());
        holder.subjectText.setText(resource.getSubject());

        // Set the width to be half of the screen width (minus padding)
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.width = (context.getResources().getDisplayMetrics().widthPixels - 48) / ITEMS_PER_ROW;
        holder.itemView.setLayoutParams(layoutParams);

        holder.itemView.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(resource.getPdfUrl()));
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                android.widget.Toast.makeText(context, 
                    "Error opening PDF", 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return resources.size();
    }

    static class ResourceViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView subjectText;

        ResourceViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.resource_title);
            subjectText = itemView.findViewById(R.id.resource_subject);
        }
    }
} 