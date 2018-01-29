package com.example.sheheryar.propertydodo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CompanyProperties extends AppCompatActivity {

    private SharedPreferences sharedPreference;
    String company_id,company_name,property_id;
    private ListView listView;
    private ArrayList<PropertyList> stringArrayList;
    private PropertiesListViewAdapter1 adapter;
    String[] myList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_properties);
        sharedPreference =  getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        company_id = extras.getString("company_id");
        company_name = extras.getString("company_name");
        property_id = extras.getString("property_id");

        myList = getIntent().getStringArrayExtra("items_to_parse");


        TextView toolbarname= (TextView) findViewById(R.id.toolbarname);
        if (company_id != null) {
            toolbarname.setText(company_name);
        }
        else {
            toolbarname.setText("Properties in area");
        }

        listView = (ListView) findViewById(R.id.list_item_property);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String a= stringArrayList.get(i).getId();
            Intent intent = new Intent(CompanyProperties.this, PropertyDetail.class);
            intent.putExtra("property_id",a);
            startActivity(intent);
            }
        });

        ImageView backbutton= (ImageView) findViewById(R.id.backproperties);
        backbutton.setOnClickListener(new View.OnClickListener() {
            // Start new list activity
            public void onClick(View v) {
                if (company_id != null) {
                    Intent intent = new Intent(CompanyProperties.this, PropertyDetail.class);
                    intent.putExtra("property_id", property_id);
                    startActivity(intent);
                }
                else if (myList.length != 0){

                    Intent intent = new Intent(CompanyProperties.this, MainActivity.class);
                    SharedPreferences.Editor editor  = sharedPreference.edit();
                    editor.putString("map_property_list", "2");
                    editor.commit();
                    startActivity(intent);
                }
            }
        });
        getData();
    }

    private void getData() {
        String url = getString(R.string.base_url)+"properties/paginate";

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CompanyProperties.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(CompanyProperties.this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        try {

            JSONArray responses = null;
            try {
                responses = new JSONObject(response).getJSONArray("properties");
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

                    String propertyid = responses.getJSONObject(i).getString("id");
                    String companyid = responses.getJSONObject(i).getString("company_id");

                    propertyList.setCategory(responses.getJSONObject(i).getString("category"));
                    propertyList.setBathrooms(responses.getJSONObject(i).getString("bathrooms"));
                    propertyList.setBedrooms(responses.getJSONObject(i).getString("bedrooms"));
                    propertyList.setCountry(responses.getJSONObject(i).getString("country"));
                    propertyList.setState(responses.getJSONObject(i).getString("state"));
                    propertyList.setCity(responses.getJSONObject(i).getString("city"));
                    propertyList.setImage(responses.getJSONObject(i).getString("image"));
                    propertyList.setStatus(responses.getJSONObject(i).getString("status"));

                    if (company_id != null)
                    {
                        if (company_id.equals(companyid)) {
                            stringArrayList.add(propertyList);
                        }
                    }
                    else if (myList.length != 0)
                    {
                        for (int j = 0; j< myList.length; j++){

                            if (myList[j].equals(propertyid))
                            {
                                stringArrayList.add(propertyList);
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter = new PropertiesListViewAdapter1(CompanyProperties.this, stringArrayList);
            listView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
