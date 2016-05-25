package com.android.hubin.pulldownlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * 说明：单呼控制按钮适配器
 *
 * @author
 * @Date 2015-3-5
 */
public class CallControlAdapter extends BaseAdapter
{
    public LayoutInflater mInflater;
    private ViewHolder mViewHolder;
    private List<ControlBtnItem> mDataSource;
    private Context context;

    public CallControlAdapter(Context context, List<ControlBtnItem> dataSrc)
    {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        mDataSource = dataSrc;
        if (mDataSource == null)
        {
            mDataSource = new ArrayList<ControlBtnItem>();
        }
    }

    @Override
    public int getCount()
    {

        return mDataSource == null ? 0 : mDataSource.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView != null)
        {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = mInflater.inflate(R.layout.adapter_control_btn_item, null);
            mViewHolder = new ViewHolder();
            mViewHolder.controlItem = (TextView) convertView.findViewById(R.id.call_control_item);
            convertView.setTag(mViewHolder);
        }
        if(mDataSource == null || mDataSource.size() <= position)
        {
            return convertView;
        }
        ControlBtnItem controlBtnItem = mDataSource.get(position);
        if(controlBtnItem == null)
        {
            return convertView;
        }
        
        mViewHolder.controlItem.setText(controlBtnItem.getName());
        if (controlBtnItem.getName().equals(context.getString(R.string.dial_dtmf)))
        {
            mViewHolder.controlItem.setTextSize(16);
        }
        Drawable drawable = context.getResources().getDrawable(controlBtnItem.getIconRes());
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mViewHolder.controlItem.setCompoundDrawables(null, drawable, null, null);
        mViewHolder.controlItem.setBackground(context.getResources().getDrawable(controlBtnItem.getBgRes()));
        mViewHolder.controlItem.setOnClickListener(controlBtnItem.getClickListener());
        mViewHolder.controlItem.setSelected(controlBtnItem.isSelected());
        return convertView;
    }

    class ViewHolder
    {
        TextView controlItem;
    }
    
    public void clear()
    {
        this.context = null;
        this.mDataSource = null;
    }

}
