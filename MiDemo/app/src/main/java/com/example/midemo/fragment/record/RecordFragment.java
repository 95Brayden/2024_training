package com.example.midemo.fragment.record;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.midemo.entity.Type;
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
        List<Type> list = typeDAO.getTypeList(getTypeKind());
        typeList.addAll(list);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(getImageResource());
    }

    @Override
    public void saveAccountToDB() {
        accountItem.setKind(getTypeKind());
        accountDAO.insertAccount(accountItem);
    }

    protected abstract int getTypeKind();
    protected abstract int getImageResource();
}
