package com.android.hubin.pulldownlist;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

/**
 * 说明：
 *
 * @author hezhen
 * @Date 2016-3-8
 */
public class TimerText extends TextView
{
    private static final String TAG = "TimerText";
    // 分钟与毫秒的换算关系
    private static final long MINUTE_RELATION = 1000 * 60;
    // 小时与毫秒的换算关系
    private static final long HOUR_RELATION = 1000 * 60 * 60;
    private long startTime;
    private boolean isAbsoluteTime;
    private Runnable mTimeUpdateThread;

    public TimerText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setStartTime(long startTime, boolean isAbsoluteTime)
    {
        this.startTime = startTime;
        this.isAbsoluteTime = isAbsoluteTime;
    }

    @Override
    protected void onAttachedToWindow()
    {
        super.onAttachedToWindow();
        if (startTime > 0)
        {
            mTimeUpdateThread = new Runnable()
            {
                @Override
                public void run()
                {
                    new Handler().postDelayed(mTimeUpdateThread, 1000);
                    setText(getCallTime(startTime));
                    invalidate();
                }
            };
            new Handler().post(mTimeUpdateThread);
        }
    }

    private String getCallTime(long startTimer)
    {
        final long timeSpace = (isAbsoluteTime ? SystemClock.elapsedRealtime() : Calendar.getInstance().getTimeInMillis()) - startTimer;
        final long hour = timeSpace / HOUR_RELATION;
        final long minute = timeSpace / MINUTE_RELATION - hour * 60;
        final long second = timeSpace / 1000 - hour * 60 * 60 - minute * 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    @Override
    protected void onDetachedFromWindow()
    {
        super.onDetachedFromWindow();
        new Handler().removeCallbacks(mTimeUpdateThread);
    }
}
