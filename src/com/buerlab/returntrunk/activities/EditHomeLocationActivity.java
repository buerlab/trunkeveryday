package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.User;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by teddywu on 14-6-17.
 */
public class EditHomeLocationActivity extends EditProfileBaseActivity {
    private static final String TAG = "EditHomeLocationActivity" ;
    ActionBar mActionBar;

    EditText mLocationEdit ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_home_location);
        init();
        setActionBarLayout(R.layout.actionbar,"编辑常住地" );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        mLocationEdit = (EditText)findViewById(R.id.edit_homeLocation);
        mLocationEdit.setText(User.getInstance().homeLocation);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    public void onOptionSave(MenuItem i)
    {
        final String location = mLocationEdit.getText().toString();
        if(location.length()==0){
            Toast toast = Toast.makeText(getApplicationContext(), "请输入你的常住地", 2);
            toast.show();
            return;
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("homeLocation", location );
        NetService service = new NetService(this);
        final Activity self = this;
        service.setUserData(map, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().homeLocation = location;
                    DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
                    EventCenter.shared().dispatch(evt);
                    finish();
                }
                else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });
    }





}