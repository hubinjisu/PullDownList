package com.android.hubin.pulldownlist;

import java.util.ArrayList;

/**
 * 说明：呼叫列表单元
 *
 * @author hubin
 * @Date 2015-4-2
 */
public class CallListItem extends BaseListItem
{
    private static final long serialVersionUID = 1L;

    private CallModel callModel;

    // 对端号码通讯录中的组层级
    private ArrayList<String> groups;

    private int status;

//    private int type;

    private String mediaType;
    // 是否入会操作
    private boolean isIntoConf;

    // 单呼转会议
//    private boolean isTransferToConf = false;
    private boolean isSpeakerOn;
    private boolean isMuteOn;

    // 用于会议背景音控制
    private boolean isConfBgmEnabled;

    // 当作为会议成员时，被主席方语音控制的状态
    private boolean isAudioClosed;
    // 当作为会议成员时，被主席方语音控制的状态
    private boolean videoClosed;
    // 当作为会议成员时，被主席方语音控制的状态
    private boolean videoShareStarted;

    private boolean isDropDown;

    private boolean isOutSideAudio;

    private boolean isActionShow;
    private boolean isActionHide;

    // 闭铃开关标识
//    private boolean isCloseRing;

    public boolean isActionShow()
    {
        return isActionShow;
    }

    public void setIsActionShow(boolean isActionShow)
    {
        this.isActionShow = isActionShow;
    }

    public boolean isActionHide()
    {
        return isActionHide;
    }

    public void setIsActionHide(boolean isActionHide)
    {
        this.isActionHide = isActionHide;
    }

    public ArrayList<String> getGroups()
    {
        return groups;
    }

    public void setGroups(ArrayList<String> groups)
    {
        this.groups = groups;
    }

    public boolean isIntoConf()
    {
        return isIntoConf;
    }

    public void setIntoConf(boolean isIntoConf)
    {
        this.isIntoConf = isIntoConf;
    }

    public CallModel getCallModel()
    {
        return callModel;
    }

    public void setCallModel(CallModel callModel)
    {
        this.callModel = callModel;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public boolean isAudioClosed()
    {
        return isAudioClosed;
    }

    public void setAudioClosed(boolean isAudioEnabled)
    {
        this.isAudioClosed = isAudioEnabled;
    }

    public boolean isVideoClosed()
    {
        return videoClosed;
    }

    public void setVideoClosed(boolean videoClosed)
    {
        this.videoClosed = videoClosed;
    }

    public boolean isVideoShareStarted()
    {
        return videoShareStarted;
    }

    public void setVideoShareStarted(boolean videoShareStarted)
    {
        this.videoShareStarted = videoShareStarted;
    }

    public boolean isSpeakerOn()
    {
        return isSpeakerOn;
    }

    public void setSpeakerOn(boolean isSpeakerOn)
    {
        this.isSpeakerOn = isSpeakerOn;
    }

    public boolean isMuteOn()
    {
        return isMuteOn;
    }

    public void setMuteOn(boolean isMuteOn)
    {
        this.isMuteOn = isMuteOn;
    }

    public boolean isConfBgmEnabled()
    {
        return isConfBgmEnabled;
    }

    public void setConfBgmEnabled(boolean isConfBgmEnabled)
    {
        this.isConfBgmEnabled = isConfBgmEnabled;
    }

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

    public boolean isDropDown()
    {
        return isDropDown;
    }

    public void setIsDropDown(boolean isDropDown)
    {
        this.isDropDown = isDropDown;
    }

    public boolean isOutSideAudio()
    {
        return isOutSideAudio;
    }

    public void setIsOutSideAudio(boolean isOutSideAudio)
    {
        this.isOutSideAudio = isOutSideAudio;
    }

//    public boolean isCloseRing()
//    {
//        return isCloseRing;
//    }
//
//    public void setCloseRing(boolean isCloseRing)
//    {
//        this.isCloseRing = isCloseRing;
//    }
}
