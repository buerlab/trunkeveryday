package com.buerlab.returntrunk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.adapters.CommentListAdapter;
import com.buerlab.returntrunk.adapters.TrunkListAdapter;
import com.buerlab.returntrunk.adapters.UserCompleteDataCommentListAdapter;
import com.buerlab.returntrunk.models.Trunk;
import com.buerlab.returntrunk.models.UserCompleteData;
import com.buerlab.returntrunk.net.NetProtocol;
import com.buerlab.returntrunk.net.NetService;
import com.buerlab.returntrunk.views.MyListView;
import com.buerlab.returntrunk.views.StarsView;
import com.buerlab.returntrunk.views.StarsViewWithText;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.UnsupportedEncodingException;

/**
 * Created by teddywu on 14-6-17.
 */
public class UserCompleteDataActivity extends BackBaseActivity {
    private static final String TAG = "UserCompleteDataActivity" ;

    StarsViewWithText starsViewWithText;
    String userId;
    String getType;
    String nickname;
    NetService netService;

    UserCompleteData data;

    TextView textview_nickname;
    StarsView stars_view;
    TextView count_textview;
    TextView average_star_num;
    TextView textview_phoneNum;
    TextView home_location;

    ImageView IDNumVerifyIcon;
    TextView IDNumTextView;

    ImageView driverLicenseVerifyIcon;
    TextView driverLicenseTextView;

    ImageView trunk_license_verify;
    TextView textview_trunk_license;

    ImageView support_location_verify;
    TextView textview_support_location;

    FrameLayout tab_trunk_wrapper;

    LinearLayout container;

