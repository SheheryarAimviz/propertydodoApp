package com.example.sheheryar.propertydodo1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.id.list;

public class PropertiesListViewAdapter1 extends BaseAdapter{

    private LayoutInflater layoutInflater;
    private List<PropertyList> propertyLists;
    // seacrhing
    private List<PropertyList> searchList;

    public PropertiesListViewAdapter1(Context  context, List<PropertyList> objects) {

        layoutInflater = LayoutInflater.from(context);
        this.propertyLists = objects;

        // seacrhing
        this.searchList = new ArrayList<>();
        this.searchList.addAll(propertyLists);
    }

    @Override
    public int getCount() {
        return propertyLists.size();
    }

    @Override
    public Object getItem(int position) {
        return propertyLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.property_listview,null);

        TextView price = (TextView) convertView.findViewById(R.id.price);
        TextView rent_type = (TextView) convertView.findViewById(R.id.rent_type);
        TextView category = (TextView) convertView.findViewById(R.id.category);
        TextView bedrooms = (TextView) convertView.findViewById(R.id.bedrooms);
        TextView bathrooms = (TextView) convertView.findViewById(R.id.bathrooms);
        TextView country = (TextView) convertView.findViewById(R.id.country);
        TextView state = (TextView) convertView.findViewById(R.id.state);
        TextView city = (TextView) convertView.findViewById(R.id.city);
        TextView status = (TextView) convertView.findViewById(R.id.status);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);

        String image =propertyLists.get(position).getImage();

        if (image == "null")
        {
            imageView.setImageResource(R.drawable.house);
        }
        else {

            Picasso.with(convertView.getContext())
                    .load(propertyLists.get(position).getImage())
                    .into(imageView);
        }
        String _status = propertyLists.get(position).getStatus();
        if (_status == "null")
        {
            status.setText("");
        }
        else
        {
            if (image == "null")
            {
                status.setText("VERIFIED LISTING");
                status.setBackgroundColor(R.color.green);
            }
            else {
                status.setText("VERIFIED LISTING");
            }
        }

        price.setText(propertyLists.get(position).getPrice());
        rent_type.setText(propertyLists.get(position).getRent_type());
        category.setText(propertyLists.get(position).getCategory());
        bedrooms.setText(propertyLists.get(position).getBedrooms());
        bathrooms.setText(propertyLists.get(position).getBathrooms());
        country.setText(propertyLists.get(position).getCountry());
        state.setText(propertyLists.get(position).getState());
        city.setText(propertyLists.get(position).getCity());

        return convertView;
    }

    // Filter method
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        propertyLists.clear();
        if (charText.length() == 0) {
            propertyLists.addAll(searchList);
        }
        notifyDataSetChanged();
    }
}
