package com.lsjr.zizisteward.ymz.utils;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerHolder> {


    private Context context;
    private int layouId;
    private List<T> mDatas;

    public BaseRecyclerAdapter(Context context,List<T> datas, int itemLayoutId) {
        this.context = context;
        this.mDatas = datas;
        this.layouId = itemLayoutId;
        setListData(mDatas);
    }


    /**
     * Recycler适配器填充方法
     *
     * @param holder viewholder
     * @param item   javabean
     *               isScrolling RecyclerView是否正在滚动
     */
    protected abstract void convert(BaseRecyclerHolder holder, T item, int position);

    @Override
    public BaseRecyclerHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(layouId, viewGroup, false);
        return new BaseRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder viewHolder, int position) {
        convert(viewHolder, mDatas.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 添加集合数据到原集合首位，下拉刷新时使用
     *
     * @param list
     */
    public void addToFirst(List<T> list) {
        if (list == null)
            return;
        this.mDatas.addAll(0, list);
        notifyDataSetChanged();
    }

    public void addToFirst(T t) {
        if (t == null)
            return;
        this.mDatas.add(0, t);
        notifyDataSetChanged();
    }

    /**
     * 添加数据到末尾，用于上拉加载等情况。<BR>
     * 不清楚原集合，添加到末尾。
     *
     * @param list
     */
    public void addToLast(List<T> list) {
        if (list == null)
            return;
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 添加元素到集合末尾
     *
     * @param t
     */
    public void addToLast(T t) {
        if (t == null)
            return;
        this.mDatas.add(t);
        notifyDataSetChanged();
    }


    /**
     * 设置数据。<BR>
     * 会清空原集合所有数据,后添加。
     *
     * @param list
     */
    public void setListData(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>(0);
        }
        this.mDatas = list;
        notifyDataSetChanged();
    }


    public void setData(List<T> list) {
        list.clear();
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }


}
