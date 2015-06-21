/**
 * Filename:    DbHelper.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-10 上午10:23:02
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-10    王涛             1.0          1.0 Version
 */
package com.founder.poetry.db;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    @Override
    public void onCreate(SQLiteDatabase db) {
        OrmDataBase(DBConstant.beanClassArrary, db);
    }

    public DBHelper(Context context) {
        super(context, DBConstant.DBNAME, null, DBConstant.dbVer);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    /**
     * 
     * @Title: OrmDataBase
     * @Description: 根据类，创建数据库表
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public boolean OrmDataBase(Class[] classes, SQLiteDatabase db) {
        boolean isSuc = false;
        try {
            if (classes != null && classes.length > 0) {
                for (int i = 0; i < classes.length; i++) {
                    ClassToDataBaseTable(classes[i], db);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuc;
    }

    /**
     * 检测表是否存在
     * 
     * @param name
     *            表名
     * @param db
     *            数据库对象
     * @return 表存在返回true 表不存在返回false
     */
    public boolean isTableExist(String name, SQLiteDatabase db) {
        boolean exist = false;

        try {
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

        return exist;
    }

    /**
     * 
     * @Title: ClassToDataBaseTable
     * @Description: 根据类实例创建数据库表 每张表都存在_id这个字段用于标示，一个自动增长的列，在必要的啥时候，你可以能会使用到它
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public void ClassToDataBaseTable(Class<?> _Class, SQLiteDatabase db) {
        try {
            boolean istableexist = isTableExist(_Class.getSimpleName(), db);
            // 判断表是否在数据库中存在 不存在则创建
            if (istableexist == false) {
                // 不存在创建表
                db.execSQL("CREATE TABLE IF NOT EXISTS [" + _Class.getSimpleName()
                        + "]([uuid] CHAR NOT NULL, _id INTEGER PRIMARY KEY AUTOINCREMENT)");
            }
            // CREATE TABLE IF NOT EXISTS [C] ([uuid] CHAR NOT NULL, countId INTEGER PRIMARY KEY AUTOINCREMENT)
            // 查询数据库中该表的字段，如果某个字段不存在，则添加这个字段
            Cursor cursor = db.rawQuery("select * from " + _Class.getSimpleName(), null);
            Field[] FieldArrary = _Class.getDeclaredFields();
            for (int j = 0; j < FieldArrary.length; j++) {
                if (cursor != null) {
                    // 返回下标为-1表示数据库当中没有该字段
                    if (cursor.getColumnIndex(FieldArrary[j].getName()) == -1) {
                        String columnType = ReflectionObjectTypeToDataType(FieldArrary[j].getType().toString());
                        try {
                            db.execSQL("alter table " + _Class.getSimpleName() + " ADD COLUMN "
                                    + FieldArrary[j].getName() + " " + columnType);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // 关闭游标
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                cursor = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @Title: ReflectionObjectTypeToDataType
     * @Description: 根据属性的类型判断，设置对应sqlite数据库表的字段的类型
     * @author 王涛
     * @date 2014-6-10
     * @version 1.0
     */
    public static String ReflectionObjectTypeToDataType(String subClassType) {
        String columnType = "";
        if ("class java.lang.String".equals(subClassType)) {
            columnType = " CHAR";
        } else if ("long".equals(subClassType) || "Long".equals(subClassType)) {
            columnType = " NUMBER";
        } else if ("int".equals(subClassType) || "Int".equals(subClassType)) {
            columnType = " NUMBER";
        } else if (("double".equals(subClassType) || "Double".equals(subClassType))) {
            columnType = " NUMBER";
        }// 需要其他类型在此加入
        return columnType;
    }
}
