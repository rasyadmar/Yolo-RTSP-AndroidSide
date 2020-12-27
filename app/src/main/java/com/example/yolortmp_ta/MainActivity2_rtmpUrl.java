package com.example.yolortmp_ta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity2_rtmpUrl extends AppCompatActivity implements edit_field.edit_field_listerner{
    private TextView RTMPurlinput;
    private Button btn_editRTMP;
    private String RTMPurl;

    public String getRTMPurl(){
        return RTMPurl;
    }

    public void openEditField(){
        edit_field edit_field = new edit_field();
        edit_field.show(getSupportFragmentManager(),"edit_field");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_rtmp_url);

        RTMPurlinput = (TextView) findViewById(R.id.RTMPurlInput);
        btn_editRTMP = (Button) findViewById(R.id.EditRTMP);

        btn_editRTMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditField();
            }
        });
    }

    @Override
    public void applyText(String RTMPurl) {
        RTMPurlinput.setText(RTMPurl);
    }
}