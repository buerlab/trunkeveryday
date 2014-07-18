package com.buerlab.returntrunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.buerlab.returntrunk.AssetManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.models.Address;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-26.
 */
public class PickAddrView extends LinearLayout {

    public interface OnAddrListener{
        //addr is a list contains 3 elements, prov,city,region in order.
        public void OnAddrChanged(List<String> addr);
    }

    public WheelView provWheel = null;
    public WheelView cityWheel = null;
    public WheelView regionWheel = null;
//    public LinearLayout regWheelContainer = null;

    private OnAddrListener mListener = null;

    private Context mContext = null;
    private boolean provScrolling = false;
    private boolean cityScroling = false;
    private boolean regScrolling = false;
    private Address mAddr = null;

    public PickAddrView(final Context context){
        super(context);

        init(context);
    }
    public PickAddrView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setAddr(String addrString){
        mAddr = new Address(addrString);
        List<String> addrList = mAddr.getAddrList();
        if(addrList.size() >= 2){
            List<String> provs = Arrays.asList(AssetManager.shared().getProvinces());
            String provName = addrList.get(0);
            int index = provs.indexOf(provName);
            if(index >= 0)
                provWheel.setCurrentItem(index);

            List<String> cities = Arrays.asList(AssetManager.shared().getCities(provName));
            String cityName = addrList.get(1);
            index = cities.indexOf(cityName);
            if(index >= 0)
                cityWheel.setCurrentItem(index);

            if(addrList.size() > 2){
                List<String> regions = Arrays.asList(AssetManager.shared().getRegions(provName, cityName));
                String regionName = addrList.get(2);
                index = regions.indexOf(regionName);
                if(index >= 0){
                    regionWheel.setCurrentItem(index);
                    regionWheel.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private  void init(Context context){
        mContext = context;

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (View)inflater.inflate(R.layout.pick_addr_view, this);
        final String[] provs = AssetManager.shared().getProvinces();

        ArrayWheelAdapter provAdapter = new ArrayWheelAdapter(context, provs);
        provWheel = (WheelView)view.findViewById(R.id.pick_addr_prov);
        provWheel.setVisibleItems(6);
        provWheel.setViewAdapter(provAdapter);

        cityWheel = (WheelView)view.findViewById(R.id.pick_addr_city);
        cityWheel.setVisibleItems(6);
        cityWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, AssetManager.shared().getCities(provs[0])));

        String[] cities = AssetManager.shared().getCities(provs[0]);
        String[] regions = AssetManager.shared().getRegions(provs[0], cities[0]);

        regionWheel = (WheelView)view.findViewById(R.id.pick_addr_reg);
        regionWheel.setVisibleItems(6);
        regionWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {}

            @Override
            public void onScrollingFinished(WheelView wheel) {
                if(mListener != null){
                    mListener.OnAddrChanged(getCurrAddr());
                }
            }
        });
        if(regions != null && regions.length > 0)
        {
            regionWheel.setVisibility(VISIBLE);
//            LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            regionWheel = createRegView(new ArrayWheelAdapter(context, regions));
//            regWheelContainer.addView(regionWheel, parms);
        }else {
            regionWheel.setVisibility(GONE);
        }

        provWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!provScrolling){
                    updateCity();
                    updateReg();
                }
            }
        });

        provWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                provScrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                provScrolling = false;
                updateCity();
                updateReg();
            }
        });

        cityWheel.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if(!cityScroling) {
                    updateReg();
                }
            }
        });

        cityWheel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                cityScroling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                cityScroling = false;
                updateReg();
            }
        });

    }


    private WheelView createRegView(ArrayWheelAdapter<String> adapter){
        WheelView view = createWheelView(mContext, adapter);

        view.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {}

            @Override
            public void onScrollingFinished(WheelView wheel) {
                if(mListener != null){
                    mListener.OnAddrChanged(getCurrAddr());
                }
            }
        });
        return view;
    }


    public void setListener(OnAddrListener listener){
        mListener = listener;
        //第一次读地址
        if(mListener != null){
            mListener.OnAddrChanged(getCurrAddr());
        }

    }

    private void updateCity(){
        String currProv = AssetManager.shared().getProvinces()[provWheel.getCurrentItem()];
        String[] cities = AssetManager.shared().getCities(currProv);
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext, cities);
        cityWheel.setViewAdapter(adapter);
        cityWheel.setCurrentItem(0);
    }

    public void updateReg(){
        String currProv = AssetManager.shared().getProvinces()[provWheel.getCurrentItem()];
        String currCity = AssetManager.shared().getCities(currProv)[cityWheel.getCurrentItem()];
        String[] regions = AssetManager.shared().getRegions(currProv, currCity);
        if(regions != null && regions.length > 0){
            ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(mContext, regions);
            if(regionWheel != null){
                regionWheel.setViewAdapter(adapter);
                regionWheel.setVisibility(VISIBLE);
            }
            else{
                regionWheel.setVisibility(GONE);
//                LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                regionWheel = createRegView(adapter);

//                regWheelContainer.addView(regionWheel, parms);
            }
            regionWheel.setCurrentItem(0);

        }else if(regionWheel != null){
            regionWheel.setVisibility(GONE);
//            regWheelContainer.removeView(regionWheel);
//            regionWheel = null;
        }

        if(mListener != null){
            mListener.OnAddrChanged(getCurrAddr());
        }
    }

    private List<String> getCurrAddr(){
        List<String> addr = new ArrayList<String>();
        String prov = AssetManager.shared().getProvinces()[provWheel.getCurrentItem()];
        String[] cities = AssetManager.shared().getCities(prov);
        String city = cities[cityWheel.getCurrentItem()];
        addr.add(prov);
        addr.add(city);
        String[] regions = AssetManager.shared().getRegions(prov, city);
        if(regionWheel != null && regions.length > 0){
            addr.add(regions[regionWheel.getCurrentItem()]);
        }
        return addr;
    }

    private WheelView createWheelView(Context context, ArrayWheelAdapter adapter){
        WheelView wheelView = new WheelView(context);
        wheelView.setViewAdapter(adapter);
        wheelView.setVisibleItems(6);
        return wheelView;
    }
}
