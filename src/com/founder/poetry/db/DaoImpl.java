/**
 * Filename:    DaoImpl.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-11 上午9:49:59
 * Description: 数据库操作实现类
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-11    王涛             1.0          1.0 Version
 */
package com.founder.poetry.db;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DaoImpl<T> implements Dao<T> {

    protected Class<T> dataClass;
    // 上下文，句柄
    private final Context mContext;
    // DBHelper对象
    private DBHelper helper;
    private static Cursor cursor = null;
    private static Object Lock = new Object();
    private static SQLiteDatabase db = null;

    public DaoImpl(Class<T> dataClass, Context context) {

        this.dataClass = dataClass;
        helper = new DBHelper(context.getApplicationContext());
        mContext = context.getApplicationContext();
    }

    @Override
    public Cursor getCursorByOrderFiled(String orderByColumn, boolean desc) throws SQLException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new SQLException("do not have calss ");

            }
            db = helper.getWritableDatabase();
            // 查询数据库
            cursor = db.query(dataClass.getSimpleName(), null, null, null, null, null, orderByColumn + " "
                    + (desc ? "desc" : "asc"));

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
            } else {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }

            }
            return cursor;
        }
    }

    @Override
    public ArrayList<T> queryByField(String fieldName, String valued) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new SQLException("do not have calss ");
            }
            ArrayList<T> resultList = null;
            // 查询数据库
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    cursor = db.query(dataClass.getSimpleName(), tableInfo, fieldName + " = ?",
                            new String[] { valued }, null, null, null);

                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }
            return resultList;
        }
    }

    /**
     * 
     * @Title: getTableInfo
     * @Description: 获取表格中的字段
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    private String[] getTableInfo() {

        Field[] subclass = dataClass.getDeclaredFields();
        Class<? super T> superclass = dataClass.getSuperclass();
        // 父类的数据
        Field[] superSubClass = superclass.getDeclaredFields();
        String[] tableInfo = new String[subclass.length + superSubClass.length];
        for (int i = 0; i < subclass.length; i++) {

            tableInfo[i] = subclass[i].getName();
        }

        for (int j = subclass.length; j < superSubClass.length + subclass.length; j++) {
            tableInfo[j] = superSubClass[j - subclass.length].getName();
        }
        return tableInfo;
    }

    /**
     * 
     * @Title: getDataFromCursor
     * @Description: 从游标中获取需要的字段
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @SuppressWarnings("unchecked")
    private ArrayList<T> getDataFromCursor(Cursor cursor) throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        synchronized (Lock) {
            if (cursor == null) {
                return null;
            }
            ArrayList<T> resultList = new ArrayList<T>();

            // 开始组装值的时候
            while (cursor.moveToNext()) {

                Object result = Class.forName(dataClass.getName()).newInstance();
                Field[] subclass = dataClass.getDeclaredFields();
                Class superclass = dataClass.getSuperclass();
                // 父类的数据
                Field[] superSubClass = superclass.getDeclaredFields();
                for (int i = 0; i < superSubClass.length; i++) {

                    Field valField = superSubClass[i];
                    String valFieldType = valField.getType().toString();
                    valField.setAccessible(true);
                    if (cursor.getColumnIndex(valField.getName()) == -1) {
                        continue;
                    }
                    if ("class java.lang.String".equals(valFieldType)) {
                        valField.set(result, cursor.getString(cursor.getColumnIndex(valField.getName())));
                    } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                        valField.set(result, cursor.getLong(cursor.getColumnIndex(valField.getName())));
                    } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                        valField.set(result, cursor.getInt(cursor.getColumnIndex(valField.getName())));
                    } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                        valField.set(result, cursor.getDouble(cursor.getColumnIndex(valField.getName())));
                    } else if ("float".equals(valFieldType) || "Float".equals(valFieldType)) {
                        valField.set(result, cursor.getDouble(cursor.getColumnIndex(valField.getName())));
                    }
                }
                // 子类数据
                for (int i = 0; i < subclass.length; i++) {

                    Field valField = subclass[i];
                    String valFieldType = valField.getType().toString();
                    valField.setAccessible(true);
                    if (cursor.getColumnIndex(valField.getName()) == -1) {
                        continue;
                    }
                    if ("class java.lang.String".equals(valFieldType)) {
                        valField.set(result, cursor.getString(cursor.getColumnIndex(valField.getName())));
                    } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                        valField.set(result, cursor.getLong(cursor.getColumnIndex(valField.getName())));
                    } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                        valField.set(result, cursor.getInt(cursor.getColumnIndex(valField.getName())));
                    } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                        valField.set(result, cursor.getDouble(cursor.getColumnIndex(valField.getName())));
                    } else if ("float".equals(valFieldType) || "Float".equals(valFieldType)) {
                        valField.set(result, cursor.getFloat(cursor.getColumnIndex(valField.getName())));
                    }
                }
                resultList.add((T) result);
            }
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
            return resultList;
        }
    }

    @Override
    public void close() {

        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

    @Override
    public List<T> queryAllData() throws SQLException, ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new ClassNotFoundException("do not have class ");
            }
            ArrayList<T> resultList = null;
            // 查询数据库
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    cursor = db.query(dataClass.getSimpleName(), tableInfo, null, null, null, null, null);
                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }

            return resultList;
        }
    }

    @Override
    public List<T> queryAllDataByOrderFiled(String orderByColumn, boolean desc) throws SQLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new ClassNotFoundException("do not have class ");
            }
            ArrayList<T> resultList = null;
            // 查询数据库
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    cursor = db.query(dataClass.getSimpleName(), null, null, null, null, null, orderByColumn + " "
                            + (desc ? "desc" : "asc"));
                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }

            return resultList;
        }
    }

    @Override
    public List<T> queryAllDataByOrderFiled(String orderByColumn, boolean desc, int pageSize, int offset)
            throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new ClassNotFoundException("do not have class ");
            }
            ArrayList<T> resultList = null;
            // 查询数据库
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    String sql = "select * from " + dataClass.getSimpleName() + " order by " + orderByColumn
                            + (desc ? " desc" : " asc") + "  limit " + pageSize + " offset " + offset;
                    cursor = db.rawQuery(sql, null);

                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }

            return resultList;
        }
    }

    /**
     * 
     */
    @Override
    public List<T> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
            String orderBy, boolean orderType) throws SQLException {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            ArrayList<T> resultList = null;
            // 查询数据库
            if (orderType) {
                orderBy = orderBy + "asc";
            } else {
                orderBy = orderBy + "desc";
            }
            cursor = db.query(dataClass.getSimpleName(), columns, selection, selectionArgs, groupBy, having, orderBy);
            try {
                resultList = getDataFromCursor(cursor);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    if (!cursor.isClosed()) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
            return resultList;
        }
    }

    @Override
    public List<T> queryForFieldValues(Map<String, Object> fieldValues) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new SQLException("do not have calss ");
            }
            ArrayList<T> resultList = null;
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    // 遍历map对象获得selection和对应的值
                    String[] valuedArr = null;
                    StringBuffer selectionSb = null;
                    int i = 0;
                    if (fieldValues != null) {
                        valuedArr = new String[fieldValues.size()];
                        selectionSb = new StringBuffer();
                        Iterator iter = fieldValues.entrySet().iterator();
                        while (iter.hasNext()) {
                            Map.Entry entry = (Map.Entry) iter.next();
                            String key = (String) entry.getKey();
                            String val = (String) entry.getValue();
                            valuedArr[i++] = val;
                            selectionSb.append(key);
                            selectionSb.append(" = ?");
                        }
                    }

                    cursor = db.query(dataClass.getSimpleName(), tableInfo,
                            selectionSb == null ? null : selectionSb.toString(), valuedArr, null, null, null);

                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }
            return resultList;
        }
    }

    @Override
    public int updateTalbeInfo(ContentValues values, Map<String, Object> fieldValues) throws SQLException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new SQLException("do not have calss ");
            }
            int resultFlag = -1;
            db = helper.getWritableDatabase();
            // 查询数据库
            // 遍历map对象获得selection和对应的值
            String[] valuedArr = null;
            StringBuffer selectionSb = null;
            int i = 0;
            if (fieldValues != null) {
                Iterator iter = fieldValues.entrySet().iterator();
                valuedArr = new String[fieldValues.size()];
                selectionSb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    valuedArr[i++] = val;
                    selectionSb.append(key);
                    selectionSb.append(" = ?");
                }
            }
            resultFlag = db.update(dataClass.getSimpleName(), values,
                    selectionSb == null ? null : selectionSb.toString(), valuedArr);
            db.close();
            return resultFlag;
        }
    }

    @Override
    public int delTalbelInfo(Map<String, Object> fieldValues) throws SQLException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new SQLException("do not have calss ");
            }
            int resultFlag = -1;
            db = helper.getWritableDatabase();
            // 遍历map对象获得selection和对应的值

            String[] valuedArr = null;
            StringBuffer selectionSb = null;
            int i = 0;
            if (fieldValues != null) {
                Iterator iter = fieldValues.entrySet().iterator();
                valuedArr = new String[fieldValues.size()];
                selectionSb = new StringBuffer();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    String val = (String) entry.getValue();
                    valuedArr[i++] = val;
                    selectionSb.append(key);
                    selectionSb.append(" = ?");
                }
            }
            resultFlag = db.delete(dataClass.getSimpleName(), selectionSb == null ? null : selectionSb.toString(),
                    valuedArr);
            return resultFlag;
        }
    }

    /**
     * 批量插入数量处理
     */
    @Override
    public int insertDataToDB(ArrayList<T> dataList) throws SQLException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new SQLException("do not have calss ");
            }
            db = helper.getWritableDatabase();
            db.execSQL("PRAGMA cache_size=12000;");
            db.beginTransaction();
            // 批量插入数据

            for (int i = 0; i < dataList.size(); i++) {
                try {
                    Field uuidField = dataClass.getSuperclass().getDeclaredField("uuid");
                    uuidField.setAccessible(true);
                    String uuid = java.util.UUID.randomUUID().toString();
                    ContentValues values = new ContentValues();
                    values.put(uuidField.getName(), uuid);
                    // 获取子类属性及值
                    Field[] subclass = dataClass.getDeclaredFields();
                    for (int j = 0; j < subclass.length; j++) {
                        Field valField = subclass[j];
                        valField.setAccessible(true);
                        String valFieldType = valField.getType().toString();
                        if ("class java.lang.String".equals(valFieldType)) {
                            values.put(valField.getName(), (String) valField.get(dataList.get(i)));
                        } else if ("long".equals(valFieldType) || "Long".equals(valFieldType)) {
                            values.put(valField.getName(), (Long) valField.get(dataList.get(i)));
                        } else if ("int".equals(valFieldType) || "Int".equals(valFieldType)) {
                            values.put(valField.getName(), (Integer) valField.get(dataList.get(i)));
                        } else if ("double".equals(valFieldType) || "Double".equals(valFieldType)) {
                            values.put(valField.getName(), (Double) valField.get(dataList.get(i)));
                        }
                    }
                    if (values != null) {
                        try {
                            db = helper.getWritableDatabase();
                            db.insert(dataClass.getSimpleName(), null, values);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            db.setTransactionSuccessful();
            db.endTransaction();
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
            return dataList.size();
        }
    }

    private void releaseData() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }
        if (db != null && db.isOpen()) {
            db.close();
            db = null;
        }
    }

    @Override
    public int delete(String whereClause, String[] whereArgs) throws SQLException {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            // 查询数据库
            int number = db.delete(dataClass.getSimpleName(), whereClause, whereArgs);

            return number;
        }
    }

    @Override
    public long insert(String nullColumnHack, ContentValues values) {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            // 插入数据库
            long number = db.insert(dataClass.getSimpleName(), nullColumnHack, values);
            return number;
        }
    }

    @Override
    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            // 更新数据库
            int number = db.update(dataClass.getSimpleName(), values, whereClause, whereArgs);
            return number;
        }
    }

    @Override
    public List<T> queryColumnTotalValue(String groupBy, ArrayList<String> totalColumnArrayList, String whereClause,
            String[] whereArgs, String having, ArrayList<String> orderby, boolean[] ordertype, int pageSize, int offset)
            throws SQLException {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            ArrayList<T> resultList = null;
            boolean nextFor = false;
            // 查询数据库
            // Cursor cursor = db
            // .query(dataClass.getSimpleName(), columns, null, null, groupBy, null, null);
            // select mActionName ,sum(mUserNumber) as mUserNumber ,sum(mTimeNubmer) as mTimeNubmer from OrderResultData
            // group by mActionName
            StringBuffer sqlStringBuffer = new StringBuffer();
            sqlStringBuffer.append("select  ");
            if (!isEmpty(groupBy)) {
                sqlStringBuffer.append(groupBy + " , ");
            }

            if (totalColumnArrayList != null && totalColumnArrayList.size() > 0) {
                for (int i = 0; i < totalColumnArrayList.size(); i++) {

                    if (i < totalColumnArrayList.size() - 1) {
                        sqlStringBuffer.append("sum(" + totalColumnArrayList.get(i) + ")" + " as "
                                + totalColumnArrayList.get(i) + ",");
                    } else {
                        sqlStringBuffer.append("sum(" + totalColumnArrayList.get(i) + ")" + " as "
                                + totalColumnArrayList.get(i));
                    }
                }
            }
            // 为空时取出所有列
            String[] total = getTableInfo();
            if (total != null && total.length > 0) {

                for (int other = 0; other < total.length; other++) {

                    if (total[other].equals(groupBy)) {
                        continue;
                    }
                    if (totalColumnArrayList != null && totalColumnArrayList.size() > 0) {

                        nextFor = false;
                        for (int i = 0; i < totalColumnArrayList.size(); i++) {
                            if (total[other].equals(totalColumnArrayList.get(i))) {
                                nextFor = true;
                            }
                        }
                        if (nextFor) {
                            continue;
                        }
                    }

                    sqlStringBuffer.append(" , " + total[other] + " ");
                }

            }

            sqlStringBuffer.append(" from " + dataClass.getSimpleName());
            if (whereArgs != null && whereArgs.length > 0) {

                sqlStringBuffer.append(" where ");
                // 组装数据
                String[] whereClauseString = whereClause.trim().split("\\?");

                if (whereArgs != null && whereArgs.length > 0 && whereClauseString.length == whereArgs.length) {

                    for (int other = 0; other < whereArgs.length; other++) {

                        sqlStringBuffer.append(" " + whereClauseString[other] + " ");
                        sqlStringBuffer.append(" " + whereArgs[other] + " ");
                    }
                }
            } else {

                if (!isEmpty(whereClause)) {
                    sqlStringBuffer.append(" where ");
                    sqlStringBuffer.append(whereClause + " ");
                }
            }
            if (!isEmpty(groupBy)) {
                sqlStringBuffer.append(" group by " + groupBy);
            }
            if (!isEmpty(having)) {
                sqlStringBuffer.append(" having " + having);
            }

            if (orderby != null && orderby.size() > 0 && ordertype != null && ordertype.length == orderby.size()) {
                sqlStringBuffer.append(" order by ");
                for (int iOrderType = 0; iOrderType < orderby.size(); iOrderType++) {

                    sqlStringBuffer.append(" " + orderby.get(iOrderType) + " ");
                    if (ordertype[iOrderType]) {
                        sqlStringBuffer.append(" asc ");
                    } else {
                        sqlStringBuffer.append(" desc ");
                    }
                    if (iOrderType < orderby.size() - 1) {
                        sqlStringBuffer.append(",");
                    }
                }

            }
            if (pageSize != -1) {
                sqlStringBuffer.append("  limit " + pageSize + " offset " + offset);
            }
            try {
                cursor = db.rawQuery(sqlStringBuffer.toString(), null);
                resultList = getDataFromCursor(cursor);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    if (!cursor.isClosed()) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
            return resultList;
        }
    }

    public static boolean isEmpty(CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }

    @Override
    public List<T> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
            String orderBy, boolean ordertype, int paseSize, int offset) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            ArrayList<T> resultList = null;
            // 查询数据库
            if (!isEmpty(orderBy)) {
                if (ordertype) {
                    orderBy = orderBy + " asc ";
                } else {
                    orderBy = orderBy + " desc ";
                }
            }

            String limit = "";
            if (paseSize > 0 && offset >= 0) {
                limit = limit + " " + offset + " ," + paseSize + " ";
            }
            if (isEmpty(limit)) {
                cursor = db.query(dataClass.getSimpleName(), columns, selection, selectionArgs, groupBy, having,
                        orderBy);
            } else {
                cursor = db.query(dataClass.getSimpleName(), columns, selection, selectionArgs, groupBy, having,
                        orderBy, limit);
            }

            try {
                resultList = getDataFromCursor(cursor);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    if (!cursor.isClosed()) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
            return resultList;
        }
    }

    @Override
    public List<T> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
            boolean ordertype, int paseSize, int offset) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        return query(null, selection, selectionArgs, groupBy, having, orderBy, ordertype, paseSize, offset);
    }

    @Override
    public List<T> query(String selection, String[] selectionArgs, String orderBy, boolean ordertype, int paseSize,
            int offset) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        return query(null, selection, selectionArgs, null, null, orderBy, ordertype, paseSize, offset);
    }

    @Override
    public List<T> query(String selection, String[] selectionArgs, int paseSize, int offset) throws SQLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException {
        return query(null, selection, selectionArgs, null, null, null, false, paseSize, offset);
    }

    @Override
    public List<T> query(String selection, String[] selectionArgs) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        return query(null, selection, selectionArgs, null, null, null, false, -1, -1);
    }

    @Override
    public int getTotalNumberCount(String[] columns, String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, boolean ordertype) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            // 查询数据库
            if (!isEmpty(orderBy)) {
                if (ordertype) {
                    orderBy = orderBy + " asc ";
                } else {
                    orderBy = orderBy + " desc ";
                }
            }

            String limit = "";
            if (isEmpty(limit)) {
                cursor = db.query(dataClass.getSimpleName(), columns, selection, selectionArgs, groupBy, having,
                        orderBy);
            } else {
                cursor = db.query(dataClass.getSimpleName(), columns, selection, selectionArgs, groupBy, having,
                        orderBy, limit);
            }

            if (cursor == null) {
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
                return 0;
            } else {
                int totalNumber = cursor.getCount();
                cursor.close();
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
                return totalNumber;
            }
        }
    }

    @Override
    public List<T> queryColumnTotalValue(String[] groupBy, ArrayList<String> totalColumnArrayList, String whereClause,
            String[] whereArgs, String having, ArrayList<String> orderby, boolean[] ordertype, int pageSize, int offset)
            throws SQLException {
        synchronized (Lock) {
            releaseData();
            db = helper.getWritableDatabase();
            ArrayList<T> resultList = null;
            boolean nextFor = false;
            // 查询数据库
            // Cursor cursor = db
            // .query(dataClass.getSimpleName(), columns, null, null, groupBy, null, null);
            // select mActionName ,sum(mUserNumber) as mUserNumber ,sum(mTimeNubmer) as mTimeNubmer from OrderResultData
            // group by mActionName
            StringBuffer sqlStringBuffer = new StringBuffer();
            sqlStringBuffer.append("select  ");
            if (groupBy != null && groupBy.length > 0) {

                for (int i = 0; i < groupBy.length; i++) {
                    sqlStringBuffer.append(groupBy[i] + " , ");
                }

            }
            if (totalColumnArrayList != null && totalColumnArrayList.size() > 0) {
                for (int i = 0; i < totalColumnArrayList.size(); i++) {

                    if (i < totalColumnArrayList.size() - 1) {
                        sqlStringBuffer.append("sum(" + totalColumnArrayList.get(i) + ")" + " as "
                                + totalColumnArrayList.get(i) + ",");
                    } else {
                        sqlStringBuffer.append("sum(" + totalColumnArrayList.get(i) + ")" + " as "
                                + totalColumnArrayList.get(i));
                    }
                }
            }
            // 为空时取出所有列
            String[] total = getTableInfo();
            if (total != null && total.length > 0) {

                for (int other = 0; other < total.length; other++) {

                    if (total[other].equals(groupBy)) {
                        continue;
                    }
                    if (totalColumnArrayList != null && totalColumnArrayList.size() > 0) {

                        nextFor = false;
                        for (int i = 0; i < totalColumnArrayList.size(); i++) {
                            if (total[other].equals(totalColumnArrayList.get(i))) {
                                nextFor = true;
                            }
                        }
                        if (nextFor) {
                            continue;
                        }
                    }

                    sqlStringBuffer.append(" , " + total[other] + " ");
                }

            }

            sqlStringBuffer.append(" from " + dataClass.getSimpleName());
            if (whereArgs != null && whereArgs.length > 0) {

                sqlStringBuffer.append(" where ");
                // 组装数据
                String[] whereClauseString = whereClause.trim().split("\\?");

                if (whereArgs != null && whereArgs.length > 0 && whereClauseString.length == whereArgs.length) {

                    for (int other = 0; other < whereArgs.length; other++) {

                        sqlStringBuffer.append(" " + whereClauseString[other] + " ");
                        sqlStringBuffer.append(" " + whereArgs[other] + " ");
                    }
                }
            } else {

                if (!isEmpty(whereClause)) {
                    sqlStringBuffer.append(" where ");
                    sqlStringBuffer.append(whereClause + " ");
                }
            }
            if (groupBy != null && groupBy.length > 0) {

                sqlStringBuffer.append(" group by ");
                for (int i = 0; i < groupBy.length; i++) {

                    if (i == groupBy.length - 1) {
                        sqlStringBuffer.append(groupBy[i]);
                    } else {
                        sqlStringBuffer.append(groupBy[i] + " , ");
                    }

                }

            }

            if (!isEmpty(having)) {
                sqlStringBuffer.append(" having " + having);
            }

            if (orderby != null && orderby.size() > 0 && ordertype != null && ordertype.length == orderby.size()) {
                sqlStringBuffer.append(" order by ");
                for (int iOrderType = 0; iOrderType < orderby.size(); iOrderType++) {

                    sqlStringBuffer.append(" " + orderby.get(iOrderType) + " ");
                    if (ordertype[iOrderType]) {
                        sqlStringBuffer.append(" asc ");
                    } else {
                        sqlStringBuffer.append(" desc ");
                    }
                    if (iOrderType < orderby.size() - 1) {
                        sqlStringBuffer.append(",");
                    }
                }

            }
            if (pageSize != -1) {
                sqlStringBuffer.append("  limit " + pageSize + " offset " + offset);
            }
            android.util.Log.v("test", "sql get count------ff------->" + sqlStringBuffer.toString());
            try {
                cursor = db.rawQuery(sqlStringBuffer.toString(), null);
                resultList = getDataFromCursor(cursor);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    if (!cursor.isClosed()) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
            return resultList;
        }
    }

    @Override
    public int queryTotalCountBySelection(String selection, String[] selectionArgs) throws SQLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException {

        int totalNumber = 0;
        synchronized (Lock) {
            releaseData();

            try {

                StringBuffer sqlStringBuffer = new StringBuffer();
                sqlStringBuffer.append("select  count(*) as total from  OrderResultData   ");
                sqlStringBuffer.append("where " + selection);
                android.util.Log.v("test", "sqlStringBuffer------------->" + sqlStringBuffer.toString());
                cursor = db.rawQuery(sqlStringBuffer.toString(), null);
                totalNumber = cursor.getInt(cursor.getColumnIndex("total"));

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    if (!cursor.isClosed()) {
                        cursor.close();
                        cursor = null;
                    }
                }
            }
            if (db != null && db.isOpen()) {
                db.close();
                db = null;
            }
        }
        android.util.Log.v("test", "totalnumber-------ss--------->" + totalNumber);
        return totalNumber;
    }

    @Override
    public List<T> queryAllDataByOrderFiled(String orderByColumn, String where, boolean desc, int pageSize, int offset)
            throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new ClassNotFoundException("do not have class ");
            }
            ArrayList<T> resultList = null;
            // 查询数据库
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    cursor = db.query(dataClass.getSimpleName(), null, where, null, null, null, orderByColumn + " "
                            + (desc ? "desc" : "asc"));
                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }

            return resultList;
        }
    }

    @Override
    public List<T> queryAllData(String where) throws SQLException, ClassNotFoundException, IllegalAccessException,
            InstantiationException {
        synchronized (Lock) {
            releaseData();
            if (dataClass == null) {
                throw new ClassNotFoundException("do not have class ");
            }
            ArrayList<T> resultList = null;
            // 查询数据库
            String[] tableInfo = getTableInfo();
            if (tableInfo != null && tableInfo.length > 0) {
                try {
                    db = helper.getWritableDatabase();
                    // 查询数据库
                    cursor = db.query(dataClass.getSimpleName(), tableInfo, where, null, null, null, null);
                    resultList = getDataFromCursor(cursor);

                } catch (ClassNotFoundException e) {
                    throw e;
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InstantiationException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        if (!cursor.isClosed()) {
                            cursor.close();
                            cursor = null;
                        }
                    }
                }
                if (db != null && db.isOpen()) {
                    db.close();
                    db = null;
                }
            }

            return resultList;
        }
    }
}
