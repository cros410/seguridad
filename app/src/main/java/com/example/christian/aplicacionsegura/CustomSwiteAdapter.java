package com.example.christian.aplicacionsegura;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Christian on 18/05/2017.
 */

public class CustomSwiteAdapter extends PagerAdapter {

    private int[] images_resources= {R.drawable.ic_celular ,R.drawable.ic_audiotrack, R.drawable.ic_estadisticas };
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomSwiteAdapter(Context context) {
        this.context = context;
    }



    @Override
    public int getCount() {
        return images_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == (LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.tipo_swipe, container , false);
        ImageView imageView = (ImageView) item_view.findViewById(R.id.tipo_swipe);
        imageView.setImageResource(images_resources[position]);
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
