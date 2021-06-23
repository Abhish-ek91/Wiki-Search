package com.app.wikisearch.presenter;

import com.app.wikisearch.model.DataList;

import java.util.ArrayList;

public interface MainContract {
    interface view{
        void showProgress();
        void hideProgress();
        void showItemList(ArrayList<DataList> dataLists);
        void onItemListError(String message);

        interface onItemClickListener{
            void itemClickListener(int id);
        }
    }

    interface presenter{
        void networkCheck();
        void onSearch(String title);
        void onDataSuccess(ArrayList<DataList> dataLists);
        void onDataError(String message);
    }

    interface interactor{
        void getSearchKey(String title);
        void getOfflineSearchKey();
        interface OnNetworkListener{
            void onNetworkAvailable();
            void onNetworkError();
        }

        void getNetworkListener(OnNetworkListener onNetworkListener);
    }
}
