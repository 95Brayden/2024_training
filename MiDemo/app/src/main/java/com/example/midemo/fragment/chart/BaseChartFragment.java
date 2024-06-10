package com.example.midemo.fragment.chart;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.midemo.R;
import com.example.midemo.adapter.ChartItemAdapter;
import com.example.midemo.bean.ChartItemBean;
import com.example.midemo.dao.AccountDAO;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础图表BaseFragment类，负责通用的图表初始化和设置工作
 */
abstract public class BaseChartFragment extends Fragment {
    protected ListView chartLv;
    protected int year;
    protected int month;
    protected List<ChartItemBean> mDatas;   // 数据源
    private ChartItemAdapter itemAdapter;
    protected BarChart barChart;     // 代表柱状图的控件
    protected TextView chartTv;     // 如果没有收支情况，显示的TextView

    protected AccountDAO accountDAO;  // AccountDAO 对象
    protected Context mContext;  // 上下文对象

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;  // 保存上下文对象
        accountDAO = new AccountDAO(mContext);  // 初始化 AccountDAO 对象
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itemlv_chart, container, false);
        chartLv = view.findViewById(R.id.frag_chart_lv);
        // 获取Activity传递的数据
        Bundle bundle = getArguments();
        if (bundle != null) {
            year = bundle.getInt("year");
            month = bundle.getInt("month");
        }
        // 设置数据源
        mDatas = new ArrayList<>();
        // 设置适配器
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(itemAdapter);
        // 添加头布局
        addLVHeaderView();
        return view;
    }

    /**
     * 添加ListView的头布局
     */
    protected void addLVHeaderView() {
        // 将布局转换成View对象
        View headerView = getLayoutInflater().inflate(R.layout.item_chartfrag_top, null);
        // 将View添加到ListView的头布局上
        chartLv.addHeaderView(headerView);
        // 查找头布局当中包含的控件
        barChart = headerView.findViewById(R.id.item_chartfrag_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv);
        // 设定柱状图不显示描述
        barChart.getDescription().setEnabled(false);
        // 设置柱状图的内边距
        barChart.setExtraOffsets(20, 20, 20, 20);
        setAxis(year, month); // 设置坐标轴
        // 设置坐标轴显示的数据
        setAxisData(year, month);
    }

    /**
     * 设置坐标轴显示的数据
     * @param year 年份
     * @param month 月份
     */
    protected abstract void setAxisData(int year, int month);

    /**
     * 设置柱状图坐标轴的显示
     * @param year 年份
     * @param month 月份
     */
    protected void setAxis(int year, final int month) {
        // 设置X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 设置x轴显示在下方
        xAxis.setDrawGridLines(true);  // 设置绘制该轴的网格线
        // 设置x轴标签的个数
        xAxis.setLabelCount(31);
        xAxis.setTextSize(12f);  // x轴标签的大小
        // 设置X轴显示的值的格式
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int val = (int) value;
                if (val == 0) {
                    return month + "-1";
                }
                if (val == 14) {
                    return month + "-15";
                }
                // 根据不同的月份，显示最后一天的位置
                if (month == 2) {
                    if (val == 27) {
                        return month + "-28";
                    }
                } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                    if (val == 30) {
                        return month + "-31";
                    }
                } else if (month == 4 || month == 6 || month == 9 || month == 11) {
                    if (val == 29) {
                        return month + "-30";
                    }
                }
                return "";
            }
        });

        xAxis.setYOffset(10); // 设置标签对x轴的偏移量，垂直方向

        // y轴在子类的设置
        setYAxis(year, month);
    }

    /**
     * 设置y轴，因为最高的坐标不确定，所以在子类当中设置
     * @param year 年份
     * @param month 月份
     */
    protected abstract void setYAxis(int year, int month);

    /**
     * 设置日期
     * @param year 年份
     * @param month 月份
     */
    public void setDate(int year, int month) {
        this.year = year;
        this.month = month;
        // 清空柱状图当中的数据
        barChart.clear();
        barChart.invalidate();  // 重新绘制柱状图
        setAxis(year, month);
        setAxisData(year, month);
        // 加载新的数据
        loadData(year, month, getKind());
    }

    /**
     * 加载数据
     * @param year 年份
     * @param month 月份
     * @param kind 类型
     */
    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = accountDAO.getChartItemListFromAccount(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * 获取类型
     * @return 类型
     */
    protected abstract int getKind();
}
