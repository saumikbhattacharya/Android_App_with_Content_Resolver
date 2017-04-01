package com.sampleapplicationone.saubhattacharya.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.sampleapplicationone.saubhattacharya.R;

public class NavigationAdapter extends BaseAdapter{

    Context context;
    String[] nav_drawer_icon = new String[10];
    String PackageName;

    public NavigationAdapter(Context con, String[] icon, String pckg_name)
    {
        super();
        nav_drawer_icon = icon;
        context = con;
        PackageName = pckg_name;
    }

    @Override
    public int getCount() {
        return nav_drawer_icon.length;
    }

    @Override
    public Object getItem(int i) {
        return nav_drawer_icon[i];
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            vi = inflater.inflate(R.layout.each_nav_drawer_icon, viewGroup, false);

        ImageView imageView = (ImageView)vi.findViewById(R.id.each_nav_drawer_icon_img);
        int res_id = vi.getResources().getIdentifier(nav_drawer_icon[i],"drawable",PackageName);
        imageView.setImageDrawable(vi.getResources().getDrawable(res_id,null));

        return vi;
    }
}
