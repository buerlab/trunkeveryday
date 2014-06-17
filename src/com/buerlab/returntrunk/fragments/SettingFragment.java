package com.buerlab.returntrunk.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class SettingFragment extends Fragment implements NewTrunkDialog.NewTrunkDialogListener{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.setting_frag, container, false);
    }

    public void init(){
        View view = getView();

        ViewGroup trunksContainer = (ViewGroup)view.findViewById(R.id.setting_trunks);
        for(Trunk trunk : User.getInstance().trunks){
            TextView textView = new TextView(getActivity());
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(20);
            textView.setText(trunk.toString());
            trunksContainer.addView(textView);
        }

        TextView userText = (TextView)view.findViewById(R.id.setting_user);
        userText.setText(User.getInstance().username);
        TextView nickNameText = (TextView)view.findViewById(R.id.setting_nickName);
        nickNameText.setText(User.getInstance().nickName);


        final SettingFragment self = this;
        Button addTrunk = (Button)view.findViewById(R.id.setting_frag_add);
        addTrunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTrunkDialog dialog = new NewTrunkDialog();
                dialog.setListener(self);
                dialog.show(getFragmentManager(), "addtrunk");
            }
        });

        Button removeTrunk = (Button)view.findViewById(R.id.setting_frag_remove);
        removeTrunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final Activity activity = getActivity();
        Button unregisterBtn = (Button)view.findViewById(R.id.setting_unregister);
        unregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = activity.getSharedPreferences(activity.getString(R.string.app_name), 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivity(intent);

                Toast toast = Toast.makeText(activity.getApplicationContext(), "已注销", 2);
                toast.show();

                activity.finish();
            }
        });
    }

    public void onNewTrunkDialogConfirm(final Trunk trunk){
        NetService service = new NetService(getActivity());
        service.addUserTrunk(trunk, new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    User.getInstance().trunks.add(trunk);
                    updateTrunks();
                }else{
                    Utils.defaultNetProAction(getActivity(), result);
                }
            }
        });
    }
    public void onNewTrunkDialogCancel(){

    }

    private void updateTrunks(){
        ViewGroup trunksContainer = (ViewGroup)getView().findViewById(R.id.setting_trunks);
        trunksContainer.removeAllViewsInLayout();
        for(Trunk trunk : User.getInstance().trunks){
            TextView textView = new TextView(getActivity());
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(20);
            textView.setText(trunk.toString());
            trunksContainer.addView(textView);
        }
    }
}