package com.example.kpx.holeinmywallet;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapVisualization extends AppCompatActivity
        implements OnMapReadyCallback{

    private FloatingActionButton main, history, weightedHistory, heatMap, recommendations;

    private boolean buttonsToggled = false;

    private enum DisplayMode{
        HISTORY,
        WEIGHTED_HISTORY,
        HEAT_MAP,
        RECOMMENDATIONS
    }
    DisplayMode displayMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_visualization);

        main = (FloatingActionButton) findViewById(R.id.mainFab);
        history = (FloatingActionButton) findViewById(R.id.historyFab);
        weightedHistory = (FloatingActionButton) findViewById(R.id.weightedHistoryFab);
        heatMap = (FloatingActionButton) findViewById(R.id.heatMapFab);
        recommendations = (FloatingActionButton) findViewById(R.id.recommendationsFab);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleButtons();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMode = DisplayMode.HISTORY;
                toggleButtons();
                updateMainButton();
            }
        });

        weightedHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMode = DisplayMode.WEIGHTED_HISTORY;
                toggleButtons();
                updateMainButton();
            }
        });

        heatMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMode = DisplayMode.HEAT_MAP;
                toggleButtons();
                updateMainButton();
            }
        });

        recommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayMode = DisplayMode.RECOMMENDATIONS;
                toggleButtons();
                updateMainButton();
            }
        });

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void toggleButtons() {
        buttonsToggled = !buttonsToggled;
        history.setAlpha(buttonsToggled ? 1f : 0f);
        history.setClickable(buttonsToggled);
        weightedHistory.setAlpha(buttonsToggled ? 1f : 0f);
        weightedHistory.setClickable(buttonsToggled);
        heatMap.setAlpha(buttonsToggled ? 1f : 0f);
        heatMap.setClickable(buttonsToggled);
        recommendations.setAlpha(buttonsToggled ? 1f : 0f);
        recommendations.setClickable(buttonsToggled);
    }

    private void updateMainButton() {
        switch(displayMode) {
            default:
                main.setBackgroundTintList(history.getBackgroundTintList());
                break;
            case WEIGHTED_HISTORY:
                main.setBackgroundTintList(weightedHistory.getBackgroundTintList());
                break;
            case HEAT_MAP:
                main.setBackgroundTintList(heatMap.getBackgroundTintList());
                break;
            case RECOMMENDATIONS:
                main.setBackgroundTintList(recommendations.getBackgroundTintList());
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
