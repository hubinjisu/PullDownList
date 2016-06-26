package com.android.hubin.pulldownlist;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 说明：pull down list adapter
 *
 * @author hubin
 * @Date 2015-4-1
 */
public class PullDownListAdapter extends RecyclerView.Adapter<PullDownListAdapter.ViewHolder>
{
    public static final int UPDATE_LIST = 0;
    public static final int UPDATE_LIST_ITEM = 1;
    private static final String TAG = "PullDownListAdapter";
    public List<CallListItem> mListItems;
    protected LayoutInflater inflater;
    private Context context;
    private int controlViewHeight;
    private Handler mHandler;

    /**
     * 动态菜单列表适配器
     * @param mContext
     */
    public PullDownListAdapter(Context mContext)
    {
        this.context = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.controlViewHeight = (int) context.getResources().getDimension(R.dimen.call_list_ctr_height);
        this.mHandler = new AdapterHandler(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView = inflater.inflate(R.layout.adapter_pull_down_list_item, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CallListItem item = mListItems.get(position);
        if (item == null)
        {
            return;
        }

        try
        {
            // show the item info
            holder.nameView.setText(item.getName());
            holder.numberView.setText(item.getNumber());
            holder.priorityView.setVisibility(View.VISIBLE);
            holder.iconView.setImageResource(R.drawable.channel_left_scall);
            // init control buttons based on status
            handleStatus(holder, item);
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (mListItems != null && mListItems.size() > 0)
        {
            return mListItems.size();
        }
        else
        {
            return 0;
        }
    }

    /**
     * 添加列表数据
     * @param listItems
     */
    public void setListItem(List<? extends BaseListItem> listItems)
    {
        if (listItems != null)
        {
            mListItems = (List<CallListItem>) listItems;
            notifyDataSetChanged();
        }
    }

    private void handleStatus(ViewHolder viewHolder, final CallListItem item)
    {
        ArrayList<ControlBtnItem> controlBtnItems = new ArrayList<ControlBtnItem>();
        viewHolder.timerView.setStartTime(item.getCallModel().getAbsoluteConnectTime(), true);
        // for test
        controlBtnItems.add(new ControlBtnItem(context.getString(R.string.call_hangup), R.drawable.call_hangup_btn_select_bg, R.drawable
                .call_control_btn_click_bg));
        controlBtnItems.add(new ControlBtnItem("保持", R.drawable.call_hold_btn_select_bg, R.drawable.call_control_btn_select_bg));
        controlBtnItems.add(new ControlBtnItem("静音", R.drawable.call_mute_btn_select_bg, R.drawable.call_control_btn_select_bg));
        controlBtnItems.add(new ControlBtnItem(context.getString(R.string.call_handsfree), R.drawable.call_speaker_btn_select_bg, R.drawable
                .call_control_btn_select_bg));
        controlBtnItems.add(new ControlBtnItem(context.getString(R.string.return_conf), R.drawable.call_to_conf_btn_click_bg, R.drawable
                .call_control_btn_click_bg));
        if (item.getName().equals("name1"))
        {
            controlBtnItems.add(new ControlBtnItem(context.getString(R.string.transfer_to_conf), R.drawable.call_to_conf_btn_click_bg, R.drawable
                    .call_control_btn_click_bg));
            controlBtnItems.add(new ControlBtnItem("转接", R.drawable.call_forward_btn_click_bg, R.drawable.call_control_btn_click_bg));
        }
        else if (item.getName().equals("name2"))
        {
            controlBtnItems.add(new ControlBtnItem(context.getString(R.string.transfer_to_conf), R.drawable.call_to_conf_btn_click_bg, R.drawable
                    .call_control_btn_click_bg));
            controlBtnItems.add(new ControlBtnItem("转接", R.drawable.call_forward_btn_click_bg, R.drawable.call_control_btn_click_bg));
            controlBtnItems.add(new ControlBtnItem("视频", R.drawable.call_video_mute_btn_select_bg, R.drawable.call_control_btn_select_bg));
        }

        viewHolder.statusView.setText(context.getString(R.string.call_connect));
        viewHolder.callStatus.setImageResource(R.drawable.call_status_connect);

        boolean hasSecondLine = false;
        // more than 6, then add the "show more" button
        if (controlBtnItems.size() > 6)
        {
            controlBtnItems.add(5, createShowMoreControlItem(item));
            hasSecondLine = true;
        }
        viewHolder.controlView.setAdapter(new CallControlAdapter(context, controlBtnItems));

        // update the pulling down view
        updateDropDownList(viewHolder, item, hasSecondLine);
    }

    private ControlBtnItem createShowMoreControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem("更多", R.drawable.call_more_btn_select_bg, R.drawable.call_control_btn_select_bg);
        controlBtnItem.setIsSelected(listItem.isDropDown());
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    boolean enable = !v.isSelected();
                    v.setSelected(enable);
                    if (enable)
                    {
                        listItem.setIsActionShow(true);
                        // 隐藏其他已下拉的菜单
                        for (int i = 0; i < mListItems.size(); i++) {
                            if (mListItems.get(i).isDropDown())
                            {
                                listItem.setIsActionHide(false);
                                mListItems.get(i).setIsDropDown(false);
                                notifyItemChanged(i);
                                break;
                            }
                        }
                    }
                    else
                    {
                        listItem.setIsActionHide(true);
                    }
                    listItem.setIsDropDown(enable);
                    notifyItemChanged(mListItems.indexOf(listItem));
                }
                catch (Exception e)
                {
                }
            }
        });
        return controlBtnItem;
    }

    private void updateDropDownList(ViewHolder viewHolder, CallListItem item, boolean hasSecondLine)
    {
        if (!hasSecondLine)
        {
            item.setIsDropDown(false);
            viewHolder.controlView.getLayoutParams().height = controlViewHeight;
        }
        else
        {
            if (item.isDropDown())
            {
                if (item.isActionShow())
                {
                    item.setIsActionShow(false);
                    viewHolder.controlView.startAnimation(expand(viewHolder.controlView, controlViewHeight, controlViewHeight * 2, true, 100));
                }
                else
                {
                    viewHolder.controlView.getLayoutParams().height = controlViewHeight * 2;
                    // viewHolder.controlView.requestLayout();
                }
            }
            else
            {
                if (item.isActionHide())
                {
                    item.setIsActionHide(false);
                    viewHolder.controlView.startAnimation(expand(viewHolder.controlView, controlViewHeight, controlViewHeight * 2, false, 100));
                }
                else
                {
                    viewHolder.controlView.getLayoutParams().height = controlViewHeight;
                    // viewHolder.controlView.requestLayout();
                }
            }
        }
    }

    /**
     * 展开伸缩动画
     */
    public static Animation expand(final View v, final int lowerHeight, final int higherHeight, final boolean expand, long duration)
    {
        try
        {
            if (expand)
            {
                v.getLayoutParams().height = lowerHeight;
            }
            else
            {
                v.getLayoutParams().height = higherHeight;
            }
            v.setVisibility(View.VISIBLE);
            Animation a = new Animation()
            {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t)
                {
                    int newHeight = 0;
                    if (expand)
                    {
                        newHeight = (int) (lowerHeight + (higherHeight - lowerHeight) * interpolatedTime);
                    }
                    else
                    {
                        newHeight = (int) (higherHeight - (higherHeight - lowerHeight) * interpolatedTime);
                    }
                    v.getLayoutParams().height = newHeight;
                    v.requestLayout();
                    if (interpolatedTime == 1 && !expand && lowerHeight == 0)
                    {
                        v.setVisibility(View.GONE);
                    }
                }

                @Override
                public boolean willChangeBounds()
                {
                    return true;
                }
            };
            a.setDuration(duration);
            a.setInterpolator(new AccelerateDecelerateInterpolator());
            return a;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 方法说明 : 更新列表所有记录
     *
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    public void updateList()
    {
        if (mHandler != null)
        {
            mHandler.removeMessages(UPDATE_LIST);
            Message msg = mHandler.obtainMessage();
            msg.what = UPDATE_LIST;
            mHandler.sendMessageDelayed(msg, 100);
        }
    }

    public void clear()
    {
        this.context = null;
        this.mHandler = null;
    }

    private static class AdapterHandler extends Handler
    {
        private WeakReference<PullDownListAdapter> mOwner;

        public AdapterHandler(PullDownListAdapter owner)
        {
            mOwner = new WeakReference<PullDownListAdapter>(owner);
        }

        public void handleMessage(Message msg)
        {
            try
            {
                if (mOwner.get() != null)
                {
                    switch (msg.what)
                    {
                        case UPDATE_LIST_ITEM:
                            // 更新列表单条记录状态
                            break;
                        case UPDATE_LIST:
                            // 更新列表全部记录状态
                            ArrayList<CallListItem> callListItems = new ArrayList<>();
                            CallModel callModel = new CallModel();
                            callModel.setAbsoluteConnectTime(SystemClock.elapsedRealtime());
                            CallListItem item1 = new CallListItem();
                            item1.setName("name1");
                            item1.setNumber("1111");
                            item1.setCallModel(callModel);
                            CallListItem item2 = new CallListItem();
                            item2.setName("name2");
                            item2.setNumber("2222");
                            item2.setCallModel(callModel);
                            CallListItem item3 = new CallListItem();
                            item3.setName("name3");
                            item3.setNumber("3333");
                            item3.setCallModel(callModel);
                            callListItems.add(item1);
                            callListItems.add(item2);
                            callListItems.add(item3);
                            mOwner.get().setListItem(callListItems);
                            break;

                        default:
                            break;
                    }
                }
            }
            catch (Exception e)
            {
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView iconView;
        ImageView callStatus;
        TextView nameView;
        TextView numberView;
        TextView statusView;
        TimerText timerView;
        TextView priorityView;
        GridView controlView;
        int index;

        public ViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.caller_icon);
            callStatus = (ImageView) itemView.findViewById(R.id.status_call);
            nameView = (TextView) itemView.findViewById(R.id.caller_name);
            numberView = (TextView) itemView.findViewById(R.id.caller_number);
            statusView = (TextView) itemView.findViewById(R.id.call_status);
            timerView = (TimerText) itemView.findViewById(R.id.call_list_timer);
            priorityView = (TextView) itemView.findViewById(R.id.call_priority);
            controlView = (GridView) itemView.findViewById(R.id.call_control);
        }
    }

}
