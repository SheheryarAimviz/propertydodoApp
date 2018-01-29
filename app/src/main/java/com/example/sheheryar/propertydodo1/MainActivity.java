package com.example.sheheryar.propertydodo1;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import fr.ganfra.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreference;
    FloatingActionButton fabmap,fabproperty,fab_currentloc;
    Dialog dialog;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    private ProgressDialog progressDialog;

    List<CharSequence> list = new ArrayList<CharSequence>();
    ArrayList<filtersType> filtersTypes;
    ArrayList<filters_max_area> filters_areas;
    ArrayList<filters_max_bedroom> filters_bedrooms;
    ArrayList<filtersfurnishing> filtersfurnishings;
    ArrayList<filterspayment> filterspayments;
    ArrayList<filters_max_price> filters_prices;

    String[] furnishings, payment, type, price, bedroom, area;

    String txt_contract_type, txt_type, txt_max_area, txt_min_area, txt_max_bedroom, txt_min_bedroom, txt_max_price, txt_min_price, txt_furnishing, txt_payment;

    Spinner filter_type, filter_max_area, filter_min_area, filter_min_bedrooms, filter_max_bedrooms, filter_min_price, filter_max_price, filter_furnishings,filter_payment;
    TextView filter_type_txt, filter_max_area_txt, filter_min_area_txt, filter_min_bedrooms_txt, filter_max_bedrooms_txt, filter_min_price_txt, filter_max_price_txt, filter_furnishings_txt,filter_payment_txt;
    String type_shared, max_area_shared, min_area_shared, max_price_shared, min_price_shared, max_bedroom_shared, min_bedroom_shared, furnishing_shared, payment_shared;

    ArrayList<Country> allcountry;
    CountryListAdapter adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreference =  getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);


        if (sharedPreference.getString("map_property_list",null)!= null)
        {
            sharedPreference.edit().remove("map_property_list").apply();
        }

        fab_currentloc = (FloatingActionButton) findViewById(R.id.fab_currentloc);
        fab_currentloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


        fabmap = (FloatingActionButton) findViewById(R.id.fab_map1);
        fabmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, PropertiesMapFragment.newInstance());
            transaction.commit();
            fabmap.setVisibility(View.GONE);
            fabproperty.setVisibility(View.VISIBLE);
            fab_currentloc.setVisibility(View.VISIBLE);

            SharedPreferences.Editor editor  = sharedPreference.edit();
            editor.putString("map_property_list", "2");
            editor.commit();

            }
        });


        fabproperty = (FloatingActionButton) findViewById(R.id.fab_property);
        fabproperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, PropertiesFragment.newInstance());
                transaction.commit();
                fabmap.setVisibility(View.VISIBLE);
                fabproperty.setVisibility(View.GONE);
                fab_currentloc.setVisibility(View.GONE);

                SharedPreferences.Editor editor  = sharedPreference.edit();
                editor.putString("map_property_list", "1");
                editor.commit();
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                            if (sharedPreference.getString("map_property_list",null)== null) {
                                selectedFragment = PropertiesFragment.newInstance();

                                fabmap.setVisibility(View.VISIBLE);
                                fabproperty.setVisibility(View.GONE);
                                fab_currentloc.setVisibility(View.GONE);
                            }
                            else if (sharedPreference.getString("map_property_list",null).equals("2")){

                                selectedFragment = PropertiesMapFragment.newInstance();
                                fabmap.setVisibility(View.GONE);
                                fabproperty.setVisibility(View.VISIBLE);
                                fab_currentloc.setVisibility(View.VISIBLE);
                            }

                            else if (sharedPreference.getString("map_property_list",null).equals("1")){

                                selectedFragment = PropertiesFragment.newInstance();

                                fabmap.setVisibility(View.VISIBLE);
                                fab_currentloc.setVisibility(View.GONE);
                                fabproperty.setVisibility(View.GONE);
                            }

                                break;
                            case R.id.action_item2:
                                selectedFragment = SavedFragment.newInstance();
                                fabmap.setVisibility(View.GONE);
                                fabproperty.setVisibility(View.GONE);
                                fab_currentloc.setVisibility(View.GONE);
                                break;
                            case R.id.action_item3:
                                selectedFragment = SettingsFragment.newInstance();
                                fabmap.setVisibility(View.GONE);
                                fabproperty.setVisibility(View.GONE);
                                fab_currentloc.setVisibility(View.GONE);
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        if (sharedPreference.getString("map_property_list",null)== null)
        {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, PropertiesFragment.newInstance());
            transaction.commit();
            fabmap.setVisibility(View.VISIBLE);
            fab_currentloc.setVisibility(View.GONE);
            fabproperty.setVisibility(View.GONE);
        }
        else if (sharedPreference.getString("map_property_list",null).equals("2")){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, PropertiesMapFragment.newInstance());
            transaction.commit();
            fabmap.setVisibility(View.GONE);
            fabproperty.setVisibility(View.VISIBLE);
            fab_currentloc.setVisibility(View.VISIBLE);
        }

        else if (sharedPreference.getString("map_property_list",null).equals("1")){

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, PropertiesFragment.newInstance());
            transaction.commit();
            fabmap.setVisibility(View.VISIBLE);
            fab_currentloc.setVisibility(View.GONE);
            fabproperty.setVisibility(View.GONE);
        }

        getFurnishingData();
        getTypeData();
        getPaymentData();
        getSearchingData();

        String arealist = "[{ 'id': '', 'area': 'None' }, { 'id': 500, 'area': '500' }, { 'id': 600, 'area': '600' }, { 'id': 700, 'area': '700' }, { 'id': 800, 'area': '800' }, { 'id': 900, 'area': '900' }, { 'id': 1000, 'area': '1,000' }, { 'id': 1100, 'area': '1,100' }, { 'id': 1200, 'area': '1,200' }, { 'id': 1300, 'area': '1,300' }, { 'id': 1400, 'area': '1,400' }, { 'id': 1500, 'area': '1,500' }, { 'id': 1600, 'area': '1,600' }, { 'id': 1800, 'area': '1,800' }, { 'id': 2000, 'area': '2,000' }, { 'id': 2200, 'area': '2,200' }, { 'id': 2400, 'area': '2,400' }, { 'id': 2600, 'area': '2,600' }, { 'id': 2800, 'area': '2,800' }, { 'id': 3000, 'area': '3,000' }, { 'id': 3200, 'area': '3,200' }, { 'id': 3400, 'area': '3,400' }, { 'id': 3600, 'area': '3,600' }, { 'id': 3800, 'area': '3,800' }, { 'id': 4200, 'area': '4,200' }, { 'id': 4600, 'area': '4,600' }, { 'id': 5000, 'area': '5,000' }, { 'id': 5400, 'area': '5,400' }, { 'id': 5800, 'area': '5,800' }, { 'id': 6200, 'area': '6,200' }, { 'id': 6600, 'area': '6,600' }, { 'id': 7000, 'area': '7,000' }, { 'id': 7400, 'area': '7,400' }, { 'id': 7800, 'area': '7,800' }, { 'id': 8200, 'area': '8,200' }, { 'id': 9000, 'area': '9,000' }]";
        String pricelist = "[{'id': '','price': 'None'},{'id': '100000','price': '100,000 AED'},{'id': '200000','price': '200,000 AED'},{'id': '300000','price': '300,000 AED'},{'id': '400000','price': '400,000 AED'},{'id': '500000','price': '500,000 AED'},{'id': '600000','price': '600,000 AED'},{'id': '700000','price': '700,000 AED'},{'id': '800000','price': '800,000 AED'},{'id': '900000','price': '900,000 AED'},{'id': '1000000','price': '1,000,000 AED'},{'id': '1100000','price': '1,100,000 AED'},{'id': '1200000','price': '1,200,000 AED'},{'id': '1300000','price': '1,300,000 AED'},{'id': '1400000','price': '1,400,000 AED'},{'id': '1500000','price': '1,500,000 AED'},{'id': '1600000','price': '1,600,000 AED'},{'id': '1700000','price': '1,700,000 AED'},{'id': '1800000','price': '1,800,000 AED'},{'id': '1900000','price': '1,900,000 AED'},{'id': '2000000','price': '2,000,000 AED'},{'id': '2250000','price': '2,250,000 AED'},{'id': '2500000','price': '2,500,000 AED'},{'id': '2750000','price': '2,750,000 AED'},{'id': '3000000','price': '3,000,000 AED'},{'id': '3250000','price': '3,250,000 AED'},{'id': '3500000','price': '3,500,000 AED'},{'id': '3750000','price': '3,750,000 AED'},{'id': '4000000','price': '4,000,000 AED'},{'id': '4250000','price': '4,250,000 AED'},{'id': '4500000','price': '4,500,000 AED'},{'id': '4750000','price': '4,750,000 AED'},{'id': '5000000','price': '5,000,000 AED'},{'id': '6000000','price': '6,000,000 AED'},{'id': '7000000','price': '7,000,000 AED'},{'id': '8000000','price': '8,000,000 AED'},{'id': '9000000','price': '9,000,000 AED'},{'id': '10000000','price': '10,000,000 AED'},{'id': '20000000','price': '20,000,000 AED'},{'id': '30000000','price': '30,000,000 AED'},{'id': '40000000','price': '40,000,000 AED'},{'id': '50000000','price': '50,000,000 AED'}]";
        String bedroomlist = "[{'id': '','bedroom': 'None'},{'id': '0','bedroom': 'Studio'},{'id': '1','bedroom': '1 Bed'},{'id': '2','bedroom': '2 Bed'},{'id': '3','bedroom': '3 Bed'},{'id': '4','bedroom': '4 Bed'},{'id': '5','bedroom': '5 Bed'},{'id': '6','bedroom': '6 Bed'},{'id': '7','bedroom': '7 Bed'}]";

        showPriceJSON(pricelist);
        showbedroomJSON(bedroomlist);
        showAreaJSON(arealist);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.menu_main, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
