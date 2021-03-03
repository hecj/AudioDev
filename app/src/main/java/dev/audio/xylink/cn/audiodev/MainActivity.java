package dev.audio.xylink.cn.audiodev;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import service.AudioThread;

public class MainActivity extends Activity {
    AudioThread mAudioThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void start(){
        mAudioThread = new AudioThread();
        mAudioThread.start();
    }

    private void end(){
        mAudioThread.stopAudio();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.start:
                start();
                Toast.makeText(this,"开始录音",Toast.LENGTH_LONG).show();
                break;

            case R.id.end:
                end();
                Toast.makeText(this,"结束录音",Toast.LENGTH_LONG).show();
                break;
        }

    }

}
