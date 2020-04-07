package com.hanks.frame.base;

import java.io.Serializable;

/**
 * Created by CC on 2016/12/11.
 */

public class BaseModel<T> implements Serializable {
    /**
     * ResultType : 0
     * Message : 操作成功
     * LogMessage : null
     * AppendData : null
     */

    private int ResultType;
    private String Message;
    private String LogMessage;
    private T AppendData;

    public int getResultType() {
        return ResultType;
    }

    public void setResultType(int ResultType) {
        this.ResultType = ResultType;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getLogMessage() {
        return LogMessage;
    }

    public void setLogMessage(String LogMessage) {
        this.LogMessage = LogMessage;
    }

    public T getAppendData() {
        return AppendData;
    }

    public void setAppendData(T AppendData) {
        this.AppendData = AppendData;
    }
}
