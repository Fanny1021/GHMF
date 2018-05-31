package com.fanny.ghmf.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.fanny.ghmf.MainActivity;
import com.fanny.ghmf.Presenter.TempPresenter;
import com.fanny.ghmf.R;
import com.fanny.ghmf.bean.HealthRecordData;
import com.fanny.ghmf.bean.TempValueInfo;
import com.fanny.ghmf.util.ConstantUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fanny on 18/3/30.
 */

public class FragmentTrendency extends NetBaseFragment<HealthRecordData> {


    @InjectView(R.id.chat_trentency)
    LineChart chatTrentency;
    private EditText et_user;
    private String TAG = "FragmentTrendency";
    private Description description;
    private Set<String> titles;
    private ArrayList<LineDataSet> lines = new ArrayList<>();
    private XAxis xAxis;
    private ArrayList<TempValueInfo> tempValueList;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_trendency;
    }


    private List<Data> datas;

    public class Data {
        public String xAxisValue;
        public float yValue;
        public float xValue;

        public Data(float xValue, float yValue, String xAxisValue) {
            this.xAxisValue = xAxisValue;
            this.yValue = yValue;
            this.xValue = xValue;
        }
    }

    public void setTempList(ArrayList<TempValueInfo> tempValueList) {
        this.tempValueList = tempValueList;
    }

    public ArrayList<TempValueInfo> getTempList() {
        return tempValueList;
    }

    @Override
    public void onStart() {
        super.onStart();
        tempValueList = getTempList();

        /**
         * 刷新图表
         */
//        drawChat();
    }

    @Override
    protected void initView(View normalView) {
        ButterKnife.inject(this, normalView);

        Bundle arguments = getArguments();
        String title = arguments.getString(ConstantUtil.TITLE);

        /**
         * 初始化表格
         * chatTrentency
         * &
         * description
         */
        initChatStyle();
        description = new Description();
        initDescription();
        chatTrentency.setDescription(description);

        /**
         * 初始化数据
         */
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();
        ArrayList<Entry> values3 = new ArrayList<>();
        ArrayList<Entry> values4 = new ArrayList<>();

        /**
         * 初始化map
         */
        HashMap<String, ArrayList<Entry>> map = new HashMap<>();


        switch (title) {
            case "体温数据":

                values1 = new ArrayList<>();

//                TempPresenter tempPresenter = new TempPresenter(MainActivity.account);
//                tempPresenter.getTempData(MainActivity.account, "temp");
//                tempValueList = tempPresenter.getTempValueList();

                if (tempValueList.size() <= 0) {
                    break;
                }
                Log.e(TAG, tempValueList.size() + "");

                for (int m = 0; m < tempValueList.size(); m++) {

                    String temp = tempValueList.get(m).getValue();
                    float v = Float.parseFloat(temp);
                    String date = tempValueList.get(m).getTime();
                    Data d = new Data(m, v, date);
                    if (datas == null) {
                        datas = new ArrayList<>();
                    }
                    datas.add(d);
                    values1.add(new Entry(d.xValue, d.yValue));
                }

                /**
                 * 处理x坐标的单位
                 */
                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return datas.get(Math.min(Math.max((int) value, 0), datas.size() - 1)).xAxisValue;
                    }
                });

                /**
                 * 前期虚拟数据
                 */
                /**
                 * Entry 坐标点对象  构造函数 第一个参数为x点坐标 第二个为y点
                 //                 */
//                values1.add(new Entry(4, (float) 24.5));
//                values1.add(new Entry(8, (float) 25.5));
//                values1.add(new Entry(12, (float) 23.6));
//                values1.add(new Entry(16, (float) 27.8));
//                values1.add(new Entry(20, (float) 27.4));
//                values1.add(new Entry(24, (float) 27.2));
//                values1.add(new Entry(28, (float) 28.2));
//                values1.add(new Entry(32, (float) 27.8));
//                values1.add(new Entry(36, (float) 27.0));
//                values1.add(new Entry(40, (float) 26.9));

