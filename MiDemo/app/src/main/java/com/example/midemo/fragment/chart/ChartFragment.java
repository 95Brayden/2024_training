package com.example.midemo.fragment.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.midemo.bean.BarChartItemBean;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * 具体图表ChartFragment类，负责特定的图表数据处理和特定行为
 */
public abstract class ChartFragment extends BaseChartFragment {
    protected int kind;

    @Override
    public void onResume() {
        super.onResume();
        loadData(year, month, kind);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置柱状图的X轴和Y轴的数据
     * @param year 年份
     * @param month 月份
     */
    @Override
    protected void setAxisData(int year, int month) {
        List<IBarDataSet> sets = new ArrayList<>();
        // 获取这个月每天的支出或收入总金额
        List<BarChartItemBean> list = accountDAO.getSumMoneyOneDayInMonth(year, month, kind);

        if (list.isEmpty()) {
            barChart.setVisibility(View.GONE);
            chartTv.setVisibility(View.VISIBLE);
        } else {
            barChart.setVisibility(View.VISIBLE);
            chartTv.setVisibility(View.GONE);

            // 设置有多少根柱子
            List<BarEntry> barEntries1 = new ArrayList<>();
            for (int i = 0; i < 31; i++) {
                // 初始化每一根柱子，添加到柱状图当中
                BarEntry entry = new BarEntry(i, 0.0f);
                barEntries1.add(entry);
            }

            for (BarChartItemBean itemBean : list) {
                int day = itemBean.getDay();   // 获取日期
                // 根据天数，获取x轴的位置
                int xIndex = day - 1;
                BarEntry barEntry = barEntries1.get(xIndex);
                barEntry.setY(itemBean.getSumMoney());
            }

            BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
            barDataSet1.setValueTextColor(Color.BLACK); // 值的颜色
            barDataSet1.setValueTextSize(8f); // 值的大小
            barDataSet1.setColor(getBarColor()); // 柱子的颜色

            // 设置柱子上数据显示的格式
            barDataSet1.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    // 此处的value默认保留一位小数
                    if (value == 0) {
                        return "";
                    }
                    return String.valueOf(value);
                }
            });

            sets.add(barDataSet1);

            BarData barData = new BarData(sets);
            barData.setBarWidth(0.2f); // 设置柱子的宽度
            barChart.setData(barData);
        }
    }

    /**
     * 设置Y轴的显示
     * @param year 年份
     * @param month 月份
     */
    @Override
    protected void setYAxis(int year, int month) {
        // 获取本月收入或支出最高的一天为多少，将他设定为y轴的最大值
        float maxMoney = accountDAO.getMaxMoneyOneDayInMonth(year, month, kind);
        float max = (float) Math.ceil(maxMoney);   // 将最大金额向上取整
        // 设置y轴
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setAxisMaximum(max);  // 设置y轴的最大值
        yAxisRight.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxisRight.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setAxisMaximum(max);
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.setEnabled(false);

        // 设置不显示图例
        Legend legend = barChart.getLegend();
        legend.setEnabled(true);
    }

    @Override
    protected int getKind() {
        return this.kind;
    }

    /**
     * 获取柱子的颜色
     * @return 柱子的颜色
     */
    protected abstract int getBarColor();
}
