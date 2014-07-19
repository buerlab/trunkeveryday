package com.buerlab.returntrunk.fragments;

//import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.adapters.CommentListAdapter;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.models.Comment;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.utils.EventLogUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class CommentListFragment extends BaseFragment implements EventCenter.OnEventListener{

    private static final String TAG = "CommentListFragment";
    View mView;
    private LinearLayout tips = null;
    CommentListAdapter mAdapter;
    ListView mListView;
    NetService service;


    TextView reminderText;
    TextView reminderText2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.comments, container, false);
        init();
        return  mView;
    }


    public void init(){
        tips = (LinearLayout)mView.findViewById(R.id.tips);
        reminderText = (TextView)mView.findViewById(R.id.reminder_text);
        reminderText2= (TextView)mView.findViewById(R.id.reminder_text2);
        mListView = (ListView)mView.findViewById(R.id.comments_list);
        mAdapter =new CommentListAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        EventCenter.shared().addEventListener(DataEvent.USER_UPDATE, this);
//        initComments();
        service = new NetService(getActivity());

        if(User.getInstance().getUserType().equals("owner")){
            reminderText2.setText("每当您达成一笔交易，将会得到货车司机对您的评分和评价，更高的评分将会给您带来更多的交易机会噢");
        }
    }

    @Override
    public void onEventCall(DataEvent e) {
        initComments();
    }

    private void initComments(){
        final CommentListAdapter adapter = mAdapter;

        if(service!=null){
            service.getComments(User.getInstance().getUserType(),0,-1,new NetService.CommentsCallBack() {
                @Override
                public void onCall(NetProtocol result, List<Comment> comments) {
                    if (result.code == NetProtocol.SUCCESS) {
                        if (comments != null && comments.size()>0) {
                            if(User.getInstance().getUserType()=="driver"){
                                User.getInstance().initDriverComments(comments);
                            }
                            if(User.getInstance().getUserType()=="owner"){
                                User.getInstance().initOwnerComments(comments);
                            }
                            adapter.setComments(comments);
                            tips.setVisibility(View.GONE);

                        }
                    }
                }
            });
        }
    }

    @Override
    public void onShow(){
        initComments();
        if(Utils.getVersionType(self.getActivity()).equals("driver")){
            EventLogUtils.EventLog(self.getActivity(), EventLogUtils.tthcc_driver_commentList_enterFragment);
        }else {
            //TODO 货主版
        }
    }


}