//                drawOneLineChat(title, values1);

                map.put(title, values1);

                break;

            case "血压数据":

                if (values1.size() > 0) {
                    values1.clear();
                }

                values1.add(new Entry(4, 84));
                values1.add(new Entry(8, 88));
                values1.add(new Entry(12, 86));
                values1.add(new Entry(16, 90));
                values1.add(new Entry(20, 84));
                values1.add(new Entry(24, 82));
                values1.add(new Entry(28, 92));
                values1.add(new Entry(32, 88));
                values1.add(new Entry(36, 80));
                values1.add(new Entry(40, 79));

                if (values2.size() > 0) {
                    values2.clear();
                }
                values2.add(new Entry(4, 120));
                values2.add(new Entry(8, 125));
                values2.add(new Entry(12, 140));
                values2.add(new Entry(16, 130));
                values2.add(new Entry(20, 120));
                values2.add(new Entry(24, 122));
                values2.add(new Entry(28, 110));
                values2.add(new Entry(32, 121));
                values2.add(new Entry(36, 108));
                values2.add(new Entry(40, 107));

                if (values3.size() > 0) {
                    values3.clear();
                }
                values3.add(new Entry(4, 74));
                values3.add(new Entry(8, 78));
                values3.add(new Entry(12, 76));
                values3.add(new Entry(16, 80));
                values3.add(new Entry(20, 74));
                values3.add(new Entry(24, 72));
                values3.add(new Entry(28, 82));
                values3.add(new Entry(32, 78));
                values3.add(new Entry(36, 70));
                values3.add(new Entry(40, 69));

                if (values4.size() > 0) {
                    values4.clear();
                }
                values4.add(new Entry(4, 110));
                values4.add(new Entry(8, 115));
                values4.add(new Entry(12, 130));
                values4.add(new Entry(16, 120));
                values4.add(new Entry(20, 110));
                values4.add(new Entry(24, 112));
                values4.add(new Entry(28, 100));
                values4.add(new Entry(32, 111));
                values4.add(new Entry(36, 98));
                values4.add(new Entry(40, 97));

