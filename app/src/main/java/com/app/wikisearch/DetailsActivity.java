package com.app.wikisearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.app.wikisearch.util.MyAppUtil;

public class DetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView myWebView = new WebView(this);
        setContentView(myWebView);

        int pageId = getIntent().getIntExtra("page_id", 0);
        MyAppUtil myAppUtil = new MyAppUtil(this);
        boolean networkStatus = myAppUtil.checkConnection();

        if (!networkStatus){
            Toast.makeText(this, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
        }else{
            String detailsUrl = "https://en.wikipedia.org/w/api.php?action=query&prop=info&inprop=protection|url|talkid&pageids=" + pageId;
            myWebView.loadUrl(detailsUrl);
        }
    }
}