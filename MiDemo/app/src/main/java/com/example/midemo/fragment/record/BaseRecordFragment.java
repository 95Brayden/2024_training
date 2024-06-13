package com.example.midemo.fragment.record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.midemo.adapter.TypeBaseAdapter;
import com.example.midemo.dialog.RemarkDialog;
import com.example.midemo.dialog.SelectTimeDialog;
import com.example.midemo.R;
import com.example.midemo.entity.AccountItem;
import com.example.midemo.entity.Type;
import com.example.midemo.utils.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener {

    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,remarkTv,timeTv;
    GridView typeGv;
    List<Type>typeList;
    TypeBaseAdapter adapter;
    AccountItem accountItem;   //将需要插入到记账本当中的数据保存成对象的形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountItem = new AccountItem();   //创建对象
        accountItem.setTypename("其他");
        accountItem.setsImageId(R.mipmap.ic_qita_fs);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_remark:
                showBZDialog();
                break;
        }
    }
    /* 弹出显示时间的对话框*/
    private void showTimeDialog() {
        SelectTimeDialog dialog = new SelectTimeDialog(requireContext());
        dialog.show();
        //设定确定按钮被点击了的监听器
        dialog.setOnEnsureListener((time, year, month, day) -> {
            timeTv.setText(time);
            accountItem.setTime(time);
            accountItem.setYear(year);
            accountItem.setMonth(month);
            accountItem.setDay(day);
        });
    }

    /* 弹出备注对话框*/
    public  void showBZDialog(){
        final RemarkDialog dialog = new RemarkDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(() -> {
            String msg = dialog.getEditText();
            if (!TextUtils.isEmpty(msg)) {
                remarkTv.setText(msg);
                accountItem.setRemark(msg);
            }
            dialog.cancel();
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);

        // 确保光标始终在文本末端
        moneyEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                moneyEt.setSelection(s.length());
            }
        });
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
        accountItem.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountItem.setYear(year);
        accountItem.setMonth(month);
        accountItem.setDay(day);
    }

    /* 设置GridView每一项的点击事件*/
    private void setGVListener() {
        typeGv.setOnItemClickListener((parent, view, position, id) -> {
            adapter.selectPos = position;
            adapter.notifyDataSetInvalidated();  //提示绘制发生变化了
            Type type = typeList.get(position);
            String typename = type.getTypename();
            typeTv.setText(typename);
            accountItem.setTypename(typename);
            int simageId = type.getSimageId();
            typeIv.setImageResource(simageId);
            accountItem.setsImageId(simageId);
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
        remarkTv = view.findViewById(R.id.frag_record_tv_remark);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        remarkTv.setOnClickListener(this);
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
            accountItem.setMoney(money);
            saveAccountToDB();
            // 返回上一级页面
            getActivity().finish();
        });
    }
    /* 让子类一定要重写这个方法*/
    public abstract void saveAccountToDB();


}