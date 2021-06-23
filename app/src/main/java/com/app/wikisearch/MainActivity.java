package com.app.wikisearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.wikisearch.adapter.ItemListAdapter;
import com.app.wikisearch.databinding.ActivityMainBinding;
import com.app.wikisearch.model.DataList;
import com.app.wikisearch.presenter.MainContract;
import com.app.wikisearch.presenter.MainPresenter;
import com.app.wikisearch.util.MyAppUtil;
import com.app.wikisearch.util.SharedPreferenceClass;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainContract.view, MainContract.view.onItemClickListener{
    private ActivityMainBinding activityMainBinding;
    MainContract.presenter presenter;
    MyAppUtil myAppUtil;
    SharedPreferenceClass sharedPreferenceClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        sharedPreferenceClass = new SharedPreferenceClass(this);
        myAppUtil = new MyAppUtil(this);
        presenter = new MainPresenter(this, myAppUtil, this);
        presenter.networkCheck();

        activityMainBinding.btnSearch.setOnClickListener(view -> {
            String title = activityMainBinding.etSearch.getText().toString();
            sharedPreferenceClass.setValue_string("last_search", title);

            if (title.equals(""))
                Toast.makeText(this, "Please add a keyword to search", Toast.LENGTH_SHORT).show();
            else
            presenter.onSearch(title);
            activityMainBinding.progressBar.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void showProgress() {
        activityMainBinding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        activityMainBinding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showItemList(ArrayList<DataList> dataLists) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        activityMainBinding.rvItems.setLayoutManager(linearLayoutManager);
        activityMainBinding.rvItems.setHasFixedSize(true);

        ItemListAdapter itemListAdapter = new ItemListAdapter(this, dataLists);
        activityMainBinding.rvItems.setAdapter(itemListAdapter);
        itemListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemListError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void itemClickListener(int id) {
        startActivity(new Intent(this, DetailsActivity.class).putExtra("page_id", id));
    }
}