package com.fanny.ghmf;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fanny.ghmf.util.SharePrefrenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserLoginActivity extends AppCompatActivity {


    @InjectView(R.id.login_account)
    ImageView loginAccount;
    @InjectView(R.id.et_login_user)
    EditText etLoginUser;
    @InjectView(R.id.login_psw)
    ImageView loginPsw;
    @InjectView(R.id.et_login_psw)
    EditText etLoginPsw;
    @InjectView(R.id.login_center)
    Button loginCenter;
    private String account;
    private String pssword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.inject(this);


        initData();

    }

    private void initData() {


        /**
         * 开机自带账号和密码
         */
        String name = (String) SharePrefrenceUtil.getData(getBaseContext(), SharePrefrenceUtil.HOST_USER_NAME, "");
        String psw = (String) SharePrefrenceUtil.getData(getBaseContext(), SharePrefrenceUtil.HOST_USER_PSW, "");
        if (!name.equals("") && !psw.equals("")) {
            etLoginUser.setText(name);
            etLoginPsw.setText(psw);
        } else {
            etLoginUser.setText("admin");
            etLoginPsw.setText("123456");
        }

        account = etLoginUser.getText().toString();
        pssword = etLoginPsw.getText().toString();
    }

    private boolean check(String account, String pssword) {

        Log.e("login", String.valueOf(account.length()));
        /**
         * 先对编辑框信息进行验证
         */
        if (!(account.length() > 0) || !(pssword.length() > 0)) {
            return false;
        }else {
//            if(!checkDatabase(account, pssword)){
//                return false;
//            }
        }
        /**
         * 对用户信息进行发送服务器端进行验证
         * 在该方法中进行操作（连网操作）
         * 若用户存在则返回true，反之false
         */

        /********************************连网操作**********************************/


        /**
         * 此处大量代码待定
         */



        return true;
    }

    private boolean checkDatabase(String account, String pssword) {

        SQLiteDatabase db = MyApp.dbopenHelper.getReadableDatabase();
        Cursor cursor = db.query("hoster", null, "account like ?", new String[]{account}, null, null, null,null);
        if (cursor != null ) {
            int i=0;
            while (cursor.moveToNext()) {
                if( cursor.getString(1).equals(pssword)){
                    i++;
                }
            }
            if(i==0){
                return false;
            }
        }else {
            return false;
        }

        return true;
    }

    @OnClick({R.id.et_login_psw, R.id.login_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_login_psw:

                break;
            case R.id.login_center:



                boolean checkResult = check(account, pssword);

                if(checkResult==true){
                    /**
                     * 保存用户信息至本地
                     */
                    SharePrefrenceUtil.saveParam(getBaseContext(),SharePrefrenceUtil.USER_NAME,account);
                    SharePrefrenceUtil.saveParam(getBaseContext(),SharePrefrenceUtil.USER_PSW,pssword);

                    /**
                     * 绑定注册用户数据
                     */
                    Bundle bundle=new Bundle();
                    bundle.putString("account","龚梦帆");//这里人为将account写为姓名中文
                    bundle.putString("psw",pssword);

                    /**
                     * 携带用户信息，跳转至主界面
                     */
                    Intent mainIntent=new Intent(UserLoginActivity.this,MainActivity.class);
                    mainIntent.putExtra("data",bundle);
                    this.startActivity(mainIntent);
                    this.finish();
                }else {
                    Toast.makeText(this,"用户未注册",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
