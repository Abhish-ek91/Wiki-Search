package com.app.wikisearch.presenter;

import android.content.Context;

import com.app.wikisearch.MainActivity;
import com.app.wikisearch.model.DataList;
import com.app.wikisearch.util.MyAppUtil;

import java.util.ArrayList;

public class MainPresenter implements MainContract.presenter, MainContract.interactor.OnNetworkListener{
    MainContract.view view;
    MyAppUtil myAppUtil;
    MainContract.interactor interactor;
    Context context;
    public MainPresenter(MainContract.view view, MyAppUtil myAppUtil, Context context) {
        this.view = view;
        this.myAppUtil = myAppUtil;
        this.context = context;
        this.interactor = new MainInteractor(this, myAppUtil, context);
    }

    @Override
    public void networkCheck() {
        interactor.getNetworkListener(this);
    }

    @Override
    public void onSearch(String title) {
        interactor.getSearchKey(title);
    }

    @Override
    public void onDataSuccess(ArrayList<DataList> dataLists) {
        view.showItemList(dataLists);
        view.hideProgress();
    }

    @Override
    public void onNetworkAvailable() {

    }

    @Override
    public void onNetworkError() {
        interactor.getOfflineSearchKey();
    }

    @Override
    public void onDataError(String message) {
        view.onItemListError(message);
        view.hideProgress();
    }
}
