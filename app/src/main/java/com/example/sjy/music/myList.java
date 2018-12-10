package com.example.sjy.music;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class myList extends AppCompatActivity {
    @BindView(R.id.rv_music)
    RecyclerView rvMusic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_list);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

    }

}
