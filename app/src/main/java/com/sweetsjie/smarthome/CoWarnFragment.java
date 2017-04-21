package com.sweetsjie.smarthome;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by sweets on 17/4/21.
 */

public class CoWarnFragment extends Fragment {

    private String coState = "正常";
    private TextView coShow;
    private TextView warnShow;
    private Switch warnSwitch;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cowarn_frag,container,false);

        warnShow = (TextView) view.findViewById(R.id.warnState);
        warnSwitch = (Switch) view.findViewById(R.id.warnSwitch);
        coShow = (TextView) view.findViewById(R.id.coState);

        //开启后台服务
        final Intent intent = new Intent(view.getContext(),BackgroundService.class);
        view.getContext().startService(intent);
        warnSwitch.setChecked(true);

        warnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    warnShow.setText("开启");
                    view.getContext().startService(intent);
                    Toast.makeText(view.getContext(),"一氧化碳报警已开启",Toast.LENGTH_SHORT).show();
                }else {
                    warnShow.setText("关闭");
                    view.getContext().stopService(intent);
                    AlarmManager alarmManager = (AlarmManager) view.getContext().getSystemService(ALARM_SERVICE);
                    Intent i = new Intent(view.getContext(),AlarmReceiver.class);
                    PendingIntent pi =  PendingIntent.getBroadcast(view.getContext(),0,i,0);
                    alarmManager.cancel(pi);
                    Toast.makeText(view.getContext(),"一氧化碳报警已关闭",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        getInformation();
        super.onResume();
    }

    //WebService线程
    private void getInformation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //组装反向地理编码的接口地址
                        StringBuilder url = new StringBuilder();
                        url.append("http://www.makercorner.cn:8080/SSMServer/user/read=1");
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpGet httpGet = new HttpGet(url.toString());
                        HttpResponse httpResponse = httpClient.execute(httpGet);
                        HttpEntity entity = httpResponse.getEntity();

                        String response = EntityUtils.toString(entity);
                        //response = response.substring(6);

                        JSONObject jsonObject = new JSONObject(response);

                        int judge = Integer.parseInt(jsonObject.getString("power"));
                        if (judge==1){
                            coState = "危险";
                        }
                        else if (judge==0){
                            coState = "正常";
                        }

                        Message message = new Message();
                        message.what = 0;
                        handler.sendMessage(message);

                        Thread.sleep(4000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //异步消息处理
    public android.os.Handler handler = new android.os.Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (coState.equals("危险")){
                        coShow.setTextColor(Color.parseColor("#CC0000"));
                    }
                    else {
                        coShow.setTextColor(Color.parseColor("#00CC00"));
                    }
                    coShow.setText(coState);
                    break;
                default:
                    break;
            }
        }

    };
}
