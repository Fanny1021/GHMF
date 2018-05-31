package com.fanny.ghmf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fanny.ghmf.adapter.RvAdapter;
import com.fanny.ghmf.util.SearchViewUtils;
import com.google.zxing.client.android.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayout mContentPanel;
    private CardView mCardViewSearch;
    private LinearLayout mSearchLayout;
    private ImageView mIvSearchBack;
    private EditText mEtSearch;
    private ImageView mClearSearch;
    private RecyclerView mRecycleview;
    private ImageView mIv_scan;
    private ImageView mIv_add;
    private LinearLayout mLL_introduce;
    private boolean isScan;
    private boolean isAdd;
    private LinearLayout mLL_scan;
    private LinearLayout mLL_add;
    private TextView tv_scanResult;
    private TextView mTv_conGuaid;
    private ImageView iv_search_return;
    private TextView mTv_conGuaid1;
    private Button btn_ok;
    private String scanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏显示
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_search);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setTitle("搜索设备");

        setSupportActionBar(mToolbar);

        iv_search_return = (ImageView) findViewById(R.id.iv_search_return);
        iv_search_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        mToolbar.setNavigationIcon(R.drawable.pic_return0);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        isScan = false;
        isAdd = false;

        findView();

        initResultItem();
//        initToolBar();
        initListener();
    }

    private int REQUEST_CODE = 0x07;

    private void initListener() {
        mIvSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchViewUtils.handleToolBar(getApplicationContext(), mCardViewSearch, mEtSearch);
            }
        });

        mIv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (isScan == false) {
//                    isScan = true;
                    mLL_introduce.setVisibility(View.GONE);
                    mLL_scan.setVisibility(View.VISIBLE);

//                }
                Intent scanIntent = new Intent(SearchActivity.this, CaptureActivity.class);
                startActivityForResult(scanIntent, REQUEST_CODE);

            }
        });

        mIv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mTv_conGuaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity.this,WebViewActivity.class);
                Bundle bundle=new Bundle();
                String title= (String) mTv_conGuaid.getText();
                bundle.putString("title",title);
                bundle.putString("url","http://192.168.1.107:8080/fanny/fan01.html");
//                SpannedString builder= (SpannedString) mTv_conGuaid.getText();
//                SpannedString[] spans = builder.getSpans(0, mTv_conGuaid.getText().length(), SpannedString.class);
//                for(SpannedString span:spans){
//                    Log.e("AAAAAA", String.valueOf(span));
//                }
//                SpannableStringBuilder builder= (SpannableStringBuilder) mTv_conGuaid.getText();
//                URLSpan[] urlSpan=builder.getSpans(0,mTv_conGuaid.getText().length(),URLSpan.class);
//                for(URLSpan span:urlSpan){
//                    Log.e("AAAAAA", String.valueOf(span));
//                }
//                bundle.putString("url",str);
                intent.putExtra("result",bundle);
                startActivity(intent);
            }
        });

        mTv_conGuaid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SearchActivity.this,WebViewActivity.class);
                Bundle bundle=new Bundle();
                String title= (String) mTv_conGuaid1.getText();
                bundle.putString("title",title);
                bundle.putString("url","http://192.168.1.107:8080/fanny/fanny07.html");
//                SpannedString builder= (SpannedString) mTv_conGuaid.getText();
//                SpannedString[] spans = builder.getSpans(0, mTv_conGuaid.getText().length(), SpannedString.class);
//                for(SpannedString span:spans){
//                    Log.e("AAAAAA", String.valueOf(span));
//                }
//                SpannableStringBuilder builder= (SpannableStringBuilder) mTv_conGuaid.getText();
//                URLSpan[] urlSpan=builder.getSpans(0,mTv_conGuaid.getText().length(),URLSpan.class);
//                for(URLSpan span:urlSpan){
//                    Log.e("AAAAAA", String.valueOf(span));
//                }
//                bundle.putString("url",str);
                intent.putExtra("result",bundle);
                startActivity(intent);
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scanCode!=null){
                    /**
                     * eventbus发送成功事件操作
                     */
                    EventBus.getDefault().post(new MessageEvent(scanCode,"IDNum"));
                    finish();
                }

            }
        });

    }

