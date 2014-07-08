package com.buerlab.returntrunk.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.events.DataEvent;
import com.buerlab.returntrunk.events.EventCenter;
import com.buerlab.returntrunk.views.PickAddrView;
import java.util.List;

/**
 * Created by teddywu on 14-7-7.
 */
public class PickAddrDialog extends Dialog implements PickAddrView.OnAddrListener{

    private Context mConext;
    public PickAddrDialog(Context context) {
        super(context);
        mConext = context;
        init();
    }


    public PickAddrDialog(Context context, int theme) {
        super(context, theme);
        mConext = context;
        init();
    }


    private void init(){
        View diaView=View.inflate(mConext, R.layout.pick_addr_view_wrapper, null);

//        Dialog dialog=new Dialog(mConext, R.style.dialog);
        setContentView(diaView);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width =Utils.getScreenSize()[0]; //设置宽度
        getWindow().setAttributes(lp);
        getWindow().getAttributes().gravity = Gravity.CENTER_VERTICAL | Gravity.BOTTOM;
        setCanceledOnTouchOutside(true);

        PickAddrView pickAddrView = (PickAddrView)findViewById(R.id.pick_addr_view);
        pickAddrView.setListener(this);

//        Button b = (Button)findViewById(R.id.btn_ok);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });

    }


    @Override
    public void OnAddrChanged(List<String> addr) {
        EventCenter.shared().dispatch(new DataEvent(DataEvent.ADDR_CHANGE, addr));
    }
}
