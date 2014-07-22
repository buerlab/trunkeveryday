package com.buerlab.returntrunk;

import android.content.Context;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.*;

/**
 * Created by zhongqiling on 14-6-24.
 */
public class AssetManager {

    public JSONObject regionDict = null;
    public JSONArray regionsList = null;

    private String regionStr = "";

    private Context mContext = null;
    public boolean hasInit = false;

    private List<String> provList = null;
    private Map<String, List<String>> citiesMap = null;
    private Map<String, List<String>> regionsMap = null;

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
            InputStream in = mContext.getResources().getAssets().open("region");
            int length = in.available();
            byte[] buffer = new byte[length];
            in.read(buffer);
            in.close();
            regionStr = EncodingUtils.getString(buffer, "UTF-8");
//            regionDict = new JSONObject(regionStr);
            regionsList = new JSONArray(regionStr);
            hasInit = true;

            citiesMap = new HashMap<String, List<String>>();
            regionsMap = new HashMap<String, List<String>>();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getProvList(){
        if(provList == null){
            provList = new ArrayList<String>();
            for(int i = 0; i < regionsList.length(); i++)
                try{
                    provList.add(regionsList.getJSONObject(i).getString("provName"));
                }catch (JSONException e){
                    continue;
                }
        }
        return provList;
    }

    public String[] getProvinces(){

        return listToArray(getProvList());
    }

    public List<String> getCitiesList(String prov){
        if(!citiesMap.containsKey(prov)){

            int provIndex = provList.indexOf(prov);
            try{
                List<String> cities = Arrays.asList(extract(regionsList.getJSONObject(provIndex).getJSONArray("cities"), "cityName"));
                citiesMap.put(prov, cities);
            }catch (JSONException e){
                return new ArrayList<String>();
            }

        }
        return citiesMap.get(prov);
    }

    public String[] getCities(String prov){
        return listToArray(getCitiesList(prov));

//        try{
////            return extractKey(regionDict.getJSONObject(prov));
//            int provIndex = provList.indexOf(prov);
//            return extract(regionsList.getJSONObject(provIndex).getJSONArray("cities"), "cityName");
//        }catch (Exception e){
//            return null;
//        }
    }

    public List<String> getReigionList(String prov, String city){
        if(!regionsMap.containsKey(city)){
            int provIndex = getProvList().indexOf(prov);
            int cityIndex = getCitiesList(prov).indexOf(city);
            try{
                List<String> regions = Arrays.asList(extract(regionsList.getJSONObject(provIndex).getJSONArray("cities").
                        getJSONObject(cityIndex).getJSONArray("regions"), "regionName"));
                regionsMap.put(city, regions);
            }catch (JSONException e){
                return new ArrayList<String>();
            }

        }
        return regionsMap.get(city);
    }

    public String[] getRegions(String prov, String city){
        return listToArray(getReigionList(prov, city));
//        try{
//            if(!regionsMap.containsKey(city)){
//                int provIndex = provList.indexOf(prov);
//
//            }
//
////            return extractKey(regionDict.getJSONObject(prov).getJSONObject(city));
//        }catch (Exception e){
//            return null;
//        }
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

    public String[] extract(JSONArray array, String attr){
        String[] result = new String[array.length()];
        for(int i = 0; i < array.length(); i++){
            try{
                result[i] = array.getJSONObject(i).getString(attr);
            }catch (JSONException e){
                continue;
            }
        }
        return result;
    }

    public String[] listToArray(List<String> list){
        String[] result = new String[list.size()];
        for(int i = 0; i < list.size(); i++){
            result[i] = list.get(i);
        }
        return result;
    }

}
