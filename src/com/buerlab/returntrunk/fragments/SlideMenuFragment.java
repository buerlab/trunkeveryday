package com.buerlab.returntrunk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import com.buerlab.returntrunk.R;

/**
 * Created by zhongqiling on 14-6-17.
 */
public class SlideMenuFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.slide_menu_frag, container, false);
        ListView menulist = (ListView)view.findViewById(R.id.slide_menu_list);
        // For the cursor adapter, specify which columns go into which views
        String[] fromColumns = getResources().getStringArray(R.array.slide_menu);
        int[] toViews = {android.R.id.text1};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1,
                                                              null, fromColumns, toViews);
        menulist.setAdapter(adapter);
        return view;
    }
}