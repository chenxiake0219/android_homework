package com.example.playmusic.Adapter;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.playmusic.Activity.ThirdActivity;
import com.example.playmusic.R;

import java.util.List;

public class SoftwareAdapter extends RecyclerView.Adapter<SoftwareAdapter.ViewHolder> {
    private List<Software> mSoftwareList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View softwareView;
        ImageView softwareImage;
        TextView softwareName;
        public ViewHolder(View view){
            super(view);
            softwareView = view;
            softwareImage = (ImageView) view.findViewById(R.id.software_image);
            softwareName = (TextView) view.findViewById(R.id.software_name);
        }
    }

    public SoftwareAdapter(List<Software> softwareList){
        mSoftwareList = softwareList;
    }

    //创建ViewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
//        加载software_item加载进来
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.software_item,parent,false);
//        创建ViewHolder实例，并把加载出来的布局传入构造函数中
        final ViewHolder holder = new ViewHolder(view);
        holder.softwareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Software software = mSoftwareList.get(position);
                Toast.makeText(view.getContext(),"you clicked view "+software.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.softwareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Software software = mSoftwareList.get(position);
//                Toast.makeText(view.getContext(),"you clicked image "+software.getName(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ThirdActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        return holder;
    }

//    用于对RecyclerView子项的数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
    public void onBindViewHolder(ViewHolder holder,int position){
//        通过position得到当前的software实例
        Software software = mSoftwareList.get(position);
//        设置image和name信息
        holder.softwareImage.setImageResource(software.getImageId());
        holder.softwareName.setText(software.getName());
    }

//    用于得到RecyclerView的子项数目
    public int getItemCount(){
        return mSoftwareList.size();
    }



//    private int resourceId;
//    public SoftwareAdapter(Context context, int textViewResourceId, List<Software> objects){
//        super(context,textViewResourceId,objects);
//        resourceId  = textViewResourceId;
//    }

//    public View getView(int position, View convertView, ViewGroup parent){
//        Software software = getItem(position);//获取当前项的software实例
//        View view;
//        ViewHolder viewHolder;
//        //有缓存就直接用，没有缓存就加载
//        if(convertView==null){
//            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//            viewHolder = new ViewHolder();
//            viewHolder.softwareImage = (ImageView) view.findViewById(R.id.software_image);
//            viewHolder.softwareName = (TextView) view.findViewById(R.id.software_name);
//            //将viewHolder存储在View中,也就是把图片和名字都加入到缓存里面
//            view.setTag(viewHolder);
//        }else{
//            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
//        }
//        viewHolder.softwareImage.setImageResource(software.getImageId());
//        viewHolder.softwareName.setText(software.getName());
//        return view;
//    }
//
//    class ViewHolder{
//        ImageView softwareImage;
//        TextView softwareName;
//    }
}
