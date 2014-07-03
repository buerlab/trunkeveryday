package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;

/**
 * Created by teddywu on 14-7-3.
 */
public class StarsView extends LinearLayout {
    ImageView star1;
    ImageView star2;
    ImageView star3;
    TextView numView;

    public StarsView(Context context) {
        super(context);
    }

    public StarsView(Context context,AttributeSet attrs){
        super(context,attrs);
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.stars_view, this);
        init();
    }

    private void init(){
        star1 = (ImageView)findViewById(R.id.star1);
        star2 = (ImageView)findViewById(R.id.star2);
        star3 = (ImageView)findViewById(R.id.star3);
        numView = (TextView)findViewById(R.id.num);
    }
    public void setStar(float num){
        if(num<0){
            return;
        }else if(num <1){
            star1.setImageResource(R.drawable.ic_action_back);
            star2.setImageResource(R.drawable.ic_action_back);
            star2.setImageResource(R.drawable.ic_action_back);
        }else if(num < 2){
            star1.setImageResource(R.drawable.ic_launcher);
            star2.setImageResource(R.drawable.ic_action_back);
            star3.setImageResource(R.drawable.ic_action_back);
        }else if(num<3){
            star1.setImageResource(R.drawable.ic_launcher);
            star2.setImageResource(R.drawable.ic_launcher);
            star3.setImageResource(R.drawable.ic_action_back);
        }else if(num==3){
            star1.setImageResource(R.drawable.ic_launcher);
            star2.setImageResource(R.drawable.ic_launcher);
            star3.setImageResource(R.drawable.ic_launcher);
        }

        double num1 = ((int)(num*10))/10.0;
        numView.setText(String.valueOf(num1));
    }
}
