package com.example.sheheryar.propertydodo1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.ViewHolder> {
    public List<PropertyList> mDataset;

    private RecyclerView mRecyclerView;
    private ArrayList<PropertyList> fareList;

    private boolean isAvailable;

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTextView;
        public ImageView imageView;
        public ViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            mTextView = (TextView) v.findViewById(R.id.amenities);
            imageView = (ImageView) v.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {


        }

        public int getIndexBySeat(String Seat)
        {
            return -1;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AmenitiesAdapter(List<PropertyList> myDataset) {
        mDataset = myDataset;
        isAvailable = true;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AmenitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.amenities_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText("  "+mDataset.get(position).getAmenities());
        holder.imageView.setImageResource(R.drawable.tick1);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}