package com.buerlab.returntrunk.MultiPicSelector;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.net.Uri;
import android.os.Environment;
import android.view.MenuItem;
import com.buerlab.returntrunk.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BaseActivity;

public class ImgFileListActivity extends BaseActivity implements OnItemClickListener{

    ListView listView;
    Util util;
    ImgFileListAdapter listAdapter;
    List<FileTraversal> locallist;
    int hasSelected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_pic_selector_file_list);
        hasSelected = getIntent().getIntExtra("hasSelected",0);
        ActionBar actionBar = getActionBar();
        if( null != actionBar ){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("照片库");
        }
        listView=(ListView) findViewById(R.id.listView1);
        util=new Util(this);
        locallist=util.LocalImgFileList();
        List<HashMap<String, String>> listdata=new ArrayList<HashMap<String,String>>();
        Bitmap bitmap[] = null;
        if (locallist!=null) {
            bitmap=new Bitmap[locallist.size()];
            for (int i = 0; i < locallist.size(); i++) {
                HashMap<String, String> map=new HashMap<String, String>();
                map.put("filecount", locallist.get(i).filecontent.size()+"张");
                map.put("imgpath", locallist.get(i).filecontent.get(0)==null?null:(locallist.get(i).filecontent.get(0)));
                map.put("filename", locallist.get(i).filename);
                listdata.add(map);
            }
        }
        listAdapter=new ImgFileListAdapter(this, listdata);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent=new Intent(this,ImgsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putParcelable("data", locallist.get(arg2));
        bundle.putInt("hasSelected",hasSelected);
        intent.putExtras(bundle);
        startActivityForResult(intent, 124);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(125);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode ==Util.EMPTY){
            setResult(Util.EMPTY);
            finish();
        }else if(resultCode ==Util.HAS_VALUE){
            setResult(Util.HAS_VALUE,data);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
