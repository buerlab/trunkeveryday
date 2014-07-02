package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.buerlab.returntrunk.MultiPicSelector.ImgCallBack;
import com.buerlab.returntrunk.MultiPicSelector.ImgsActivity;
import com.buerlab.returntrunk.MultiPicSelector.Util;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;

import java.util.ArrayList;
import java.util.List;

public class TrunkPicGridAdapter extends BaseAdapter {

	Context context;
	List<String> data;
	public Bitmap bitmaps[];
	Util util;
	OnItemClickClass onItemClickClass;
	private int index=-1;

	List<View> holderlist;
	public TrunkPicGridAdapter(Context context, List<String> data, OnItemClickClass onItemClickClass) {
		this.context=context;
		this.data=data;
		this.onItemClickClass=onItemClickClass;
		bitmaps=new Bitmap[data.size()];
		util=new Util(context);
		holderlist=new ArrayList<View>();
	}
	
	@Override
	public int getCount() {
        if(data.size()==ImgsActivity.MAX_NUM){
            return data.size();
        }else {
            return data.size()+1;
        }

	}

	@Override
	public Object getItem(int arg0) {
        if(arg0 == data.size()){
            return "add";
        }else{
		    return data.get(arg0);
        }
	}

	@Override
	public long getItemId(int arg0) {
        if(arg0 == data.size()){
            return data.size();
        }else{
            return arg0;
        }
	}
	
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		Holder holder;
		if (arg0 != index && arg0 > index) {
            arg1=LayoutInflater.from(context).inflate(R.layout.trunk_pic_item, null);
            holder=new Holder();
            holder.image=(ImageView) arg1.findViewById(R.id.image);
            holder.imageWrapper = (RelativeLayout)arg1.findViewById(R.id.image_wrapper);
            holder.imageAdd = (ImageView)arg1.findViewById(R.id.image_add);
            holder.imageAdd.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            index=arg0;
            int[] size = Utils.getScreenSize();

            int width = (size[0] - Utils.dip2px(30))/3;
            holder.imageWrapper.setLayoutParams(new RelativeLayout.LayoutParams(width,width));
            arg1.setTag(holder);
            holderlist.add(arg1);

		}else {
			holder= (Holder)holderlist.get(arg0).getTag();
			arg1=holderlist.get(arg0);
		}

        if(getItem(arg0)=="add"){
            holder.image.setVisibility(View.GONE);
            holder.imageAdd.setVisibility(View.VISIBLE);
        }else {
            if (bitmaps[arg0] == null) {
                util.imgExcute(holder.image,new ImgClallBackLisner(arg0), data.get(arg0));
            }
            else {
                holder.image.setImageBitmap(bitmaps[arg0]);
            }
            holder.image.setVisibility(View.VISIBLE);
            holder.imageAdd.setVisibility(View.GONE);
        }

		arg1.setOnClickListener(new OnPhotoClick(arg0));
		return arg1;
	}
	
	class Holder{
		ImageView imageAdd;
        ImageView image;
        RelativeLayout imageWrapper;
	}

	public class ImgClallBackLisner implements ImgCallBack{
		int num;
		public ImgClallBackLisner(int num) {
			this.num=num;
		}
		
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			bitmaps[num]=bitmap;
			imageView.setImageBitmap(bitmap);
		}
	}

	public interface OnItemClickClass{
		public void OnItemClick(View v, int Position);
	}
	
	class OnPhotoClick implements OnClickListener{
		int position;
		
		public OnPhotoClick(int position) {
			this.position=position;
		}

		@Override
		public void onClick(View v) {
			if (data!=null && onItemClickClass!=null ) {
				onItemClickClass.OnItemClick(v, position);
			}
		}
	}
	
}
