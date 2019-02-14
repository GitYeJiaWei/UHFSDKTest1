package com.ioter.uhfscan.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ioter.rfid.IReceiveTag;
import com.ioter.rfid.RfidBuilder;
import com.ioter.rfid.RfidHelper;
import com.ioter.uhfscan.bean.BaseEpc;
import com.ioter.uhfscan.common.ActivityCollecter;
import com.ioter.uhfscan.common.ScreenUtils;
import com.ioter.uhfscan.common.util.ACache;
import com.ioter.uhfscan.data.greendao.EpcModel;
import com.ioter.uhfscan.data.greendao.GreenDaoManager;
import com.ioter.uhfscan.data.greendao.dao.EpcModelDao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewBaseActivity extends AppCompatActivity {
    public RfidHelper helper;
    private ACache aCache = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollecter.addActivity(this);
        aCache = ACache.get(this);
    }

    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            BaseEpc baseEpc = (BaseEpc) msg.obj;
            if(baseEpc!=null)
            {
                handleUi(baseEpc);
            }
        }
    };

    public void handleUi(BaseEpc baseEpc){

    }

    protected EpcModelDao getEpcModelDao() {
        return GreenDaoManager.getInstance().getSession().getEpcModelDao();
    }

    /**
     * 批量插入或修改用户信息
     *
     * @param list 用户信息列表
     */
    public void saveNLists(final List<EpcModel> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        getEpcModelDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {
                    EpcModel user = list.get(i);
                    getEpcModelDao().insertOrReplace(user);
                }
            }
        });

    }

    /**
     * 删除所有数据
     */
    public void deleteAllNote() {
        getEpcModelDao().deleteAll();
    }

    /**
     * 根据用户类,删除信息
     *
     * @param user 用户信息类
     */
    public void deleteNote(EpcModel user) {
        getEpcModelDao().delete(user);
    }

    public void querydata() {
        //查询数据详细
        List<EpcModel> users = getEpcModelDao().loadAll();
        Log.i("tagGreen", "当前数量：" + users.size());
        for (int i = 0; i < users.size(); i++) {
            Log.i("tagGreen", "结果：" + users.get(i).getId() + "," + users.get(i).getEpc() + "," + users.get(i).getCasecode()
                    + "," + users.get(i).getMessage() + "," + users.get(i).getState() + "," + users.get(i).getWarehouse() + ";");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得destory
        if (helper != null) {
            helper.destroy();
        }
        ActivityCollecter.removeActivity(this);
    }

    //隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                ScreenUtils.hideKeyboard(ev, view, NewBaseActivity.this);//调用方法判断是否需要隐藏键盘
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public void reHelp() {
        if (helper != null) {
            helper.destroy();
        }
        String set1 = aCache.getAsString("key1");
        String set2 = aCache.getAsString("key2");
        String set3 = aCache.getAsString("key3");
        String set4 = aCache.getAsString("key4");

        Map<Integer, Byte> map1 = new HashMap<>();
        if (set1 != null && !set1.equals("")) {
            map1.put(0, Byte.valueOf(set1));
        }
        if (set2 != null && !set2.equals("")) {
            map1.put(1, Byte.valueOf(set2));
        }
        if (set3 != null && !set3.equals("")) {
            map1.put(2, Byte.valueOf(set3));
        }
        if (set4 != null && !set4.equals("")) {
            map1.put(3, Byte.valueOf(set4));
        }
        if (map1.size() == 0) {
            map1.put(0, (byte) 15);
            map1.put(1, (byte) 15);
        }

        byte[] setAns = new byte[map1.size()];
        byte[] setPowers = new byte[map1.size()];

        StringBuffer sb = new StringBuffer();
        int a = 0;
        Object[] key_arr = map1.keySet().toArray();
        Arrays.sort(key_arr);
        for (Object key2 : key_arr) {
            int kk = (int) key2;
            setPowers[a] = map1.get(kk);
            setAns[a] = Byte.valueOf(kk + "");
            sb.append(kk + 1 + "("+setPowers[a]+")  ");
            a++;
        }

        helper = new RfidBuilder().setConnectType(RfidBuilder.COM_TYPE).setPort("dev/ttyS1").setBaudRate(115200).setWorkAnts(setAns).setWorkAntPowers(setPowers).setReceiveTagListener(new IReceiveTag()
                //mHelper =  new RfidBuilder().setConnectType(RfidBuilder.IP_TYPE).setHost("192.168.31.188").setIpPort(8086).setWorkAnts(new byte[]{0}).setWorkAntPowers(new byte[]{25}).setReceiveTagListener(new IReceiveTag()
        {
            /**
             * 数据回调
             * @param strEPC EPC标签值
             * @param strRSSI rssi值
             * @param btAntId 天线id
             */
            @Override
            public void onReceiveTags(String strEPC, String strRSSI, byte btAntId) {
                Log.d("TAG", "epc:" + strEPC + "-----rssi:" + strRSSI + "----AntId:" + btAntId);
                Message msg = handler.obtainMessage();
                BaseEpc baseEpc = new BaseEpc();
                baseEpc._EPC = strEPC;
                msg.obj = baseEpc;
                handler.sendMessage(msg);
            }

            /**
             *
             * @param btGpio1Value 传感gpio值
             * @param btGpio2Value
             * @param btGpio3Value
             * @param btGpio4Value
             */
            @Override
            public void onGpioValue(int btGpio1Value, int btGpio2Value, int btGpio3Value, int btGpio4Value) {
                Log.d("TAG", btGpio1Value + "," + btGpio2Value + "," + btGpio3Value + "," + btGpio4Value);
            }

            @Override
            public void onStatus(boolean isWork, int statusValue) {
                if (isWork) {
                    //开启盘点
                }
                Log.d("onStatus", "onStatus:" + isWork + "-----" + statusValue);
            }

            @Override
            public void onTagLostConnect() {
                Log.d("TAG", "onTagConnect");
            }

            @Override
            public void onGpioLostConnect() {
                Log.d("TAG", "onGpioConnect");
            }
        }).build();
        helper.isRfiderWork(1);
    }


}
