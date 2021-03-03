package service;


import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioThread extends Thread {
    String TAG = "AudioThread";
    private final int mSampleRateInHz = 44100;
    private AudioRecord mAudioRecord;
    private int mMinBufferSize;
    private volatile boolean mAudioRun = true;
    public AudioThread(){
        init();
    }

    private void init(){
        mMinBufferSize = AudioRecord.getMinBufferSize(mSampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.REMOTE_SUBMIX, mSampleRateInHz, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, mMinBufferSize);
    }

    @Override
    public void run() {
        mAudioRecord.startRecording();
        Log.d("cosmop", "启动录音...");
        Log.d("cosmop",  "mMinBufferSize:"+mMinBufferSize);
        try {
            byte[] buffer = new byte[mMinBufferSize];
            int len = 0;
            while (mAudioRun) {
                len = mAudioRecord.read(buffer, 0, mMinBufferSize);
                boolean dataFlag = false;
                for(int i=0;i<buffer.length;i++){
                    if(buffer[i] != 0){
                        dataFlag = true;
                        break;
                    }
                }
                if(dataFlag){
                    Log.d("cosmop", "获取一帧音频数据长度:"+len);
                    //send(buffer,0,len);
                } else{
//                    Log.d(TAG, "无效录音len:"+len);
                }

            }
            Log.d("cosmop", "录音停止!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("cosmop",  "AudioRecord ERROR "+e.getMessage());
        }
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;
        Log.d("cosmop",  "录音服务__已退出!");
    }

    public void stopAudio(){
        mAudioRun = false;
    }

    /**
     * by hecj
     * 消息头(固定15个字节:类型+版本+时间+长度)+消息体(长度浮动)
     * 类型 2byte  short
     * 版本 1byte  byte
     * 时间 8byte  long
     * 长度 4byte  int
     * =======消息头 15个byte

    private void send( byte[] bytes,int offset,int length){
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length+15);
//        buffer.putShort(ProtocolType.Audio.type);
//        buffer.put((byte)1);
        buffer.put(DataProtocol.Audio.type);
        buffer.putLong(System.currentTimeMillis());
        buffer.putInt(length);
        buffer.put(bytes,offset,length);
        //mTcpConnection.send(buffer.array());
        mTcpConnection.sendQueue(buffer.array());
    }
     */

}
