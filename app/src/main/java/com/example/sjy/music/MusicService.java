package com.example.sjy.music;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.DynamicsProcessing;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

//service中创建线程，开启音乐
public class MusicService extends Service {
    private MediaPlayer mediaPlayer;
    private int musicPosition;
    private ArrayList<String> musicPath;//文件路径
    private int currentPosition;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (musicPath==null){
            musicPath=intent.getStringArrayListExtra("all_music_path");
        }
        int type=intent.getIntExtra("type",222);
        switch (type){
            case Config.START_NEW_MUSIC_LISTVIEW:
                musicPosition=intent.getIntExtra("music_position",0);
                currentPosition=musicPosition;
                startNewMusicListView();
                break;
            case Config.SEEKTO:
                    int progress=intent.getIntExtra("progress",0);
                    mediaPlayer.seekTo(progress);
                break;
            case Config.START_NOW_BY_TOGGLEBUTTON:
                startNewMusicListView();
                int currentTime=intent.getIntExtra("mCurrentTime",0);
                if (mediaPlayer==null) {
                    Toast.makeText(this, "播放器未初始化", Toast.LENGTH_SHORT).show();
                }
                while (!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }

                break;
            case Config.PAUSE_NOW_BY_TOGGLEBUTTON:
                while (mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case Config.SELECT_PREVIOUS_BY_TOGGLEBUTTON:
                Log.d("musicString",currentPosition+"");
                if (currentPosition==0){
                    musicPosition=musicPath.size()-1;
                }else {
                    musicPosition--;
                }
                currentPosition=musicPosition;
                startNewMusicListView();
                break;
            case Config.SELECT_NEXT_BY_TOGGELBUTTON:
                if (currentPosition==musicPath.size()-1){
                    musicPosition=0;
                }else {
                    musicPosition++;
                }
                currentPosition=musicPosition;
                startNewMusicListView();
                break;
            case 222:
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startNewMusicListView() {
        if (mediaPlayer==null){
            mediaPlayer=new MediaPlayer();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(musicPath.get(musicPosition).toString());
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                   mediaPlayer.start();
                   mediaPlayer.getCurrentPosition();
                   int duration=mediaPlayer.getDuration();
                   Intent intent2=new Intent("play");
                   intent2.putExtra("type",0);
                   intent2.putExtra("totalTime",mediaPlayer.getDuration());
                   intent2.putExtra("isChecked",true);
                   //这里发送广播给mainActivity，将总时间传递给seekbar
                   sendBroadcast(intent2);
                   MyThreadPlay myThreadPlay=new MyThreadPlay();
                   myThreadPlay.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (musicPosition==musicPath.size()-1){
                        musicPosition=0;
                    }else {
                        musicPosition++;
                    }
                    startNewMusicListView();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    class MyThreadPlay extends Thread{
        @Override
        public void run() {
            Intent intent=new Intent("play");
            intent.putExtra("type",1);
            intent.putExtra("currentTime",mediaPlayer.getCurrentPosition());
            sendBroadcast(intent);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
