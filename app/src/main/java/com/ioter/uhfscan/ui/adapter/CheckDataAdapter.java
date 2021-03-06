package com.ioter.uhfscan.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ioter.uhfscan.R;
import com.ioter.uhfscan.bean.EPC;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YJW on 2018/4/11.
 */

public class CheckDataAdapter extends BaseAdapter {
    //定义需要包装的JSONArray对象
    public List<EPC> mymodelList = new ArrayList<>();
    private Context context = null;
    //视图容器
    private LayoutInflater layoutInflater;

    public CheckDataAdapter(Context _context) {
        this.context = _context;
        //创建视图容器并设置上下文
        this.layoutInflater = LayoutInflater.from(_context);
    }

    public void updateDatas(List<EPC> datalist) {
        if (datalist == null) {
            return;
        } else {
            mymodelList.clear();
            mymodelList.addAll(datalist);
            notifyDataSetChanged();
        }

    }

    /**
     * 清空列表的所有数据
     */
    public void clearData() {
        mymodelList.clear();
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return this.mymodelList.size();
    }

    @Override
    public Object getItem(int position) {
        if (getCount() > 0) {
            return this.mymodelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            //获取list_item布局文件的视图
            convertView = layoutInflater.inflate(R.layout.checkdata_item, null);
            //获取控件对象
            listItemView = new ListItemView();
            listItemView.num = convertView.findViewById(R.id.num);
            listItemView.rel = convertView.findViewById(R.id.rel);
            listItemView.plan = convertView.findViewById(R.id.plan);
            listItemView.casecode = convertView.findViewById(R.id.casecode);
            //设置控件集到convertView
            convertView.setTag(listItemView);
        } else {
            listItemView = (CheckDataAdapter.ListItemView) convertView.getTag();
        }

        final EPC m1 = (EPC) this.getItem(position);
        listItemView.num.setText(position + 1 + "");
        listItemView.casecode.setText(m1.getEpc());
        listItemView.plan.setText(m1.getSize()+"");
        listItemView.rel.setText(m1.getRel()+"");
        return convertView;
    }

    /**
     * 使用一个类来保存Item中的元素
     * 自定义控件集合
     */
    public final class ListItemView {
        TextView num,rel,plan,casecode;
    }
}

