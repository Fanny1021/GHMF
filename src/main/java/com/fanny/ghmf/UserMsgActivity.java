package com.fanny.ghmf;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.fanny.ghmf.util.SharePrefrenceUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserMsgActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.usermsg_return)
    ImageView usermsgReturn;
    @InjectView(R.id.edit_toogle_r)
    ImageView editToogleR;
    @InjectView(R.id.edit_toogle_r1)
    ImageView editToogleR1;
    @InjectView(R.id.edit_toogle_r2)
    ImageView editToogleR2;
    @InjectView(R.id.btn_reLogin)
    Button btnReLogin;
    @InjectView(R.id.ll_edit_photo)
    LinearLayout llEditPhoto;
    @InjectView(R.id.ll_edit_name)
    LinearLayout llEditName;
    @InjectView(R.id.ll_edit_wechat)
    LinearLayout llEditWechat;
    @InjectView(R.id.ll_edit_psw)
    LinearLayout llEditPsw;
    @InjectView(R.id.tv_user_name)
    TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_msg);
        ButterKnife.inject(this);

        initData();

        usermsgReturn.setOnClickListener(this);
        btnReLogin.setOnClickListener(this);
        llEditName.setOnClickListener(this);


    }

    private void initData() {
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("data");
        tvUserName.setText((String)extras.get("account"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.usermsg_return:
                /**
                 * 重新确认用户资料，更新全局信息
                 */
                String wholeName = (String) SharePrefrenceUtil.getData(getBaseContext(), SharePrefrenceUtil.USER_NAME, "");
                String wholePhoto = "url";
                String wholePhone = "";
                String wholePsw = (String) SharePrefrenceUtil.getData(getBaseContext(), SharePrefrenceUtil.USER_PSW, "");
                MainActivity.account = wholeName;
                MainActivity.psw = wholePsw;
                /**
                 * 更新信息
                 */
                EventBus.getDefault().post(new UserEvent(wholeName, wholePsw, wholePhoto, wholePhone));

                finish();
                break;
            case R.id.btn_reLogin:
                /**
                 * 清除sp中数据，注销登录
                 */
                SharePrefrenceUtil.selectParam(getBaseContext(), SharePrefrenceUtil.USER_NAME);
                SharePrefrenceUtil.selectParam(getBaseContext(), SharePrefrenceUtil.USER_PSW);
                Intent intent = new Intent(UserMsgActivity.this, UserLoginActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.ll_edit_name:
                /**
                 * 修改姓名
                 */
                final MaterialDialog.Builder editNameDialog = new MaterialDialog.Builder(this);
                View view = View.inflate(this, R.layout.dia_edit_name, null);
                final EditText et_user_name = (EditText) view.findViewById(R.id.et_user_name);
                et_user_name.setText(tvUserName.getText());
                ImageView im_clear= (ImageView) view.findViewById(R.id.im_user_clear);
                im_clear.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        et_user_name.setText("");
                    }
                });

                editNameDialog.customView(view, true);
                editNameDialog.positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String endName = et_user_name.getText().toString();
                                if (!endName.equals("")){
                                    tvUserName.setText(endName);
                                    /**
                                     * 保存修改后的姓名
                                     */
                                    SharePrefrenceUtil.saveParam(getBaseContext(),SharePrefrenceUtil.USER_NAME,endName);
                                }
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                            }
                        })
                        .show();
                break;
            case R.id.ll_edit_photo:

                break;
            case R.id.ll_edit_wechat:

                break;
            case R.id.ll_edit_psw:

                break;
        }
    }
}
