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

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.time.LocalDateTime;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2, edit_field.edit_field_listerner  {

    FFmpeg ffmpegSender;
    FFmpeg ffmpegReceiver;
    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    boolean buttonState = false;
    private TextView RTMPurlView;
    String RTMPurlVar;

    public void openEditField(){
        edit_field edit_field = new edit_field();
        edit_field.show(getSupportFragmentManager(),"edit_field");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ffmpegSenderloadbinary();
            ffmpegReceiverloadbinary();
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final Button btn_toggleStream = findViewById(R.id.btn_toggleStream);
        final Button btn_setting = findViewById(R.id.btn_setting);
        btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_desable));

        RTMPurlView = (TextView) findViewById(R.id.RTMPurl) ;

        cameraBridgeViewBase = (JavaCameraView)findViewById(R.id.CameraView);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);

                switch(status){

                    case BaseLoaderCallback.SUCCESS:
                        cameraBridgeViewBase.enableView();
                        break;
                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
        btn_toggleStream.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String hour = Integer.toString(LocalDateTime.now().getHour());
                String minute = Integer.toString(LocalDateTime.now().getMinute());
                String second = Integer.toString(LocalDateTime.now().getSecond());

                if(!buttonState){
                    Toast.makeText(MainActivity.this, "Streaming started at " + hour + ":" + minute + ":" + second, Toast.LENGTH_LONG).show();
                    btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_enable));
                    buttonState = true;
                }
                else{
                    Toast.makeText(MainActivity.this, "Streaming ended at " + hour + ":" + minute + ":" + second, Toast.LENGTH_LONG).show();
                    btn_toggleStream.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_stream_desable));
                    buttonState = false;
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
        if (!OpenCVLoader.initDebug()){
            Toast.makeText(getApplicationContext(),"Your camera stoppend and start again", Toast.LENGTH_SHORT).show();
        }

        else
        {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBase!=null){

            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void applyText(String RTMPurl) {
        RTMPurlView.setText(RTMPurl);
        RTMPurlVar = RTMPurl;

    }

    public void ffmpegSenderloadbinary() throws FFmpegNotSupportedException {
        if (ffmpegSender == null){
            ffmpegSender = FFmpeg.getInstance(this);
            ffmpegSender.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(),"sender failed to load library",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),"sender successfully load library",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        }
    }

    public void ffmpegReceiverloadbinary() throws FFmpegNotSupportedException {
        if (ffmpegReceiver == null){
            ffmpegReceiver = FFmpeg.getInstance(this);
            ffmpegReceiver.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(),"receiver failed to load library",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(),"receiver successfully load library",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onFinish() {

                }
            });
        }
    }

    public void ffmpegSenderExec(String[] cmd) throws FFmpegCommandAlreadyRunningException {
        ffmpegSender.execute(cmd, new ExecuteBinaryResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onProgress(String message) {
                super.onProgress(message);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }

            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
    public void ffmpegReceiverExec(String[] cmd) throws FFmpegCommandAlreadyRunningException {
        ffmpegReceiver.execute(cmd, new ExecuteBinaryResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onProgress(String message) {
                super.onProgress(message);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
            }

            @Override
            public void onSuccess(String message) {
                super.onSuccess(message);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }
}