//
//        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
////                listView_porperty.setVisibility(View.GONE);
////
////                if (TextUtils.isEmpty(query)) {
////                    listView_porperty.clearTextFilter();
////                } else {
////                    listView_porperty.setFilterText(query);
////                }
////
////                Toast.makeText(MainActivity.this, "Submit", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
////                String text = newText;
////                adapter1.filter(text);
//                return true;
//            }
//        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_searching:
                openSearchingSheet();
                return true;
            case R.id.action_filter:
                openBottomSheet();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getFurnishingData() {

        String url = getString(R.string.base_url)+"properties/furnishings";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showFurnishingJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    public void showFurnishingJSON(String response){
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray responses_1 = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", "");
            jsonObj.put("furnishings", "All furnishings");
            responses_1.put(jsonObj);

            for (int i=0; i< responses.length(); i++)
            {
                responses_1.put(responses.getJSONObject(i));
            }

            filtersfurnishings = new ArrayList<filtersfurnishing>();
            List<String> list=new ArrayList<String>();

            for (int i=0; i< responses_1.length(); i++)
            {
                filtersfurnishing filter = new filtersfurnishing();
                filter.setId(responses_1.getJSONObject(i).getString("id"));
                filter.setName(responses_1.getJSONObject(i).getString("furnishings"));
                filtersfurnishings.add(filter);

                list.add(responses_1.getJSONObject(i).getString("furnishings"));
            }

            furnishings=  new String[list.size()];
            furnishings = list.toArray(furnishings);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getTypeData() {
        String url = getString(R.string.base_url)+"properties/categories";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showTypeJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    public void showTypeJSON(String response){
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            JSONArray responses_1 = new JSONArray();
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("id", "");
            jsonObj.put("category", "All");
            responses_1.put(jsonObj);

            for (int i=0; i< responses.length(); i++)
            {
                responses_1.put(responses.getJSONObject(i));
            }

            filtersTypes = new ArrayList<filtersType>();
            List<String> list=new ArrayList<String>();

            for (int i=0; i< responses_1.length(); i++)
            {
                filtersType filter = new filtersType();
                filter.setId(responses_1.getJSONObject(i).getString("id"));
                filter.setName(responses_1.getJSONObject(i).getString("category"));
                filtersTypes.add(filter);

                list.add(responses_1.getJSONObject(i).getString("category"));
            }

            type=  new String[list.size()];
            type = list.toArray(type);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getPaymentData() {

        String url = getString(R.string.base_url)+"properties/rent-types";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showPaymentJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    public void showPaymentJSON(String response){
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            filterspayments = new ArrayList<filterspayment>();
            List<String> list=new ArrayList<String>();

            for (int i=0; i< responses.length(); i++)
            {
                filterspayment filter = new filterspayment();
                filter.setId(responses.getJSONObject(i).getString("id"));
                filter.setName(responses.getJSONObject(i).getString("type"));
                filterspayments.add(filter);

                list.add(responses.getJSONObject(i).getString("type"));
            }

            payment=  new String[list.size()];
            payment = list.toArray(payment);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showbedroomJSON(String response){
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            filters_bedrooms = new ArrayList<filters_max_bedroom>();
            List<String> list=new ArrayList<String>();

            for (int i=0; i< responses.length(); i++)
            {
                filters_max_bedroom filter = new filters_max_bedroom();
                filter.setId(responses.getJSONObject(i).getString("id"));
                filter.setName(responses.getJSONObject(i).getString("bedroom"));
                filters_bedrooms.add(filter);

                list.add(responses.getJSONObject(i).getString("bedroom"));
            }

            bedroom=  new String[list.size()];
            bedroom = list.toArray(bedroom);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showPriceJSON(String response){
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            filters_prices = new ArrayList<filters_max_price>();
            List<String> list=new ArrayList<String>();

            for (int i=0; i< responses.length(); i++)
            {
                filters_max_price filter = new filters_max_price();
                filter.setId(responses.getJSONObject(i).getString("id"));
                filter.setName(responses.getJSONObject(i).getString("price"));
                filters_prices.add(filter);

                list.add(responses.getJSONObject(i).getString("price"));
            }

            price=  new String[list.size()];
            price = list.toArray(price);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void showAreaJSON(String response){
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            filters_areas = new ArrayList<filters_max_area>();
            List<String> list=new ArrayList<String>();

            for (int i=0; i< responses.length(); i++)
            {
                filters_max_area filter = new filters_max_area();
                filter.setId(responses.getJSONObject(i).getString("id"));
                filter.setName(responses.getJSONObject(i).getString("area"));
                filters_areas.add(filter);

                list.add(responses.getJSONObject(i).getString("area"));
            }

            area=  new String[list.size()];
            area = list.toArray(area);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getSearchingData() {

        String url = "http://138.197.103.4/admin.propertydodo.ae/v2/suggest-data";
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                searchJson(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }
    public void  searchJson(String response) {
        try {
            JSONArray responses= null;
            try {
                responses = new JSONArray(response);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            allcountry = new ArrayList<Country>();

            for (int i=0; i< responses.length(); i++)
            {
                Country country = new
                        Country(responses.getJSONObject(i).getString("label"), responses.getJSONObject(i).getString("desc"), responses.getJSONObject(i).getString("id"));
                allcountry.add(country);
            }
            adapter1 = new CountryListAdapter(this, allcountry);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onRadioButtonClicked(View v)
    {
        boolean  checked = ((RadioButton) v).isChecked();

        switch(v.getId()){

            case R.id.radioForSale:
                if(checked)
                    filter_type.setVisibility(View.VISIBLE);
                    filter_max_area.setVisibility(View.VISIBLE);
                    filter_min_area.setVisibility(View.VISIBLE);
                    filter_max_bedrooms.setVisibility(View.VISIBLE);
                    filter_min_bedrooms.setVisibility(View.VISIBLE);
                    filter_max_price.setVisibility(View.VISIBLE);
                    filter_min_price.setVisibility(View.VISIBLE);
                    filter_furnishings.setVisibility(View.GONE);
                    filter_payment.setVisibility(View.GONE);

                    filter_type_txt.setVisibility(View.VISIBLE);
                    filter_max_area_txt.setVisibility(View.VISIBLE);
                    filter_min_area_txt.setVisibility(View.VISIBLE);
                    filter_max_bedrooms_txt.setVisibility(View.VISIBLE);
                    filter_min_bedrooms_txt.setVisibility(View.VISIBLE);
                    filter_max_price_txt.setVisibility(View.VISIBLE);
                    filter_min_price_txt.setVisibility(View.VISIBLE);
                    filter_furnishings_txt.setVisibility(View.GONE);
                    filter_payment_txt.setVisibility(View.GONE);

                    txt_contract_type = "2";

                break;

            case R.id.radioForRent:
                if(checked)
                    filter_type.setVisibility(View.VISIBLE);
                    filter_max_area.setVisibility(View.VISIBLE);
                    filter_min_area.setVisibility(View.VISIBLE);
                    filter_max_bedrooms.setVisibility(View.VISIBLE);
                    filter_min_bedrooms.setVisibility(View.VISIBLE);
                    filter_max_price.setVisibility(View.VISIBLE);
                    filter_min_price.setVisibility(View.VISIBLE);
                    filter_furnishings.setVisibility(View.VISIBLE);
                    filter_payment.setVisibility(View.VISIBLE);

                    filter_type_txt.setVisibility(View.VISIBLE);
                    filter_max_area_txt.setVisibility(View.VISIBLE);
                    filter_min_area_txt.setVisibility(View.VISIBLE);
                    filter_max_bedrooms_txt.setVisibility(View.VISIBLE);
                    filter_min_bedrooms_txt.setVisibility(View.VISIBLE);
                    filter_max_price_txt.setVisibility(View.VISIBLE);
                    filter_min_price_txt.setVisibility(View.VISIBLE);
                    filter_furnishings_txt.setVisibility(View.VISIBLE);
                    filter_payment_txt.setVisibility(View.VISIBLE);

                    txt_contract_type = "1";

                break;

            case R.id.radioCommercecialSale:
                if(checked)
                    filter_type.setVisibility(View.VISIBLE);
                    filter_max_area.setVisibility(View.VISIBLE);
                    filter_min_area.setVisibility(View.VISIBLE);
                    filter_max_bedrooms.setVisibility(View.GONE);
                    filter_min_bedrooms.setVisibility(View.GONE);
                    filter_max_price.setVisibility(View.VISIBLE);
                    filter_min_price.setVisibility(View.VISIBLE);
                    filter_furnishings.setVisibility(View.GONE);
                    filter_payment.setVisibility(View.GONE);

                    filter_type_txt.setVisibility(View.VISIBLE);
                    filter_max_area_txt.setVisibility(View.VISIBLE);
                    filter_min_area_txt.setVisibility(View.VISIBLE);
                    filter_max_bedrooms_txt.setVisibility(View.GONE);
                    filter_min_bedrooms_txt.setVisibility(View.GONE);
                    filter_max_price_txt.setVisibility(View.VISIBLE);
                    filter_min_price_txt.setVisibility(View.VISIBLE);
                    filter_furnishings_txt.setVisibility(View.GONE);
                    filter_payment_txt.setVisibility(View.GONE);

                    txt_contract_type = "4";

                break;


            case R.id.radioCommercecialRent:
                if(checked)
                    filter_type.setVisibility(View.VISIBLE);
                    filter_max_area.setVisibility(View.VISIBLE);
                    filter_min_area.setVisibility(View.VISIBLE);
                    filter_max_bedrooms.setVisibility(View.GONE);
                    filter_min_bedrooms.setVisibility(View.GONE);
                    filter_max_price.setVisibility(View.VISIBLE);
                    filter_min_price.setVisibility(View.VISIBLE);
                    filter_furnishings.setVisibility(View.GONE);
                    filter_payment.setVisibility(View.VISIBLE);

                    filter_type_txt.setVisibility(View.VISIBLE);
                    filter_max_area_txt.setVisibility(View.VISIBLE);
                    filter_min_area_txt.setVisibility(View.VISIBLE);
                    filter_max_bedrooms_txt.setVisibility(View.GONE);
                    filter_min_bedrooms_txt.setVisibility(View.GONE);
                    filter_max_price_txt.setVisibility(View.VISIBLE);
                    filter_min_price_txt.setVisibility(View.VISIBLE);
                    filter_furnishings_txt.setVisibility(View.GONE);
                    filter_payment_txt.setVisibility(View.VISIBLE);

                    txt_contract_type = "3";

                break;
        }
    }

    public void openBottomSheet () {

        sharedPreference =  getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        String type_spinner_value = sharedPreference.getString("type_spinner_value",null);
        String max_area_spinner_value = sharedPreference.getString("max_area_spinner_value",null);
        String min_area_spinner_value = sharedPreference.getString("min_area_spinner_value",null);
        String max_price_spinner_value = sharedPreference.getString("max_price_spinner_value",null);
        String min_price_spinner_value = sharedPreference.getString("min_price_spinner_value",null);
        String max_bedroom_spinner_value = sharedPreference.getString("max_bedroom_spinner_value",null);
        String min_bedroom_spinner_value = sharedPreference.getString("min_bedroom_spinner_value",null);
        String furnishing_spinner_value = sharedPreference.getString("furnishing_spinner_value",null);
        String payment_spinner_value = sharedPreference.getString("payment_spinner_value",null);
        String contract_type_value = sharedPreference.getString("txt_contract_type", "");

        View view = getLayoutInflater ().inflate (R.layout.bottom_sheet, null);

        final RadioButton rb1 = (RadioButton) view.findViewById(R.id.radioForSale);
        final RadioButton rb2 = (RadioButton) view.findViewById(R.id.radioForRent);
        final RadioButton rb3 = (RadioButton) view.findViewById(R.id.radioCommercecialSale);
        final RadioButton rb4 = (RadioButton) view.findViewById(R.id.radioCommercecialRent);
        final TextView filter_type_txt_1  = (TextView) view.findViewById(R.id.filter_type_txt);
        final TextView filter_max_area_txt_1 = (TextView) view.findViewById(R.id.filter_max_area_txt);
        final TextView filter_min_area_txt_1 = (TextView) view.findViewById(R.id.filter_min_area_txt);
        final TextView filter_min_bedrooms_txt_1 = (TextView) view.findViewById(R.id.filter_min_bedrooms_txt);
        final TextView filter_max_bedrooms_txt_1 = (TextView) view.findViewById(R.id.filter_max_bedrooms_txt);
        final TextView filter_min_price_txt_1 = (TextView) view.findViewById(R.id.filter_min_price_txt);
        final TextView filter_max_price_txt_1 = (TextView) view.findViewById(R.id.filter_max_price_txt);
        final TextView filter_furnishings_txt_1 = (TextView) view.findViewById(R.id.filter_furnishings_txt);
        final TextView filter_payment_txt_1 = (TextView) view.findViewById(R.id.filter_payment_txt);
        final Spinner filter_type_1  = (Spinner) view.findViewById(R.id.filter_type);
        final Spinner filter_max_area_1 = (Spinner) view.findViewById(R.id.filter_max_area);
        final Spinner filter_min_area_1 = (Spinner) view.findViewById(R.id.filter_min_area);
        final Spinner filter_min_bedrooms_1 = (Spinner) view.findViewById(R.id.filter_min_bedrooms);
        final Spinner filter_max_bedrooms_1 = (Spinner) view.findViewById(R.id.filter_max_bedrooms);
        final Spinner filter_min_price_1 = (Spinner) view.findViewById(R.id.filter_min_price);
        final Spinner filter_max_price_1 = (Spinner) view.findViewById(R.id.filter_max_price);
        final Spinner filter_furnishings_1 = (Spinner) view.findViewById(R.id.filter_furnishings);
        final Spinner filter_payment_1 = (Spinner) view.findViewById(R.id.filter_payment);


        filter_type_txt  = (TextView) view.findViewById(R.id.filter_type_txt);
        filter_max_area_txt = (TextView) view.findViewById(R.id.filter_max_area_txt);
        filter_min_area_txt = (TextView) view.findViewById(R.id.filter_min_area_txt);
        filter_min_bedrooms_txt = (TextView) view.findViewById(R.id.filter_min_bedrooms_txt);
        filter_max_bedrooms_txt = (TextView) view.findViewById(R.id.filter_max_bedrooms_txt);
        filter_min_price_txt = (TextView) view.findViewById(R.id.filter_min_price_txt);
        filter_max_price_txt = (TextView) view.findViewById(R.id.filter_max_price_txt);
        filter_furnishings_txt = (TextView) view.findViewById(R.id.filter_furnishings_txt);
        filter_payment_txt = (TextView) view.findViewById(R.id.filter_payment_txt);

        filter_type  = (Spinner) view.findViewById(R.id.filter_type);
        filter_max_area = (Spinner) view.findViewById(R.id.filter_max_area);
        filter_min_area = (Spinner) view.findViewById(R.id.filter_min_area);
        filter_min_bedrooms = (Spinner) view.findViewById(R.id.filter_min_bedrooms);
        filter_max_bedrooms = (Spinner) view.findViewById(R.id.filter_max_bedrooms);
        filter_min_price = (Spinner) view.findViewById(R.id.filter_min_price);
        filter_max_price = (Spinner) view.findViewById(R.id.filter_max_price);
        filter_furnishings = (Spinner) view.findViewById(R.id.filter_furnishings);
        filter_payment = (Spinner) view.findViewById(R.id.filter_payment);

        if (contract_type_value.equals("2"))
        {
            rb1.setChecked(true);

            filter_type_1.setVisibility(View.VISIBLE);
            filter_max_area_1.setVisibility(View.VISIBLE);
            filter_min_area_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_1.setVisibility(View.VISIBLE);
            filter_min_bedrooms_1.setVisibility(View.VISIBLE);
            filter_max_price_1.setVisibility(View.VISIBLE);
            filter_min_price_1.setVisibility(View.VISIBLE);
            filter_furnishings_1.setVisibility(View.GONE);
            filter_payment_1.setVisibility(View.GONE);

            filter_type_txt_1.setVisibility(View.VISIBLE);
            filter_max_area_txt_1.setVisibility(View.VISIBLE);
            filter_min_area_txt_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_txt_1.setVisibility(View.VISIBLE);
            filter_min_bedrooms_txt_1.setVisibility(View.VISIBLE);
            filter_max_price_txt_1.setVisibility(View.VISIBLE);
            filter_min_price_txt_1.setVisibility(View.VISIBLE);
            filter_furnishings_txt_1.setVisibility(View.GONE);
            filter_payment_txt_1.setVisibility(View.GONE);
        }
        else if (contract_type_value.equals("1"))
        {
            rb2.setChecked(true);

            filter_type_1.setVisibility(View.VISIBLE);
            filter_max_area_1.setVisibility(View.VISIBLE);
            filter_min_area_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_1.setVisibility(View.VISIBLE);
            filter_min_bedrooms_1.setVisibility(View.VISIBLE);
            filter_max_price_1.setVisibility(View.VISIBLE);
            filter_min_price_1.setVisibility(View.VISIBLE);
            filter_furnishings_1.setVisibility(View.VISIBLE);
            filter_payment_1.setVisibility(View.VISIBLE);

            filter_type_txt_1.setVisibility(View.VISIBLE);
            filter_max_area_txt_1.setVisibility(View.VISIBLE);
            filter_min_area_txt_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_txt_1.setVisibility(View.VISIBLE);
            filter_min_bedrooms_txt_1.setVisibility(View.VISIBLE);
            filter_max_price_txt_1.setVisibility(View.VISIBLE);
            filter_min_price_txt_1.setVisibility(View.VISIBLE);
            filter_furnishings_txt_1.setVisibility(View.VISIBLE);
            filter_payment_txt_1.setVisibility(View.VISIBLE);
        }
        else if (contract_type_value.equals("4"))
        {
            rb3.setChecked(true);

            filter_type_1.setVisibility(View.VISIBLE);
            filter_max_area_1.setVisibility(View.VISIBLE);
            filter_min_area_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_1.setVisibility(View.GONE);
            filter_min_bedrooms_1.setVisibility(View.GONE);
            filter_max_price_1.setVisibility(View.VISIBLE);
            filter_min_price_1.setVisibility(View.VISIBLE);
            filter_furnishings_1.setVisibility(View.GONE);
            filter_payment_1.setVisibility(View.GONE);

            filter_type_txt_1.setVisibility(View.VISIBLE);
            filter_max_area_txt_1.setVisibility(View.VISIBLE);
            filter_min_area_txt_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_txt_1.setVisibility(View.GONE);
            filter_min_bedrooms_txt_1.setVisibility(View.GONE);
            filter_max_price_txt_1.setVisibility(View.VISIBLE);
            filter_min_price_txt_1.setVisibility(View.VISIBLE);
            filter_furnishings_txt_1.setVisibility(View.GONE);
            filter_payment_txt_1.setVisibility(View.GONE);
        }
        else if (contract_type_value.equals("3"))
        {
            rb4.setChecked(true);

            filter_type_1.setVisibility(View.VISIBLE);
            filter_max_area_1.setVisibility(View.VISIBLE);
            filter_min_area_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_1.setVisibility(View.GONE);
            filter_min_bedrooms_1.setVisibility(View.GONE);
            filter_max_price_1.setVisibility(View.VISIBLE);
            filter_min_price_1.setVisibility(View.VISIBLE);
            filter_furnishings_1.setVisibility(View.GONE);
            filter_payment_1.setVisibility(View.VISIBLE);

            filter_type_txt_1.setVisibility(View.VISIBLE);
            filter_max_area_txt_1.setVisibility(View.VISIBLE);
            filter_min_area_txt_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_txt_1.setVisibility(View.GONE);
            filter_min_bedrooms_txt_1.setVisibility(View.GONE);
            filter_max_price_txt_1.setVisibility(View.VISIBLE);
            filter_min_price_txt_1.setVisibility(View.VISIBLE);
            filter_furnishings_txt_1.setVisibility(View.GONE);
            filter_payment_txt_1.setVisibility(View.VISIBLE);
        }
        else {

            rb2.setChecked(true);

            filter_type_1.setVisibility(View.VISIBLE);
            filter_max_area_1.setVisibility(View.VISIBLE);
            filter_min_area_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_1.setVisibility(View.VISIBLE);
            filter_min_bedrooms_1.setVisibility(View.VISIBLE);
            filter_max_price_1.setVisibility(View.VISIBLE);
            filter_min_price_1.setVisibility(View.VISIBLE);
            filter_furnishings_1.setVisibility(View.VISIBLE);
            filter_payment_1.setVisibility(View.VISIBLE);

            filter_type_txt_1.setVisibility(View.VISIBLE);
            filter_max_area_txt_1.setVisibility(View.VISIBLE);
            filter_min_area_txt_1.setVisibility(View.VISIBLE);
            filter_max_bedrooms_txt_1.setVisibility(View.VISIBLE);
            filter_min_bedrooms_txt_1.setVisibility(View.VISIBLE);
            filter_max_price_txt_1.setVisibility(View.VISIBLE);
            filter_min_price_txt_1.setVisibility(View.VISIBLE);
            filter_furnishings_txt_1.setVisibility(View.VISIBLE);
            filter_payment_txt_1.setVisibility(View.VISIBLE);
        }


        Button search = (Button) view.findViewById( R.id.filter_search);
        TextView reset_txt = (TextView) view.findViewById( R.id.reset_textview);
        ImageView crossbutton = (ImageView) view.findViewById( R.id.crossbutton);

        final Dialog mBottomSheetDialog = new Dialog (MainActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
        mBottomSheetDialog.show ();

        final ArrayAdapter<String> arrayTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, type);
        final ArrayAdapter<String> arrayAreaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, area);
        final ArrayAdapter<String> arrayPriceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, price);
        final ArrayAdapter<String> arrayBedRoomAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, bedroom);
        final ArrayAdapter<String> arrayfurnishingsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, furnishings);
        final ArrayAdapter<String> arrayPaymentAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, payment);

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);

                    filter_type.setAdapter(arrayTypeAdapter);
                    filter_max_area.setAdapter(arrayAreaAdapter);
                    filter_min_area.setAdapter(arrayAreaAdapter);
                    filter_max_price.setAdapter(arrayPriceAdapter);
                    filter_min_price.setAdapter(arrayPriceAdapter);
                    filter_max_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_min_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_furnishings.setAdapter(arrayfurnishingsAdapter);
                    filter_payment.setAdapter(arrayPaymentAdapter);

                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                    rb4.setChecked(false);

                    filter_type.setAdapter(arrayTypeAdapter);
                    filter_max_area.setAdapter(arrayAreaAdapter);
                    filter_min_area.setAdapter(arrayAreaAdapter);
                    filter_max_price.setAdapter(arrayPriceAdapter);
                    filter_min_price.setAdapter(arrayPriceAdapter);
                    filter_max_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_min_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_furnishings.setAdapter(arrayfurnishingsAdapter);
                    filter_payment.setAdapter(arrayPaymentAdapter);

                }
            }
        });

        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb4.setChecked(false);

                    filter_type.setAdapter(arrayTypeAdapter);
                    filter_max_area.setAdapter(arrayAreaAdapter);
                    filter_min_area.setAdapter(arrayAreaAdapter);
                    filter_max_price.setAdapter(arrayPriceAdapter);
                    filter_min_price.setAdapter(arrayPriceAdapter);
                    filter_max_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_min_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_furnishings.setAdapter(arrayfurnishingsAdapter);
                    filter_payment.setAdapter(arrayPaymentAdapter);

                }
            }
        });

        rb4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                    rb3.setChecked(false);

                    filter_type.setAdapter(arrayTypeAdapter);
                    filter_max_area.setAdapter(arrayAreaAdapter);
                    filter_min_area.setAdapter(arrayAreaAdapter);
                    filter_max_price.setAdapter(arrayPriceAdapter);
                    filter_min_price.setAdapter(arrayPriceAdapter);
                    filter_max_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_min_bedrooms.setAdapter(arrayBedRoomAdapter);
                    filter_furnishings.setAdapter(arrayfurnishingsAdapter);
                    filter_payment.setAdapter(arrayPaymentAdapter);

                }
            }
        });

        filter_type.setAdapter(arrayTypeAdapter);
        if (type_spinner_value != null) {
            int spinnerPosition = arrayTypeAdapter.getPosition(type_spinner_value);
            filter_type.setSelection(spinnerPosition);
        }

        filter_max_area.setAdapter(arrayAreaAdapter);
        filter_min_area.setAdapter(arrayAreaAdapter);
        if (max_area_spinner_value != null) {
            int spinnerPosition = arrayAreaAdapter.getPosition(max_area_spinner_value);
            filter_max_area.setSelection(spinnerPosition);
        }
        if (min_area_spinner_value != null) {
            int spinnerPosition = arrayAreaAdapter.getPosition(min_area_spinner_value);
            filter_min_area.setSelection(spinnerPosition);
        }

        filter_max_price.setAdapter(arrayPriceAdapter);
        filter_min_price.setAdapter(arrayPriceAdapter);
        if (max_price_spinner_value != null) {
            int spinnerPosition = arrayPriceAdapter.getPosition(max_price_spinner_value);
            filter_max_price.setSelection(spinnerPosition);
        }
        if (min_price_spinner_value != null) {
            int spinnerPosition = arrayPriceAdapter.getPosition(min_price_spinner_value);
            filter_min_price.setSelection(spinnerPosition);
        }

        filter_max_bedrooms.setAdapter(arrayBedRoomAdapter);
        filter_min_bedrooms.setAdapter(arrayBedRoomAdapter);
        if (max_bedroom_spinner_value != null) {
            int spinnerPosition = arrayBedRoomAdapter.getPosition(max_bedroom_spinner_value);
            filter_max_bedrooms.setSelection(spinnerPosition);
        }
        if (min_bedroom_spinner_value != null) {
            int spinnerPosition = arrayBedRoomAdapter.getPosition(min_bedroom_spinner_value);
            filter_min_bedrooms.setSelection(spinnerPosition);
        }

        filter_furnishings.setAdapter(arrayfurnishingsAdapter);
        if (furnishing_spinner_value != null) {
            int spinnerPosition = arrayfurnishingsAdapter.getPosition(furnishing_spinner_value);
            filter_furnishings.setSelection(spinnerPosition);
        }

        filter_payment.setAdapter(arrayPaymentAdapter);
        if (payment_spinner_value != null) {
            int spinnerPosition = arrayfurnishingsAdapter.getPosition(payment_spinner_value);
            filter_payment.setSelection(spinnerPosition);
        }

        filter_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_shared = filtersTypes.get(position).getName();
                txt_type = filtersTypes.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_max_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                max_area_shared = filters_areas.get(position).getName();
                txt_max_area = filters_areas.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_min_area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                min_area_shared = filters_areas.get(position).getName();
                txt_min_area = filters_areas.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_max_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                max_price_shared = filters_prices.get(position).getName();
                txt_max_price = filters_prices.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_min_price.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                min_price_shared = filters_prices.get(position).getName();
                txt_min_price = filters_prices.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_max_bedrooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                max_bedroom_shared = filters_bedrooms.get(position).getName();
                txt_max_bedroom = filters_bedrooms.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_min_bedrooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                min_bedroom_shared = filters_bedrooms.get(position).getName();
                txt_min_bedroom = filters_bedrooms.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_furnishings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                furnishing_shared = filtersfurnishings.get(position).getName();
                txt_furnishing = filtersfurnishings.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        filter_payment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payment_shared = filterspayments.get(position).getName();
                txt_payment = filterspayments.get(position).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        reset_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rb2.setChecked(true);

                filter_type_1.setVisibility(View.VISIBLE);
                filter_max_area_1.setVisibility(View.VISIBLE);
                filter_min_area_1.setVisibility(View.VISIBLE);
                filter_max_bedrooms_1.setVisibility(View.VISIBLE);
                filter_min_bedrooms_1.setVisibility(View.VISIBLE);
                filter_max_price_1.setVisibility(View.VISIBLE);
                filter_min_price_1.setVisibility(View.VISIBLE);
                filter_furnishings_1.setVisibility(View.VISIBLE);
                filter_payment_1.setVisibility(View.VISIBLE);

                filter_type_txt_1.setVisibility(View.VISIBLE);
                filter_max_area_txt_1.setVisibility(View.VISIBLE);
                filter_min_area_txt_1.setVisibility(View.VISIBLE);
                filter_max_bedrooms_txt_1.setVisibility(View.VISIBLE);
                filter_min_bedrooms_txt_1.setVisibility(View.VISIBLE);
                filter_max_price_txt_1.setVisibility(View.VISIBLE);
                filter_min_price_txt_1.setVisibility(View.VISIBLE);
                filter_furnishings_txt_1.setVisibility(View.VISIBLE);
                filter_payment_txt_1.setVisibility(View.VISIBLE);


                filter_type.setAdapter(arrayTypeAdapter);
                filter_max_area.setAdapter(arrayAreaAdapter);
                filter_min_area.setAdapter(arrayAreaAdapter);
                filter_max_price.setAdapter(arrayPriceAdapter);
                filter_min_price.setAdapter(arrayPriceAdapter);
                filter_max_bedrooms.setAdapter(arrayBedRoomAdapter);
                filter_min_bedrooms.setAdapter(arrayBedRoomAdapter);
                filter_furnishings.setAdapter(arrayfurnishingsAdapter);
                filter_payment.setAdapter(arrayPaymentAdapter);

            }
        });
        crossbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss ();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, PropertiesFragment.newInstance());
                transaction.commit();
                fabmap.setVisibility(View.VISIBLE);
                fabproperty.setVisibility(View.GONE);
                fab_currentloc.setVisibility(View.GONE);

                SharedPreferences.Editor editor  = sharedPreference.edit();
                editor.putString("txt_type", txt_type);
                editor.putString("txt_max_area", txt_max_area);
                editor.putString("txt_min_area", txt_min_area);
                editor.putString("txt_max_price", txt_max_price);
                editor.putString("txt_min_price", txt_min_price);
                editor.putString("txt_max_bedroom", txt_max_bedroom);
                editor.putString("txt_min_bedroom", txt_min_bedroom);
                editor.putString("txt_furnishing", txt_furnishing);
                editor.putString("txt_payment", txt_payment);
                editor.putString("txt_contract_type", txt_contract_type);

                editor.putString("type_spinner_value", type_shared);
                editor.putString("max_area_spinner_value", max_area_shared);
                editor.putString("min_area_spinner_value", min_area_shared);
                editor.putString("max_price_spinner_value", max_price_shared);
                editor.putString("min_price_spinner_value", min_price_shared);
                editor.putString("max_bedroom_spinner_value", max_bedroom_shared);
                editor.putString("min_bedroom_spinner_value", min_bedroom_shared);
                editor.putString("furnishing_spinner_value", furnishing_shared);
                editor.putString("payment_spinner_value", payment_shared);

                editor.commit();

                mBottomSheetDialog.dismiss ();
            }
        });

    }

    public void openSearchingSheet () {

        sharedPreference =  getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        String country_id = sharedPreference.getString("country_id","");
        final String country_name = sharedPreference.getString("country_name","");

        View view = getLayoutInflater ().inflate (R.layout.searching_sheet, null);

        ImageView crossbutton = (ImageView) view.findViewById( R.id.crossbutton);

        final Dialog mBottomSheetDialog = new Dialog (MainActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity (Gravity.TOP);
        mBottomSheetDialog.show ();

        crossbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss ();
            }
        });

        final android.widget.SearchView simpleSearchView = (android.widget.SearchView) view.findViewById(R.id.simpleSearchView); // inititate a search view
        simpleSearchView.setIconified(false);
        simpleSearchView.setQueryHint(country_name);

        final ListView listView_porperty = (ListView) view.findViewById(R.id.listview_country);

        listView_porperty.setAdapter(adapter1);


        listView_porperty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a= allcountry.get(i).getId();
                String b= allcountry.get(i).getName();

                SharedPreferences.Editor editor  = sharedPreference.edit();
                editor.putString("country_id", a);
                editor.putString("country_name", b);
                editor.commit();


                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, PropertiesFragment.newInstance());
                transaction.commit();
                fabmap.setVisibility(View.VISIBLE);
                fabproperty.setVisibility(View.GONE);
                fab_currentloc.setVisibility(View.GONE);

                mBottomSheetDialog.dismiss ();
            }
        });
        simpleSearchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listView_porperty.setVisibility(View.GONE);

                if (TextUtils.isEmpty(query)) {
                    listView_porperty.clearTextFilter();
                } else {
                    listView_porperty.setFilterText(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // do something when text changes

                String text = newText;
                adapter1.filter(text);

                listView_porperty.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

}
