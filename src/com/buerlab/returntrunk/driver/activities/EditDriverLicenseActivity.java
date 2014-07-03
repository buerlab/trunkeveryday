package com.buerlab.returntrunk.driver.activities;

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
import com.buerlab.returntrunk.activities.EditProfileBaseActivity;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by teddywu on 14-6-17.
 */
public class EditDriverLicenseActivity extends EditProfileBaseActivity {
    private static final String TAG = "EditDriverLicenseActivity" ;
    ActionBar mActionBar;

    EditText mDriverLicenseEdit ;
    Button mPicBtn;
    Bitmap mBitmap;
    NetService service;
    final Activity self = this;
    private String uploadFile = null;
    private String actionUrl ="http://115.29.8.74:9289/upload/driverLicense";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_edit_driverlicense);
        init();
        setActionBarLayout(R.layout.actionbar, "审核驾驶证");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        mDriverLicenseEdit = (EditText)findViewById(R.id.edit_driverlicense);
        mDriverLicenseEdit.setText(User.getInstance().driverLicense);

        mPicBtn =(Button)findViewById(R.id.btn_pic);
        mPicBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.showPickPicDialog(self,"选择驾驶证照片");
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
        final String driverLicense = mDriverLicenseEdit.getText().toString();
        if(driverLicense.length()==0){
            Utils.showToast(this,"请输入你的驾驶证号");
            return;
        }

        if(mBitmap == null){
            Utils.showToast(this,"请提供驾驶证照片");
        }

        String filename = mDriverLicenseEdit.getText().toString().trim()+ "_" + User.getInstance().userId;
        service.uploadPic(actionUrl,mBitmap,filename,new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS){
                    //上传完图片再改资料
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("driverLicense", driverLicense);
                    map.put("driverLicenseVerified","1");
                    service.setUserData(map, new NetService.NetCallBack() {
                        @Override
                        public void onCall(NetProtocol result) {
                            if (result.code == NetProtocol.SUCCESS) {
                                User.getInstance().driverLicense = driverLicense;
                                User.getInstance().driverLicenseVerified = "1";
                                DataEvent evt = new DataEvent(DataEvent.USER_UPDATE, null);
                                EventCenter.shared().dispatch(evt);
                                finish();
                            } else {
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