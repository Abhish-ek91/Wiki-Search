package com.app.wikisearch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wikisearch.MainActivity;
import com.app.wikisearch.databinding.AdapterItemListBinding;
import com.app.wikisearch.model.DataList;
import com.app.wikisearch.presenter.MainContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter <ItemListAdapter.MyViewHolder>{
    MainContract.view.onItemClickListener itemClickListener;
    ArrayList<DataList> dataLists;
    public ItemListAdapter(MainContract.view.onItemClickListener itemClickListener, ArrayList<DataList> dataLists) {
        this.dataLists = dataLists;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdapterItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int pageId = dataLists.get(position).getId();
        String title = dataLists.get(position).getTitle();
        String image = dataLists.get(position).getImage();
        String description = dataLists.get(position).getDescription();

        holder.itemListBinding.tvTitle.setText(title);
        holder.itemListBinding.tvDescription.setText(description);

        if (!image.equals("")){
            Picasso.get().load(image).into(holder.itemListBinding.ivImg);
        }

        holder.itemListBinding.cvItemParent.setOnClickListener(view -> {
            itemClickListener.itemClickListener(pageId);
        });

    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final AdapterItemListBinding itemListBinding;
        public MyViewHolder(AdapterItemListBinding itemListBinding) {
            super(itemListBinding.getRoot());
            this.itemListBinding = itemListBinding;
        }
    }
}
