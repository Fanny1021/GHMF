package com.fanny.ghmf.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.fanny.ghmf.R;
import com.fanny.ghmf.util.SharePrefrenceUtil;
import com.fanny.ghmf.util.SocketUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketConDialog extends DialogFragment {

    private Socket socket;
    private AlertDialog.Builder socket_con_builder;
    private View socket_con_view;
    private AutoCompleteTextView socket_con_ip;
    private AlertDialog socket_con_dialog;
    private EditText socket_con_port;
    private Button socket_con_connect;
    private Button socket_con_disconnect;
    private String strIP;
    private String strPort;
    private RelativeLayout rl_progress;
    private ProgressBar pb;
    private TextView tv_result;

    @Override  //适合对简单dialog进行处理，可以利用Dialog.Builder直接返回Dialog对象
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        socket_con_builder = new AlertDialog.Builder(getActivity());
        socket_con_view = View.inflate(getActivity(), R.layout.dialog_socket_connect, null);

        /**
         * 连接进度
         */
        rl_progress = (RelativeLayout) socket_con_view.findViewById(R.id.rl_progress);
        pb = (ProgressBar) socket_con_view.findViewById(R.id.pb_socket_con);
        tv_result = (TextView) socket_con_view.findViewById(R.id.tv_con_pb);

        socket_con_ip = (AutoCompleteTextView) socket_con_view.findViewById(R.id.auto_tv_ip);
        // 获取搜索ip记录文件内容
        String history = (String) SharePrefrenceUtil.getData(getContext(), "ip_history", "");
        // 用逗号分割内容返回数组
        String[] history_arr = history.split(",");

        ArrayAdapter<String> arr_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, history_arr);

        // 保留前50条数据
        if (history_arr.length > 50) {
            String[] newArrays = new String[50];
            // 实现数组之间的复制
            System.arraycopy(history_arr, 0, newArrays, 0, 50);
            arr_adapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_dropdown_item_1line, history_arr);
        }
        // 设置ip选择框的适配器
        socket_con_ip.setAdapter(arr_adapter);
        socket_con_port = (EditText) socket_con_view.findViewById(R.id.et_socket_port);
        /**
         * 显示最后一次ip地址和port
         */
        String last_ip = (String) SharePrefrenceUtil.getData(getContext(), "SocketIp", "");
        String last_port = (String) SharePrefrenceUtil.getData(getContext(), "SocketPort", "");
        if (!last_ip.equals("") && !last_port.equals("")) {
            socket_con_ip.setText(last_ip);
            socket_con_port.setText(last_port);
        }

        socket_con_connect = (Button) socket_con_view.findViewById(R.id.btn_socket_connect);
        socket_con_disconnect = (Button) socket_con_view.findViewById(R.id.btn_socket_disconnect);

        View titleView = View.inflate(getActivity(), R.layout.dia_title, null);
        socket_con_builder.setTitle("手动连接网络")
                .setCustomTitle(titleView)
                .setView(socket_con_view);
        socket_con_dialog = socket_con_builder.create();
        socket_con_dialog.setCanceledOnTouchOutside(false);

        return socket_con_dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        socket_con_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 显示进度布局
                 */
                rl_progress.setVisibility(View.VISIBLE);

                if (SocketUtil.socket != null) {
                    /**
                     * 清空连接的socket
                     */
                    try {
                        SocketUtil.socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                String text = socket_con_ip.getText().toString();
                //保存ip地址
                String[] arr = text.split(".");
                if (arr.length < 4) {
                    // 获取搜索框信息
                    String old_text = (String) SharePrefrenceUtil.getData(getContext(), "ip_history", "");
                    // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
                    StringBuilder builder = new StringBuilder(old_text);
                    builder.append(text + ",");
                    // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
                    if (!old_text.contains(text + ",")) {
                        SharePrefrenceUtil.saveParam(getContext(), "ip_history", builder.toString());
//                        Toast.makeText(getContext(), text + "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(getContext(), text + "已存在", Toast.LENGTH_SHORT).show();
                    }
                } else {//                    Toast.makeText(getContext(), text + "添加失败", Toast.LENGTH_SHORT).show();
                }

                /**
                 * socket 连接线程
                 * 使用AsyncTask
                 */
                strIP = socket_con_ip.getText().toString();
                strPort = socket_con_port.getText().toString();

                SocketTask socketTask = new SocketTask();
                socketTask.execute(strIP, strPort);

            }
        });

        socket_con_disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                socket_con_dialog.dismiss();
            }
        });

    }

    /**
     * Params表示用于AsyncTask执行任务的参数的类型,这里是String
     * Progress表示在后台线程处理的过程中，可以阶段性地发布结果的数据类型,这里是Object
     * Result表示任务全部完成后所返回的数据类型,这里是Boolean
     */
    private class SocketTask extends AsyncTask<String, Object, Socket> {
        @Override
        protected Socket doInBackground(String... params) {
            String strIP = params[0];
            String strPort = params[1];
            try {
                socket = new Socket(strIP, Integer.parseInt(strPort));
                socket.setSoTimeout(500);
                publishProgress(socket);
            } catch (IOException e) {
                e.printStackTrace();
                publishProgress(e.toString());
            }

            return socket;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            if (values == null) {
                Log.e("socket", "socket null");
            } else {
                Class<? extends Object[]> aClass = values.getClass();
                String name = aClass.getName();
                Log.e("socket", "socket not null:" + name);
            }
        }

        @Override
        protected void onPostExecute(Socket socket) {
            super.onPostExecute(socket);

            if (socket != null && socket.isConnected()) {
                /**
                 * 保存socket的ip和端口号
                 */
                SharePrefrenceUtil.saveParam(getContext(), "SocketIp", strIP);
                SharePrefrenceUtil.saveParam(getContext(), "SocketPort", strPort);
                /**
                 * 设置全局socket
                 */
                SocketUtil.setSocket(socket);
                SocketUtil.setConnectStaus(1);//设置socket连接状态
                Log.e("socket", "连接成功");
                /**
                 * 界面ui更新
                 */
                EventBus.getDefault().post("3");
                socket_con_dialog.dismiss();
            } else {
                /**
                 * 未连接
                 */
                SocketUtil.setConnectStaus(0);
                Log.e("socket", "连接失败");
                /**
                 * 界面ui更新
                 */
                EventBus.getDefault().post("4");
                rl_progress.setVisibility(View.GONE);
                socket_con_dialog.setCanceledOnTouchOutside(true);
            }
        }
    }

}
