package com.ioter.uhfscan.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ioter.uhfscan.ui.activity.NewBaseActivity;


public class IoterDialog extends Dialog
{
    protected NewBaseActivity mBaseActivity;

    public IoterDialog(Context context)
    {
        super(context);
        if (context instanceof NewBaseActivity)
        {
            mBaseActivity = (NewBaseActivity) context;
        }
    }

    public IoterDialog(Context context, int theme)
    {
        super(context, theme);
        if (context instanceof NewBaseActivity)
        {
            mBaseActivity = (NewBaseActivity) context;
        }
    }

    public IoterDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
        if (context instanceof NewBaseActivity)
        {
            mBaseActivity = (NewBaseActivity) context;
        }
    }

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view, LayoutParams params)
    {
        super.setContentView(view, params);
    }
}