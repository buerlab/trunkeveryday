package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;

/**
 * Created by teddywu on 14-7-3.
 */
public class StarsView extends LinearLayout {
    ImageView star1;
    ImageView star2;
    ImageView star3;

    public StarsView(Context context) {
        super(context);
    }

    public StarsView(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stars_view, this);
        init();
    }

    private void init(){
        star1 = (ImageView)findViewById(R.id.star1);
        star2 = (ImageView)findViewById(R.id.star2);
        star3 = (ImageView)findViewById(R.id.star3);
    }
    public void setStar(double num){
        if(num<0){
            return;
        }else if(num <0.5){
            star1.setImageResource(R.drawable.xingxing1);
            star2.setImageResource(R.drawable.xingxing1);
            star3.setImageResource(R.drawable.xingxing1);
        }
        else if(num <1){
            star1.setImageResource(R.drawable.xingxing3);
            star2.setImageResource(R.drawable.xingxing1);
            star3.setImageResource(R.drawable.xingxing1);
        }else if(num < 1.5){
            star1.setImageResource(R.drawable.xingxing2);
            star2.setImageResource(R.drawable.xingxing1);
            star3.setImageResource(R.drawable.xingxing1);
        }else if(num < 2){
            star1.setImageResource(R.drawable.xingxing2);
            star2.setImageResource(R.drawable.xingxing3);
            star3.setImageResource(R.drawable.xingxing1);
        }else if(num<2.5){
            star1.setImageResource(R.drawable.xingxing2);
            star2.setImageResource(R.drawable.xingxing2);
            star3.setImageResource(R.drawable.xingxing1);
        }else if(num<3){
            star1.setImageResource(R.drawable.xingxing2);
            star2.setImageResource(R.drawable.xingxing2);
            star3.setImageResource(R.drawable.xingxing3);
        }else if(num==3){
            star1.setImageResource(R.drawable.xingxing2);
            star2.setImageResource(R.drawable.xingxing2);
            star3.setImageResource(R.drawable.xingxing2);
        }
    }

    public void setSize(int dp){
        int px =Utils.dip2px(dp);
        LayoutParams params = new LayoutParams(px,px);
        star1.setLayoutParams(params);
        star2.setLayoutParams(params);
        star3.setLayoutParams(params);
    }
}
