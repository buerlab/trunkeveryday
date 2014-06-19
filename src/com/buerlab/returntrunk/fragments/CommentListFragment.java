package com.buerlab.returntrunk.fragments;

//import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class CommentListFragment extends BaseFragment implements NewTrunkDialog.NewTrunkDialogListener, AddCommentDialog.AddCommentDialogListener{

    Button addCommentBtn;
    View mView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.setting_frag, container, false);
        init();
        return  mView;
    }

    public void init(){

        ViewGroup trunksContainer = (ViewGroup)mView.findViewById(R.id.setting_trunks);
        for(Trunk trunk : User.getInstance().trunks){
            TextView textView = new TextView(getActivity());
            textView.setTextColor(Color.GRAY);
            textView.setTextSize(20);
            textView.setText(trunk.toString());
            trunksContainer.addView(textView);
        }

        TextView userText = (TextView)mView.findViewById(R.id.setting_user);
        userText.setText(User.getInstance().username);
        TextView nickNameText = (TextView)mView.findViewById(R.id.setting_nickName);
        nickNameText.setText(User.getInstance().nickName);

        final CommentListFragment self = this;
        Button addTrunk = (Button)mView.findViewById(R.id.setting_frag_add);
        addTrunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTrunkDialog dialog = new NewTrunkDialog();
                dialog.setListener(self);
                dialog.show(getActivity().getFragmentManager(), "addtrunk");
            }
        });

        Button removeTrunk = (Button)mView.findViewById(R.id.setting_frag_remove);
        removeTrunk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        final FragmentActivity activity = getActivity();
        Button unregisterBtn = (Button)mView.findViewById(R.id.setting_unregister);
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

        addCommentBtn = (Button)mView.findViewById(R.id.comment_add);
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCommentDialog commentdialog = new AddCommentDialog();
                commentdialog.setListener(self);
                commentdialog.show(getActivity().getFragmentManager(), "addComment");
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

    @Override
    public void onAddCommentDialogConfirm() {

    }

    @Override
    public void oAddCommentDialogCancel() {

    }
}