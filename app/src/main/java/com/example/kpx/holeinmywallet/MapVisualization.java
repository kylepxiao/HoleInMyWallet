package com.example.kpx.holeinmywallet;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;

public class MapVisualization extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    public static GoogleMap mMap;
    public static List<LatLng> points;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_visualization);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        mMap = googleMap;
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_visualization, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.logout) {
            FirebaseAuth.getInstance().signOut();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.recommendations) {
            mMap.clear();
            mapUserToItemRecommendations(10);
            // Handle the camera action
        } else if (id == R.id.heatmap) {

        } else if (id == R.id.history) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void addHeatMap(List<LatLng> list) {
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        TileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        TileOverlay mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private void getNearbyMerchants(Location location, int radius) {
        String url = "http://api.reimaginebanking.com/merchants?lat=" + location.getLatitude() + "&lng=" + location.getLongitude() + "&rad=" + radius + "&key=faa42463727cc73a03c766e63a424800";

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(response.getJSONObject("geocode").getDouble("lat"), response.getJSONObject("geocode").getDouble("lng"))).title("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error with JSON Volley response");
                    }
                });
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void mapUserToItemRecommendations(final int numResults) {
        final RequestQueue mRequestQueue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        // Start the queue
        mRequestQueue.start();

        // URL request string
        String url = "https://westus.api.cognitive.microsoft.com/recommendations/v4.0/models/54d982ff-b237-43e2-81d5-10d5d377c3fa/recommend/user?userId=59e2c887ceb8abe24252d812&numberOfResults=" + numResults;

        // Request a string response from the provided URL.
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("includeMetaData", "false");
        parameters.put("buildId", "1661103");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new JSONObject(parameters), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray recommendedItems = response.getJSONArray("recommendedItems");
                            for(int i = 0; i < numResults; i++) {
                                //Log.i("asdf", recommendedItems.getJSONObject(i).getJSONArray("items").getJSONObject(0).get("name").toString());
                                //mMap.addMarker(new MarkerOptions().position(new LatLng(-33.852 + i, 151.211)).title(recommendedItems.getJSONObject(i).getJSONArray("items").getJSONObject(0).get("name").toString()));
                                String id = recommendedItems.getJSONObject(i).getJSONArray("items").getJSONObject(0).get("id").toString();
                                String name = recommendedItems.getJSONObject(i).getJSONArray("items").getJSONObject(0).get("name").toString();
                                mapMerchant(id, name, mRequestQueue);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                headers.put("Ocp-Apim-Subscription-Key", "e1d3d941efc64e40a90e11b8dd8179f0");
                return headers;
            }
        };
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private void mapMerchant(String id, final String name, RequestQueue mRequestQueue){
        // URL request string
        String url = "http://api.reimaginebanking.com/enterprise/merchants/" + id + "?key=faa42463727cc73a03c766e63a424800";

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mMap.addMarker(new MarkerOptions().position(new LatLng(response.getJSONObject("geocode").getDouble("lat"), response.getJSONObject("geocode").getDouble("lng"))).title(name));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", "Error with JSON Volley response");
                    }
                });
        // Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
