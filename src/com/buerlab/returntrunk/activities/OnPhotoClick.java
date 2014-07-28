package com.buerlab.returntrunk.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.buerlab.returntrunk.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by teddywu on 14-7-25.
 */
public class OnPhotoClick implements View.OnClickListener {
    private final static String TAG = "OnPhotoClick";
    int position;
    ArrayList<String> trunkPicFilePaths;
    Context mContext;
    public OnPhotoClick(Context context, int position,ArrayList<String> trunkPicFilePaths) {
        mContext = context;
        this.position=position;
        this.trunkPicFilePaths = trunkPicFilePaths;
    }

    @Override
    public void onClick(View v) {
        String[] urls = new String[trunkPicFilePaths.size()];
        try {
            for (int i=0;i<urls.length;i++){
                String[] path = trunkPicFilePaths.get(i).split("/");
                urls[i] =  mContext.getString(R.string.server_addr2);
                for(int j =0;j<path.length;j++){
                    if(path[j] !=null && path[j].length()>0){
                        urls[i] += "/"+ java.net.URLEncoder.encode(path[j],"utf-8");
                    }
                }
            }

            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putStringArray("urls",urls);
            Intent intent = new Intent(mContext,GalleryUrlActivity.class);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }catch (UnsupportedEncodingException e){
            Log.e(TAG, e.toString());
        }
    }
}

