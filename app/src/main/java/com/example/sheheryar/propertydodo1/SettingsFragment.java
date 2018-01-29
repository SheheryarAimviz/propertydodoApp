package com.example.sheheryar.propertydodo1;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class SettingsFragment extends Fragment {

    private SharedPreferences sharedPreference;

    ListView list;

    public SettingsFragment() {
    }

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        final android.app.AlertDialog.Builder popDialog = new android.app.AlertDialog.Builder(getActivity());
        final LayoutInflater inflaters = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        final android.app.AlertDialog ad = popDialog.create();
        final android.app.AlertDialog ad_currency = popDialog.create();

        sharedPreference =  getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        String area_unit = sharedPreference.getString("txt_area_unit","");
        String currency = sharedPreference.getString("txt_currency","");
        final String[]titles = new String[]{"Area unit","Currency"};
        final String[]subtitle;


        if (area_unit == "" && currency == "" )
        {
            subtitle = new String[]{"Square foot", "AED"};
        }else if(area_unit == "")
        {
            subtitle = new String[]{"Square foot", currency};
        }else if(currency == "")
        {
            subtitle = new String[]{area_unit, "AED"};
        }else {
            subtitle = new String[]{area_unit, currency};
        }


        List<RowData> rowData;
        rowData =new ArrayList<RowData>();
        for(int i=0;i<titles.length;i++){
            RowData data =new RowData();
            data.setSubtitle(subtitle[i]);
            data.setTitle(titles[i]);
            rowData.add(data);
        }

        list = (ListView) rootView.findViewById(R.id.settinglist);
        final MyAdapter_Setting adapter = new MyAdapter_Setting(getActivity(), rowData);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final View Viewlayout = inflaters.inflate(R.layout.featured_areaunit,
                        (ViewGroup) getActivity().findViewById(R.id.layout_dialog));
                ad.setTitle("Select area unit");
                ad.setView(Viewlayout);

                sharedPreference =  getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                String area_unit = sharedPreference.getString("txt_area_unit","");


                final RadioButton rb1 = (RadioButton) Viewlayout.findViewById(R.id.radioForfoot);
                final RadioButton rb2 = (RadioButton) Viewlayout.findViewById(R.id.radioFormeter);

                if (rb1.getText().equals(area_unit))
                {
                    rb1.setChecked(true);
                }
                else if(rb2.getText().equals(area_unit))
                {
                    rb2.setChecked(true);
                }
                else
                {
                    rb1.setChecked(true);
                }

                TextView cancel =(TextView) Viewlayout.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });
                RadioGroup radioGroup = (RadioGroup) Viewlayout.findViewById(R.id.radioGrp);
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        RadioButton rb=(RadioButton)Viewlayout.findViewById(checkedId);

                        SharedPreferences.Editor editor  = sharedPreference.edit();
                        editor.putString("txt_area_unit", rb.getText().toString());
                        editor.commit();


                        sharedPreference =  getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                        String currency = sharedPreference.getString("txt_currency","AED");

                        final String[]titles = new String[]{"Area unit","Currency"};

                        final String[]subtitle = new String[]{rb.getText().toString(), currency};

                        List<RowData> rowData;
                        rowData =new ArrayList<RowData>();
                        for(int i=0;i<titles.length;i++){
                            RowData data =new RowData();
                            data.setSubtitle(subtitle[i]);
                            data.setTitle(titles[i]);
                            rowData.add(data);
                        }
                        final MyAdapter_Setting adapter = new MyAdapter_Setting(getActivity(), rowData);
                        list.setAdapter(adapter);

                        ad.dismiss();
                    }
                });



                final View Viewlayout_currency = inflaters.inflate(R.layout.featuredcurrency,
                        (ViewGroup) getActivity().findViewById(R.id.layout_dialog_currency));
                ad_currency.setTitle("Select Currency");
                ad_currency.setView(Viewlayout_currency);

                sharedPreference =  getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                String currency = sharedPreference.getString("txt_currency","");

                final RadioButton rb_aed = (RadioButton) Viewlayout_currency.findViewById(R.id.radioFor_aed);
                final RadioButton rb_usd = (RadioButton) Viewlayout_currency.findViewById(R.id.radioFor_usd);

                if (rb_aed.getText().equals(currency))
                {
                    rb_aed.setChecked(true);
                }
                else if(rb_usd.getText().equals(currency))
                {
                    rb_usd.setChecked(true);
                }
                else
                {
                    rb_aed.setChecked(true);
                }

                TextView cancel_abc =(TextView) Viewlayout_currency.findViewById(R.id.cancel_currency);
                cancel_abc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad_currency.dismiss();
                    }
                });
                RadioGroup radioGroup_currency = (RadioGroup) Viewlayout_currency.findViewById(R.id.radioGrp_currency);
                radioGroup_currency.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb =(RadioButton) Viewlayout_currency.findViewById(checkedId);

                        SharedPreferences.Editor editor  = sharedPreference.edit();
                        editor.putString("txt_currency", rb.getText().toString());
                        editor.commit();

                        sharedPreference =  getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
                        String area_unit = sharedPreference.getString("txt_area_unit","Square foot");


                        final String[]titles = new String[]{"Area unit","Currency"};
                        final String[]subtitle = new String[]{area_unit, rb.getText().toString()};

                        List<RowData> rowData;
                        rowData =new ArrayList<RowData>();
                        for(int i=0;i<titles.length;i++){
                            RowData data =new RowData();
                            data.setSubtitle(subtitle[i]);
                            data.setTitle(titles[i]);
                            rowData.add(data);
                        }
                        final MyAdapter_Setting adapter = new MyAdapter_Setting(getActivity(), rowData);
                        list.setAdapter(adapter);

                        ad_currency.dismiss();
                    }
                });

                if (i == 0) {

                    ad.show();
                } else {

                    ad_currency.show();
                }
            }
        });

        return rootView;
    }

}
