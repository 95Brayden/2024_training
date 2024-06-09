package com.example.midemo.fragment.record;


import com.example.midemo.R;
import com.example.midemo.db.DBManager;
import com.example.midemo.bean.TypeBean;

import java.util.List;

/**
*记录页面当中的支出模块
 */
public class OutcomeFragment extends BaseRecordFragment {
    // 重写
    @Override
    public void loadDataToGV() {
        super.loadDataToGV();
        //获取数据库当中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccountToDB() {
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}