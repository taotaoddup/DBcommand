/**
 * Filename:    DBConstant.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-10 上午11:35:40
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-10    王涛             1.0          1.0 Version
 */
package com.founder.poetry.db;

import android.content.Context;

import com.founder.poetry.db.table.OrderResultData;


public class DBConstant {

    // 数据库版本
    public static final int dbVer = 12;
    // 数据库名称
    public static final String DBNAME = "HDENFORCEMENT";
    /** 数据库表，对应的bean类，自己动态的添加 **/
    /** 每张表都存在_id这个字段用于标示一个自动增长的列，在必要的啥时候，你可以能会使用到它 ，比如排序或者作为查询条件 **/
    public static Class[] beanClassArrary = new Class[] { OrderResultData.class };
    /**
     * 在你使用数据库工具提供的基础的几个操作方法(saveToDB(),modifyToDB()
     * ,deleteFromDB(),isExistInDataBase())之前，一定要调用setmInstance方法为后续的数据库操作提供上下文对象， 如果你没有调用该方法，程序是无法正常运行的
     * 所以，最好你重写系统的Application类，，这样在应用刚刚启动时，就可以调用setmInstance方法为mInstance初始化，方便以后使用， 否则，你需要使用另外的一组公用的方法(saveToDB(Context
     * context),modifyToDB(Context context) ,deleteFromDB(Context context),isExistInDataBase(Context
     * context)),主要这里的context对象 最好是通过getApplicationContext()获得的
     */
    private static Context mInstance = null;

    public static Context getmInstance() {
        return mInstance;
    }

    public static void setmInstance(Context mInstance) {
        DBConstant.mInstance = mInstance;
    }
}
