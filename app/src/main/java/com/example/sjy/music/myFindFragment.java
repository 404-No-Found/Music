package com.example.sjy.music;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;


public class myFindFragment extends Fragment {
    ImageView ivFind;
    RecyclerView reTv;
    HomeAdapter mAdapter;
    private List<String> mFindData;
    private ArrayList<Integer> mHeights;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.find_list, container, false);
        initData();
        reTv = view.findViewById(R.id.re_tv);
        ivFind =view.findViewById(R.id.iv_find);
        //设置数据适配器
        reTv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //在这里将数据放入列表
        reTv.setAdapter(mAdapter = new HomeAdapter());
        return view;
    }

    private void initData() {
        //在这里先给假数据
        mFindData=new ArrayList<String>();
        for (int i=0;i<60;i++){
            mFindData.add("Data "+i);
        }
        //获取瀑布流高度，并将其保存在数组中
        mHeights=new ArrayList<Integer>();
        for (int i=0;i<mFindData.size();i++){
            mHeights.add((int) (100+Math.random()*300));
        }
    }

    private class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>{

        @NonNull
        //用已完成的view替代recycleView中的item
        @Override
        public HomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.card_view_find,parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull HomeAdapter.MyViewHolder myViewHolder, int position) {
            ViewGroup.LayoutParams lp=myViewHolder.tv.getLayoutParams();
            //将文字填充到瀑布流的item中
            myViewHolder.tv.setText(mFindData.get(position));
            //设置瀑布流的高度
            lp.height=mHeights.get(position);
            myViewHolder.tv.setLayoutParams(lp);
        }

        @Override
        public int getItemCount() {
            return mHeights.size();
        }
        //规定填充文字到瀑布流的方法及在自定义item中的填充位置
        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public MyViewHolder(@NonNull View view) {
                super(view);
                tv=view.findViewById(R.id.id_num);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}

