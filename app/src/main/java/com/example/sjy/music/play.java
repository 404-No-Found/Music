package com.example.sjy.music;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.os.Environment.DIRECTORY_MUSIC;

public class play extends AppCompatActivity implements AdapterView.OnItemClickListener, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.sb)
    SeekBar sb;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.pause)
    Button pause;
    @BindView(R.id.previous)
    Button previous;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.play_artist_name)
    TextView artistName;
    private ListView mListView;
    public boolean isRelease = true;
    private MediaPlayer player;
    private MyReceiver receiver;
    private  ArrayList<String> files_path=new ArrayList<String>();
    private File musicDir;
    private File[] files;
    private LayoutInflater inflater;
    private MyListAdapter mAdapter;
    private int currentTime;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);
        verifyStoragePermissions(this);
    }
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };


    private void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }else {
                initData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode== REQUEST_EXTERNAL_STORAGE){
            if (permissions[0]=="android.permission.READ_EXTERNAL_STORAGE"&&permissions[1]=="android.permission.WRITE_EXTERNAL_STORAGE"){
                initData();
            }
            else {
                Toast.makeText(this, "有权限未开启", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "相关权限未开启", Toast.LENGTH_SHORT).show();
        }



    }
    private void initData() {
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("play");
        registerReceiver(receiver,filter);
        musicDir=Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC);//获取SD卡目录

        File[] files = Environment.getExternalStorageDirectory().listFiles();
        for (int i = 0; i < files.length; i++) {
            Log.e("files",files[i].getAbsolutePath());
        }
        this.files =musicDir.listFiles();//列出这个目录的所有文件
        if (this.files !=null){
            for (int i = 0; i< this.files.length; i++){
                //获取并传递音频文件的路径
                files_path.add(this.files[i].getAbsolutePath().toString());
                Log.d("file_path",files_path.get(i));
            }
            //开启service
            Intent intent=new Intent(getApplicationContext(),MusicService.class);
            intent.putStringArrayListExtra("all_music_path",files_path);

            startService(intent);
            inflater=getLayoutInflater();
            mAdapter=new MyListAdapter(inflater, this.files);
            mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(this);//对列表中的歌曲设置点击事件
            sb.setOnSeekBarChangeListener(this);
        }

    }



    @OnClick({R.id.pause, R.id.previous, R.id.next,R.id.iv_back})
    public void onViewClicked(View view) {
        Intent intent=new Intent(getApplicationContext(),MusicService.class);
        switch (view.getId()) {
            case R.id.pause:
                if (isRelease){
                    intent.putExtra("type",Config.START_NOW_BY_TOGGLEBUTTON);
                    intent.putExtra("mCurrentTime",currentTime);
                    startService(intent);
                    isRelease=!isRelease;
                }else {
                    intent.putExtra("type",Config.PAUSE_NOW_BY_TOGGLEBUTTON);
                    startService(intent);
                    isRelease=!isRelease;
                }
                break;
            case R.id.previous:
                intent.putExtra("type",Config.SELECT_PREVIOUS_BY_TOGGLEBUTTON);
                startService(intent);
                break;
            case R.id.next:
                intent.putExtra("type",Config.SELECT_NEXT_BY_TOGGELBUTTON);
                startService(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int type=intent.getIntExtra("type",0);
            Date date=new Date();
            SimpleDateFormat format=new SimpleDateFormat("mm:ss");
            switch (type){
                case 0:
                    int duration=intent.getIntExtra("totalTime",0);
                    date.setTime(duration);
                    String format_duration=format.format(date);
                    sb.setMax(duration);
                    startTime.setText(""+format_duration);
                    break;
                case 1:
                    currentTime=intent.getIntExtra("currentTime",0);
                    date.setTime(currentTime);
                    String format_currentTime=format.format(date);
                    endTime.setText(""+format_currentTime);
                    sb.setProgress(currentTime);
                    break;
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
