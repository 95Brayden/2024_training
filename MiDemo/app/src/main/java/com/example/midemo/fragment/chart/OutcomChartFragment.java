package com.example.midemo.fragment.chart;

import android.graphics.Color;

/**
 * A simple {@link ChartFragment} subclass.
 */
public class OutcomChartFragment extends ChartFragment {
    public OutcomChartFragment() {
        kind = 0; // 设置支出类型
    }

    @Override
    protected int getBarColor() {
        return Color.RED; // 设置支出柱子的颜色
    }
}
