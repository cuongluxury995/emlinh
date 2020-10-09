/*
 * Copyright (c) 2011-2020 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.waterpurifier.heremap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.waterpurifier.R;
import com.here.android.mpa.common.GeoBoundingBox;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.AndroidXMapFragment;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.MapRoute;
import com.here.android.mpa.mapping.MapScreenMarker;
import com.here.android.mpa.routing.CoreRouter;
import com.here.android.mpa.routing.RouteOptions;
import com.here.android.mpa.routing.RoutePlan;
import com.here.android.mpa.routing.RouteResult;
import com.here.android.mpa.routing.RouteWaypoint;
import com.here.android.mpa.routing.Router;
import com.here.android.mpa.routing.RoutingError;
import com.here.android.mpa.search.AutoSuggest;
import com.here.android.mpa.search.AutoSuggestPlace;
import com.here.android.mpa.search.AutoSuggestQuery;
import com.here.android.mpa.search.AutoSuggestSearch;
import com.here.android.mpa.search.DiscoveryRequest;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.Place;
import com.here.android.mpa.search.PlaceRequest;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.TextAutoSuggestionRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

/**
 * This class encapsulates the properties and functionality of the Map view. It also implements 3
 * types of AutoSuggest requests that HERE Mobile SDK for Android (Premium) provides as example.
 */
public class MapFragmentView implements LocationListener {
    public static List<DiscoveryResult> s_discoverResultList;
    private AndroidXMapFragment m_mapFragment;
    private AppCompatActivity m_activity;
    private View m_mapFragmentContainer;
    private Map m_map, q_map, n_map;
    private Button m_placeDetailButton;
    private SearchView m_searchView;
    private SearchListener m_searchListener;
    private List<MapObject> m_mapObjectList = new ArrayList<>();
    private Spinner m_localeSpinner;
    private AutoSuggestAdapter m_autoSuggestAdapter;
    private List<AutoSuggest> m_autoSuggests;
    private ListView m_resultsListView;
    private TextView m_collectionSizeTextView;
    private LinearLayout m_filterOptionsContainer;
    private CheckBox m_useFilteringCheckbox;

    TextView createRoute;
    Button btnGps;

    private MapRoute m_mapRoute;

    private static Image m_marker_image;
    private MapScreenMarker m_tap_marker;

    private final LinkedList<MapMarker> m_map_markers = new LinkedList<>();

    private static final String[] AVAILABLE_LOCALES = {
            "",
            "af-ZA",
            "sq-AL",
            "ar-SA",
            "az-Latn-AZ",
            "eu-ES",
            "be-BY",
            "bg-BG",
            "ca-ES",
            "zh-CN",
            "zh-TW",
            "hr-HR",
            "cs-CZ",
            "da-DK",
            "nl-NL",
            "en-GB",
            "en-US",
            "et-EE",
            "fa-IR",
            "fil-PH",
            "fi-FI",
            "fr-FR",
            "fr-CA",
            "gl-ES",
            "de-DE",
            "el-GR",
            "ha-Latn-NG",
            "he-IL",
            "hi-IN",
            "hu-HU",
            "id-ID",
            "it-IT",
            "ja-JP",
            "kk-KZ",
            "ko-KR",
            "lv-LV",
            "lt-LT",
            "mk-MK",
            "ms-MY",
            "nb-NO",
            "pl-PL",
            "pt-BR",
            "pt-PT",
            "ro-RO",
            "ru-RU",
            "sr-Latn-CS",
            "sk-SK",
            "sl-SI",
            "es-MX",
            "es-ES",
            "sv-SE",
            "th-TH",
            "tr-TR",
            "uk-UA",
            "uz-Latn-UZ",
            "vi-VN"
    };

    public MapFragmentView(AppCompatActivity activity) {
        m_activity = activity;
        m_searchListener = new SearchListener();
        m_autoSuggests = new ArrayList<>();

        createRoute = m_activity.findViewById(R.id.createRoute);
        btnGps = m_activity.findViewById(R.id.btnGps);

        /*
         * The map fragment is not required for executing AutoSuggest requests. However in this example,
         * we will use it to simplify of the SDK initialization.
         */
        initMapFragment();
        initControls();
    }

