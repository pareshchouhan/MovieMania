package com.futuretraxex.moviemania.NetworkServices;

import android.net.Uri;
import android.os.Bundle;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.futuretraxex.moviemania.utils.Constant;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by dragonSlayer on 11/21/2015.
 *
 * Design Issue : Should we internally handle Exceptions or pass them to calle
 * I prefer internally handling them in services that would make code inconsistent and is against the guide lines.
 * but we have threads in here so I am not exactly sure what I need to do.
 */
public class NetworkFetchService {

    /**
     * Fetch Popular movies on a background thread and notifies the calle using the onSuccess and onFaliure callbacks
     * pass URL params in the urlParams Bundle
     * like page -> 3
     * Movies are bundled as String Array in 'movie_list'
     * @param urlParams URL parameters to pass to GET call, additional params (excluding api_key) should be passed through urlParams.
     * @param networkFetchServiceCB see {@link NetworkFetchServiceCB} for more information.
     */
    public static void fetchPopularMovies(final Bundle urlParams, final NetworkFetchServiceCB networkFetchServiceCB)    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.init();
                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Bundle data = new Bundle();
                Uri uri = Uri.parse(Constant.API_GET_POPULAR_MOVIES).buildUpon()
                        .appendQueryParameter("api_key", Constant.API_KEY)
                        .build();
                if(urlParams != null)   {
                    for(String key : urlParams.keySet())    {
                        Logger.w("Key : " + key  + " Value : " + urlParams.getString(key));
                        uri = uri.buildUpon()
                                .appendQueryParameter(key, urlParams.getString(key))
                                .build();
                    }
                }
                Request request = new Request.Builder()
                        .url(uri.toString())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject result = new JSONObject(responseData);
                    JSONArray moviesArray = result.getJSONArray("results");
                    ArrayList<String> movieArrayList = new ArrayList<String>(moviesArray.length());
                    for(int i = 0 ;i < moviesArray.length(); i++)   {
                        movieArrayList.add(moviesArray.getJSONObject(i).toString());
                    }
                    data.putString("message", "success");
                    data.putStringArrayList("movie_list", movieArrayList);
                    networkFetchServiceCB.onSuccess(data);
                }
                catch(IOException iox)  {
                    Logger.e(iox.toString());
                    iox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
                catch(JSONException jsox)   {
                    Logger.e(jsox.toString());
                    jsox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Parsing Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
            }
        }).start();
    }


    /**
     * Fetch top rated movies on a background thread and notifies the calle using the onSuccess and onFaliure callbacks
     * @param urlParams URL parameters to pass to GET call
     * @param networkFetchServiceCB see {@link NetworkFetchServiceCB} for more information.
     */
    public static void fetchTopRatedMovies(final Bundle urlParams, final NetworkFetchServiceCB networkFetchServiceCB)   {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.init();
                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Bundle data = new Bundle();
                Uri uri = Uri.parse(Constant.API_GET_TOP_RATED_MOVIES).buildUpon()
                        .appendQueryParameter("api_key", Constant.API_KEY)
                        .build();

                if(urlParams != null)   {
                    for(String key : urlParams.keySet())    {
                        uri = uri.buildUpon()
                                .appendQueryParameter(key, urlParams.getString(key))
                                .build();
                    }
                }

                Request request = new Request.Builder()
                        .url(uri.toString())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    JSONObject result = new JSONObject(responseData);
                    JSONArray moviesArray = result.getJSONArray("results");
                    ArrayList<String> movieArrayList = new ArrayList<String>(moviesArray.length());
                    for(int i = 0 ;i < moviesArray.length(); i++)   {
                        movieArrayList.add(moviesArray.getJSONObject(i).toString());
                    }
                    data.putString("message", "success");
                    data.putStringArrayList("movie_list", movieArrayList);
                    networkFetchServiceCB.onSuccess(data);
                }
                catch(IOException iox)  {
                    Logger.e(iox.toString());
                    iox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
                catch(JSONException jsox)   {
                    Logger.e(jsox.toString());
                    jsox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Parsing Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
            }
        }).start();
    }


    public static void fetchMovieData(final long movieId, final NetworkFetchServiceCB networkFetchServiceCB)    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.init();
                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Bundle data = new Bundle();
                Uri uri = Uri.parse(Constant.API_GET_MOVIE_DETAIL).buildUpon()
                        .appendEncodedPath("" + movieId)
                        .appendQueryParameter("api_key", Constant.API_KEY)
                        .build();

