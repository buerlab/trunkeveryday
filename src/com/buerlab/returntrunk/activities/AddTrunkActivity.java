package com.buerlab.returntrunk.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.buerlab.returntrunk.*;
import com.buerlab.returntrunk.MultiPicSelector.ImgFileListActivity;
import com.buerlab.returntrunk.MultiPicSelector.Util;
import com.buerlab.returntrunk.adapters.TrunkPicGridAdapter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.views.MyGridView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhongqiling on 14-6-23.
 */
public class AddTrunkActivity extends EditProfileBaseActivity {


    EditText typeText;
    EditText lengthText;
    EditText loadText;
    EditText lisenceText;
    EditText trunkLicenseText;
    Button mPicBtn;
    Button mTrunkLicensePicBtn;
    NetService service;

    Boolean isEdited = false;
    Bitmap mTrunkLicenseBitmap = null;
    MyGridView mPicGridView;
    TrunkPicGridAdapter mTrunkPicGridAdapter;
    final AddTrunkActivity self = this;
    private String uploadFile = null;
    private String verifyTrunkLicenseUrl ="http://115.29.8.74:9289/upload/trunkLicense";
    private String uploadTrunkPicUrl ="http://115.29.8.74:9288/upload/trunkpic";
    ArrayList<String> picFileNames= new ArrayList<String>();
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_trunk_frag);
        init();
        initData();
    }

    private void init(){
        setActionBarLayout(R.layout.actionbar, "添加货车");
        typeText = (EditText)findViewById(R.id.set_trunk_type);
        lengthText = (EditText)findViewById(R.id.set_trunk_length);
        loadText = (EditText)findViewById(R.id.set_trunk_load);
        lisenceText = (EditText)findViewById(R.id.set_trunk_licensePlate);
        trunkLicenseText = (EditText)findViewById(R.id.set_trunk_license);
//
//        mPicBtn =(Button)findViewById(R.id.add_trunk_pic);
//        mPicBtn.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(self, ImgFileListActivity.class);
//                startActivityForResult(intent, Util.REQUEST);
//            }
//        });

        mTrunkLicensePicBtn = (Button)findViewById(R.id.add_trunkLicense_pic);
        mTrunkLicensePicBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.showPickPicDialog(self,"上传行驶证");
            }
        });

        mPicGridView = (MyGridView)findViewById(R.id.pic_gridview);
        addPicToGridLayout(picFileNames);
        service = new NetService(this);
    }

    private void initData(){
        isEdited = getIntent().getBooleanExtra("isEdited",false);
    }

    @Override
    public void onOptionSave(MenuItem i) {
        saveTrunkData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    private void saveTrunkData(){
        if(isEdited){

        }else {

            if(dataVerify()){
                final float len = Float.parseFloat(lengthText.getText().toString());
                final float load = Float.parseFloat(loadText.getText().toString());
                final String type =typeText.getText().toString();
                final String license = lisenceText.getText().toString();
                final String trunkLicense =trunkLicenseText.getText().toString();
                Trunk trunk = new Trunk(type,len,load,license);
                service.addUserTrunk(trunk, new NetService.NetCallBack() {
                    @Override
                    public void onCall(NetProtocol result) {
                        if(result.code == NetProtocol.SUCCESS){
                            if(!trunkLicense.isEmpty() && mTrunkLicenseBitmap!=null){
                                String filename = license+ "_"+ User.getInstance().userId + trunkLicense;
                                service.uploadPic(verifyTrunkLicenseUrl,mTrunkLicenseBitmap,filename, new NetService.NetCallBack(){

                                    @Override
                                    public void onCall(NetProtocol result) {

                                    }
                                });
                            }

//                            service.getUserData(new NetService.NetCallBack() {
//                                @Override
//                                public void onCall(NetProtocol result) {
//                                    if(result.code == NetProtocol.SUCCESS && result.data !=null){
//                                        User.getInstance().initUser(result.data);
//                                        //注册用户初始化事件，用于个人资料得以初始化数据
//                                        DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
//                                        EventCenter.shared().dispatch(evt);
//
//                                        Utils.showToast(self,"保存成功");
////                                        finish();
//                                    }
//                                }
//                            });

                        }else {
                            Utils.defaultNetProAction(self, result);
                        }
                    }
                });
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 结果码不等于取消时候
        if (resultCode != RESULT_CANCELED) {

                switch (requestCode) {
                    case Utils.IMAGE_REQUEST_CODE:
                        startPhotoZoom(data.getData());
                        break;
                    case Utils.CAMERA_REQUEST_CODE:
                        if (Utils.hasSDCard()) {
                            File tempFile = new File(Environment.getExternalStorageDirectory(),Utils.IMAGE_FILE_NAME);
                            startPhotoZoom(Uri.fromFile(tempFile));
                        } else {
                            Utils.showToast(this,"未找到存储卡，无法存储照片！");
                        }

                        break;
                    case Utils.RESULT_REQUEST_CODE:
                        if (data != null) {
                            setImageToView(data);
                        }
                        break;
                    case Util.REQUEST:
                        if(resultCode == Util.HAS_VALUE){
                            ArrayList<String> _fileNames = data.getStringArrayListExtra("files");
                            picFileNames.addAll(_fileNames);
                            addPicToGridLayout(picFileNames);
//                            for(int i=0;i<fileNames.size();i++){
//                                Utils.showToast(self,fileNames.get(i));
//                            }
                        }else if(resultCode==Util.EMPTY){
                            Utils.showToast(self,"什么都没有选");
                        }
                        break;

                }


            }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        if(uri==null){
            Log.i("tag", "The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Utils.RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     *
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {

            mTrunkLicenseBitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(mTrunkLicenseBitmap);
            mTrunkLicensePicBtn.setBackgroundDrawable(drawable);
        }
    }

    private boolean dataVerify(){

        if(lisenceText.getText().toString().isEmpty()){
            Utils.showToast(this,"车牌号不能为空");
            return  false;
        }

        if(typeText.getText().toString().isEmpty()){
            Utils.showToast(this,"货车类型不能为空");
            return  false;
        }

        try {
            float len = Float.parseFloat(lengthText.getText().toString());
            if(len<0){
                Utils.showToast(this,"货车长度不能为负");
                return  false;
            }

            if(len>20){
                Utils.showToast(this,"货车长度不合理");
                return  false;
            }
        }catch (Exception e){
            Utils.showToast(this,"货车长度输入有误");
            return false;
        }

        try {
            float load = Float.parseFloat(loadText.getText().toString());
            if(load<0){
                Utils.showToast(this,"货车载重不能为负");
                return  false;
            }

            if(load>100){
                Utils.showToast(this,"货车载重不合理");
                return  false;
            }
        }catch (Exception e){
            Utils.showToast(this,"货车载重输入有误");
            return false;
        }

        return  true;
    }

    private void addPicToGridLayout(ArrayList<String> fileList){
        mTrunkPicGridAdapter =new TrunkPicGridAdapter(this, fileList,onItemClickClass);
        mPicGridView.setAdapter(mTrunkPicGridAdapter);
    }

    TrunkPicGridAdapter.OnItemClickClass onItemClickClass=new TrunkPicGridAdapter.OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int Position) {
            if(mTrunkPicGridAdapter.getItem(Position)=="add"){
                Intent intent = new Intent();
                intent.putExtra("hasSelected", picFileNames.size());
                intent.setClass(self, ImgFileListActivity.class);
                startActivityForResult(intent, Util.REQUEST);
            }else {
                picFileNames.remove(Position);
                addPicToGridLayout(picFileNames);
                Utils.showToast(self,"delete it");
            }

        }
    };
}