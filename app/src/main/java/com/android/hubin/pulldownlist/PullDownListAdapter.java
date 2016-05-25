package com.android.hubin.pulldownlist;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
public class PullDownListAdapter extends BaseAdapter
{
    public static final int UPDATE_LIST = 0;
    public static final int UPDATE_LIST_ITEM = 1;
    private static final String TAG = "PullDownListAdapter";
    public List<CallListItem> mListItems;
    protected LayoutInflater inflater;
    private ViewHolder viewHolder;
    private Context context;
    private int controlViewHeight;
    private ListView callListView;
    private Handler mHandler;

    public PullDownListAdapter(Context mContext)
    {
        this.context = mContext;
        this.inflater = LayoutInflater.from(mContext);
        this.controlViewHeight = (int) context.getResources().getDimension(R.dimen.call_list_ctr_height);
        this.mHandler = new AdapterHandler(this);
    }

    @Override
    public int getCount()
    {
        if (mListItems != null && mListItems.size() > 0)
        {
            return mListItems.size();
        }
        else
        {
            return 0;
        }
    }

    @Override
    public Object getItem(int position)
    {
        if (mListItems != null && position >= 0 && position < mListItems.size())
        {
            return mListItems.get(position);
        }
        return new CallListItem();
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    public void setListItem(List<? extends BaseListItem> listItems)
    {
        if (listItems != null)
        {
            mListItems = (List<CallListItem>) listItems;
            notifyDataSetChanged();
        }
    }

    public void setListView(ListView listView)
    {
        this.callListView = listView;
    }

    /**
     * 方法说明 : 更新列表单行记录状态
     *
     * @param callListItem
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    private void updateSingleRow(CallListItem callListItem)
    {
        if (callListView != null)
        {
            int start = callListView.getFirstVisiblePosition();
            for (int i = start, j = callListView.getLastVisiblePosition(); i <= j; i++)
            {
                if (callListItem == callListView.getItemAtPosition(i))
                {
                    View view = callListView.getChildAt(i - start);
                    getView(i, view, callListView);
                    break;
                }
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView = inflater.inflate(R.layout.adapter_pull_down_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.iconView = (ImageView) convertView.findViewById(R.id.caller_icon);
            viewHolder.callStatus = (ImageView) convertView.findViewById(R.id.status_call);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.caller_name);
            viewHolder.numberView = (TextView) convertView.findViewById(R.id.caller_number);
            viewHolder.statusView = (TextView) convertView.findViewById(R.id.call_status);
            viewHolder.timerView = (TimerText) convertView.findViewById(R.id.call_list_timer);
            viewHolder.priorityView = (TextView) convertView.findViewById(R.id.call_priority);
            viewHolder.controlView = (GridView) convertView.findViewById(R.id.call_control);
            convertView.setTag(viewHolder);
        }
        if (mListItems == null || mListItems.size() <= position)
        {
            return convertView;
        }
        final CallListItem item = mListItems.get(position);
        if (item == null)
        {
            return convertView;
        }

        try
        {
            viewHolder.index = position;
            // show the item info
            viewHolder.nameView.setText(item.getName());
            viewHolder.numberView.setText(item.getNumber());
            viewHolder.priorityView.setVisibility(View.VISIBLE);
            viewHolder.iconView.setImageResource(R.drawable.channel_left_scall);
            // init control buttons based on status
            handleStatus(item);
        }
        catch (Exception e)
        {
        }

        return convertView;
    }

    private void handleStatus(final CallListItem item)
    {
        ArrayList<ControlBtnItem> controlBtnItems = new ArrayList<ControlBtnItem>();
        viewHolder.timerView.setStartTime(item.getCallModel().getAbsoluteConnectTime(), true);
        // for test
        controlBtnItems.add(createScallHangupControlItem(item));
        controlBtnItems.add(createScallHoldControlItem(item));
        controlBtnItems.add(createScallMuteControlItem(item));
        controlBtnItems.add(createScallSpeakerControlItem(item));
        controlBtnItems.add(createScallIntoConfControlItem(item));
        if (item.getName().equals("name1"))
        {
            controlBtnItems.add(createScallTransferToConfControlItem(item));
            controlBtnItems.add(createScallForwardControlItem(item));
        }
        else if (item.getName().equals("name2"))
        {
            controlBtnItems.add(createScallTransferToConfControlItem(item));
            controlBtnItems.add(createScallForwardControlItem(item));
            controlBtnItems.add(createVideoControlItem(item));
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
        updateDropDownList(item, hasSecondLine);
    }

    private ControlBtnItem createScallHangupControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName(context.getString(R.string.call_hangup));
        controlBtnItem.setIconRes(R.drawable.call_hangup_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_click_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallSpeakerControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName(context.getString(R.string.call_handsfree));
        controlBtnItem.setIconRes(R.drawable.call_speaker_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
        controlBtnItem.setIsSelected(listItem.isSpeakerOn());
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallIntoConfControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName(context.getString(R.string.return_conf));
        controlBtnItem.setIconRes(R.drawable.call_to_conf_btn_click_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_click_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallAnswerControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("接听");
        controlBtnItem.setIconRes(R.drawable.call_answer);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_click_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallHoldControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("保持");
        controlBtnItem.setIconRes(R.drawable.call_hold_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallMuteControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("静音");
        controlBtnItem.setIconRes(R.drawable.call_mute_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
        controlBtnItem.setIsSelected(listItem.isMuteOn());
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallTransferToConfControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName(context.getString(R.string.transfer_to_conf));
        controlBtnItem.setIconRes(R.drawable.call_to_conf_btn_click_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_click_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createDtmfControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName(context.getString(R.string.dial_dtmf));
        controlBtnItem.setIconRes(R.drawable.call_dtmf_btn_click_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

//    private ControlBtnItem createCloseRingControlItem(final CallListItem listItem)
//    {
//        ControlBtnItem controlBtnItem = new ControlBtnItem();
//        controlBtnItem.setName("闭铃");
//        controlBtnItem.setIconRes(R.drawable.call_close_bell_btn_select_bg);
//        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
//        controlBtnItem.setIsSelected(listItem.isCloseRing());
//        controlBtnItem.setClickListener(new OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                try
//                {
//                    if (listItem.getStatus() == CommonConstantEntry.SCALL_STATE_CONNECT)
//                    {
//                        boolean enable = !listItem.isCloseRing();
//                        Log.debug(TAG, "click:closeBell:: enable:" + enable);
//                        v.setSelected(enable);
//                        listItem.setCloseRing(enable);
//                    }
//                    else
//                    {
//                        ToastUtil.showToast(UiApplication.getCurrentContext().getString(R.string.fail_state));
//                    }
//                }
//                catch (Exception e)
//                {
//                    Log.exception(TAG, e);
//                }
//            }
//        });
//        return controlBtnItem;
//    }

    private ControlBtnItem createVideoControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("视频");
        controlBtnItem.setIconRes(R.drawable.call_video_mute_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
        controlBtnItem.setIsSelected(!listItem.isVideoClosed());
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createOutsideAudioControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("声音控制");
        controlBtnItem.setIconRes(R.drawable.call_outside_audio_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
        controlBtnItem.setIsSelected(listItem.isOutSideAudio());
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createConfCloseControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("关闭");
        controlBtnItem.setIconRes(R.drawable.call_hangup_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_click_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    private void updateDropDownList(CallListItem item, boolean hasSecondLine)
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

    private ControlBtnItem createShowMoreControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("更多");
        controlBtnItem.setIconRes(R.drawable.call_more_btn_select_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_select_bg);
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
                        for (CallListItem callItem : mListItems)
                        {
                            if (callItem.isDropDown())
                            {
                                listItem.setIsActionHide(false);
                                callItem.setIsDropDown(false);
                                updateListItem(callItem);
                                break;
                            }
                        }
                    }
                    else
                    {
                        listItem.setIsActionHide(true);
                    }
                    listItem.setIsDropDown(enable);
                    updateListItem(listItem);
                }
                catch (Exception e)
                {
                }
            }
        });
        return controlBtnItem;
    }

    private ControlBtnItem createScallForwardControlItem(final CallListItem listItem)
    {
        ControlBtnItem controlBtnItem = new ControlBtnItem();
        controlBtnItem.setName("转接");
        controlBtnItem.setIconRes(R.drawable.call_forward_btn_click_bg);
        controlBtnItem.setBgRes(R.drawable.call_control_btn_click_bg);
        controlBtnItem.setClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });
        return controlBtnItem;
    }

    /**
     * 方法说明 : 更新列表单条记录
     *
     * @param callListItem
     * @return void
     * @author hubin
     * @Date 2015-4-14
     */
    public void updateListItem(CallListItem callListItem)
    {
        if (mHandler != null && callListItem != null)
        {
            mHandler.removeMessages(UPDATE_LIST_ITEM, callListItem);
            Message msg = mHandler.obtainMessage();
            msg.what = UPDATE_LIST_ITEM;
            msg.obj = callListItem;
            mHandler.sendMessageDelayed(msg, 100);
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
                            mOwner.get().updateSingleRow((CallListItem) msg.obj);
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

    class ViewHolder
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

}