//                drawFourLineChat("收缩压数据", "舒张压数据", "脉率数据", "平均压数据", values1, values2, values3, values4);
                map.put("收缩压数据", values1);
                map.put("舒张压数据", values2);
                map.put("脉率数据", values3);
                map.put("平均压数据", values4);
                break;

            case "血糖数据":

                if (values1.size() > 0) {
                    values1.clear();
                }
                values1.add(new Entry(4, 74));
                values1.add(new Entry(8, 78));
                values1.add(new Entry(12, 76));
                values1.add(new Entry(16, 80));
                values1.add(new Entry(20, 74));
                values1.add(new Entry(24, 72));
                values1.add(new Entry(28, 82));
                values1.add(new Entry(32, 78));
                values1.add(new Entry(36, 70));
                values1.add(new Entry(40, 69));

                if (values2.size() > 0) {
                    values2.clear();
                }
                values2.add(new Entry(4, 110));
                values2.add(new Entry(8, 115));
                values2.add(new Entry(12, 130));
                values2.add(new Entry(16, 120));
                values2.add(new Entry(20, 110));
                values2.add(new Entry(24, 112));
                values2.add(new Entry(28, 100));
                values2.add(new Entry(32, 111));
                values2.add(new Entry(36, 98));
                values2.add(new Entry(40, 97));

                if (values3.size() > 0) {
                    values3.clear();
                }
                values3.add(new Entry(4, 10));
                values3.add(new Entry(8, 15));
                values3.add(new Entry(12, 30));
                values3.add(new Entry(16, 20));
                values3.add(new Entry(20, 10));
                values3.add(new Entry(24, 12));
                values3.add(new Entry(28, 10));
                values3.add(new Entry(32, 11));
                values3.add(new Entry(36, 28));
                values3.add(new Entry(40, 27));

                map.put("血糖数据", values1);
                map.put("尿酸数据", values2);
                map.put("总胆固醇数据", values3);

                break;

            case "血氧数据":

                if (values1.size() > 0) {
                    values1.clear();
                }

                values1.add(new Entry(4, 74));
                values1.add(new Entry(8, 78));
                values1.add(new Entry(12, 76));
                values1.add(new Entry(16, 80));
                values1.add(new Entry(20, 74));
                values1.add(new Entry(24, 72));
                values1.add(new Entry(28, 82));
                values1.add(new Entry(32, 78));
                values1.add(new Entry(36, 70));
                values1.add(new Entry(40, 69));


                if (values2.size() > 0) {
                    values2.clear();
                }

                values2.add(new Entry(4, 110));
                values2.add(new Entry(8, 115));
                values2.add(new Entry(12, 130));
                values2.add(new Entry(16, 120));
                values2.add(new Entry(20, 110));
                values2.add(new Entry(24, 112));
                values2.add(new Entry(28, 100));
                values2.add(new Entry(32, 111));
                values2.add(new Entry(36, 98));
                values2.add(new Entry(40, 97));

                if (values3.size() > 0) {
                    values3.clear();
                }

                values3.add(new Entry(4, 10));
                values3.add(new Entry(8, 15));
                values3.add(new Entry(12, 30));
                values3.add(new Entry(16, 20));
                values3.add(new Entry(20, 10));
                values3.add(new Entry(24, 12));
                values3.add(new Entry(28, 10));
                values3.add(new Entry(32, 11));
                values3.add(new Entry(36, 28));
                values3.add(new Entry(40, 27));


                map.put("血氧数据", values1);
                map.put("脉率数据", values2);
                map.put("血流灌注值数据", values3);

                break;

            case "心电数据":
                if (values1.size() > 0) {
                    values1.clear();
                }

                values1.add(new Entry(4, 74));
                values1.add(new Entry(8, 78));
                values1.add(new Entry(12, 76));
                values1.add(new Entry(16, 80));
                values1.add(new Entry(20, 74));
                values1.add(new Entry(24, 72));
                values1.add(new Entry(28, 82));
                values1.add(new Entry(32, 78));
                values1.add(new Entry(36, 70));
                values1.add(new Entry(40, 69));
                map.put("心电数据", values1);

                break;

            case "睡眠数据":
                if (values1.size() > 0) {
                    values1.clear();
                }

                values1.add(new Entry(4, 74));
                values1.add(new Entry(8, 78));
                values1.add(new Entry(12, 76));
                values1.add(new Entry(16, 80));
                values1.add(new Entry(20, 74));
                values1.add(new Entry(24, 72));
                values1.add(new Entry(28, 82));
                values1.add(new Entry(32, 78));
                values1.add(new Entry(36, 70));
                values1.add(new Entry(40, 69));


                if (values2.size() > 0) {
                    values2.clear();
                }

                values2.add(new Entry(4, 110));
                values2.add(new Entry(8, 115));
                values2.add(new Entry(12, 130));
                values2.add(new Entry(16, 120));
                values2.add(new Entry(20, 110));
                values2.add(new Entry(24, 112));
                values2.add(new Entry(28, 100));
                values2.add(new Entry(32, 111));
                values2.add(new Entry(36, 98));
                values2.add(new Entry(40, 97));

                if (values3.size() > 0) {
                    values3.clear();
                }

                values3.add(new Entry(4, 10));
                values3.add(new Entry(8, 15));
                values3.add(new Entry(12, 30));
                values3.add(new Entry(16, 20));
                values3.add(new Entry(20, 10));
                values3.add(new Entry(24, 12));
                values3.add(new Entry(28, 10));
                values3.add(new Entry(32, 11));
                values3.add(new Entry(36, 28));
                values3.add(new Entry(40, 27));


                map.put("脉搏血氧饱和度", values1);
                map.put("脉率", values2);
                map.put("血流灌注指数", values3);
                break;

            default:

                break;

        }

        /**
         * 画表
         */
        if (map.size() > 0) {
            drawChat(map);
        }

    }

    public void setLineStyle(LineDataSet set, int colorId) {

        //设置数据1  参数1：数据源 参数2：图例名称
        set.setColor(colorId);
        set.setCircleColor(colorId);
        set.setLineWidth(1f);//设置线宽
        set.setCircleRadius(3f);//设置焦点圆心的大小
        set.enableDashedHighlightLine(10f, 5f, 0f);//点击后的高亮线的显示样式
        set.setHighlightLineWidth(2f);//设置点击交点后显示高亮线宽
        set.setHighlightEnabled(true);//是否禁用点击高亮线
        set.setHighLightColor(Color.RED);//设置点击交点后显示交高亮线的颜色
        set.setValueTextSize(9f);//设置显示值的文字大小
        set.setDrawFilled(false);//设置禁用范围背景填充
        //格式化显示数据
        final DecimalFormat mFormat = new DecimalFormat("###,###,##0");
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return mFormat.format(value);
            }
        });
        if (Utils.getSDKInt() >= 18) {
            // fill drawable only supported on api level 18 and above
//                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
//                set1.setFillDrawable(drawable);//设置范围背景填充
            set.setFillColor(Color.BLACK);
        } else {
            set.setFillColor(Color.BLACK);
        }
    }

    int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW};


    private void drawChat(HashMap<String, ArrayList<Entry>> datas) {

        titles = datas.keySet();//title的set集合
//        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        Iterator<String> it = titles.iterator();

        int i = 0;
        //判断图表1中原来是否有数据
        if (chatTrentency.getData() != null &&
                chatTrentency.getData().getDataSetCount() > 0) {
            /**
             * 获取数据
             */
//            int i = 0;
            while (it.hasNext()) {

                String title = it.next();

                ArrayList<Entry> value = datas.get(title);

//                ILineDataSet set = dataSets.get(i);
                LineDataSet set = lines.get(i);
                //获取数据1
                set = (LineDataSet) chatTrentency.getData().getDataSetByIndex(i);
                set.setValues(value);

                i++;
            }
            /**
             * 刷新数据
             */
            chatTrentency.getData().notifyDataChanged();
            chatTrentency.notifyDataSetChanged();

        } else {
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            int j = 0;
            /**
             * 设置数据
             */
            while (it.hasNext()) {

                String title = it.next();

                ArrayList<Entry> value = datas.get(title);

                LineDataSet set = new LineDataSet(value, title);
                setLineStyle(set, colors[i]);
                //保存LineDataSet集合
                dataSets.add(set); // add the datasets
                //创建LineData对象 属于LineChart折线图的数据集合
                i++;
            }

            LineData data = new LineData(dataSets);
            // 添加到图表中
            chatTrentency.setData(data);
            //绘制图表
            chatTrentency.invalidate();

        }

    }

    private void initChatStyle() {
        chatTrentency.setDrawBorders(false);

        chatTrentency.setDrawGridBackground(true);

        chatTrentency.setGridBackgroundColor(Color.WHITE & 0x70ffffff);

        chatTrentency.setTouchEnabled(true);

        chatTrentency.setDragEnabled(true);

        chatTrentency.setScaleEnabled(true);

        chatTrentency.setPinchZoom(false);

        chatTrentency.setBackgroundColor(Color.GRAY);

        Legend mLegend = chatTrentency.getLegend();
        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setFormSize(6f); //字体
        mLegend.setTextColor(Color.WHITE); //颜色

//        chat1.setVisibleXRange(1, 7);   //x轴可显示的坐标范围
        //x轴的标示
        xAxis = chatTrentency.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
//        xAxis.setTextColor(Color.WHITE);    //字体的颜色
//        xAxis.setTextSize(10f); //字体大小
        xAxis.setGridColor(Color.WHITE);//网格线颜色
        xAxis.setDrawGridLines(true); //不显示网格线
//        xAxis.setValueFormatter();
//        xAxis.setTypeface(mTf);

        YAxis axisLeft = chatTrentency.getAxisLeft(); //y轴左边标示
        YAxis axisRight = chatTrentency.getAxisRight(); //y轴右边标示
//        axisLeft.setTextColor(Color.WHITE); //字体颜色
//        axisLeft.setTextSize(10f); //字体大小
//        axisLeft.setAxisMaxValue(1000f); //最大值
        axisLeft.setLabelCount(6, true); //显示格数
        axisLeft.setGridColor(Color.WHITE); //网格线颜色
//        axisLeft.setTypeface(mTf);

        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(false);

    }

    private void initDescription() {
        description.setText("数据分析图表");
        description.setTextColor(Color.RED);
        description.setTextSize(10);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}


