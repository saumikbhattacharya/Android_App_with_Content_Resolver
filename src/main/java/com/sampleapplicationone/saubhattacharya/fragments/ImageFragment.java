package com.sampleapplicationone.saubhattacharya.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.sampleapplicationone.saubhattacharya.adapters.ImageAdapter;
import com.sampleapplicationone.saubhattacharya.R;

import java.util.ArrayList;

public class ImageFragment extends Fragment
{
    ArrayList<String> image_list = new ArrayList<>();
    GridView image_gridview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_layout_images, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        Bundle bundle = getArguments();
        image_list = (ArrayList<String>) bundle.getSerializable("image_list");

        ImageAdapter baseAdapter = new ImageAdapter(getActivity(),image_list);
        image_gridview = (GridView)view.findViewById(R.id.image_grid);
        image_gridview.setAdapter(baseAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem action_show_only_number = menu.findItem(R.id.action_show_only_number);
        action_show_only_number.setVisible(false);
        MenuItem action_show_only_email = menu.findItem(R.id.action_show_only_email);
        action_show_only_email.setVisible(false);
        MenuItem action_show_hidden_contacts = menu.findItem(R.id.action_show_hidden_contacts);
        action_show_hidden_contacts.setVisible(false);
    }
}
