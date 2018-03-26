package net.weissenburger.movieapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Jon Weissenburger on 3/26/18.
 */

public class ServiceFragment extends Fragment {

    private static final String URL = "https://api.reelgood.com/v1/roulette/netflix?nocache=true&kind=2&minimumScore=4&availability=onAnySource";

    RequestQueue requestQueue;
    ServiceResponseHandler handler;
    ImageLoader imageLoader;

    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int cacheSize = maxMemory / 8;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof ServiceResponseHandler) {
            handler = (ServiceResponseHandler) context;
        } else {
            // throw some error
        }

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }

        if (imageLoader == null) {
            if (imageLoader == null) {

                imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
                    private final android.support.v4.util.LruCache<String, Bitmap> mCache = new android.support.v4.util.LruCache<String, Bitmap>(cacheSize) {
                        @Override
                        protected int sizeOf(String key, Bitmap value) {
                            return value.getByteCount() / 1024;
                        }
                    };

                    public void putBitmap(String url, Bitmap bitmap) {
                        mCache.put(url, bitmap);
                    }

                    public Bitmap getBitmap(String url) {
                        return mCache.get(url);
                    }
                });
            }
        }

    }

    public void getMovieData() {
        JsonObjectRequest request = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MovieResponse movieResponse = new Gson().fromJson(response.toString(), MovieResponse.class);
                if (handler != null) {
                    handler.onResponse(movieResponse);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // todo: error handling
                if (handler != null) {
                    handler.onError();
                }
            }
        });

        requestQueue.add(request);

        // todo: get movie data
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    interface ServiceResponseHandler {
        void onResponse(MovieResponse response);
        void onError();

    }

}
