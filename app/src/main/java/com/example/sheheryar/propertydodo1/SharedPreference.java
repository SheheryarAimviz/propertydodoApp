package com.example.sheheryar.propertydodo1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;

public class SharedPreference {

    public static final String PREFS_NAME = "PRODUCT_APP";
    public static final String FAVORITES = "Product_Favorite";

    public SharedPreference() {
        super();
    }

    public void saveFavorites(Context context, List<PropertyList> favorites) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString(FAVORITES, jsonFavorites);

        editor.commit();
    }

    public void addFavorite(Context context, PropertyList product) {
        List<PropertyList> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<PropertyList>();
        favorites.add(product);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, PropertyList product) {
        ArrayList<PropertyList> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(product);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<PropertyList> getFavorites(Context context) {
        SharedPreferences settings;
        List<PropertyList> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            PropertyList[] favoriteItems = gson.fromJson(jsonFavorites,
                    PropertyList[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<PropertyList>(favorites);
        } else
            return null;

        return (ArrayList<PropertyList>) favorites;
    }
}