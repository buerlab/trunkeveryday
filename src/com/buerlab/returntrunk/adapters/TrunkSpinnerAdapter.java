package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.buerlab.returntrunk.*;

import java.util.List;


/**
 * Created by zhongqiling on 14-6-12.
 */
public class TrunkSpinnerAdapter extends BaseAdapter {

    private List<String> trunks;
    private Context mContext;

    public TrunkSpinnerAdapter(Context context, List<String> _trunks){
        mContext = context;
        trunks = _trunks;
    }

    @Override
    public int getCount(){
        return trunks.size();
    }

    @Override
    public Object getItem(int position){
        return null;
    }

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        String s = trunks.get(position);
        TextView view = convertView == null ? new TextView(mContext) : (TextView)convertView;

        view.setText(s);

        return view;
    }
}
