package com.fanny.ghmf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    private ImageView iv_return;
    private TextView tv_title;
    private Toolbar toolbar;
    private WebView mWeb_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        iv_return = (ImageView) findViewById(R.id.iv_web_return);
        tv_title = (TextView) findViewById(R.id.tv_web_title);
        iv_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWeb_content = (WebView) findViewById(R.id.web_content);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle result = intent.getBundleExtra("result");
        String url = result.getString("url");
        String title=result.getString("title");
        tv_title.setText(title);
        mWeb_content.loadUrl(url);

    }

}
