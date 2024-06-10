package com.example.midemo.fragment.record;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.midemo.bean.TypeBean;
import com.example.midemo.dao.AccountDAO;
import com.example.midemo.dao.TypeDAO;

import java.util.List;

/**
 * 基类记录页面
 */
public abstract class RecordFragment extends BaseRecordFragment {

    protected TypeDAO typeDAO;
    protected AccountDAO accountDAO;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        typeDAO = new TypeDAO(context);
        accountDAO = new AccountDAO(context);
    }

    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        // 获取数据库当中的数据源
        List<TypeBean> list = typeDAO.getTypeList(getTypeKind());
        typeList.addAll(list);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(getImageResource());
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(getTypeKind());
        accountDAO.insertAccount(accountBean);
    }

    protected abstract int getTypeKind();
    protected abstract int getImageResource();
}
