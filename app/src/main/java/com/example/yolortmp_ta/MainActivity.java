package com.example.yolortmp_ta;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AutomaticZenRule;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.rtplibrary.rtsp.RtspCamera1;
import com.pedro.rtsp.utils.ConnectCheckerRtsp;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.time.LocalDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, edit_field.edit_field_listerner, ConnectCheckerRtsp {

    private Button btn_toggleStream;
    private Button btn_setting;
    private RtspCamera1 rtspCamera1;
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    private TextView RTSPurlView;
    private String RTSPurlVar;
    private String url;

    public void openEditField(){
        edit_field edit_field = new edit_field();
        edit_field.show(getSupportFragmentManager(),"edit_field");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        btn_toggleStream = findViewById(R.id.btn_toggleStream);
        btn_setting = findViewById(R.id.btn_setting);
        btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_desable));

        RTSPurlView = (TextView) findViewById(R.id.RTMPurl) ;

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

        rtspCamera1 = new RtspCamera1(surfaceView, this);
        rtspCamera1.prepareVideo(640,360,10,1000,0);
        rtspCamera1.prepareVideo();
        rtspCamera1.prepareAudio();
//        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
//        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
//        cameraBridgeViewBase.setCvCameraViewListener(this);
//
//        baseLoaderCallback = new BaseLoaderCallback(this) {
//            @Override
//            public void onManagerConnected(int status) {
//                super.onManagerConnected(status);
//
//                switch(status){
//
//                    case BaseLoaderCallback.SUCCESS:
//                        cameraBridgeViewBase.enableView();
//                        break;
//                    default:
//                        super.onManagerConnected(status);
//                        break;
//                }
//            }
//        };
        btn_toggleStream.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String hour = Integer.toString(LocalDateTime.now().getHour());
                String minute = Integer.toString(LocalDateTime.now().getMinute());
                String second = Integer.toString(LocalDateTime.now().getSecond());

                if(!rtspCamera1.isStreaming()){
                    if(RTSPurlVar!=null){
                        if(RTSPurlVar.contains("rtsp://")){
                            rtspCamera1.startStream(RTSPurlVar);
                            Toast.makeText(MainActivity.this, "Streaming started at " + hour + ":" + minute + ":" + second, Toast.LENGTH_LONG).show();
                            btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_enable));
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Enter your server Url",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this,"Enter your server Url",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    rtspCamera1.stopStream();
                    Toast.makeText(MainActivity.this, "Streaming ended at " + hour + ":" + minute + ":" + second, Toast.LENGTH_LONG).show();
                    btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_desable));

                }
            }
        });
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditField();
            }
        });

    }


    @Override
    public void onCameraViewStarted(int width, int height) {
//        flippedFrame = new Mat(height,width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat frame = inputFrame.rgba();
//        Core.rotate(frame,flippedFrame,Core.ROTATE_90_CLOCKWISE);
        return frame;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void applyText(String RTSPurl) {
        RTSPurlView.setText(RTSPurl);
        RTSPurlVar = RTSPurl;
    }

    @Override
    public void onAuthErrorRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Auth error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtsp(String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connection failed. " + s, Toast.LENGTH_SHORT).show();
                rtspCamera1.stopStream();
                btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_desable));
            }
        });
    }

    @Override
    public void onConnectionSuccessRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Connection success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDisconnectRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNewBitrateRtsp(long l) {

    }
}