package com.aru.mispectacle.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aru.mispectacle.R;
import com.aru.mispectacle.model.Spectacle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Md Zakir Hossen on 10/9/2015.
 */
public class SpectacleFrameAdapter extends ArrayAdapter {

    private View view;
    private TextView tvSpectacleBrand;
    private TextView tvSpectacleCategory;
    private TextView tvSpectaclePrice;
    private ArrayList<Spectacle> spectacles;
    private ImageView spectacleImageView;

    public SpectacleFrameAdapter(Context context, int resource, ArrayList<Spectacle> spectacles) {
        super(context, resource, spectacles);
        this.spectacles = spectacles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        view = View.inflate(getContext(), R.layout.spectacle_list_item, null);
        tvSpectacleBrand = (TextView) view.findViewById(R.id.tv_sf_list_brand);
        tvSpectacleCategory = (TextView) view.findViewById(R.id.tv_sf_list_category);
        tvSpectaclePrice = (TextView) view.findViewById(R.id.tv_sf_list_price);
        spectacleImageView = (ImageView) view.findViewById(R.id.iv_sp_list_spectacle);

        int id = getContext().getResources().getIdentifier("s4", "drawable", getContext().getPackageName());
        spectacleImageView.setImageDrawable(getContext().getResources().getDrawable(id));
        tvSpectacleBrand.setText(spectacles.get(position).getSpectacleBrand());
        tvSpectacleCategory.setText(spectacles.get(position).getSpectacleCategoryId() + "");
        tvSpectaclePrice.setText(spectacles.get(position).getSpectaclePrice() + "");


        return view;
    }
}
