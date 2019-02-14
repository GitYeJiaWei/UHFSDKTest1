package com.ioter.uhfscan.data.greendao;


import com.ioter.uhfscan.AppApplication;
import com.ioter.uhfscan.data.greendao.dao.DaoMaster;
import com.ioter.uhfscan.data.greendao.dao.DaoSession;

//工具类调用方法，设置单例模式
public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoManager mInstance; //单例模式

    /**
     * 初始化数据
     */
    private GreenDaoManager(){
        if (mInstance==null){
            DaoMaster.DevOpenHelper devOpenHelper = new
                    DaoMaster.DevOpenHelper(AppApplication.getApplication(), "user1-db", null);//此处为自己需要处理的表
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    /**
     * 对外唯一实例的接口
     * @return
     */
    public static GreenDaoManager getInstance(){
        if (mInstance==null){
            synchronized (GreenDaoManager.class){//保证异步处理安全操作
                /**
                 * 静态内部类，实例化对象使用
                 */
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
