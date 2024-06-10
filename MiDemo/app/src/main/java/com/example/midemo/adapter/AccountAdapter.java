package com.example.midemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.midemo.R;
import com.example.midemo.entity.AccountItem;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    Context context;
    List<AccountItem> mDatas;
    LayoutInflater inflater;
    int year, month, day;

    public AccountAdapter(Context context, List<AccountItem> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_mainlv, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        AccountItem bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getTypename());
        holder.remarkTv.setText(bean.getRemark());
        holder.moneyTv.setText("￥ " + bean.getMoney());
        if (bean.getYear() == year && bean.getMonth() == month && bean.getDay() == day) {
            String time = bean.getTime().split(" ")[1];
            holder.timeTv.setText("今天 " + time);
        } else {
            holder.timeTv.setText(bean.getTime());
        }
        return convertView;
    }

    class ViewHolder {
        ImageView typeIv;
        TextView typeTv, remarkTv, timeTv, moneyTv;

        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            remarkTv = view.findViewById(R.id.item_mainlv_tv_remark);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);
        }
    }
}