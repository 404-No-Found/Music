package com.example.sjy.music;

import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;

public class MyListAdapter extends BaseAdapter {


    //传递所有文件路径
    private LayoutInflater minflate;
    private File[] musicDirs;

    public MyListAdapter (LayoutInflater minflate,File[] musicDirs){
        super();
        this.minflate=minflate;
        this.musicDirs=musicDirs;
    }
    @Override
    public int getCount() {
        return musicDirs.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        if (convertView==null){
            vh = new ViewHolder();
            convertView = minflate.inflate(R.layout.item_music, null);
            vh.mtv=convertView
                    .findViewById(R.id.music_name);
            vh.mtv_author=convertView
                    .findViewById(R.id.artist_name);
            convertView.setTag(vh);//通过setTag规定赋值规则
        }
        else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.mtv.setText(musicDirs[position].getName());
        //通过MediaMetadataRetriever的对象来获取音频文件的歌名，歌手，以及图片
        MediaMetadataRetriever mnr=new MediaMetadataRetriever();
        Log.d("路径",musicDirs[position].getAbsolutePath());
        mnr.setDataSource(musicDirs[position].getAbsolutePath());
        String author=mnr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        //获取作者名，如果不为空，对textView进行重置
        if (author!=null){
            vh.mtv_author.setText(author);
        }else {
            vh.mtv_author.setText("未知");
        }
//        //获取二进制图片
//        byte[] img = mnr.getEmbeddedPicture();
//        if (img != null) {
//            //将二进制文件转换成位图进行添加专辑图片
//            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
//            vh.img.setImageBitmap(bitmap);
//        } else {
//            vh.img.setImageResource(R.drawable.audio_identify_singer_default);
//        }

        return convertView;
    }
    class ViewHolder{
        TextView mtv;
        TextView mtv_author;
    }
}
