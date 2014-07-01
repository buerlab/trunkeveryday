package com.buerlab.returntrunk.owner.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.fragments.BaseFragment;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class OwnerHomeFragment extends BaseFragment {

    private  List<Integer> fragtaglist = Arrays.asList(R.id.owner_home_find_trunk_frag, R.id.owner_home_send_goods_frag);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.owner_home_frag, container, false);

        Button sendbtn = (Button)view.findViewById(R.id.bottom_send_btn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeFrag(0);
            }
        });

        Button findBtn = (Button)view.findViewById(R.id.bottom_list_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHomeFrag(1);
            }
        });

        setHomeFrag(0);

        return view;
    }

    private void setHomeFrag(int i){

        execSetFrag(i, fragtaglist);
    }

    private void execSetFrag(int index, List<Integer> tags){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < tags.size(); i++){
            Fragment fragment = manager.findFragmentById(tags.get(i));
            if(fragment !=null){
                if(i == index){
                    transaction.show(fragment);
                }else{
                    transaction.hide(fragment);
                }
            }
        }
        transaction.commit();
    }
}