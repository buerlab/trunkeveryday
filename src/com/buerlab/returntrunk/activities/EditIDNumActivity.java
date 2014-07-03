package com.buerlab.returntrunk.activities;

import android.app.ActionBar;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by teddywu on 14-6-17.
 */
public class EditIDNumActivity extends EditProfileBaseActivity{
    private static final String TAG = "EditIDNumActivity" ;
    ActionBar mActionBar;

    EditText mIDNumEdit ;
    Button mPicBtn;
    Bitmap mBitmap;
    NetService service;
    final Activity self = this;
    private String uploadFile = null;
    private String actionUrl ="http://115.29.8.74:9289/upload/IDNum";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_idnum);
        init();
        setActionBarLayout(R.layout.actionbar, "审核身份证");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        mIDNumEdit = (EditText)findViewById(R.id.edit_IDNum);
        mIDNumEdit.setText(User.getInstance().IDNum);

        mPicBtn =(Button)findViewById(R.id.btn_pic);
        mPicBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.showPickPicDialog(self,"选择身份证照片");
            }
        });

        service = new NetService(this);

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
                        Toast toast = Toast.makeText(this, "未找到存储卡，无法存储照片！", 2);
                        toast.show();
                    }

                    break;
                case Utils.RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data);
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
            mBitmap = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(mBitmap);
            mPicBtn.setBackgroundDrawable(drawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_profile, menu);
        return true;
    }

    public void onOptionSave(MenuItem i)
    {
        final String idNum = mIDNumEdit.getText().toString();
        if(idNum.length()==0){
            Utils.showToast(this,"请输入你的身份证");
            return;
        }

        if(!Utils.matchIDNumRex(idNum)){
            Utils.showToast(this,"身份证的格式错误，请重新输入");
            return;
        }

        if(mBitmap == null){
            Utils.showToast(this,"请提供身份证正面照");
        }

        String filename = mIDNumEdit.getText().toString().trim()+ "_" + User.getInstance().userId;
        service.uploadPic(actionUrl,mBitmap,filename,new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    //上传完图片再改资料
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("IDNum", idNum);
                    map.put("IDNumVerified","1");
                    service.setUserData(map, new NetService.NetCallBack() {
                        @Override
                        public void onCall(NetProtocol result) {
                            if(result.code == NetProtocol.SUCCESS){
                                User.getInstance().IDNum = idNum;
                                User.getInstance().IDNumVerified = "1";
                                DataEvent evt = new DataEvent(DataEvent.USER_UPDATE,null);
                                EventCenter.shared().dispatch(evt);
                                finish();
                            }
                            else{
                                Utils.defaultNetProAction(self, result);
                            }
                        }
                    });
                }
                else{
                    Utils.defaultNetProAction(self, result);
                }
            }
        });


    }
}