//
//    private void drawFourLineChat(String title1,
//                                  String title2,
//                                  String title3,
//                                  String title4,
//                                  ArrayList<Entry> values1,
//                                  ArrayList<Entry> values2,
//                                  ArrayList<Entry> values3,
//                                  ArrayList<Entry> values4) {
//
//        LineDataSet set1;
//        LineDataSet set2;
//        LineDataSet set3;
//        LineDataSet set4;
//
//        if (chatTrentency.getData() != null &&
//                chatTrentency.getData().getDataSetCount() > 0) {
//            //获取数据1
//            set1 = (LineDataSet) chatTrentency.getData().getDataSetByIndex(0);
//            set1.setValues(values1);
//            set2 = (LineDataSet) chatTrentency.getData().getDataSetByIndex(1);
//            set2.setValues(values2);
//            set3 = (LineDataSet) chatTrentency.getData().getDataSetByIndex(2);
//            set3.setValues(values3);
//            set4 = (LineDataSet) chatTrentency.getData().getDataSetByIndex(3);
//            set4.setValues(values4);
//            //刷新数据
//            chatTrentency.getData().notifyDataChanged();
//            chatTrentency.notifyDataSetChanged();
//        } else {
//            set1 = new LineDataSet(values1, title1);
//            setLineStyle(set1, Color.BLUE);
//
//            set2 = new LineDataSet(values2, title2);
//            setLineStyle(set2, Color.GREEN);
//
//            set3 = new LineDataSet(values3, title3);
//            setLineStyle(set3, Color.RED);
//
//            set4 = new LineDataSet(values4, title4);
//            setLineStyle(set4, Color.YELLOW);
//
//            //保存LineDataSet集合
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1); // add the datasets
//            dataSets.add(set2);
//            dataSets.add(set3); // add the datasets
//            dataSets.add(set4);
//            //创建LineData对象 属于LineChart折线图的数据集合
//            LineData data = new LineData(dataSets);
//            // 添加到图表中
//            chatTrentency.setData(data);
//            //绘制图表
//            chatTrentency.invalidate();
//
//        }
//    }
//
//
//    private void drawOneLineChat(String title, ArrayList<Entry> values1) {
//
//        LineDataSet set1;
//        /**
//         * 处理图表
//         */
//        //判断图表1中原来是否有数据
//        if (chatTrentency.getData() != null &&
//                chatTrentency.getData().getDataSetCount() > 0) {
//            //获取数据1
//            set1 = (LineDataSet) chatTrentency.getData().getDataSetByIndex(0);
//            set1.setValues(values1);
//            //刷新数据
//            chatTrentency.getData().notifyDataChanged();
//            chatTrentency.notifyDataSetChanged();
//        } else {
//            set1 = new LineDataSet(values1, title);
//            setLineStyle(set1, Color.BLUE);
//            //保存LineDataSet集合
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(set1); // add the datasets
//            //创建LineData对象 属于LineChart折线图的数据集合
//            LineData data = new LineData(dataSets);
//            // 添加到图表中
//            chatTrentency.setData(data);
//            //绘制图表
//            chatTrentency.invalidate();
//        }
//    }