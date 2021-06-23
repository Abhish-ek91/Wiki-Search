package com.app.wikisearch.presenter;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.wikisearch.model.DataList;
import com.app.wikisearch.util.MyAppUtil;
import com.app.wikisearch.util.SharedPreferenceClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainInteractor implements MainContract.interactor{
    MainContract.presenter presenter;
    MyAppUtil myAppUtil;
    Context context;
    ArrayList<DataList>dataLists;
    int id;
    String title, description, image;
    SharedPreferenceClass sharedPreferenceClass;
    public MainInteractor(MainContract.presenter presenter, MyAppUtil myAppUtil, Context context) {
        this.myAppUtil = myAppUtil;
        this.presenter = presenter;
        this.context = context;
        this.sharedPreferenceClass = new SharedPreferenceClass(context);
        this.dataLists = new ArrayList<>();

    }


    @Override
    public void getSearchKey(String searchKey) {

        String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&generator=prefixsearch&prop=pageimages%7Cpageterms&gpslimit=5&piprop=thumbnail&pithumbsize=50&formatversion=2&wbptterms=description&gpssearch="+searchKey;
        dataLists.clear();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONObject jsonObject1 = response.getJSONObject("query");
                    JSONArray jsonArray = jsonObject1.getJSONArray("pages");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                        id = jsonObject2.getInt("pageid");
                        title = jsonObject2.getString("title");

                        if (jsonObject2.has("thumbnail")) {
                            JSONObject jsonObject3 = jsonObject2.getJSONObject("thumbnail");
                            image = jsonObject3.getString("source");
                        } else {
                            image = "";
                        }

                        if (jsonObject2.has("thumbnail")) {
                            JSONObject jsonObject4 = jsonObject2.getJSONObject("terms");
                            JSONArray jsonArray1 = jsonObject4.getJSONArray("description");
                            description = jsonArray1.getString(0);
                        } else {
                            description = "No Description Available";
                        }

                        dataLists.add(new DataList(id, title, image, description));
                    }
                    presenter.onDataSuccess(dataLists);
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                    presenter.onDataError("" + jsonException);
                }
            }
        }, error ->
                presenter.onDataError("" + error))

        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }
                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void getOfflineSearchKey() {
        String searchKey = sharedPreferenceClass.getValue_string("last_search");

        if (!searchKey.equals("")){
            String url = "https://en.wikipedia.org/w/api.php?action=query&format=json&generator=prefixsearch&prop=pageimages%7Cpageterms&gpslimit=5&piprop=thumbnail&pithumbsize=50&formatversion=2&wbptterms=description&gpssearch="+searchKey;
            dataLists.clear();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject jsonObject1 = response.getJSONObject("query");
                        JSONArray jsonArray = jsonObject1.getJSONArray("pages");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);

                            id = jsonObject2.getInt("pageid");
                            title = jsonObject2.getString("title");

                            if (jsonObject2.has("thumbnail")) {
                                JSONObject jsonObject3 = jsonObject2.getJSONObject("thumbnail");
                                image = jsonObject3.getString("source");
                            } else {
                                image = "";
                            }

                            if (jsonObject2.has("thumbnail")) {
                                JSONObject jsonObject4 = jsonObject2.getJSONObject("terms");
                                JSONArray jsonArray1 = jsonObject4.getJSONArray("description");
                                description = jsonArray1.getString(0);
                            } else {
                                description = "No Description Available";
                            }

                            dataLists.add(new DataList(id, title, image, description));
                        }
                        presenter.onDataSuccess(dataLists);
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        presenter.onDataError("" + jsonException);
                    }
                }
            }, error ->
                    presenter.onDataError("" + error))

            {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                        if (cacheEntry == null) {
                            cacheEntry = new Cache.Entry();
                        }
                        final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                        final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                        long now = System.currentTimeMillis();
                        final long softExpire = now + cacheHitButRefreshed;
                        final long ttl = now + cacheExpired;
                        cacheEntry.data = response.data;
                        cacheEntry.softTtl = softExpire;
                        cacheEntry.ttl = ttl;
                        String headerValue;
                        headerValue = response.headers.get("Date");
                        if (headerValue != null) {
                            cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        headerValue = response.headers.get("Last-Modified");
                        if (headerValue != null) {
                            cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                        }
                        cacheEntry.responseHeaders = response.headers;
                        final String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(new JSONObject(jsonString), cacheEntry);
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    } catch (JSONException e) {
                        return Response.error(new ParseError(e));
                    }
                }

                @Override
                protected void deliverResponse(JSONObject response) {
                    super.deliverResponse(response);
                }

                @Override
                public void deliverError(VolleyError error) {
                    super.deliverError(error);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    return super.parseNetworkError(volleyError);
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObjectRequest);
        }else{
            presenter.onDataError("No Last Search Found");
        }
    }

    @Override
    public void getNetworkListener(OnNetworkListener onNetworkListener){
        boolean networkStatus = myAppUtil.checkConnection();

        if (!networkStatus){
            onNetworkListener.onNetworkError();
        }else{
            onNetworkListener.onNetworkAvailable();
        }
    }
}
