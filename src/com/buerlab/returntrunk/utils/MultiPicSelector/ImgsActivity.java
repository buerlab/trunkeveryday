package com.buerlab.returntrunk.utils.MultiPicSelector;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.activities.BackBaseActivity;
import com.buerlab.returntrunk.activities.BaseActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;

public class ImgsActivity extends BackBaseActivity {

    Bundle bundle;
    FileTraversal fileTraversal;
    GridView imgGridView;
    ImgsAdapter imgsAdapter;
    LinearLayout select_layout;
    Util util;
    RelativeLayout relativeLayout2;
    HashMap<Integer, ImageView> hashImage;
    Button choise_button;
    ArrayList<String> filelist;

    public final static int MAX_NUM = 8;
    final ImgsActivity self =this;
    int hasSelected;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_pic_selector);

        setActionBarLayout("选择照片");

        imgGridView=(GridView) findViewById(R.id.gridView1);
        bundle= getIntent().getExtras();
        fileTraversal=bundle.getParcelable("data");
        hasSelected = bundle.getInt("hasSelected");
        imgsAdapter=new ImgsAdapter(this, fileTraversal.filecontent,onItemClickClass);
        imgGridView.setAdapter(imgsAdapter);
        select_layout=(LinearLayout) findViewById(R.id.selected_image_layout);
        relativeLayout2=(RelativeLayout) findViewById(R.id.relativeLayout2);
        choise_button=(Button) findViewById(R.id.button3);
        hashImage=new HashMap<Integer, ImageView>();
        filelist=new ArrayList<String>();
//		imgGridView.setOnItemClickListener(this);
        util=new Util(this);
    }

    class BottomImgIcon implements OnItemClickListener{

        int index;
        public BottomImgIcon(int index) {
            this.index=index;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

        }
    }

    @SuppressLint("NewApi")
    public ImageView iconImage(String filepath,int index,CheckBox checkBox) throws FileNotFoundException{
        LinearLayout.LayoutParams params=new LayoutParams(relativeLayout2.getMeasuredHeight()-10, relativeLayout2.getMeasuredHeight()-10);
        ImageView imageView=new ImageView(this);
        imageView.setLayoutParams(params);
        imageView.setBackgroundResource(R.drawable.imgbg);
        float alpha=100;
        imageView.setAlpha(alpha);
        util.imgExcute(imageView, imgCallBack, filepath);
        imageView.setOnClickListener(new ImgOnclick(filepath,checkBox));
        return imageView;
    }

    ImgCallBack imgCallBack=new ImgCallBack() {
        @Override
        public void resultImgCall(ImageView imageView, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    };

    class ImgOnclick implements OnClickListener{
        String filepath;
        CheckBox checkBox;
        public ImgOnclick(String filepath,CheckBox checkBox) {
            this.filepath=filepath;
            this.checkBox=checkBox;
        }
        @Override
        public void onClick(View arg0) {
            checkBox.setChecked(false);
            select_layout.removeView(arg0);
            choise_button.setText("已选择("+select_layout.getChildCount()+")张");
            filelist.remove(filepath);
        }
    }

    ImgsAdapter.OnItemClickClass onItemClickClass=new ImgsAdapter.OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int Position, CheckBox checkBox) {
            String filapath=fileTraversal.filecontent.get(Position);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                select_layout.removeView(hashImage.get(Position));
                filelist.remove(filapath);
                choise_button.setText("已选择("+select_layout.getChildCount()+")张");
            }else {
                try {
                    if(select_layout.getChildCount()<MAX_NUM -hasSelected){
                        checkBox.setChecked(true);
                        Log.i("img", "img choise position->"+Position);
                        ImageView imageView=iconImage(filapath, Position,checkBox);
                        if (imageView !=null) {
                            hashImage.put(Position, imageView);
                            filelist.add(filapath);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            select_layout.addView(imageView);
                            choise_button.setText("已选择("+select_layout.getChildCount()+")张");
                        }
                    }else{
                        Utils.showToast(self,"最多选择"+(MAX_NUM -hasSelected)+ "张图片");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    /**
     * FIXME
     * 亲只需要在这个方法把选中的文档目录已list的形式传过去即可
     * @param view
     */
    public void sendfiles(View view){
        DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
        EventCenter.shared().dispatch(evt);
        finish();
//        Intent intent =new Intent(this, MainActivity.class);
//        Bundle bundle=new Bundle();
//        bundle.putStringArrayList("files", filelist);
//        intent.putExtras(bundle);
//        startActivity(intent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Util.BACK);
                finish();
                break;
            case R.id.menu_confirm:
                if(filelist.isEmpty()){
                    setResult(Util.EMPTY);
                }else{
                    Bundle bundle=new Bundle();
                    bundle.putStringArrayList("files", filelist);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    setResult(Util.HAS_VALUE,intent);
                }
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_comfirm, menu);
        return true;
    }
}
