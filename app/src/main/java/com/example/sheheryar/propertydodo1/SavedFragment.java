package com.example.sheheryar.propertydodo1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class SavedFragment extends Fragment {

    private ListView listView;
    private ArrayList<PropertyList> stringArrayList;
    private PropertiesListViewAdapter1 adapter;
    String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences prefs;
    private TextView info;
    LoginButton loginButton;
    CallbackManager callbackManager;
    public SavedFragment() {
        // Required empty public constructor
    }

    public static SavedFragment newInstance() {
        SavedFragment fragment = new SavedFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved, container, false);
        FacebookSdk.sdkInitialize(getApplicationContext());

        initializeControls();
        info = (TextView)rootView.findViewById(R.id.info);
        loginwithFB();

        listView = (ListView) rootView.findViewById(R.id.list_item_saved);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String a= stringArrayList.get(i).getId();

                Intent intent = new Intent(getActivity().getApplication(), PropertyDetail.class);
                intent.putExtra("property_id",a);
                intent.putExtra("property_image",stringArrayList.get(i).getImage());
                startActivity(intent);
            }
        });

        prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        getData();

        return  rootView;
    }

    private void initializeControls()
    {
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) getActivity().findViewById(R.id.login_button);
    }
    private void loginwithFB()
    {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
               Toast.makeText(getActivity(), loginResult.getAccessToken().getUserId()+" & "+loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );
            }

            @Override
            public void onCancel() {

                Toast.makeText(getActivity(), "Login attempt canceled.", Toast.LENGTH_LONG).show();
                info.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed.");
                Toast.makeText(getActivity(), "Login attempt failed."+error.toString() , Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private ProgressDialog progressDialog;
    private void getData() {

        String url = getString(R.string.base_url)+"properties/paginate";

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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void showJSON(String response){
        try {

            JSONArray responses = null;
            try {

                responses = new JSONObject(response).getJSONArray("properties");
//                responses = new JSONObject(response).getJSONObject("properties").getJSONArray("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            stringArrayList = new ArrayList<>();
            PropertyList propertyList;


            for (int i = 0; i < responses.length(); i++) {

                String id=responses.getJSONObject(i).getString("id");
                String propertyid = prefs.getString(id, null);

                if (id.equals(propertyid))
                {
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
            }
            adapter = new PropertiesListViewAdapter1(getActivity(), stringArrayList);
            listView.setAdapter(adapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
