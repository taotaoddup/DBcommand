/**
 * Filename:    DefaultDao.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-10 上午11:42:30
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-10    王涛             1.0          1.0 Version
 */
package com.founder.poetry.db;

import java.lang.reflect.Field;

import com.founder.poetry.db.table.BaseData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DefaultDao {

    // 锁对象
    // private static final Object lock = new Object();
    // 上下文，句柄
    private static Context mContext;
    // DBHelper对象
    private static DBHelper helper;
    // DefaultDao操作句柄
    private static DefaultDao _Instance;

    private DefaultDao(Context context) {
        helper = new DBHelper(context.getApplicationContext());
    }

    public static DefaultDao getInstance(Context context) {
        if (_Instance == null || mContext == null
                || context.getApplicationContext() != mContext.getApplicationContext()) {
            mContext = context.getApplicationContext();
            _Instance = new DefaultDao(context);
        }
        return _Instance;
    }

    public static DBHelper getHelper() {
        return helper;
    }

    /**
     * 
     * @Title: isTableExist
     * @Description: 判断某一个表是否存在
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean isTableExist(String name) {
        boolean exist = false;
        SQLiteDatabase db = null;
        try {
            db = helper.getWritableDatabase();
            Cursor cursor;
            cursor = db.rawQuery("select count(name) from sqlite_master where type='table' and name='" + name + "'",
                    null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                int tablecount = cursor.getInt(0);
                if (tablecount > 0) {
                    exist = true;
                }
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null) {
            db.close();
            db = null;

        }
        return exist;
    }

    /**
     * 
     * @Title: DataModify
     * @Description: 根据bean对象，更新数据库中对应列的数据
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean DataModify(BaseData bean) {
        boolean modifsuc = false;
        SQLiteDatabase db = null;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            String uuid = (String) uuidField.get(bean);

            db = helper.getWritableDatabase();
            ContentValues values = new ContentValues();
            Field[] subclass = bean.getClass().getDeclaredFields();
            for (int i = 0; i < subclass.length; i++) {
                Field valField = subclass[i];
                valField.setAccessible(true);
                String valFieldType = valField.getType().toString();
                if ("class java.lang.String".equals(valFieldType)) {
                    values.put(valField.getName(), (String) valField.get(bean));
                } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                    values.put(valField.getName(), (Long) valField.get(bean));
                } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                    values.put(valField.getName(), (Integer) valField.get(bean));
                } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                    values.put(valField.getName(), (Double) valField.get(bean));
                } else if ("float".equals(valFieldType) || "Float".equals(valFieldType)) {
                    values.put(valField.getName(), (Float) valField.get(bean));
                }
                // 需要其他类型在此加入
            }
            if (values != null) {

                // uuid是由java工具类产生的，永远都不会重复
                int result = db.update(bean.getClass().getSimpleName(), values, "uuid='" + uuid + "'", null);
                if (result > 0) {
                    modifsuc = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
        return modifsuc;
    }

    /**
     * 
     * @Title: DataDelete
     * @Description: 根据bean删除数据库元素
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean DataDelete(BaseData bean) {
        boolean deletesuc = false;
        SQLiteDatabase db = null;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            String uuid = (String) uuidField.get(bean);
            db = helper.getWritableDatabase();

            int result = db.delete(bean.getClass().getSimpleName(), "uuid='" + uuid + "'", null);
            if (result > 0) {
                deletesuc = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null && db.isOpen()) {
            db.close();
        }
        return deletesuc;
    }

    /**
     * 
     * @Title: isExistInDataBase
     * @Description: 某个对象在表中是否存在
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     */
    public boolean isExistInDataBase(BaseData bean) {

        boolean existData = false;
        SQLiteDatabase db = null;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            String uuid = (String) uuidField.get(bean);
            db = helper.getWritableDatabase();
            Cursor cursor = db.query(bean.getClass().getSimpleName(), null, "uuid='" + uuid + "'", null, null, null,
                    null);
            if (cursor != null && cursor.getCount() > 0) {
                existData = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null && db.isOpen()) {
            db.close();
        }
        return existData;

    }

    /**
     * 
     * @Title: DataSave
     * @Description: 保存到数据库中
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean DataSave(BaseData bean) {
        boolean savesuc = false;
        try {
            Field uuidField = bean.getClass().getSuperclass().getDeclaredField("uuid");
            uuidField.setAccessible(true);
            String uuid = java.util.UUID.randomUUID().toString();
            ContentValues values = new ContentValues();
            values.put(uuidField.getName(), uuid);
            // 获取子类属性及值
            Field[] subclass = bean.getClass().getDeclaredFields();
            for (int i = 0; i < subclass.length; i++) {
                Field valField = subclass[i];
                valField.setAccessible(true);
                String valFieldType = valField.getType().toString();
                if ("class java.lang.String".equals(valFieldType)) {
                    values.put(valField.getName(), (String) valField.get(bean));
                } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                    values.put(valField.getName(), (Long) valField.get(bean));
                } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                    values.put(valField.getName(), (Integer) valField.get(bean));
                } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                    values.put(valField.getName(), (Double) valField.get(bean));
                } else if ("float".equals(valFieldType) || "Float".equals(valFieldType)) {
                    values.put(valField.getName(), (Float) valField.get(bean));
                }
                // 需要其他类型在此加入
            }
            if (values != null) {
                SQLiteDatabase db = null;
                try {
                    db = helper.getWritableDatabase();
                    db.insert(bean.getClass().getSimpleName(), null, values);
                    savesuc = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return savesuc;
    }

    /**
     * 
     * @Title: close
     * @Description: 关闭所有打开的数据库连接
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public void close() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

}
