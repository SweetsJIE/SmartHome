package com.sweetsjie.smarthome;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by sweets on 17/4/21.
 */

public class HumidityFragment extends Fragment {

    private TextView humidityShow;
    private String humidity = "0";
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.humidity_frag,container,false);

        humidityShow = (TextView) view.findViewById(R.id.humidityState);

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

                        humidity = jsonObject.getString("speed");

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
                    humidityShow.setText(humidity+"%RH");
                    break;
                default:
                    break;
            }
        }

    };
}
