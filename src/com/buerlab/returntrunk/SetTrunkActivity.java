package com.buerlab.returntrunk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by zhongqiling on 14-6-11.
 */
public class SetTrunkActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.set_trunk_activity);

        final Activity self = this;
        Button confirmBtn = (Button)findViewById(R.id.set_trunk_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText typeText = (EditText)self.findViewById(R.id.set_trunk_type);
                String type =typeText.getText().toString();
                EditText lengthText = (EditText)self.findViewById(R.id.set_trunk_length);
                float length = Float.valueOf(lengthText.getText().toString());
                EditText loadText = (EditText)self.findViewById(R.id.set_trunk_load);
                float load = Float.valueOf(loadText.getText().toString());
                EditText lisenceText = (EditText)self.findViewById(R.id.set_trunk_licensePlate);
                String listence = lisenceText.getText().toString();
                final Trunk trunk = new Trunk(type, length, load, listence);

                NetService service = new NetService(self);
                service.addUserTrunk(trunk, new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if(result.code == NetProtocol.SUCCESS){
                            User.getInstance().addTrunk(trunk);

                            Toast toast = Toast.makeText(self, "添加货车成功！", 2);
                            toast.show();

                            Utils.safeSwitchToMainActivity(self);
                        }else{
                            Utils.defaultNetProAction(self, result);
                        }
                    }
                });
            }
        });
    }
}