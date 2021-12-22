package com.jdeco.estimationapp.objects;

import java.io.Serializable;

/**
 * Created by mmuneer on 03/21/2017.
 */
public class ResultCode implements Serializable
{
    public ResultCode()
    {

    }
    public ResultCode(String resultCode, String resultMsg, String extraParam) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.extraParam = extraParam;
    }

    public String getExtraParam() {
        return extraParam;
    }

    public void setExtraParam(String extraParam) {
        this.extraParam = extraParam;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    String resultCode;
    String resultMsg;
    String extraParam;
}
