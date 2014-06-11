package com.buerlab.returntrunk;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends Activity{

    private int currFrag = -1;
    private List<String> fragsList = Arrays.asList("sendbill", "findbill","setting");

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button sendbtn = (Button)findViewById(R.id.bottom_send_btn);
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrFrag(0);
            }
        });

        Button findBtn = (Button)findViewById(R.id.bottom_list_btn);
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrFrag(1);
            }
        });

        Button mineBtn = (Button)findViewById(R.id.bottom_mine_btn);
        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrFrag(2);
            }
        });

        setCurrFrag(0);
    }

    private void setCurrFrag(int index){
        if(currFrag == index)
            return;

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        for(int i = 0; i < fragsList.size(); i++){
            Fragment fragment = manager.findFragmentByTag(fragsList.get(i));
            if(i == index){
                transaction.show(fragment);
            }else{
                transaction.hide(fragment);
            }
        }
        transaction.commit();
        currFrag = index;
    }

}
