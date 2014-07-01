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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.adapters.CommentListAdapter;
import com.buerlab.returntrunk.adapters.SendBillListAdapter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.util.List;

/**
 * Created by zhongqiling on 14-6-4.
 */
public class CommentListFragment extends BaseFragment{

    View mView;
    private TextView tips = null;
    CommentListAdapter mAdapter;
    ListView mListView;
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

        initComments();
    }

    private void initComments(){
        final CommentListAdapter adapter = mAdapter;
        NetService service = new NetService(getActivity());
        service.getComments(0,-1,new NetService.CommentsCallBack() {
            @Override
            public void onCall(NetProtocol result, List<Comment> comments) {
                if (result.code == NetProtocol.SUCCESS) {
                    if (comments != null) {
                        User.getInstance().initComments(comments);
                        adapter.setComments(comments);

                        tips.setAlpha(0.0f);
                    }
                }
            }
        });
    }
}