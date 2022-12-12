package com.example.healthplus;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Home extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    SensorManager sensorManager;
    Sensor stepCountSensor;
    TextView daily_activity_step;
    TextView daily_activity_time;
    TextView daily_activity_kcal;
    TextView target_step;
    TextView daily_step;
    ProgressBar daily_activity_progressbar;

    // 현재 걸음 수
    int currentSteps = 0;
    public Home() {
        // Required empty public constructor
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_home, container, false);
        daily_activity_step = rootView.findViewById(R.id.daily_activity_step);
        daily_activity_time = rootView.findViewById(R.id.daily_activity_time);
        daily_activity_kcal = rootView.findViewById(R.id.daily_activity_kcal);
        daily_activity_progressbar = rootView.findViewById(R.id.daily_activity_progressbar);
        target_step = rootView.findViewById(R.id.target_step);
        daily_step = rootView.findViewById(R.id.daily_step);

        // 걸음 센서 연결
        // * 옵션
        // - TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
        // - TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        return rootView;
    }

    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null) {
            // 센서 속도 설정
            // * 옵션
            // - SENSOR_DELAY_NORMAL: 20,000 초 딜레이
            // - SENSOR_DELAY_UI: 6,000 초 딜레이
            // - SENSOR_DELAY_GAME: 20,000 초 딜레이
            // - SENSOR_DELAY_FASTEST: 딜레이 없음
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        /*
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_DETECTOR){
            if(sensorEvent.values[0]==1.0f){
                // 센서 이벤트가 발생할때 마다 걸음수 증가
                currentSteps++;
                daily_activity_step.setText(String.valueOf(currentSteps));
            }
        }
`       */
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(sensorEvent.values[0]>1.0f) {
                currentSteps++;
                daily_activity_step.setText(String.valueOf(currentSteps));
                daily_step.setText(String.valueOf(currentSteps));
                daily_activity_progressbar.setProgress(currentSteps/Integer.parseInt(target_step.getText().toString())*100);
            }
            //daily_activity_progressbar.setProgress((int) sensorEvent.values[0]/(Integer.parseInt(target_step.getText().toString())));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}