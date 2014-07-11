package com.buerlab.returntrunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.buerlab.returntrunk.R;
import com.buerlab.returntrunk.Utils;
import com.buerlab.returntrunk.models.Comment;
import com.buerlab.returntrunk.models.User;
import com.buerlab.returntrunk.views.NickNameBarView;
import com.buerlab.returntrunk.views.StarsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqiling on 14-6-18.
 */
public class UserCompleteDataCommentListAdapter extends BaseAdapter {

    private List<Comment> mComments = new ArrayList<Comment>();
    private LayoutInflater mInflater = null;

    public UserCompleteDataCommentListAdapter(Context context){
        mInflater = LayoutInflater.from(context);
    }

    public void setComments(List<Comment> Comments){
        mComments = Comments;
        notifyDataSetChanged();
    }

    public void addComment(Comment Comment){
        mComments.add(Comment);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return mComments.size();
    }

    @Override
    public Object getItem(int position){
        return mComments.get(position);
    }

    @Override
    public long getItemId(int position){return position;}

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup){
        Comment comment = mComments.get(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.user_complete_data_comment_item, null);
            holder = new ViewHolder();
                /*得到各个控件的对象*/
            holder.nickNameBarView = (NickNameBarView) convertView.findViewById(R.id.nickname_bar);
            holder.star = (StarsView) convertView.findViewById(R.id.star);
            holder.star.setSize(16);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.commentText = (TextView) convertView.findViewById(R.id.comment_text);

            convertView.setTag(holder); //绑定ViewHolder对象

        }else{
            holder = (ViewHolder) convertView.getTag(); //取出ViewHolder对象
        }

        holder.nickNameBarView.setUser(comment.mUser, User.getInstance().getUserType());
//        holder.nickname.setText(comment.fromUserName);
        holder.star.setStar(comment.starNum);

        holder.time.setText(Utils.timestampToDisplay( (long)Float.parseFloat(comment.commentTime)* 1000,Utils.YEAR_MONTH_DAY ) );
        holder.commentText.setText(comment.text);
        return convertView;
    }

    /*存放控件 的ViewHolder*/
    public final class ViewHolder {
//        public TextView nickname;
        public NickNameBarView nickNameBarView;
        public StarsView star;
        public TextView time;
        public TextView commentText;
    }

}
