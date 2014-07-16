package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class TrunkListAdapter extends BaseAdapter {

    private List<Trunk> mTrunk = new ArrayList<Trunk>();
    private LayoutInflater mInflater = null;
    private View.OnLongClickListener mOnLongClickListener;
    private Context mContext;
    OnPhotoClickClass onPhotoClickClass;
    OnSetTrunkClickClass onSetTrunkClickClass;
    OnEditTrunkClickClass onEditTrunkClickClass;
    public TrunkListAdapter(Context context,
                            View.OnLongClickListener onLongClickListener,
                            OnPhotoClickClass onPhotoClickClass,
                            OnSetTrunkClickClass onSetTrunkClickClass,
                            OnEditTrunkClickClass onEditTrunkClickClass){
        mInflater = LayoutInflater.from(context);
        this.onPhotoClickClass = onPhotoClickClass;
        this.onSetTrunkClickClass = onSetTrunkClickClass;
        this.onEditTrunkClickClass = onEditTrunkClickClass;
        mContext = context;
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
            holder.picGridLayout = (GridLayout)convertView.findViewById(R.id.pic_gridview);
            holder.verifyIcon = (ImageView)convertView.findViewById(R.id.verify_icon);
            holder.verifyText = (TextView)convertView.findViewById(R.id.verify_text);
            holder.position = position;

            holder.isVerified =Integer.parseInt(trunk.trunkLicenseVerified);
            holder.isUsedImageView = (ImageView) convertView.findViewById(R.id.set_current_trunk_btn);
            convertView.setTag(holder); //绑定ViewHolder对象
            convertView.setOnLongClickListener(mOnLongClickListener);
            holder.isUsedImageView.setOnClickListener(new OnSetTrunkClick(position,trunk));

            holder.typeIcon = (ImageView)convertView.findViewById(R.id.type_icon);


        }else{
            holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
        }

        holder.licensePlateTxtView.setText(trunk.lisencePlate);
        holder.typeTxtView.setText(trunk.type);
        double num1 = ((int)(trunk.load*10))/10.0;
        holder.loadTxtView.setText(num1+"吨");
        num1 =  ((int)(trunk.length)*10)/10.0;
        holder.lengthTxtView.setText(num1+ "米");
        holder.position = position;

        int trunkLisenceVerified =  Integer.parseInt(trunk.trunkLicenseVerified);
        switch (trunkLisenceVerified){
            case 0: holder.verifyIcon.setImageResource(R.drawable.qt2_zy);
                    holder.verifyText.setText("未审核");break;
            case 1: holder.verifyIcon.setImageResource(R.drawable.qt_dd);
                    holder.verifyText.setText("审核中");break;
            case 2: holder.verifyIcon.setImageResource(R.drawable.verified);
                    holder.verifyText.setText("通过审核");break;
            case 3: holder.verifyIcon.setImageResource(R.drawable.qt2_wtg);
                    holder.verifyText.setText("审核失败");break;
            default:break;
        }
        if(trunk.isUsed){
            holder.isUsedImageView.setImageResource(R.drawable.sdqc2);
        }else{
            holder.isUsedImageView.setImageResource(R.drawable.sdqc1);
        }

        if(trunk.type.equals("厢车") ){
            holder.typeIcon.setImageResource(R.drawable.che_xc);
        }else if(trunk.type.equals("低栏车") ){
            holder.typeIcon.setImageResource(R.drawable.che_dlc);
        }else if(trunk.type.equals("高栏车") ){
            holder.typeIcon.setImageResource(R.drawable.che_glc);
        }else if(trunk.type.equals("集装车") ){
            holder.typeIcon.setImageResource(R.drawable.che_jzxc);
        }else if(trunk.type.equals("面包车") ){
            holder.typeIcon.setImageResource(R.drawable.che_mbc);
        }else if(trunk.type.equals("平板车") ){
            holder.typeIcon.setImageResource(R.drawable.che_cbc);
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        holder.picGridLayout.removeAllViews();
        int width = (Utils.getScreenSize()[0] - 40)/4;

        if(trunk.trunkPicFilePaths!=null){
            for(int i =0;i<trunk.trunkPicFilePaths.size();i++){
                ImageView iv = new ImageView(mContext);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(0,0,5,5);
                params.width = width;
                params.height = width;
                holder.picGridLayout.addView(iv,params);
                imageLoader.displayImage(mContext.getString(R.string.server_addr2)+ trunk.trunkPicFilePaths.get(i), iv);
                iv.setOnClickListener(new OnPhotoClick(i,trunk));
            }

        }

        convertView.setOnClickListener(new OnEditTrunkClick(position,trunk));

        return convertView;
    }

    public interface OnPhotoClickClass {
        public void OnItemClick(View v, int Position,Trunk trunk);
    }

    class OnPhotoClick implements View.OnClickListener {
        int position;
        Trunk mTrunk;
        public OnPhotoClick(int position,Trunk trunk) {
            this.position=position;
            this.mTrunk = trunk;
        }
        @Override
        public void onClick(View v) {
            onPhotoClickClass.OnItemClick(v, position,mTrunk);
        }

    }

    public interface OnSetTrunkClickClass {
        public void OnItemClick(View v, int Position,Trunk trunk);
    }

    class OnSetTrunkClick implements View.OnClickListener {
        int position;
        Trunk mTrunk;

        public OnSetTrunkClick(int position,Trunk trunk) {
            this.position=position;
            this.mTrunk = trunk;
        }
        @Override
        public void onClick(View v) {
            onSetTrunkClickClass.OnItemClick(v, position,mTrunk);
        }
    }

    public interface OnEditTrunkClickClass {
        public void OnItemClick(View v, int Position,Trunk trunk);
    }

    class OnEditTrunkClick implements View.OnClickListener {
        int position;
        Trunk mTrunk;

        public OnEditTrunkClick(int position,Trunk trunk) {
            this.position=position;
            this.mTrunk = trunk;
        }
        @Override
        public void onClick(View v) {
            onEditTrunkClickClass.OnItemClick(v, position,mTrunk);
        }
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
        public TextView licensePlateTxtView;
        public TextView typeTxtView;
        public TextView loadTxtView;
        public TextView lengthTxtView;
        public ImageView verifyIcon;
        public TextView verifyText;
        public GridLayout picGridLayout;
        public ImageView isUsedImageView;
        public ImageView typeIcon;
        public int position;
        public int isVerified;
    }

}
