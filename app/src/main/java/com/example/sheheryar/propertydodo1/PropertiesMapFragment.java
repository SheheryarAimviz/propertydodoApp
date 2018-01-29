package com.example.sheheryar.propertydodo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropertiesMapFragment extends Fragment{
    private ArrayList<LatLng> stringArrayList;
    MapView mMapView;
    private GoogleMap mMap;
    private ClusterManager<MyMapItem> mClusterManager;

    public PropertiesMapFragment() {}

    public static PropertiesMapFragment newInstance() {
        PropertiesMapFragment fragment = new PropertiesMapFragment();
        return fragment;
    }

    List<String> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_properties_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        addItems();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
//                mMap.setMyLocationEnabled(true);

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(25.0686,55.1514), 8));
                mClusterManager = new ClusterManager<MyMapItem>(getActivity(), mMap);
                mMap.setOnCameraChangeListener(mClusterManager);
                mMap.setOnMarkerClickListener(mClusterManager);
                mClusterManager.setRenderer(new CustomClusterRenderer(getActivity(), mMap, mClusterManager));


                mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyMapItem>() {
                    @Override
                    public boolean onClusterItemClick(MyMapItem item) {
                        Intent intent = new Intent(getActivity().getApplication(), PropertyDetail.class);
                        intent.putExtra("property_id",item.getTitle());
                        intent.putExtra("code","map");
                        startActivity(intent);
                        return false;
                    }
                });


                mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyMapItem>() {
                    @Override
                    public boolean onClusterClick(Cluster<MyMapItem> cluster) {
                        Intent intent = new Intent(getActivity(), CompanyProperties.class);
                        intent.putExtra("items_to_parse", getClustertitle(cluster.getItems().toArray()));
                        startActivityForResult(intent, 0);
                        return false;
                    }
                });
            }
        });

        return  rootView;
    }


    public String[] getClustertitle(Object[] markerList){

        String[] markerInfo = new String[markerList.length];
        for (int i =0; i<markerInfo.length;
                i++) {

           MyMapItem markerObject = (MyMapItem) markerList[i];
           markerInfo[i] =markerObject.getTitle();
        }

        return markerInfo;
    }

    private void addItems() {


        String url = getString(R.string.base_url)+"properties/map";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray responses = null;
                try {
                    responses = new JSONObject(response).getJSONArray("locations");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                list = new ArrayList<String>();

                for (int i = 0; i < responses.length(); i++) {
                    try {


                        String lats = responses.getJSONObject(i).getString("Lat");

                        if (lats == "null")
                        {

                        }else {
                        Double lat = Double.parseDouble(responses.getJSONObject(i).getString("Lat"));
                        Double log = Double.parseDouble(responses.getJSONObject(i).getString("Long"));
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.bed);
                        String title = responses.getJSONObject(i).getString("property");
                        list.add(title);
                        String snippet = "and this is the snippet.";

                        MyMapItem offsetItem = new MyMapItem(lat, log, title, snippet ,icon);
                        mClusterManager.addItem(offsetItem);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

}