    MyListView comments_list;
    UserCompleteDataCommentListAdapter mAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail);

        init();
        initData();
        if(nickname!=null){
            setActionBarLayout(nickname+"的个人资料");
        }else {
            setActionBarLayout("个人资料" );
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(){
        Utils.init(this);

        container = (LinearLayout)findViewById(R.id.container);
        container.setVisibility(View.GONE);
        textview_nickname = (TextView)findViewById(R.id.textview_nickname);
        stars_view = (StarsView)findViewById(R.id.stars_view);

        count_textview = (TextView)findViewById(R.id.comment_count2);
        average_star_num = (TextView)findViewById(R.id.average_star_num);
        textview_phoneNum = (TextView)findViewById(R.id.textview_phoneNum);
        home_location = (TextView)findViewById(R.id.home_location);

        IDNumVerifyIcon = (ImageView)findViewById(R.id.idnum_verify);
        IDNumTextView = (TextView)findViewById(R.id.textview_IDNum);

        driverLicenseVerifyIcon = (ImageView)findViewById(R.id.driver_license_verify);
        driverLicenseTextView = (TextView)findViewById(R.id.textview_driverLicense);

        trunk_license_verify = (ImageView)findViewById(R.id.trunk_license_verify);
        textview_trunk_license = (TextView)findViewById(R.id.textview_trunk_license);

        support_location_verify = (ImageView)findViewById(R.id.support_location_verify);
        textview_support_location = (TextView)findViewById(R.id.textview_support_location);

        tab_trunk_wrapper = (FrameLayout)findViewById(R.id.tab_trunk_wrapper);
        comments_list =(MyListView)findViewById(R.id.comment_list_view);
        mAdapter =new UserCompleteDataCommentListAdapter(this);
        comments_list.setAdapter(mAdapter);
    }

    private void initData(){
        userId = getIntent().getStringExtra("userId");
        getType = getIntent().getStringExtra("getType");
        nickname = getIntent().getStringExtra("nickname");
        if(userId.isEmpty() || getType.isEmpty()){
            Utils.showToast(this,"无法获取个人资料");
            return;
        }
        netService = new NetService(this);
        netService.getUserCompleteData(userId,getType,new NetService.NetCallBack() {
            @Override
            public void onCall(NetProtocol result) {
                if(result.code == NetProtocol.SUCCESS && result.data!=null){
                    data = new UserCompleteData(result.data);
                    render();
                }else {
                    Utils.defaultNetProAction(self,result);
                }
            }
        });
    }
    private  void render(){
        if(data ==null){
            return;
        }

        container.setVisibility(View.VISIBLE);
        textview_nickname.setText(data.nickName);
        stars_view.setStar(data.stars);
        average_star_num.setText(String.valueOf(data.stars));

        if(data.comments !=null){
            count_textview.setText(String.valueOf(data.comments.size()) );
        }else {
            count_textview.setText("0");
        }

        textview_phoneNum.setText(getFormatPhoneNum(data.phoneNum));
        home_location.setText(data.homeLocation);

        int IDNumVerified =  Integer.parseInt(data.IDNumVerified);
        switch (IDNumVerified){
            case 0: IDNumTextView.setText("未审核");
                IDNumVerifyIcon.setImageResource(R.drawable.qt2_wsh);
                break;
            case 1:IDNumTextView.setText("审核中");
                IDNumVerifyIcon.setImageResource(R.drawable.qt_dd);
                break;
            case 2:IDNumTextView.setText("通过审核");
                IDNumVerifyIcon.setImageResource(R.drawable.verified);
                break;
            case 3:IDNumTextView.setText("审核失败");
                IDNumVerifyIcon.setImageResource(R.drawable.qt2_wtg);
                break;
            default:break;
        }

        if(getType.equals("owner")){
            findViewById(R.id.driver_license_wrapper).setVisibility(View.GONE);
            findViewById(R.id.trunk_license_wrapper).setVisibility(View.GONE);
            findViewById(R.id.support_location_wrapper).setVisibility(View.GONE);
            findViewById(R.id.IDNum_wrapper).setBackgroundResource(R.color.white);
        }

        int driverLisenceVerified =  Integer.parseInt(data.driverLicenseVerified);
        switch (driverLisenceVerified){
            case 0: driverLicenseTextView.setText("未审核");
                driverLicenseVerifyIcon.setImageResource(R.drawable.qt2_wsh);
                break;
            case 1:driverLicenseTextView.setText("审核中");
                driverLicenseVerifyIcon.setImageResource(R.drawable.qt_dd);
                break;
            case 2:driverLicenseTextView.setText("通过审核");
                driverLicenseVerifyIcon.setImageResource(R.drawable.verified);
                break;
            case 3:driverLicenseTextView.setText("审核失败");
                driverLicenseVerifyIcon.setImageResource(R.drawable.qt2_wsh);
                break;
            default:break;
        }

        int trunkLisenceVerified =  Integer.parseInt(data.trunkLicenseVerified);
        switch (trunkLisenceVerified){
            case 0: textview_trunk_license.setText("未审核");
                trunk_license_verify.setImageResource(R.drawable.qt2_wsh);
                break;
            case 1:textview_trunk_license.setText("审核中");
                trunk_license_verify.setImageResource(R.drawable.qt_dd);
                break;
            case 2:textview_trunk_license.setText("通过审核");
                trunk_license_verify.setImageResource(R.drawable.verified);
                break;
            case 3:textview_trunk_license.setText("审核失败");
                trunk_license_verify.setImageResource(R.drawable.qt2_wsh);
                break;
            default:break;
        }

        renderTrunk();

        renderComment();
    }


    private void renderTrunk(){
        View convertView = LayoutInflater.from(this).inflate(R.layout.trunk_item, null);
        ImageView set_current_trunk_btn = (ImageView)convertView.findViewById(R.id.set_current_trunk_btn);
        set_current_trunk_btn.setVisibility(View.GONE);
        tab_trunk_wrapper.addView(convertView);

        Trunk trunk = data.trunk;

        if(trunk ==null){
            tab_trunk_wrapper.setVisibility(View.GONE);
            return;
        }

        ViewHolder holder = new ViewHolder();
        /*得到各个控件的对象*/
        holder.licensePlateTxtView = (TextView) convertView.findViewById(R.id.licensePlate);
        holder.typeTxtView = (TextView) convertView.findViewById(R.id.type);
        holder.loadTxtView = (TextView) convertView.findViewById(R.id.load);
        holder.lengthTxtView = (TextView) convertView.findViewById(R.id.length);
        holder.picGridLayout = (GridLayout)convertView.findViewById(R.id.pic_gridview);
        holder.verifyIcon = (ImageView)convertView.findViewById(R.id.verify_icon);
        holder.verifyText = (TextView)convertView.findViewById(R.id.verify_text);

        holder.isVerified =Integer.parseInt(trunk.trunkLicenseVerified);
        holder.isUsedImageView = (ImageView) convertView.findViewById(R.id.set_current_trunk_btn);
        convertView.setTag(holder); //绑定ViewHolder对象

        holder.licensePlateTxtView.setText(trunk.lisencePlate);
        holder.typeTxtView.setText(trunk.type);
        holder.loadTxtView.setText(String.valueOf(trunk.load));
        holder.lengthTxtView.setText(String.valueOf(trunk.length));

        int trunkLisenceVerified2 =  Integer.parseInt(trunk.trunkLicenseVerified);
        switch (trunkLisenceVerified2){
            case 0: holder.verifyIcon.setImageResource(R.drawable.qt2_zy);
                holder.verifyText.setText("未审核");break;
            case 1: holder.verifyIcon.setImageResource(R.drawable.qt_dd);
                holder.verifyText.setText("审核中");break;
            case 2: holder.verifyIcon.setImageResource(R.drawable.qt2_wtg);
                holder.verifyText.setText("通过审核");break;
            case 3: holder.verifyIcon.setImageResource(R.drawable.qt2_wtg);
                holder.verifyText.setText("审核失败");break;
            default:break;
        }

        ImageLoader imageLoader = ImageLoader.getInstance();
        holder.picGridLayout.removeAllViews();
        int width = (Utils.getScreenSize()[0] - 40)/3;
        if(trunk.trunkPicFilePaths!=null){
            for(int i =0;i<trunk.trunkPicFilePaths.size();i++){
                ImageView iv = new ImageView(this);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.setMargins(0,0,5,5);
                params.width = width;
                params.height = width;
                holder.picGridLayout.addView(iv,params);
                imageLoader.displayImage(this.getString(R.string.server_addr2)+ trunk.trunkPicFilePaths.get(i), iv);
                iv.setOnClickListener(new OnPhotoClick(i,trunk));
            }

        }
    }

    class OnPhotoClick implements View.OnClickListener {
        int position;
        Trunk trunk;
        public OnPhotoClick(int position,Trunk trunk) {
            this.position=position;
            this.trunk = trunk;
        }

        @Override
        public void onClick(View v) {
            String[] urls = new String[trunk.trunkPicFilePaths.size()];
            try {
                for (int i=0;i<urls.length;i++){
                    String[] path = trunk.trunkPicFilePaths.get(i).split("/");
                    urls[i] = getString(R.string.server_addr2);
                    for(int j =0;j<path.length;j++){
                        if(!path[j].isEmpty()){
                            urls[i] += "/"+ java.net.URLEncoder.encode(path[j],"utf-8");
                        }
                    }
                }

                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putStringArray("urls",urls);
                Intent intent = new Intent(self,GalleryUrlActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }catch (UnsupportedEncodingException e){
                Log.e(TAG, e.toString());
            }
        }
    }

    /*存放控件 的ViewHolder*/
    private final class ViewHolder {
        public TextView licensePlateTxtView;
        public TextView typeTxtView;
        public TextView loadTxtView;
        public TextView lengthTxtView;
        public ImageView verifyIcon;
        public TextView verifyText;
        public GridLayout picGridLayout;
        public ImageView isUsedImageView;
        public int position;
        public int isVerified;
    }


    private void renderComment(){
        mAdapter.setComments(data.comments);
    }
    private String getFormatPhoneNum(String phonenum){
        if (phonenum.isEmpty())
            return phonenum;

        String str1 = phonenum.substring(0,3);
        String str2 = phonenum.substring(3,7);
        return str1 + " " + str2 + " ****";
    }

}