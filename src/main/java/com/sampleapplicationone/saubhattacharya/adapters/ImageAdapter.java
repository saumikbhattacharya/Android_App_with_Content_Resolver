package com.sampleapplicationone.saubhattacharya.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sampleapplicationone.saubhattacharya.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter{

    ArrayList<String> imageFilePath = new ArrayList<>();
    Context context;

    public ImageAdapter(Context con, ArrayList<String> filePath)
    {
        super();
        context = con;
        imageFilePath = filePath;
    }
    @Override
    public int getCount() {
        return imageFilePath.size();
    }

    @Override
    public Object getItem(int i) {
        return imageFilePath.get(i);
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
            vi = inflater.inflate(R.layout.each_grid_item, viewGroup, false);

        ImageView imageview_grid = (ImageView)vi.findViewById(R.id.imageview_grid);
        Glide.with(context).load(imageFilePath.get(i)).centerCrop().into(imageview_grid);

        //Picasso.with(context).load(imageFilePath.get(i)).centerCrop().resize(50,50).into(imageview_grid);
        //Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath.get(i));
        //imageview_grid.setImageBitmap(bitmap);

        return vi;
    }
}
