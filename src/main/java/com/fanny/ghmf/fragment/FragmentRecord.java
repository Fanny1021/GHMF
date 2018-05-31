package com.fanny.ghmf.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.fanny.ghmf.MainActivity;
import com.fanny.ghmf.Presenter.BloodOxyPresenter;
import com.fanny.ghmf.Presenter.BloodPressurePresenter;
import com.fanny.ghmf.Presenter.BloodSugarPresenter;
import com.fanny.ghmf.Presenter.EcgPresenter;
import com.fanny.ghmf.Presenter.SleepPresenter;
import com.fanny.ghmf.Presenter.TempPresenter;
import com.fanny.ghmf.R;
import com.fanny.ghmf.adapter.BloodOxyAdapter;
import com.fanny.ghmf.adapter.BloodPressureAdapter;
import com.fanny.ghmf.adapter.BloodSugarAdapter;
import com.fanny.ghmf.adapter.EcgAdapter;
import com.fanny.ghmf.adapter.SleepAdapter;
import com.fanny.ghmf.adapter.TempAdapter;
import com.fanny.ghmf.bean.HealthRecordData;
import com.fanny.ghmf.bean.TempValueInfo;
import com.fanny.ghmf.util.ConstantUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Fanny on 18/3/30.
 */

public class FragmentRecord extends NetBaseFragment<HealthRecordData> {


    @InjectView(R.id.healthdata_listview)
    PullToRefreshListView healthdataListview;
    private String TAG;
    private BloodPressureAdapter adapter1;
    private BloodPressurePresenter presenter;
    private BloodSugarAdapter adapter2;
    private BloodOxyAdapter adapter3;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public interface CallBack {
        void onClick(ArrayList<TempValueInfo> tempValueList);
    }

    private CallBack callBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CallBack) {
            callBack = (CallBack) context;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_health_record;
    }

    @Override
    protected void initView(View normalView) {

        ButterKnife.inject(this, normalView);

        Bundle arguments = getArguments();
        String title = arguments.getString(ConstantUtil.TITLE);

        switch (title) {
            case "体温数据":
                TempPresenter tempPresenter = new TempPresenter(MainActivity.account);
                tempPresenter.getTempData(MainActivity.account, "temp");
                /**
                 * 下面获取到的list可以传递给fragenttendecny中
                 * 方法：通过接口给activity，再传递给tendency中
                 */
                ArrayList<TempValueInfo> tempValueList = tempPresenter.getTempValueList();
                callBack.onClick(tempValueList);

                TempAdapter adapter = new TempAdapter(getContext(), tempValueList);
                healthdataListview.setAdapter(adapter);
                break;
            case "血压数据":
                /**
                 * 方法一
                 */
//                BloodPressurePresenter bloodPressurePresenter=new BloodPressurePresenter(MainActivity.account);
//                bloodPressurePresenter.getBloodPressureData(MainActivity.account,"bloodpressure");
//                ArrayList<BloodPressureValueInfo> bloodPressureValueInfoArrayList=bloodPressurePresenter.getBloodPressureValueList();
//                BloodPressureAdapter bloodPressureAdapter=new BloodPressureAdapter(getContext(),bloodPressureValueInfoArrayList);
//                healthdataListview.setAdapter(bloodPressureAdapter);
                /**
                 * 方法二
                 */
                adapter1 = new BloodPressureAdapter(getActivity(), this);
                healthdataListview.setAdapter(adapter1);
                /**
                 * 开发初期没有服务器，代码走到这里时，界面需要等待打印log出来之后才会看到界面的数据显示
                 */
                presenter = new BloodPressurePresenter(MainActivity.account, adapter1);
                presenter.getBloodPressureData(MainActivity.account, "bloodpressure");

                break;

            case "血糖数据":
                adapter2 = new BloodSugarAdapter(getActivity(),this);
                healthdataListview.setAdapter(adapter2);

                BloodSugarPresenter presenter1=new BloodSugarPresenter(MainActivity.account,adapter2);
                presenter1.getBloodSugarData(MainActivity.account,"bloodsugar");

                break;

            case "血氧数据":
                adapter3 = new BloodOxyAdapter(getActivity(),this);
                healthdataListview.setAdapter(adapter3);

                BloodOxyPresenter presenter3=new BloodOxyPresenter(MainActivity.account,adapter3);
                presenter3.getBloodOxyData(MainActivity.account,"bloodoxy");

                break;

            case "心电数据":

                EcgAdapter adapter4=new EcgAdapter(getActivity(),this);
                healthdataListview.setAdapter(adapter4);

                EcgPresenter presenter4=new EcgPresenter(MainActivity.account,adapter4);
                presenter4.getEcgData(MainActivity.account,"ecg");

                break;

            case "睡眠数据":
                SleepAdapter adapter5=new SleepAdapter(getActivity(),this);
                healthdataListview.setAdapter(adapter5);

                SleepPresenter presenter5=new SleepPresenter(MainActivity.account,adapter5);
                presenter5.getSleepData(MainActivity.account,"sleep");


        }

        healthdataListview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.e("纪录下拉刷新列表", "onPullDownToRefresh");
                String label = DateUtils.formatDateTime(
                        getContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                String label = DateUtils.formatDateTime(
                        getContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                Log.e("纪录加载刷新列表", "onPullUpToRefresh");
                new GetDataTask().execute();
            }
        });

    }

    /**
     * 模拟刷新圆形进度
     */
    private class GetDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            /**
             * 说明：如果在这里刷新加载显示数据，一定注意使用同一个presenter实例，否则无法获取到同一个数据
             */
//            presenter.getBloodPressureData(MainActivity.account, "bloodpressure");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            healthdataListview.onRefreshComplete();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
