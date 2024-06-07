package com.example.midemo.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.midemo.Dialog.BeiZhuDialog;
import com.example.midemo.Dialog.SelectTimeDialog;
import com.example.midemo.R;
import com.example.midemo.db.AccountBean;
import com.example.midemo.db.TypeBean;
import com.example.midemo.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;   //将需要插入到记账本当中的数据保存成对象的形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();   //创建对象
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }
    /* 弹出显示时间的对话框*/
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(Objects.requireNonNull(getContext()));
        dialog.show();
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener((time, year, month, day) -> {
            timeTv.setText(time);
            accountBean.setTime(time);
            accountBean.setYear(year);
            accountBean.setMonth(month);
            accountBean.setDay(day);
        });
    }

    /* 弹出备注对话框*/
    public  void showBZDialog(){
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(() -> {
            String msg = dialog.getEditText();
            if (!TextUtils.isEmpty(msg)) {
                beizhuTv.setText(msg);
                accountBean.setBeizhu(msg);
            }
            dialog.cancel();
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //给GridView填充数据的方法
        loadDataToGV();
        setGVListener(); //设置GridView每一项的点击事件
        return view;
    }
    /* 获取当前时间，显示在timeTv上*/
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);
    }

    /* 设置GridView每一项的点击事件*/
    private void setGVListener() {
        typeGv.setOnItemClickListener((parent, view, position, id) -> {
            adapter.selectPos = position;
            adapter.notifyDataSetInvalidated();  //提示绘制发生变化了
            TypeBean typeBean = typeList.get(position);
            String typename = typeBean.getTypename();
            typeTv.setText(typename);
            accountBean.setTypename(typename);
            int simageId = typeBean.getSimageId();
            typeIv.setImageResource(simageId);
            accountBean.setsImageId(simageId);
        });
    }

    /* 给GridView填出数据的方法*/
    public void loadDataToGV() {
        typeList = new ArrayList<>();
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeGv = view.findViewById(R.id.frag_record_gv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);
        //让自定义软键盘显示出来
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView, moneyEt);
        boardUtils.showKeyboard();
        //设置接口，监听确定按钮按钮被点击了
        boardUtils.setOnEnsureListener(() -> {
            //获取输入钱数
            String moneyStr = moneyEt.getText().toString();
            if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                getActivity().finish();
                return;
            }
            float money = Float.parseFloat(moneyStr);
            //获取记录的信息，保存在数据库当中
            accountBean.setMoney(money);
            saveAccountToDB();
            // 返回上一级页面
            getActivity().finish();
        });
    }
    /* 让子类一定要重写这个方法*/
    public abstract void saveAccountToDB();


}