//                if(movieId != null)   {
//                    for(String key : movieId.keySet())    {
//                        uri = uri.buildUpon()
//                                .appendQueryParameter(key, movieId.getString(key))
//                                .build();
//                    }
//                }

                Request request = new Request.Builder()
                        .url(uri.toString())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    data.putString("message", "success");
                    data.putString("movie_data", responseData);
                    networkFetchServiceCB.onSuccess(data);
                }
                catch(IOException iox)  {
                    Logger.e(iox.toString());
                    iox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
            }
        }).start();
    }

    public static void fetchMovieTrailer(final long movieId, final NetworkFetchServiceCB networkFetchServiceCB)  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.init();
                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Bundle data = new Bundle();
                Uri uri = Uri.parse(Constant.API_GET_MOVIE_TRAILER).buildUpon()
                        .appendEncodedPath("" + movieId)
                        .appendEncodedPath("videos")
                        .appendQueryParameter("api_key", Constant.API_KEY)
                        .build();

                Request request = new Request.Builder()
                        .url(uri.toString())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    data.putString("message", "success");
                    JSONObject resultObject = new JSONObject(responseData);
                    JSONArray trailersList = resultObject.getJSONArray("results");
                    data.putString("trailer_data", trailersList.toString());
                    networkFetchServiceCB.onSuccess(data);
                }
                catch(IOException|JSONException iox)  {
                    Logger.e(iox.toString());
                    iox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
            }
        }).start();
    }

    public static void fetchMovieReviews(final long movieId, final NetworkFetchServiceCB networkFetchServiceCB)  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.init();
                OkHttpClient client = new OkHttpClient();
                client.networkInterceptors().add(new StethoInterceptor());
                Bundle data = new Bundle();
                Uri uri = Uri.parse(Constant.API_GET_MOVIE_TRAILER).buildUpon()
                        .appendEncodedPath("" + movieId)
                        .appendEncodedPath("reviews")
                        .appendQueryParameter("api_key", Constant.API_KEY)
                        .build();

                Request request = new Request.Builder()
                        .url(uri.toString())
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    data.putString("message", "success");
                    JSONObject resultObject = new JSONObject(responseData);
                    JSONArray reviewList = resultObject.getJSONArray("results");
                    data.putString("review_data", reviewList.toString());
                    networkFetchServiceCB.onSuccess(data);
                }
                catch(IOException|JSONException iox)  {
                    Logger.e(iox.toString());
                    iox.printStackTrace();
                    //TODO : Add user-friendly messages
                    data.putString("message","Exception Occoured");
                    networkFetchServiceCB.onFailure(data);
                }
            }
        }).start();
    }

    public static void fetchMovieDataReviewandTrailer(final long movieId, final NetworkFetchServiceCB networkFetchServiceCB)    {

        NetworkFetchService.fetchMovieData(movieId, new NetworkFetchServiceCB() {
            @Override
            public void onSuccess(Bundle data) {
                final String movie_data = data.getString("movie_data");

                NetworkFetchService.fetchMovieTrailer(movieId, new NetworkFetchServiceCB() {
                    @Override
                    public void onSuccess(Bundle data) {
                        final String trailer_data = data.getString("trailer_data");

                        NetworkFetchService.fetchMovieReviews(movieId, new NetworkFetchServiceCB() {
                            @Override
                            public void onSuccess(Bundle data) {
                                final String review_data = data.getString("review_data");
                                Bundle finalData = new Bundle();
                                finalData.putString("movie_data",movie_data);
                                finalData.putString("trailer_data",trailer_data);
                                finalData.putString("review_data",review_data);
                                networkFetchServiceCB.onSuccess(finalData);
                            }
                            
                            @Override
                            public void onFailure(Bundle data) {
                                //// FIXME: 25-01-2016 Add meaningful error messages.
                                networkFetchServiceCB.onFailure(null);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Bundle data) {
                        //// FIXME: 25-01-2016 Add meaningful error messages.
                        networkFetchServiceCB.onFailure(null);
                    }
                });
            }

            @Override
            public void onFailure(Bundle data) {
                //// FIXME: 25-01-2016 Add meaningful error messages.
                networkFetchServiceCB.onFailure(null);
            }
        });
    }

    /**
     * An interface for providing callbacks.
     * Bundle 'data' contains a key 'message' , which either contains data or error message.
     */
    public interface NetworkFetchServiceCB {
        public void onSuccess(Bundle data);
        public void onFailure(Bundle data);
    }
}