//    private void initToolBar() {
//        mToolbar.inflateMenu(R.menu.menu_search);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int menuItem = item.getItemId();
//                switch (menuItem) {
//                    case R.id.action_search:
//                        SearchViewUtils.handleToolBar(getApplicationContext(), mCardViewSearch, mEtSearch);
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                SearchViewUtils.handleToolBar(getApplicationContext(), mCardViewSearch, mEtSearch);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initResultItem() {
        ArrayList<String> list = new ArrayList<>();
        list.add("洗澡机");
        list.add("洗头机");
        list.add("护理床");
        list.add("护理器");
        list.add("智能监控");

        RvAdapter adapter = new RvAdapter(list, new RvAdapter.IListener() {

            @Override
            public void normalItemClick(String s) {
                Toast.makeText(SearchActivity.this, s, Toast.LENGTH_SHORT).show();
                mEtSearch.setText(s);

                mLL_introduce.setVisibility(View.GONE);
                mLL_scan.setVisibility(View.VISIBLE);

                tv_scanResult.setText("结果："+s);

                /**
                 * 隐藏搜索框
                 */
                SearchViewUtils.handleToolBar(getApplicationContext(), mCardViewSearch, mEtSearch);

            }

            @Override
            public void clearItemClick() {
                Toast.makeText(SearchActivity.this, "清除历史纪录", Toast.LENGTH_SHORT).show();
            }
        });
        mRecycleview.setAdapter(adapter);
        mRecycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();

    }

    private void findView() {
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mContentPanel = (LinearLayout) findViewById(R.id.contentPanel);
        mCardViewSearch = (CardView) findViewById(R.id.cardView_search);
        mSearchLayout = (LinearLayout) findViewById(R.id.search_layout);
        mIvSearchBack = (ImageView) findViewById(R.id.iv_search_back);
        mEtSearch = (EditText) findViewById(R.id.et_search);
        mClearSearch = (ImageView) findViewById(R.id.clearSearch);
        mRecycleview = (RecyclerView) findViewById(R.id.recycleview);
        mIv_scan = (ImageView) findViewById(R.id.iv_scanEquip);
        mIv_add = (ImageView) findViewById(R.id.iv_addEquip);
        mLL_introduce = (LinearLayout) findViewById(R.id.ll_introduce);

        mTv_conGuaid = (TextView) findViewById(R.id.tv_connect_guaid);
        mTv_conGuaid1 = (TextView) findViewById(R.id.tv_connect_guaid1);
//        mTv_conGuaid.setMovementMethod(LinkMovementMethod.getInstance());

        mLL_scan = (LinearLayout) findViewById(R.id.ll_scan);
        tv_scanResult = (TextView) findViewById(R.id.tv_scanResult);
        btn_ok = (Button) findViewById(R.id.btn_device_add_ok);
        mLL_add = (LinearLayout) findViewById(R.id.ll_add);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if(resultCode==REQUEST_CODE){
                if (null != data) {
                    Bundle bundle = data.getBundleExtra("result");

                    if (bundle == null) {
                        Log.e("AAAAAAAAAAAAAAA","null为空"+bundle.toString());
                        tv_scanResult.setText("结果："+"未搜索到结果");
                        return;
                    }else {
                        Log.e("AAAAAAAAAAAAAAA","null不为空"+bundle.getString("resultStr"));
                        /**
                         * scanCode ：扫描获取到的信息
                         */
                        scanCode = bundle.getString("resultStr");
                        tv_scanResult.setText("结果："+ scanCode);
                        /**
                         * eventbus发送成功事件操作
                         */
//                        EventBus.getDefault().post(new MessageEvent(scanCode,"IDNum"));
                    }

                }
            }

        }
    }
}
