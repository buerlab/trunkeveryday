package com.buerlab.returntrunk.owner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.fragments.BaseFragment;
import com.umeng.analytics.MobclickAgent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class OwnerHomeFragment extends BaseFragment {

    private static final String TAG = "OwnerHomeFragment";

    private boolean hasInit = false;
    TextView tabTextTrunk;
    TextView tabTextGoods;
    ImageView logoTrunk;
    ImageView logoGoods;


    private  List<Integer> fragtaglist = Arrays.asList(R.id.owner_home_find_trunk_frag, R.id.owner_home_send_goods_frag);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.owner_home_frag, container, false);

        LinearLayout sendbtn = (LinearLayout)view.findViewById(R.id.tab_trunk_wrapper);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeFrag(0);
                showTrunkTab();
            }
        });

        LinearLayout findBtn = (LinearLayout)view.findViewById(R.id.tab_goods_wrapper);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeFrag(1);

                showGoodsTab();
            }
        });

        tabTextTrunk =(TextView)view.findViewById(R.id.tab_text_trunk);
        tabTextGoods =(TextView)view.findViewById(R.id.tab_text_goods);
        logoGoods =(ImageView)view.findViewById(R.id.tab_logo_goods);
        logoTrunk =(ImageView)view.findViewById(R.id.tab_logo_trunk);

        showTrunkTab();
        return view;
    }

    @Override
    public void onShow(){
        if(!hasInit){
            setHomeFrag(0);
            hasInit = true;
        }
    }

    private void showTrunkTab(){
        tabTextTrunk.setTextColor(getResources().getColor(R.color.tab_clicked_blue)) ;
        tabTextGoods.setTextColor(getResources().getColor(R.color.tab_gray));
        logoTrunk.setImageResource(R.drawable.tab_che_push);
        logoGoods.setImageResource(R.drawable.tab_huo);
    }

    private void showGoodsTab(){
        tabTextTrunk.setTextColor(getResources().getColor(R.color.tab_gray)) ;
        tabTextGoods.setTextColor(getResources().getColor(R.color.tab_clicked_blue));
        logoTrunk.setImageResource(R.drawable.tab_che);
        logoGoods.setImageResource(R.drawable.tab_huo_push);
    }
    private void setHomeFrag(int i){

        execSetFrag(i, fragtaglist);
    }

    private void execSetFrag(int index, List<Integer> tags){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < tags.size(); i++){
            BaseFragment fragment = (BaseFragment)manager.findFragmentById(tags.get(i));
            if(fragment !=null){
                if(i == index){
                    transaction.show(fragment);
                    fragment.onShow();
                }else{
                    transaction.hide(fragment);
                }
            }
        }
        transaction.commit();
    }
}