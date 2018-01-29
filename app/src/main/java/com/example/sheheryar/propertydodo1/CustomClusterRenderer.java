package com.example.sheheryar.propertydodo1;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by sheheryar on 11/3/2017.
 */

public class CustomClusterRenderer
        extends DefaultClusterRenderer<MyMapItem> {
    Context context;


    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<MyMapItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
    }

    @Override
    protected int getColor(int clusterSize) {
        return Color.RED;
//        return getColor(R.color.cluster_color);
//        return R.color.cluster_color;
    }


    @Override
    protected void onBeforeClusterItemRendered(MyMapItem item, MarkerOptions markerOptions) {
        Bitmap bitmap = createStoreMarker(item.getTitle());
        markerOptions.icon(BitmapDescriptorFactory.
                fromBitmap(bitmap));
    }

    private Bitmap createStoreMarker(String text) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View markerLayout = layoutInflater.inflate(R.layout.markerlayout, null);

        TextView markerRating = (TextView) markerLayout.findViewById(R.id.marker_text);
        markerRating.setText(text);

        markerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        markerLayout.layout(0, 0, markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight());

        final Bitmap bitmap = Bitmap.createBitmap(markerLayout.getMeasuredWidth(), markerLayout.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        markerLayout.draw(canvas);
        return bitmap;
    }
}

