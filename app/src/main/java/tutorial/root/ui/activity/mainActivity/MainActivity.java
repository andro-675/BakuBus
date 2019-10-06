package tutorial.root.ui.activity.mainActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

import tutorial.root.R;
import tutorial.root.pojo.response.Bus;
import tutorial.root.ui.activity.mainActivity.adapter.BusListAdapter;
import tutorial.root.utils.Constants;

public class MainActivity extends AppCompatActivity
        implements
        MainActivityContractor.View,
        OnMapReadyCallback,
        PermissionsListener,
        BusListAdapter.BusListAdapterCallback {

    private final Context context = MainActivity.this;

    private MainActivityPresenter presenter;
    private BusListAdapter busListAdapter;

    //UI
    private Toolbar toolbar = null;
    private FrameLayout frameLayoutBusList = null;
    private TextView loadingText = null;
    private RecyclerView busList = null;
    private LinearLayout routeCard = null;
    private TextView routeCardTitle = null;
    private ImageButton switchRouteButton = null;
    private MapView mapView = null;
    private FloatingActionButton fabUseLocation = null;

    //MAP
    private MapboxMap mapboxMap = null;
    private PermissionsManager permissionsManager = null;
    private Style style = null;

    private String routeHolder, busCodeHolder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //connecting to mapBox
        Mapbox.getInstance(context, getString(R.string.map_box_token));

        setContentView(R.layout.activity_main);
        new MainActivityPresenter(this);

        busListAdapter = new BusListAdapter(context);

        initializeUI();
        setListeners();
        configureRecyclerView();

        setSupportActionBar(toolbar);

        //start to execute the map
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    ////// LIFECYCLE //////

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    ////// TOOLBAR METHODS //////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bus_list) {
            openOrCloseBusList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ////// CONTRACTOR METHODS //////

    @Override
    public void setPresenter(MainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void initializeUI() {
        toolbar = findViewById(R.id.toolbar);
        frameLayoutBusList = findViewById(R.id.frame_layout_bus_list);
        busList = findViewById(R.id.recycler_view_bus_list);
        loadingText = findViewById(R.id.text_view_loading);
        mapView = findViewById(R.id.mapView);
        fabUseLocation = findViewById(R.id.fab_user_location);
        routeCard = findViewById(R.id.linear_layout_route_card);
        routeCardTitle = findViewById(R.id.text_view_route);
        switchRouteButton = findViewById(R.id.button_switch);
    }

    @Override
    public void setListeners() {

        fabUseLocation.setOnClickListener(view -> enableLocationComponent(style));

        switchRouteButton.setOnClickListener(view -> {
            if (routeHolder != null && busCodeHolder != null)
                presenter.switchRoute(busCodeHolder, routeHolder);
        });

    }

    @Override
    public void configureRecyclerView() {
        busList.setAdapter(busListAdapter);
    }

    //update bus list which user can see
    @Override
    public void configureBusList(List<Bus> busList) {
        if(busList.size() != 0) {
            hideLoading();
            busListAdapter.updateBusList(busList);
        }
    }

    //change selected bus parameters according to the selection of user
    @Override
    public void changeRoute(String busCode, String route) {
        routeCardTitle.setText(route);

        busCodeHolder = busCode;
        routeHolder = route;

        //change bus parameters in parameters and redraw polyline according to the current data
        presenter.changeSelectedBus(busCode, route);
        presenter.calculateCurrentSpecificBusRoute();
    }

    @Override
    public void openOrCloseBusList() {
        if (frameLayoutBusList.getVisibility() == View.GONE)
            frameLayoutBusList.setVisibility(View.VISIBLE);
        else frameLayoutBusList.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (loadingText.getVisibility() == View.VISIBLE) loadingText.setVisibility(View.GONE);
    }

    @Override
    public void showRouteCard() {
        if (routeCard.getVisibility() == View.INVISIBLE) routeCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeAllMarkers() {
        mapboxMap.clear();
    }

    @Override
    public void addMarkerForBus(float lang, float lat) {
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lang))
        );
    }

    @Override
    public void drawWayLine(List<Bus> busList) {

        LatLng[] latLong = new LatLng[busList.size()];

        for(int i = 0; i < busList.size(); i++) {
            Bus bus = busList.get(i);
            latLong[i] = new LatLng(
                    Float.parseFloat(bus.getAttributes().getLatitude().replace(",",".")),
                    Float.parseFloat(bus.getAttributes().getLongitude().replace(",","."))
            );
        }

        mapboxMap.addPolyline(new PolylineOptions()
                .add(latLong)
                .color(Color.parseColor(Constants.COLOR_BLACK))
                .width(Constants.polylineWidth)
        );

    }

    ////// MAP METHODS //////

    @Override
    public void onMapReady(@NonNull MapboxMap mapboxMap) {

        MainActivity.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            MainActivity.this.style = style;
            enableLocationComponent(style);
        });

    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(this::enableLocationComponent);
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    ///// CALLBACKS //////

    //demonstrate selected route
    @Override
    public void showRoute(String busCode, String route) {
        openOrCloseBusList();
        showRouteCard();
        changeRoute(busCode, route);
    }

}
