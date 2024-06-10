package com.example.midemo.fragment.chart;

import android.graphics.Color;

/**
 * A simple {@link ChartFragment} subclass.
 */
public class IncomeChartFragment extends ChartFragment {

    public IncomeChartFragment() {
        kind = 1; // 设置收入类型
    }

    @Override
    protected int getBarColor() {
        return Color.BLUE; // 设置收入柱子的颜色
    }
}
