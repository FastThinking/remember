package com.lzx.allenLee.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzx.allenLee.R;
import com.lzx.allenLee.bean.PasswordInfo;
import com.lzx.allenLee.util.encryptionUtil.EncryptUtil;

public class AdapterPasswordList extends BaseAdapter {
    List<PasswordInfo> list;
    ViewHolder holder;
    private LayoutInflater mInflater;
    private Context context;

    public AdapterPasswordList(Context context, List<PasswordInfo> mList) {
        this.context = context;
        this.list = new ArrayList<PasswordInfo>();
        if (mList != null) {
            list.addAll(mList);
        }
        this.mInflater = LayoutInflater.from(context);
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return Long.valueOf(list.get(position).getUserId());
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {        //初始化convertView

            holder = new ViewHolder();
            //容器视图
            convertView = mInflater.inflate(R.layout.lv_password_item, null);
            holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userId);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
            holder.tv_password = (TextView) convertView.findViewById(R.id.tv_password);
            holder.tv_isLogin = (TextView) convertView.findViewById(R.id.tv_isLogin);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_userId.setText(list.get(position).getUserId() + "");
        holder.tv_title.setText(list.get(position).getTitle());
        holder.tv_userName.setText(EncryptUtil.decrypt(list.get(position).getUserName()));
        holder.tv_password.setText(EncryptUtil.decrypt(list.get(position).getPassword()));
        holder.tv_isLogin.setText(list.get(position).getIsLogin());
        return convertView;
    }


    /**
     * ListView一行的视图控件
     */
    public class ViewHolder {

        public TextView tv_userId;
        public TextView tv_title;
        public TextView tv_userName;
        public TextView tv_password;
        public TextView tv_isLogin;
    }

    public void setList(List<PasswordInfo> mList) {
        if (mList != null) {
            list.clear();
            list.addAll(mList);
            notifyDataSetChanged();
        }
    }
}
