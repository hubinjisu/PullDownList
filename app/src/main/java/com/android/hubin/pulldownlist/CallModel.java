package com.android.hubin.pulldownlist;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 说明：通话对象
 *
 * @author hubin
 * @Date 2015-4-1
 */
public class CallModel implements Parcelable
{
    /**
     * @Fields CREATOR : Parcel read
     */
    public static final Creator<CallModel> CREATOR = new Creator<CallModel>()
    {
        public CallModel createFromParcel(Parcel source)
        {
            CallModel callModel = new CallModel();
            callModel.sessionId = source.readString();
            callModel.isStarter = source.readByte() != 0;
            callModel.priority = source.readInt();
            callModel.startTime = source.readLong();
            callModel.endTime = source.readLong();
            callModel.connectTime = source.readLong();
            callModel.absoluteStartTime = source.readLong();
            callModel.absoluteEndTime = source.readLong();
            callModel.absoluteConnectTime = source.readLong();
            callModel.isVideo = source.readByte() != 0;
            callModel.isConfMember = source.readByte() != 0;
            callModel.channel = source.readInt();
            callModel.videoPacketMode = source.readInt();
            callModel.videoResolutionType = source.readString();
            return callModel;
        }

        public CallModel[] newArray(int size)
        {
            return new CallModel[size];
        }
    };
    // 通话ID
    private String sessionId;
    // 本机是否是通话发起者
    private boolean isStarter;
    // 本呼叫的优先级
    private int priority = -1;
    // 呼叫开始时间
    private long startTime;
    // 通话建立时间
    private long connectTime;
    // 呼叫结束时间
    private long endTime;
    // 呼叫开始绝对时间
    private long absoluteStartTime;
    // 通话建立绝对时间
    private long absoluteConnectTime;
    // 呼叫结束绝对时间
    private long absoluteEndTime;
    // 视频标识
    private boolean isVideo;
    // 会议成员标识，用于作为会议成员的单呼标识
    private boolean isConfMember;
    // 声音通道：左手柄，右手柄
    private int channel;
    // 远端视频软解码标识
    private int videoPacketMode;
    // 远端视频分辨率类型
    private String videoResolutionType;

    public String getVideoResolutionType()
    {
        return videoResolutionType;
    }

    public void setVideoResolutionType(String videoResolutionType)
    {
        this.videoResolutionType = videoResolutionType;
    }

    public long getAbsoluteEndTime()
    {
        return absoluteEndTime;
    }

    public void setAbsoluteEndTime(long absoluteEndTime)
    {
        this.absoluteEndTime = absoluteEndTime;
    }

    public long getAbsoluteConnectTime()
    {
        return absoluteConnectTime;
    }

    public void setAbsoluteConnectTime(long absoluteConnectTime)
    {
        this.absoluteConnectTime = absoluteConnectTime;
    }

    public long getAbsoluteStartTime()
    {
        return absoluteStartTime;
    }

    public void setAbsoluteStartTime(long absoluteStartTime)
    {
        this.absoluteStartTime = absoluteStartTime;
    }

    public String getSessionId()
    {
        return sessionId;
    }

    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }

    public boolean isStarter()
    {
        return isStarter;
    }

    public void setStarter(boolean isStarter)
    {
        this.isStarter = isStarter;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public long getStartTime()
    {
        return startTime;
    }

    public void setStartTime(long startTime)
    {
        this.startTime = startTime;
    }

    public long getConnectTime()
    {
        return connectTime;
    }

    public void setConnectTime(long connectTime)
    {
        this.connectTime = connectTime;
    }

    public long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(long endTime)
    {
        this.endTime = endTime;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(sessionId);
        dest.writeByte((byte) (isStarter ? 1 : 0));
        dest.writeInt(priority);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeLong(connectTime);
        dest.writeLong(absoluteStartTime);
        dest.writeLong(absoluteEndTime);
        dest.writeLong(absoluteConnectTime);
        dest.writeByte((byte) (isVideo ? 1 : 0));
        dest.writeByte((byte) (isConfMember ? 1 : 0));
        dest.writeInt(channel);
        dest.writeInt(videoPacketMode);
        dest.writeString(videoResolutionType);
    }

    public boolean isVideo()
    {
        return isVideo;
    }

    public void setVideo(boolean isVideo)
    {
        this.isVideo = isVideo;
    }

    public boolean isConfMember()
    {
        return isConfMember;
    }

    public void setConfMember(boolean isConfMember)
    {
        this.isConfMember = isConfMember;
    }

    public int getChannel()
    {
        return channel;
    }

    public void setChannel(int channel)
    {
        this.channel = channel;
    }

    public int getVideoPacketMode()
    {
        return videoPacketMode;
    }

    public void setVideoPacketMode(int videoPacketMode)
    {
        this.videoPacketMode = videoPacketMode;
    }

}
