package com.buerlab.returntrunk;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class SettingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.setting_frag, container, false);
        TextView userText = (TextView)view.findViewById(R.id.setting_user);
        userText.setText(User.getInstance().username);
        TextView typeText = (TextView)view.findViewById(R.id.setting_userType);
        typeText.setText(User.getInstance().getUserType());
        TextView nickNameText = (TextView)view.findViewById(R.id.setting_nickName);
        nickNameText.setText(User.getInstance().nickName);

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
        return view;
    }
}