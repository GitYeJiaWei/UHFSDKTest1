package com.ioter.uhfscan.bean;

public class BaseBean<T> extends BaseEntity
{
    public static final int SUCCESS = 0;
    private int Status;

    private String Message;

    private T Data;


    public boolean success()
    {
        return (Status == SUCCESS);
    }

    public int getStatus()
    {
        return Status;
    }

    public void setStatus(int status)
    {
        this.Status = status;
    }

    public String getMessage()
    {
        return Message;
    }

    public void setMessage(String message)
    {
        this.Message = message;
    }

    public T getData()
    {
        return Data;
    }

    public void setData(T data)
    {
        this.Data = data;
    }


}
