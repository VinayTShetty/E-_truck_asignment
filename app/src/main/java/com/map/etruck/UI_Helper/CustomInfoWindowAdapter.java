package com.map.etruck.UI_Helper;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.map.etruck.R;

public class CustomInfoWindowAdapter  implements GoogleMap.InfoWindowAdapter {
    private Activity context;

    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(marker.getTitle());
        return view;
    }
}
