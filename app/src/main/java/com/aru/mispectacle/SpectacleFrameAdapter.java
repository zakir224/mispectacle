package com.aru.mispectacle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Md Zakir Hossen on 10/9/2015.
 */
public class SpectacleFrameAdapter extends ArrayAdapter {

    View view;
    TextView textView;
    String[] obStrings;
    ImageView imageView;

    public SpectacleFrameAdapter(Context context, int resource, String[] strings) {
        super(context, resource,strings);
        this.obStrings = strings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        view = View.inflate(getContext(),R.layout.spectacle_list_item,null);
//        textView = (TextView) view.findViewById(R.id.tvCategory);
//        textView.setText(obStrings[position]);
        imageView = (ImageView) view.findViewById(R.id.ivSpectacle);

        int id = getContext().getResources().getIdentifier(obStrings[position],"drawable",getContext().getPackageName());
        imageView.setImageDrawable(getContext().getResources().getDrawable(id));
        return view;
    }
}
