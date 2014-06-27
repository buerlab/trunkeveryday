package com.buerlab.returntrunk;

import android.content.Context;
import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-24.
 */
public class AssetManager {

    public JSONObject regionDict = null;

    private String regionStr = "";

    private Context mContext = null;
    private boolean hasInit = false;

    static private AssetManager instance = null;
    static public AssetManager shared(){
        if(instance == null){
            instance = new AssetManager();
        }
        return instance;
    }

    public void init(Context context){
        mContext = context;
        try{
            InputStream in = mContext.getResources().getAssets().open("regions");
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            in.close();
            regionStr = EncodingUtils.getString(buffer, "UTF-8");
            regionDict = new JSONObject(regionStr);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] getProvinces(){
        return extractKey(regionDict);
    }

    public String[] getCities(String prov){
        try{
            return extractKey(regionDict.getJSONObject(prov));
        }catch (Exception e){
            return null;
        }
    }

    public String[] getRegions(String prov, String city){
        try{
            return extractKey(regionDict.getJSONObject(prov).getJSONObject(city));
        }catch (Exception e){
            return null;
        }
    }

    public String[] extractKey(JSONObject object){
        String[] result = new String[object.length()];
        Iterator it = object.keys();
        int i = 0;
        while (it.hasNext()){
            result[i++] = (String)it.next();
        }
        return result;
    }

}