    private AndroidXMapFragment getMapFragment() {
        return (AndroidXMapFragment) m_activity.getSupportFragmentManager().findFragmentById(R.id.mapfragment);
    }

    private void initMapFragment() {
        /* Locate the mapFragment UI element */
        m_mapFragment = getMapFragment();
        m_mapFragmentContainer = m_activity.findViewById(R.id.mapfragment);


        // This will use external storage to save map cache data, it is also possible to set
        // private app's path
        String path = new File(m_activity.getExternalFilesDir(null), ".here-map-data")
                .getAbsolutePath();
        // This method will throw IllegalArgumentException if provided path is not writable
        com.here.android.mpa.common.MapSettings.setDiskCacheRootPath(path);

        if (m_mapFragment != null) {
            LocationManager locationManager = (LocationManager) m_activity.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(m_activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(m_activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
            final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            /* Initialize the AndroidXMapFragment, results will be given via the called back. */
            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {
                    if (error == Error.NONE) {
                        m_map = m_mapFragment.getMap();
                        m_map.setCenter(new GeoCoordinate(21.035981, 105.813573),
                                Map.Animation.NONE);
                        q_map = m_mapFragment.getMap();
                        q_map.setCenter(new GeoCoordinate(21.035981, 105.813573), Map.Animation.NONE);

                        Image marker_img1 = new Image();
                        try {
                            marker_img1.setImageResource(R.drawable.cafe);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // create a MapMarker centered at current location with png image.
                        MapMarker marker1 = new MapMarker(q_map.getCenter(), marker_img1);
                        /*
                         * Set MapMarker draggable.
                         * How to move to?
                         * In order to activate dragging of the MapMarker you have to do a long press on
                         * the MapMarker then move it to a new position and release the MapMarker.
                         */

                        marker1.setDraggable(true);

                        // add a MapMarker to current active map.
                        q_map.addMapObject(marker1);
                        q_map.setZoomLevel(13.2);

                        btnGps.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                n_map = m_mapFragment.getMap();
                                n_map.setCenter(new GeoCoordinate(location.getLatitude(), location.getLongitude()), Map.Animation.NONE);

                                Image marker_img2 = new Image();
                                try {
                                    marker_img2.setImageResource(R.drawable.imagegps);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // create a MapMarker centered at current location with png image.
                                MapMarker marker2 = new MapMarker(n_map.getCenter(), marker_img2);
                                /*
                                 * Set MapMarker draggable.
                                 * How to move to?
                                 * In order to activate dragging of the MapMarker you have to do a long press on
                                 * the MapMarker then move it to a new position and release the MapMarker.
                                 */

                                marker2.setDraggable(true);

                                // add a MapMarker to current active map.
                                n_map.addMapObject(marker2);
                                n_map.setZoomLevel(13.2);


                                CoreRouter coreRouter = new CoreRouter();

                                /* Initialize a RoutePlan */
                                RoutePlan routePlan = new RoutePlan();

                                /*
                                 * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
                                 * route calculation,including transport modes,route types and route restrictions etc.Please
                                 * refer to API doc for full list of APIs
                                 */
                                RouteOptions routeOptions = new RouteOptions();
                                /* Other transport modes are also available e.g Pedestrian */
                                routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
                                /* Disable highway in this route. */
                                routeOptions.setHighwaysAllowed(false);
                                /* Calculate the shortest route available. */
                                routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
                                /* Calculate 1 route. */
                                routeOptions.setRouteCount(1);
                                /* Finally set the route option */
                                routePlan.setRouteOptions(routeOptions);

                                /* Define waypoints for the route */
                                /* START: 4350 Still Creek Dr */

                                RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(location.getLatitude(), location.getLongitude()));
                                /* END: Langley BC */
                                RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(21.035981, 105.813573));

                                /* Add both waypoints to the route plan */
                                routePlan.addWaypoint(startPoint);
                                routePlan.addWaypoint(destination);

                                /* Trigger the route calculation,results will be called back via the listener */
                                coreRouter.calculateRoute(routePlan,
                                        new Router.Listener<List<RouteResult>, RoutingError>() {
                                            @Override
                                            public void onProgress(int i) {
                                                /* The calculation progress can be retrieved in this callback. */
                                            }

                                            @Override
                                            public void onCalculateRouteFinished(List<RouteResult> routeResults,
                                                                                 RoutingError routingError) {
                                                /* Calculation is done. Let's handle the result */
                                                if (routingError == RoutingError.NONE) {
                                                    m_map.removeMapObject(m_mapRoute);
                                                    if (routeResults.get(0).getRoute() != null) {
                                                        /* Create a MapRoute so that it can be placed on the map */
                                                        m_mapRoute = new MapRoute(routeResults.get(0).getRoute());
                                                        /* Show the maneuver number on top of the route */
                                                        m_mapRoute.setManeuverNumberVisible(true);

                                                        /* Add the MapRoute to the map */
                                                        m_map.addMapObject(m_mapRoute);

                                                        //-------------------------------------
                                                        String a = String.valueOf(m_mapRoute.getRoute().getLength());
                                                        createRoute.setText(a);
                                                        //--------------------------------------
                                                        /*
                                                         * We may also want to make sure the map view is orientated properly
                                                         * so the entire route can be easily seen.
                                                         */
                                                        GeoBoundingBox gbb = routeResults.get(0).getRoute()
                                                                .getBoundingBox();
                                                        m_map.zoomTo(gbb, Map.Animation.NONE,
                                                                Map.MOVE_PRESERVE_ORIENTATION);
                                                    } else {
                                                        Toast.makeText(m_activity,
                                                                "Error:route results returned is not valid",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(m_activity,
                                                            "Error:route calculation returned error code: " + routingError,
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                //------------------------------------------------------------------------------------------------
                            }
                        });
                        //-------------------------------------

                    } else {
                        new android.app.AlertDialog.Builder(m_activity).setMessage(
                                "Error : " + error.name() + "\n\n" + error.getDetails())
                                .setTitle(R.string.engine_init_error)
                                .setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int which) {
                                                m_activity.finish();
                                            }
                                        }).create().show();
                    }
                }
            });
        }
    }

    // Search view
    private void initControls() {
        m_searchView = m_activity.findViewById(R.id.search);
        m_searchView.setOnQueryTextListener(m_searchListener);
        m_localeSpinner = m_activity.findViewById(R.id.localeSpinner);
        m_collectionSizeTextView = m_activity.findViewById(R.id.editText_collectionSize);
        ArrayAdapter<CharSequence> localeAdapter = new ArrayAdapter<CharSequence>(
                m_activity, android.R.layout.simple_spinner_item, AVAILABLE_LOCALES);
        localeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        m_localeSpinner.setAdapter(localeAdapter);

        m_resultsListView = m_activity.findViewById(R.id.resultsListViev);
        m_autoSuggestAdapter = new AutoSuggestAdapter(m_activity,
                android.R.layout.simple_list_item_1, m_autoSuggests);
        m_resultsListView.setAdapter(m_autoSuggestAdapter);
        m_resultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AutoSuggest item = (AutoSuggest) parent.getItemAtPosition(position);
                handleSelectedAutoSuggest(item);

            }
        });
        // Initialize filter options view
        LinearLayout linearLayout = m_activity.findViewById(R.id.filterOptionsContainer);
        m_filterOptionsContainer = new LinearLayout(m_activity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        m_useFilteringCheckbox = new CheckBox(m_activity);
        m_useFilteringCheckbox.setText("Use filter");
        m_useFilteringCheckbox.setChecked(false);
        m_useFilteringCheckbox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        m_filterOptionsContainer.setVisibility(isChecked?View.VISIBLE:GONE);
                    }
                });

        m_filterOptionsContainer.setVisibility(GONE);
        m_filterOptionsContainer.setOrientation(LinearLayout.VERTICAL);
        m_filterOptionsContainer.setPadding(50, 0, 0, 0);

        TextAutoSuggestionRequest.AutoSuggestFilterType[] filterOptions =
                TextAutoSuggestionRequest.AutoSuggestFilterType.values();
        for (TextAutoSuggestionRequest.AutoSuggestFilterType filterOption : filterOptions) {
            CheckBox curCB = new CheckBox(m_activity);
            curCB.setChecked(false);
            curCB.setText(filterOption.toString());
            m_filterOptionsContainer.addView(curCB);
        }

        linearLayout.addView(m_useFilteringCheckbox);
        linearLayout.addView(m_filterOptionsContainer);
    }

    /**
     * Applies selected  filter to {@link TextAutoSuggestionRequest}
     * in order to filtrate according to selected settings.
     * @param request
     */
    private void applyResultFiltersToRequest(TextAutoSuggestionRequest request) {
        if (m_useFilteringCheckbox.isChecked()) {
            TextAutoSuggestionRequest.AutoSuggestFilterType[] filterOptions =
                    TextAutoSuggestionRequest.AutoSuggestFilterType.values();
            int totalFilterOptionsCount = m_filterOptionsContainer.getChildCount();
            List<TextAutoSuggestionRequest.AutoSuggestFilterType> filtersToApply =
                    new ArrayList<>(filterOptions.length);
            for (int i = 0; i < totalFilterOptionsCount; i++) {
                if (((CheckBox) m_filterOptionsContainer.getChildAt(i)).isChecked()) {
                    filtersToApply.add(filterOptions[i]);
                }
            }
            if (!filtersToApply.isEmpty()) {
                request.setFilters(EnumSet.copyOf(filtersToApply));
            }
        }
    }

    private Locale getSelectedLocale() {
        if (m_localeSpinner.getSelectedItemPosition() == 0) {
            return null;
        } else {
            return new Locale(AVAILABLE_LOCALES[m_localeSpinner.getSelectedItemPosition()]);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class SearchListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (!newText.isEmpty()) {
                doSearch(newText);
            } else {
                setSearchMode(false);
            }
            return false;
        }
    }

    private void doSearch(String query) {
        setSearchMode(true);
        /*
         Creates new TextAutoSuggestionRequest with current map position as search center
         and selected collection size with applied filters and chosen locale.
         For more details how to use TextAutoSuggestionRequest
         please see documentation for HERE Mobile SDK for Android.
         */
        TextAutoSuggestionRequest textAutoSuggestionRequest = new TextAutoSuggestionRequest(query);
        textAutoSuggestionRequest.setSearchCenter(m_map.getCenter());
        textAutoSuggestionRequest.setCollectionSize(Integer.parseInt(m_collectionSizeTextView.getText().toString()));
        applyResultFiltersToRequest(textAutoSuggestionRequest);
        Locale locale = getSelectedLocale();
        if (locale != null) {
            textAutoSuggestionRequest.setLocale(locale);
        }
        /*
           The textAutoSuggestionRequest returns its results to non-UI thread.
           So, we have to pass the UI update with returned results to UI thread.
         */
        textAutoSuggestionRequest.execute(new ResultListener<List<AutoSuggest>>() {
            @Override
            public void onCompleted(final List<AutoSuggest> autoSuggests, ErrorCode errorCode) {
                if (errorCode == errorCode.NONE) {
                    processSearchResults(autoSuggests);
                } else {
                    handleError(errorCode);
                }
            }
        });
    }

    private void processSearchResults(final List<AutoSuggest> autoSuggests) {
        m_activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                m_autoSuggests.clear();
                m_autoSuggests.addAll(autoSuggests);
                m_autoSuggestAdapter.notifyDataSetChanged();
            }
        });
    }

    private void handleSelectedAutoSuggest(AutoSuggest autoSuggest) {
        int collectionSize = Integer.parseInt(m_collectionSizeTextView.getText().toString());
        switch (autoSuggest.getType()) {
            case PLACE:
                /*
                 Gets initialized PlaceRequest with location context that allows retrieving details
                 about the place on the selected location.
                 */
                AutoSuggestPlace autoSuggestPlace = (AutoSuggestPlace) autoSuggest;
                PlaceRequest detailsRequest = autoSuggestPlace.getPlaceDetailsRequest();
                detailsRequest.execute(new ResultListener<Place>() {
                    @Override
                    public void onCompleted(Place place, ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {
                            handlePlace(place);
                        } else {
                            handleError(errorCode);
                        }
                    }
                });
                break;
            case SEARCH:
                /*
                 Gets initialized AutoSuggestSearch with location context that allows retrieving
                 DiscoveryRequest for further processing as it is described in
                 the official documentation.
                 Some example of how to handle DiscoveryResultPage you can see in
                 com.here.android.example.autosuggest.ResultListActivity
                 */
                AutoSuggestSearch autoSuggestSearch = (AutoSuggestSearch) autoSuggest;
                DiscoveryRequest discoverRequest = autoSuggestSearch.getSuggestedSearchRequest();
                discoverRequest.setCollectionSize(collectionSize);
                discoverRequest.execute(new ResultListener<DiscoveryResultPage>() {
                    @Override
                    public void onCompleted(DiscoveryResultPage discoveryResultPage,
                                            ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {
                            s_discoverResultList = discoveryResultPage.getItems();
                            Intent intent = new Intent(m_activity, ResultListActivity.class);
                            m_activity.startActivity(intent);
                        } else {
                            handleError(errorCode);
                        }
                    }
                });
                break;
            case QUERY:
                /*
                 Gets TextAutoSuggestionRequest with suggested autocomplete that retrieves
                 the collection of AutoSuggest objects which represent suggested.
                 */
                final AutoSuggestQuery autoSuggestQuery = (AutoSuggestQuery) autoSuggest;
                TextAutoSuggestionRequest queryReqest = autoSuggestQuery
                        .getRequest(getSelectedLocale());
                queryReqest.setCollectionSize(collectionSize);
                queryReqest.execute(new ResultListener<List<AutoSuggest>>() {
                    @Override
                    public void onCompleted(List<AutoSuggest> autoSuggests, ErrorCode errorCode) {
                        if (errorCode == ErrorCode.NONE) {
                            processSearchResults(autoSuggests);
                            m_searchView.setOnQueryTextListener(null);
                            m_searchView.setQuery(autoSuggestQuery.getQueryCompletion(),
                                    false);
                            m_searchView.setOnQueryTextListener(m_searchListener);
                        } else {
                            handleError(errorCode);
                        }
                    }
                });
                break;
            //Do nothing.
            case UNKNOWN:
                default:
        }
    }

    public void setSearchMode(boolean isSearch) {
        if (isSearch) {
            m_mapFragmentContainer.setVisibility(View.INVISIBLE);
            m_resultsListView.setVisibility(View.VISIBLE);
        } else {
            m_mapFragmentContainer.setVisibility(View.VISIBLE);
            m_resultsListView.setVisibility(View.INVISIBLE);
        }
    }
    // Xóa giữ liệu chỉ lấy 1 điểm đánh dấu
    MapMarker marker;
    private void handlePlace(Place place) {
        if (marker != null){
            m_map.removeMapObject(marker);
        }

        StringBuilder sb = new StringBuilder();
        // lấy địa chỉ
        sb.append("địa chỉ").append(place.getLocation().getCoordinate() + "\n");

        m_map = m_mapFragment.getMap();
        m_map.setCenter(new GeoCoordinate(place.getLocation().getCoordinate()), Map.Animation.NONE);
        /* create an image to mark coordinate when tap event happens */
        m_marker_image = new Image();

        try {
            m_marker_image.setImageResource(R.drawable.marker);
        } catch (IOException e) {
            e.printStackTrace();
        }
        marker = new MapMarker(m_map.getCenter(), m_marker_image);
        m_map.addMapObject(marker);

        //-------------------------------------------------------------------
        btnGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //--------------------------------------------------------------------
        CoreRouter coreRouter = new CoreRouter();

        /* Initialize a RoutePlan */
        RoutePlan routePlan = new RoutePlan();

        /*
         * Initialize a RouteOption. HERE Mobile SDK allow users to define their own parameters for the
         * route calculation,including transport modes,route types and route restrictions etc.Please
         * refer to API doc for full list of APIs
         */
        RouteOptions routeOptions = new RouteOptions();
        /* Other transport modes are also available e.g Pedestrian */
        routeOptions.setTransportMode(RouteOptions.TransportMode.CAR);
        /* Disable highway in this route. */
        routeOptions.setHighwaysAllowed(false);
        /* Calculate the shortest route available. */
        routeOptions.setRouteType(RouteOptions.Type.SHORTEST);
        /* Calculate 1 route. */
        routeOptions.setRouteCount(1);
        /* Finally set the route option */
        routePlan.setRouteOptions(routeOptions);

        /* Define waypoints for the route */
        /* START: 4350 Still Creek Dr */

//        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(place.getLocation().getCoordinate()));
        //khởi gps
//        LocationManager locationManager = (LocationManager) m_activity.getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(m_activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(m_activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
//        final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        RouteWaypoint startPoint = new RouteWaypoint(new GeoCoordinate(place.getLocation().getCoordinate()));
        /* END: Langley BC */
        RouteWaypoint destination = new RouteWaypoint(new GeoCoordinate(21.035981, 105.813573));

        /* Add both waypoints to the route plan */
        routePlan.addWaypoint(startPoint);
        routePlan.addWaypoint(destination);

        /* Trigger the route calculation,results will be called back via the listener */
        coreRouter.calculateRoute(routePlan,
                new Router.Listener<List<RouteResult>, RoutingError>() {
                    @Override
                    public void onProgress(int i) {
                        /* The calculation progress can be retrieved in this callback. */
                    }

                    @Override
                    public void onCalculateRouteFinished(List<RouteResult> routeResults,
                                                         RoutingError routingError) {
                        /* Calculation is done. Let's handle the result */
                        if (routingError == RoutingError.NONE) {
                            m_map.removeMapObject(m_mapRoute);
                            if (routeResults.get(0).getRoute() != null) {
                                /* Create a MapRoute so that it can be placed on the map */
                                m_mapRoute = new MapRoute(routeResults.get(0).getRoute());
                                /* Show the maneuver number on top of the route */
                                m_mapRoute.setManeuverNumberVisible(true);

                                /* Add the MapRoute to the map */
                                m_map.addMapObject(m_mapRoute);

                                //-------------------------------------
                                String a = String.valueOf(m_mapRoute.getRoute().getLength());
                                createRoute.setText(a);
                                //--------------------------------------
                                /*
                                 * We may also want to make sure the map view is orientated properly
                                 * so the entire route can be easily seen.
                                 */
                                GeoBoundingBox gbb = routeResults.get(0).getRoute()
                                        .getBoundingBox();
                                m_map.zoomTo(gbb, Map.Animation.NONE,
                                        Map.MOVE_PRESERVE_ORIENTATION);
                            } else {
                                Toast.makeText(m_activity,
                                        "Error:route results returned is not valid",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(m_activity,
                                    "Error:route calculation returned error code: " + routingError,
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //------------------------------------------------------------------------------------------------

        sb.append("Name: ").append(place.getName() + "\n");
//        sb.append("Alternative name:").append(place.getAlternativeNames());
        showMessage("Place info", sb.toString(), false);
    }



    private void handleError(ErrorCode errorCode) {
        showMessage("Error", "Error description: " + errorCode.name(), true);
    }

    private void showMessage(String title, String message, boolean isError) {
        AlertDialog.Builder builder = new AlertDialog.Builder(m_activity);
        builder.setTitle(title).setMessage(message);
        if (isError) {
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        } else {
            builder.setIcon(android.R.drawable.ic_dialog_info);
        }
        builder.setNeutralButton("OK", null);
        builder.create().show();
        setSearchMode(false);
        initMapFragment();
    }
}
