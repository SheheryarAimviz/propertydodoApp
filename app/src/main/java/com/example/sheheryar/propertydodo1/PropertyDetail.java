package com.example.sheheryar.propertydodo1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.sheheryar.propertydodo1.R.id.bathrooms;
import static com.example.sheheryar.propertydodo1.R.id.bedrooms;
import static com.example.sheheryar.propertydodo1.R.id.category;
import static com.example.sheheryar.propertydodo1.R.id.collapsing_toolbar;
import static com.example.sheheryar.propertydodo1.R.id.country;
import static com.example.sheheryar.propertydodo1.R.id.countrytop;
import static com.example.sheheryar.propertydodo1.R.id.input_phone1;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class PropertyDetail extends AppCompatActivity {
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> stringArrayList;
    private ArrayList<PropertyList> AmenitiesList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private ProgressDialog progressDialog;
    int MAX_LINES =200;

    CollapsingToolbarLayout collapsingToolbar;
    TextView categorytop, bedroomstop, rent_type, bathroomstop, citytop, statetop, countrytop, ref_number,
            category, bedrooms, bathrooms, price, area, description, company, company_address, city, state, country;
    ImageView company_logo;
    EditText name, email, phone, message;

    String txtname, txtemail, txtphone, txtmessage;

    TextView price1, category1, bedrooms1, bathrooms1, country1, state1, city1, status1;
    ImageView imageView1;
    Button company_properties,viewlocation;

    ImageButton share_button,save_button;
    boolean isPressed;


    String MY_PREFS_NAME = "MyPrefsFile";
    private SharedPreferences sharedPreference;


    String id , propertyImage ;
    String propertylat , propertylong , propertyprice , company_id , company_name , agent_contact , rent_typeheader , priceheader , propertyurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_detail);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("property_id");
        propertyImage=extras.getString("property_image");
        String code = extras.getString("code");

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mLayoutManager = new GridLayoutManager(PropertyDetail.this,2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        categorytop= (TextView) findViewById(R.id.categorytop);
        bedroomstop= (TextView) findViewById(R.id.bedroomstop);
        bathroomstop= (TextView) findViewById(R.id.bathroomstop);
        citytop= (TextView) findViewById(R.id.citytop);
        statetop= (TextView) findViewById(R.id.statetop);
        countrytop= (TextView) findViewById(R.id.countrytop);
        ref_number= (TextView) findViewById(R.id.ref_number);
        category= (TextView) findViewById(R.id.category);
        bedrooms= (TextView) findViewById(R.id.bedrooms);
        bathrooms= (TextView) findViewById(R.id.bathrooms);
        price= (TextView) findViewById(R.id.price);
        rent_type= (TextView) findViewById(R.id.rent_type);
        area= (TextView) findViewById(R.id.area);
        description= (TextView) findViewById(R.id.description);
        company= (TextView) findViewById(R.id.company);
        company_address= (TextView) findViewById(R.id.company_address);
        company_logo= (ImageView) findViewById(R.id.company_logo);
        city= (TextView) findViewById(R.id.city);
        state= (TextView) findViewById(R.id.state);
        country= (TextView) findViewById(R.id.country);

        viewlocation= (Button) findViewById(R.id.viewlocation);

        viewlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PropertyDetail.this, CurrentLocMapsActivity.class);
                intent.putExtra("lat",propertylat);
                intent.putExtra("long",propertylong);
                intent.putExtra("price",propertyprice);
                intent.putExtra("property_id",id);
                startActivity(intent);
            }
        });

        company_properties= (Button) findViewById(R.id.company_properties);

        company_properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PropertyDetail.this, CompanyProperties.class);
                intent.putExtra("company_id",company_id);
                intent.putExtra("company_name",company_name);
                intent.putExtra("property_id",id);
                startActivity(intent);
            }
        });

        getData();

        share_button= (ImageButton) findViewById(R.id.share_button);

        save_button= (ImageButton) findViewById(R.id.save_button);
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String property = prefs.getString(id, null);

        if (property == null) {
            save_button.setBackgroundResource(R.drawable.ic_heart);
        }
        else
        {
            save_button.setBackgroundResource(R.drawable.ic_saved);
            isPressed=true;
        }
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPressed){

                    sharedPreference =  getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                    save_button.setBackgroundResource(R.drawable.ic_heart);
                    sharedPreference.edit().remove(id).apply();
                    Toast.makeText(PropertyDetail.this, "Removed from saved", Toast.LENGTH_LONG).show();

                }else{
                    save_button.setBackgroundResource(R.drawable.ic_saved);

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString(id, id);
                    editor.apply();
                    Toast.makeText(PropertyDetail.this, "Property saved ", Toast.LENGTH_LONG).show();
                }
                isPressed = !isPressed;
            }
        });

        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        final LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", agent_contact, null));
                                startActivity(intent);

                                break;
                            case R.id.action_item2:
                                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                                sendIntent.setData(Uri.parse("sms:"+agent_contact));
                                sendIntent.putExtra("sms_body", "Please contact me regarding "+propertyurl);
                                startActivity(sendIntent);
                                break;
                            case R.id.action_item3:

                                final View Viewlayout = inflater.inflate(R.layout.maildailog,
                                        (ViewGroup) findViewById(R.id.layout_dialog1));
                                popDialog.setView(Viewlayout);

                                name = (EditText) Viewlayout.findViewById(R.id.input_name1);
                                email = (EditText) Viewlayout.findViewById(R.id.input_email1);
                                phone = (EditText) Viewlayout.findViewById(R.id.input_phone1);
                                message = (EditText) Viewlayout.findViewById(R.id.input_message1);

                                price1 = (TextView) Viewlayout.findViewById(R.id.price1);
                                category1 = (TextView) Viewlayout.findViewById(R.id.category1);
                                bedrooms1 = (TextView) Viewlayout.findViewById(R.id.bedrooms1);
                                bathrooms1 = (TextView) Viewlayout.findViewById(R.id.bathrooms1);
                                country1 = (TextView) Viewlayout.findViewById(R.id.country1);
                                state1 = (TextView) Viewlayout.findViewById(R.id.state1);
                                city1 = (TextView) Viewlayout.findViewById(R.id.city1);
                                status1 = (TextView) Viewlayout.findViewById(R.id.status1);
                                imageView1 = (ImageView) Viewlayout.findViewById(R.id.image_view1);

                                Picasso.with(Viewlayout.getContext())
                                        .load(propertyImage)
                                        .into(imageView1);

                                message.setText("Hi, found this property with ref:"+ref_number.getText());

                                if (state.getText().equals("1"))
                                {
                                    status1.setText("VERIFIED LISTING");
                                }
                                else
                                {
                                    status1.setText("");
                                }
                                category1.setText(category.getText());
                                bedrooms1.setText(bedrooms.getText());
                                bathrooms1.setText(bathrooms.getText());
                                country1.setText(country.getText());
                                state1.setText(state.getText());
                                city1.setText(city.getText());
                                if (rent_typeheader == "null")
                                {
                                    price1.setText(priceheader+" AED");
                                }
                                else
                                {
                                    price1.setText(priceheader+" AED/"+rent_typeheader);
                                }


                                popDialog.setPositiveButton("Send", new DialogInterface.OnClickListener(){

                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                        txtname = name.getText().toString();
                                        txtemail = email.getText().toString();
                                        txtphone = phone.getText().toString();
                                        txtmessage = message.getText().toString();

                                        sendemail(txtname,txtemail,txtphone,txtmessage);

                                    }

                                })

                                        // Button Cancel
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog,int id) {
                                                        dialog.cancel();
                                                    }
                                                });

                                popDialog.create();
                                popDialog.show();

                                break;
                        }
                        return true;
                    }
                });
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new AndroidImageAdapter(PropertyDetail.this,stringArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES =stringArrayList.size();

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }
            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }
            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }


    private void sendemail(String n, String e, String p, String m) {

        String url= getString(R.string.base_url)+"properties/mobile/mail?name="+n+"&email="+e+"&contact="+p+"&message_text="+m+"&status=0";

        progressDialog = new ProgressDialog(PropertyDetail.this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        progressDialog.getWindow().setGravity(Gravity.CENTER);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();
                try {
                    JSONObject responses = null;
                    try {
                        responses = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String Message = responses.getString("message");
                    String Status = responses.getString("status");
                    if (Status.equals("200"))
                    {
                        Toast.makeText(PropertyDetail.this, Message, Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(PropertyDetail.this, Message, Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PropertyDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PropertyDetail.this);
        requestQueue.add(stringRequest);
    }

    private void getData() {

        String url = getString(R.string.base_url)+"properties/"+id;
        progressDialog = new ProgressDialog(PropertyDetail.this);
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
                        Toast.makeText(PropertyDetail.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(PropertyDetail.this);
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        try {
            JSONObject responses = null;
            try {
                responses = new JSONObject(response).getJSONObject("properties");

            } catch (JSONException e) {
                e.printStackTrace();
            }


            String s_id = responses.getJSONObject("property").getString("id");
            String s_title = responses.getJSONObject("property").getString("title");
            String s_subtitle = responses.getJSONObject("property").getString("subtitle");
            String s_description = responses.getJSONObject("property").getString("description");
            String s_category = responses.getJSONObject("property").getString("category");
            String s_ref_number = responses.getJSONObject("property").getString("ref_number");
            String s_permit_number = responses.getJSONObject("property").getString("permit_number");
            String s_bedrooms = responses.getJSONObject("property").getString("bedrooms");
            String s_bathrooms = responses.getJSONObject("property").getString("bathrooms");
            String s_area = responses.getJSONObject("property").getString("area");
            String s_builtup_area = responses.getJSONObject("property").getString("builtup_area");
            String s_price = responses.getJSONObject("property").getString("price");
            String s_ppa = responses.getJSONObject("property").getString("ppa");
            String s_approx_rent = responses.getJSONObject("property").getString("approx_rent");
            String s_yearly_maintenance_charges = responses.getJSONObject("property").getString("yearly_maintenance_charges");
            String s_rent_type = responses.getJSONObject("property").getString("rent_type");
            String s_contract_type = responses.getJSONObject("property").getString("contract_type");
            String s_furnishings = responses.getJSONObject("property").getString("furnishings");
            String s_off_plan = responses.getJSONObject("property").getString("off_plan");
            String s_agent_id = responses.getJSONObject("property").getString("agent_id");
            String s_agent = responses.getJSONObject("property").getString("agent");
            String s_agent_contact = responses.getJSONObject("property").getString("agent_contact");
            String s_agent_userid = responses.getJSONObject("property").getString("agent_userid");
            String s_agent_image = responses.getJSONObject("property").getString("agent_image");
            String s_manager_id = responses.getJSONObject("property").getString("manager_id");
            String s_manager = responses.getJSONObject("property").getString("manager");
            String s_manager_userid = responses.getJSONObject("property").getString("manager_userid");
            String s_company_id = responses.getJSONObject("property").getString("company_id");
            String s_company = responses.getJSONObject("property").getString("company");
            String s_company_address = responses.getJSONObject("property").getString("company_address");
            String s_company_userid = responses.getJSONObject("property").getString("company_userid");
            String s_company_logo = responses.getJSONObject("property").getString("company_logo");
            String s_customer_id = responses.getJSONObject("property").getString("customer_id");
            String s_customer = responses.getJSONObject("property").getString("customer");
            String s_customer_userid = responses.getJSONObject("property").getString("customer_userid");
            String s_customer_image = responses.getJSONObject("property").getString("customer_image");
            String s_updated_at = responses.getJSONObject("property").getString("updated_at");
            String s_city = responses.getJSONObject("property").getString("city");
            String s_state = responses.getJSONObject("property").getString("state");
            String s_country = responses.getJSONObject("property").getString("country");
            String s_Lat = responses.getJSONObject("property").getString("Lat");
            String s_Long = responses.getJSONObject("property").getString("Long");
            String s_image = responses.getJSONObject("property").getString("image");
            String s_url = responses.getJSONObject("property").getString("url");

            propertylat = s_Lat;
            propertylong = s_Long;
            propertyprice = s_price;
            company_id = s_company_id;
            company_name = s_company;
            agent_contact = s_agent_contact;
            rent_typeheader = s_rent_type;
            priceheader = s_price;
            propertyurl = s_url;

            if (s_rent_type.equals("null"))
            {
                collapsingToolbar.setTitle(s_price+" AED");
            }
            else
            {
                collapsingToolbar.setTitle(s_price+" AED/"+s_rent_type);
                rent_type.setText("/"+s_rent_type);
            }
            categorytop.setText(s_category);
            bedroomstop.setText(s_bedrooms);
            bathroomstop.setText(s_bathrooms);
            citytop.setText(s_city);
            statetop.setText(s_state);
            countrytop.setText(s_country);
            ref_number.setText(s_ref_number);

            category.setText(s_category);
            bedrooms.setText(s_bedrooms);
            bathrooms.setText(s_bathrooms);
            price.setText(s_price);
            area.setText(s_area);

            description.setText(s_description);
            description.setMaxLines(7);
            final TextView show = (TextView) findViewById(R.id.more);
            show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.setText("");
                    description.setMaxLines(2000);
                }
            });

            city.setText(s_city);
            state.setText(s_state);
            country.setText(s_country);

            company.setText(s_company);
            company_address.setText(s_company_address);

            Picasso.with(PropertyDetail.this)
                    .load(s_company_logo)
                    .into(company_logo);


            stringArrayList = new ArrayList<>();
            ImageModel ImageModel;
            JSONArray jsonArray = responses.getJSONArray("images");
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    ImageModel =new ImageModel();
                    ImageModel.setImage_drawable(id+"/"+jsonArray.getJSONObject(i).getString("image"));
                    stringArrayList.add(ImageModel);
                    init();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            AmenitiesList = new ArrayList<>();
            PropertyList PropertyList;
            JSONArray jsonAmenitiesArray = responses.getJSONArray("amenities");
            for (int k = 0; k < jsonArray.length(); k++) {
                try {
                    PropertyList =new PropertyList();
                    PropertyList.setAmenities(jsonAmenitiesArray.getJSONObject(k).getString("amenity"));
                    AmenitiesList.add(PropertyList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mAdapter = new AmenitiesAdapter(AmenitiesList);
            mRecyclerView.setAdapter(mAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
