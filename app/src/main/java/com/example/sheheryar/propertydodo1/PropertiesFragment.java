package com.example.sheheryar.propertydodo1;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.security.PublicKey;
import java.util.ArrayList;
import android.app.Activity;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.app.Activity;
import android.app.AlertDialog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PropertiesFragment extends Fragment {

    private SharedPreferences sharedPreference;

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    ListView listView;
    private ArrayList<PropertyList> stringArrayList;
    private PropertiesListViewAdapter1 adapter;
    int map = 0;
    private ProgressDialog progressDialog;
    String country_id, country_name, txt_contract_type, txt_type, txt_max_area, txt_min_area, txt_max_bedroom, txt_min_bedroom, txt_max_price, txt_min_price, txt_furnishing, txt_payment;
    String totalpages ;
    int pageCount = 1;
    View footer;

    public PropertiesFragment() {}

    public static PropertiesFragment newInstance() {
        PropertiesFragment fragment = new PropertiesFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_properties, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        sharedPreference =  this.getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        txt_type = sharedPreference.getString("txt_type","");
        txt_max_area = sharedPreference.getString("txt_max_area","");
        txt_min_area = sharedPreference.getString("txt_min_area","");
        txt_max_bedroom = sharedPreference.getString("txt_max_bedroom","");
        txt_min_bedroom = sharedPreference.getString("txt_min_bedroom","");
        txt_max_price = sharedPreference.getString("txt_max_price","");
        txt_min_price = sharedPreference.getString("txt_min_price","");
        txt_furnishing = sharedPreference.getString("txt_furnishing","");
        txt_payment = sharedPreference.getString("txt_payment","");
        txt_contract_type = sharedPreference.getString("txt_contract_type","1");
        country_id = sharedPreference.getString("country_id","");
        country_name = sharedPreference.getString("country_name","Untited Arab Emirates");

        toolbar.setTitle(country_name);


        listView = (ListView) rootView.findViewById(R.id.list_item1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a= stringArrayList.get(i).getId();
                Intent intent = new Intent(getActivity().getApplication(), PropertyDetail.class);
                intent.putExtra("property_id",a);
                intent.putExtra("property_image",stringArrayList.get(i).getImage());
                intent.putExtra("code","list");
                startActivity(intent);
            }
        });


        final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflaters = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final AlertDialog ad = popDialog.create();

        FloatingActionButton fablist = (FloatingActionButton) rootView.findViewById(R.id.fab_list);
        fablist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View Viewlayout = inflaters.inflate(R.layout.featured,
                        (ViewGroup) getActivity().findViewById(R.id.layout_dialog));
                ad.setTitle("Sort by");
                ad.setView(Viewlayout);

                Button featuredbutton = (Button) Viewlayout.findViewById(R.id.featuredbutton);
                Button newestbutton = (Button) Viewlayout.findViewById(R.id.newestbutton);
                Button priceLowbutton = (Button) Viewlayout.findViewById(R.id.priceLowbutton);
                Button priceHighbutton = (Button) Viewlayout.findViewById(R.id.priceHighbutton);
                Button bedEastbutton = (Button) Viewlayout.findViewById(R.id.bedEastbutton);
                Button bedMostbutton = (Button) Viewlayout.findViewById(R.id.bedMostbutton);

                featuredbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                newestbutton.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onClick( View view) {
                        String newest="t";
                        getDataSort(newest,txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id);
                        if(ad.isShowing())
                            ad.dismiss();
                    }
                });

                priceLowbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newest="pl";
                        getDataSort(newest,txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id);
                        if(ad.isShowing())
                            ad.dismiss();
                    }
                });

                priceHighbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newest="ph";
                        getDataSort(newest,txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id);
                        if(ad.isShowing())
                            ad.dismiss();
                    }
                });

                bedEastbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newest="bl";
                        getDataSort(newest,txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id);
                        if(ad.isShowing())
                            ad.dismiss();
                    }
                });

                bedMostbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String newest="bh";
                        getDataSort(newest,txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id);
                        if(ad.isShowing())
                            ad.dismiss();
                    }
                });

                ad.show();
            }
        });

        if (country_id == "" && txt_contract_type == "1" && txt_type == "" && txt_max_area == "" && txt_min_area == "" && txt_max_bedroom == "" && txt_min_bedroom == "" && txt_max_price == "" && txt_min_price == "" && txt_furnishing == "" && txt_payment == "")
        {
            getData(country_id , txt_contract_type);
        }
        else
        {
            getDataFilter(txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id );
        }

        return  rootView;
    }

    private void getDataFilter(String type, String max_area, String min_area, String max_bed, String min_bed, String max_price, String min_price, String furnishing, String payment, String contract_type, String country_id) {

//        String url = getString(R.string.base_url)+"properties/paginate?c="+type+"&at="+max_area+"&af="+min_area+"&bt="+max_bed+"&bf="+min_bed+"&pt="+max_price+"&pf="+min_price+"&fu="+furnishing+"&rt="+payment+"&ct="+contract_type;
        String url = getString(R.string.base_url)+"properties?l="+country_id+"&c="+type+"&at="+max_area+"&af="+min_area+"&bt="+max_bed+"&bf="+min_bed+"&pt="+max_price+"&pf="+min_price+"&fu="+furnishing+"&rt="+payment+"&ct="+contract_type;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getDataSort(String sort, String type, String max_area, String min_area, String max_bed, String min_bed, String max_price, String min_price, String furnishing, String payment, String contract_type, String country_id) {

//        String url = getString(R.string.base_url)+"properties/paginate?sort="+sort+"&c="+type+"&at="+max_area+"&af="+min_area+"&bt="+max_bed+"&bf="+min_bed+"&pt="+max_price+"&pf="+min_price+"&fu="+furnishing+"&rt="+payment+"&ct="+contract_type;
        String url = getString(R.string.base_url)+"properties?l="+country_id+"&sort="+sort+"&c="+type+"&at="+max_area+"&af="+min_area+"&bt="+max_bed+"&bf="+min_bed+"&pt="+max_price+"&pf="+min_price+"&fu="+furnishing+"&rt="+payment+"&ct="+contract_type;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getData(String country_id , String type) {
//        String url = getString(R.string.base_url)+"properties/paginate?c="+type;
        String url = getString(R.string.base_url)+"properties?page="+1+"?l="+country_id+"&c="+type;
//        String url = getString(R.string.base_url)+"properties/?page="+pagenumber;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getData(String country_id, String type, int pageCount, final int totalItems){

        String url = getString(R.string.base_url)+"properties?page="+pageCount+"?l="+country_id+"&c="+type;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                showPaginationJSON(response,totalItems);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showPaginationJSON(String response,int totalItems) {
        try {
            JSONArray responses = null;
            try {
                responses = new JSONObject(response).getJSONObject("properties").getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PropertyList propertyList;

            for (int i = 0; i < responses.length(); i++) {
                try {
                    propertyList =new PropertyList();
                    propertyList.setId(responses.getJSONObject(i).getString("id"));
                    propertyList.setPrice(responses.getJSONObject(i).getString("price"));
                    if (responses.getJSONObject(i).getString("rent_type") == "null")
                    {
                    }else {
                        propertyList.setRent_type("/"+responses.getJSONObject(i).getString("rent_type"));
                    }
                    propertyList.setCategory(responses.getJSONObject(i).getString("category"));
                    propertyList.setBathrooms(responses.getJSONObject(i).getString("bathrooms"));
                    propertyList.setBedrooms(responses.getJSONObject(i).getString("bedrooms"));
                    propertyList.setCountry(responses.getJSONObject(i).getString("country"));
                    propertyList.setState(responses.getJSONObject(i).getString("state"));
                    propertyList.setCity(responses.getJSONObject(i).getString("city"));
                    propertyList.setImage(responses.getJSONObject(i).getString("image"));
                    propertyList.setStatus(responses.getJSONObject(i).getString("status"));
                    stringArrayList.add(propertyList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
            listView.setSelection(totalItems);
            pageCount++;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void showJSON(String response){
        try {
                JSONArray responses = null;
                try {
                    responses = new JSONObject(response).getJSONObject("properties").getJSONArray("data");
                    totalpages = new JSONObject(response).getJSONObject("properties").getString("last_page");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                stringArrayList = new ArrayList<>();
                PropertyList propertyList;

                for (int i = 0; i < responses.length(); i++) {
                    try {
                        propertyList =new PropertyList();
                        propertyList.setId(responses.getJSONObject(i).getString("id"));
                        propertyList.setPrice(responses.getJSONObject(i).getString("price"));
                        if (responses.getJSONObject(i).getString("rent_type") == "null")
                        {
                        }else {
                            propertyList.setRent_type("/"+responses.getJSONObject(i).getString("rent_type"));
                        }
                        propertyList.setCategory(responses.getJSONObject(i).getString("category"));
                        propertyList.setBathrooms(responses.getJSONObject(i).getString("bathrooms"));
                        propertyList.setBedrooms(responses.getJSONObject(i).getString("bedrooms"));
                        propertyList.setCountry(responses.getJSONObject(i).getString("country"));
                        propertyList.setState(responses.getJSONObject(i).getString("state"));
                        propertyList.setCity(responses.getJSONObject(i).getString("city"));
                        propertyList.setImage(responses.getJSONObject(i).getString("image"));
                        propertyList.setStatus(responses.getJSONObject(i).getString("status"));
                        stringArrayList.add(propertyList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                footer =((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.progress,null,false);
                listView.addFooterView(footer);



                adapter = new PropertiesListViewAdapter1(getActivity(), stringArrayList);
                listView.setAdapter(adapter);
                pageCount++;
                listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstItem,  int visibleItemCount, final int totalItems) {
                        Log.e("Get position", "--firstItem:" + firstItem + "  visibleItemCount:" + visibleItemCount + "  totalItems:" + totalItems + "  pageCount:" + pageCount);
                        int total = firstItem + visibleItemCount;

                        if (pageCount <= Integer.parseInt(totalpages)) {
                            if (total == totalItems) {
                                // Execute some code after 15 seconds have passed
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {

                                        if (country_id == "" && txt_contract_type == "1" && txt_type == "" && txt_max_area == "" && txt_min_area == "" && txt_max_bedroom == "" && txt_min_bedroom == "" && txt_max_price == "" && txt_min_price == "" && txt_furnishing == "" && txt_payment == "")
                                        {
                                            getData(country_id,txt_contract_type,pageCount,totalItems);
                                        }
                                        else
                                        {
                                            getDataFilter(txt_type,txt_max_area,txt_min_area,txt_max_bedroom,txt_min_bedroom,txt_max_price,txt_min_price,txt_furnishing,txt_payment,txt_contract_type,country_id );
                                        }
                                    }
                                }, 1500);
                            }
                        } else {
                            Log.e("hide footer", "footer hide");
                            listView.removeFooterView(footer);
                        }
                    }
                });
            }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

}
