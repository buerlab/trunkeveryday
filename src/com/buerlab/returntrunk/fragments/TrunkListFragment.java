package com.buerlab.returntrunk.fragments;

//import android.app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.activities.EditIDNumActivity;
import com.buerlab.returntrunk.activities.GalleryUrlActivity;
import com.buerlab.returntrunk.driver.activities.AddTrunkActivity;
import com.buerlab.returntrunk.adapters.TrunkListAdapter;
import com.buerlab.returntrunk.driver.activities.EditTrunkActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class TrunkListFragment extends BaseFragment implements EventCenter.OnEventListener{
    private static final String TAG =  "TrunkListFragment";
    View mView;
    private TextView tips = null;
    TrunkListAdapter mAdapter;
    ListView mListView;
    NetService service;
    LinearLayout mAddTrunkBtn;
    final TrunkListFragment self = this;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.trunks, container, false);

        init();
        return  mView;
    }

    public void init(){
        tips = (TextView)mView.findViewById(R.id.trunks_frag_tips);
        mListView = (ListView)mView.findViewById(R.id.trunks_list);
        mAdapter =new TrunkListAdapter(getActivity(),
                new ItemOnLongClickListener(),
                new OnPhotoClickClass(),
                new OnSetTrunkClickClass(),
                new OnEditTrunkClickClass()
        );
        mListView.setAdapter(mAdapter);

        mAddTrunkBtn = (LinearLayout)mView.findViewById(R.id.add_trunk_btn);

        mAddTrunkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AddTrunkActivity.class);
                startActivity(intent);
            }
        });
        EventCenter.shared().addEventListener(DataEvent.USER_UPDATE, this);
        service = new NetService(getActivity());
//        initTrunk();
    }


    @Override
    public void onShow(){
        NetService service = new NetService(this.getActivity());
        service.getUserDataWithoutLoading(new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if (result.code == NetProtocol.SUCCESS && result.data != null) {
                    User.getInstance().initUser(result.data);
                    //注册用户初始化事件，用于个人资料得以初始化数据
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE, null);
                    EventCenter.shared().dispatch(evt);
                }
            }
        });

    }

    @Override
    public void onEventCall(DataEvent e) {
        if(User.getInstance().trunks!=null && !User.getInstance().trunks.isEmpty()){
            mAdapter.setTrunks(User.getInstance().trunks);
            tips.setAlpha(0.0f);
        }
    }

    class ItemOnLongClickListener implements View.OnLongClickListener{

        @Override
        public boolean onLongClick(View v) {
//            Utils.showToast(getActivity(),"long click");
            showOpSelectDialog(getActivity(), v);
            return false;
        }
    }

    class OnPhotoClickClass implements TrunkListAdapter.OnPhotoClickClass{

        @Override
        public void OnItemClick(View v, int pos,Trunk trunk) {
            String[] urls = new String[trunk.trunkPicFilePaths.size()];
            try {
                for (int i=0;i<urls.length;i++){
                    String[] path = trunk.trunkPicFilePaths.get(i).split("/");
                    urls[i] = getString(R.string.server_addr2);
                    for(int j =0;j<path.length;j++){
                        if(!path[j].isEmpty()){
                            urls[i] += "/"+ java.net.URLEncoder.encode(path[j],"utf-8");
                        }
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putInt("position", pos);
                bundle.putStringArray("urls",urls);
                Intent intent = new Intent(getActivity(),GalleryUrlActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (UnsupportedEncodingException e){
                Log.e(TAG, e.toString());
            }

        }
    }

    class OnSetTrunkClickClass implements TrunkListAdapter.OnSetTrunkClickClass{

        @Override
        public void OnItemClick(View v, int Position, Trunk trunk) {
            useTrunk(trunk.lisencePlate,Position);
        }
    }


    class OnEditTrunkClickClass implements TrunkListAdapter.OnEditTrunkClickClass{

        @Override
        public void OnItemClick(View v, int Position, Trunk trunk) {
//            Utils.showToast(self.getActivity(),"OnEditTrunkClickClass");
            Intent intent = new Intent(self.getActivity(), EditTrunkActivity.class);
            intent.putExtra("trunk",trunk);
            startActivity(intent);
        }
    }

    private void showOpSelectDialog(final Activity c, final View v) {

        TrunkListAdapter.ViewHolder vh = (TrunkListAdapter.ViewHolder)v.getTag();
        final String license = vh.licensePlateTxtView.getText().toString();
        final int position = vh.position;
        String[] items;

        items = new String[] {"删除" };
        new AlertDialog.Builder(c)
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                deleteTrunk(license);
                                break;
                        }
                    }
                }).show();

    }

    private void deleteTrunk(String licensePlate){

        if (mAdapter.getCount()<=1){
            Utils.showToast(getActivity(),"至少要保留一辆货车");
            return;
        }
        service.deleteTrunk(licensePlate,new NetService.NetCallBack(){

            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS && result.arrayData!=null){
                    List<Trunk> trunks = User.extractTrunk(result.arrayData);
                    mAdapter.setTrunks(trunks);
                    Utils.showToast(getActivity(),"删除成功");
                }else{
                    Utils.defaultNetProAction(getActivity(), result);
                }
            }
        });
    }

    private void useTrunk(String licensePlate,int pos){
        if(((Trunk)mAdapter.getItem(pos)).isUsed){
            Utils.showToast(getActivity(),"此车已经是当前车辆了");
            return;
        }

        service.useTrunk(licensePlate, new NetService.NetCallBack() {

            @Override
            public void onCall(NetProtocol result) {
                if (result.code == NetProtocol.SUCCESS && result.arrayData != null) {
                    List<Trunk> trunks = User.extractTrunk(result.arrayData);
                    mAdapter.setTrunks(trunks);
                    Utils.showToast(getActivity(), "修改成功");
                } else {
                    Utils.defaultNetProAction(getActivity(), result);
                }
            }
        });

    }


}