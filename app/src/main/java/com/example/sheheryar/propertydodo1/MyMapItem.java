package com.example.sheheryar.propertydodo1;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyMapItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String mSnippet;
    private BitmapDescriptor micon;

    public MyMapItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = null;
        mSnippet = null;
    }

    public MyMapItem(double lat, double lng, String title, String snippet, BitmapDescriptor icon) {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        micon = icon;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public String getTitle() { return mTitle; }

    public String getSnippet() { return mSnippet; }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setSnippet(String snippet) {
        mSnippet = snippet;
    }


    public BitmapDescriptor getMicon() {
        return micon;
    }
}
