package com.example.kpx.holeinmywallet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreateAccountActivity extends AppCompatActivity {

    /*
    TODO: Setup firebase authentication and database
    TODO: Validate entered information
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        //Logs a user recommendation
        addUserRecommendationToQueue();
    }

    private void addUserRecommendationToQueue(){
        RequestQueue mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        // Start the queue
        mRequestQueue.start();

        String url = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/864912de-b987-4f45-975b-4eadf4d35ae9/recommend/user?userId=85526&numberOfResults=1";

        // Request a string response from the provided URL.
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("includeMetaData", "false");
        parameters.put("buildId", "1660942");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("asdf", response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error with JSON Volley response");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Ocp-Apim-Subscription-Key", "586a2994e7cb4523beb170e60f0954c3");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
