package com.buerlab.returntrunk.fragments;

//import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class CommentListFragment extends BaseFragment implements EventCenter.OnEventListener{

    private static final String TAG = "CommentListFragment";
    View mView;
    private TextView tips = null;
    CommentListAdapter mAdapter;
    ListView mListView;
    NetService service;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.comments, container, false);
        tips = (TextView)mView.findViewById(R.id.comments_frag_tips);
        init();
        return  mView;
    }

    public void init(){
        mListView = (ListView)mView.findViewById(R.id.comments_list);
        mAdapter =new CommentListAdapter(getActivity());
        mListView.setAdapter(mAdapter);

        EventCenter.shared().addEventListener(DataEvent.USER_UPDATE, this);
//        initComments();
        service = new NetService(getActivity());
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
                        if (comments != null) {
                            if(User.getInstance().getUserType()=="driver"){
                                User.getInstance().initDriverComments(comments);
                            }
                            if(User.getInstance().getUserType()=="owner"){
                                User.getInstance().initOwnerComments(comments);
                            }
                            adapter.setComments(comments);

                            tips.setAlpha(0.0f);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onShow(){
        initComments();

    }
}