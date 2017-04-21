package com.sweetsjie.smarthome;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private TemperatureFragment temperatureFragment = new TemperatureFragment();
    private HumidityFragment humidityFragment = new HumidityFragment();
    private CoWarnFragment coWarnFragment = new CoWarnFragment();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.temperature:
                    transaction.replace(R.id.content,temperatureFragment);
                    break;
                case R.id.humidity:
                    transaction.replace(R.id.content,humidityFragment);
                    break;
                case R.id.coWarn:
                    transaction.replace(R.id.content,coWarnFragment);
                    break;
                default:
                    return false;
            }
            transaction.commit();
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onResume() {
        setDefaultFragment();
        super.onResume();
    }

    public void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content, temperatureFragment);
        transaction.commit();
    }


}
