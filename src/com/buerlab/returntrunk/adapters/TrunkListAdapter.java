package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.Trunk;
import com.buerlab.returntrunk.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class TrunkListAdapter extends BaseAdapter {

    private List<Trunk> mTrunk = new ArrayList<Trunk>();
    private LayoutInflater mInflater = null;
    private View.OnLongClickListener mOnLongClickListener;
    public TrunkListAdapter(Context context, View.OnLongClickListener onLongClickListener){
        mInflater = LayoutInflater.from(context);
        mOnLongClickListener = onLongClickListener;
    }

    public void setTrunks(List<Trunk> _trunkList){
        mTrunk = _trunkList;
        notifyDataSetChanged();
    }

    public void deleteTrunk(int position){
        mTrunk.remove(position);
        notifyDataSetChanged();
    }
    public void addTrunk(Trunk trunk){
        mTrunk.add(trunk);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mTrunk.size();
    }

    @Override
    public Object getItem(int position){
        return mTrunk.get(position);
    }

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        Trunk trunk = mTrunk.get(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.trunk_item, null);
            holder = new ViewHolder();
                /*得到各个控件的对象*/
            holder.licensePlateTxtView = (TextView) convertView.findViewById(R.id.licensePlate);
            holder.typeTxtView = (TextView) convertView.findViewById(R.id.type);
            holder.loadTxtView = (TextView) convertView.findViewById(R.id.load);
            holder.lengthTxtView = (TextView) convertView.findViewById(R.id.length);
            holder.picGridLayout = (GridLayout)convertView.findViewById(R.id.pic_grid_layout);
            holder.position = position;

            holder.isVerified =Integer.parseInt(trunk.trunkLicenseVerified);
            holder.isUsedTxtView = (TextView) convertView.findViewById(R.id.isUsed);
            convertView.setTag(holder); //绑定ViewHolder对象
            convertView.setOnLongClickListener(mOnLongClickListener);

        }else{
            holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
        }

        holder.licensePlateTxtView.setText(trunk.lisencePlate);
        holder.typeTxtView.setText(trunk.type);
        holder.loadTxtView.setText(String.valueOf(trunk.load));
        holder.lengthTxtView.setText(String.valueOf(trunk.length));
        holder.position = position;

        if(trunk.isUsed){
            holder.isUsedTxtView.setVisibility(View.VISIBLE);
        }else{
            holder.isUsedTxtView.setVisibility(View.GONE);
        }
        return convertView;
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView licensePlateTxtView;
        public TextView typeTxtView;
        public TextView loadTxtView;
        public TextView lengthTxtView;
        public GridLayout picGridLayout;
        public TextView isUsedTxtView;
        public int position;
        public int isVerified;
    }

}
