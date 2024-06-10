package com.example.midemo.fragment.record;

import com.example.midemo.R;

/**
 * 收入记录页面
 */
public class IncomeFragment extends RecordFragment {

    @Override
    protected int getTypeKind() {
        return 1;
    }

    @Override
    protected int getImageResource() {
        return R.mipmap.in_qt_fs;
    }
}
