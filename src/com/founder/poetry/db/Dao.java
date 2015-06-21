/**
 * Filename:    Dao.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-11 涓婂崍9:47:36
 * Description: 鏁版嵁搴撴搷浣滄帴鍙�
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-11    鐜嬫稕             1.0          1.0 Version
 */
package com.founder.poetry.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;

public interface Dao<T> {

    /**
     * 
     * @Title: queryByUUID
     * @Description:通过某一列对应的值进行查询数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     * @param fieldName
     *            对应的列
     * @param fieldValue
     *            对应的字段
     */
    public ArrayList<T> queryByField(String fieldName, String fieldValue) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: close
     * @Description: 关闭数据库连接
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public void close();

    /**
     * 
     * @Title: queryAllData
     * @Description:查询出T,对应的表的所有数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> queryAllData() throws SQLException, ClassNotFoundException, IllegalAccessException,
            InstantiationException;
    /**
     * 
     * @Title: queryAllData
     * @Description:查询出T,对应的表的所有数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> queryAllData(String where) throws SQLException, ClassNotFoundException, IllegalAccessException,
            InstantiationException;

    /**
     * 
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @param columns
     *            : 需要查询的列
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @param groupBy
     *            : 按某一列进行分组
     * @param having
     *            : groupBy 分组过滤条件
     * @param orderBy
     *            : 需要排序的列
     * @param ordertype
     *            : orderby 排序规则
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
            String orderBy, boolean ordertype) throws SQLException, ClassNotFoundException, IllegalAccessException,
            InstantiationException;

    /**
     * 
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @param columns
     *            : 需要查询的列
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @param groupBy
     *            : 按某一列进行分组
     * @param having
     *            : groupBy 分组过滤条件
     * @param orderBy
     *            : 需要排序的列
     * @param ordertype
     *            : orderby 排序规则
     * @param paseSize
     *            : 每页的数据
     * @param offset
     *            : 去除前面多少条数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
            String orderBy, boolean ordertype, int paseSize, int offset) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException;

    public int getTotalNumberCount(String[] columns, String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, boolean ordertype) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @param groupBy
     *            : 按某一列进行分组
     * @param having
     *            : groupBy 分组过滤条件
     * @param orderBy
     *            : 需要排序的列
     * @param ordertype
     *            : orderby 排序规则
     * @param paseSize
     *            : 每页的数据
     * @param offset
     *            : 去除前面多少条数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy,
            boolean ordertype, int paseSize, int offset) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @param orderBy
     *            : 需要排序的列
     * @param ordertype
     *            : orderby 排序规则
     * @param paseSize
     *            : 每页的数据
     * @param offset
     *            : 去除前面多少条数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> query(String selection, String[] selectionArgs, String orderBy, boolean ordertype, int paseSize,
            int offset) throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @param paseSize
     *            : 每页的数据
     * @param offset
     *            : 去除前面多少条数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> query(String selection, String[] selectionArgs, int paseSize, int offset) throws SQLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: query
     * @Description: 基础的查询接口，用于预留接口，给其他未知条件的查询
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public List<T> query(String selection, String[] selectionArgs) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: query
     * @Description:
     * @param selection
     *            : where 条件
     * @param selectionArgs
     *            : where 条件中的参数值
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     */
    public int queryTotalCountBySelection(String selection, String[] selectionArgs) throws SQLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: getCursorByOrderFiled
     * @Description:
     * @param orderByColumn
     *            : 需要排序的列
     * @param desc
     *            : orderByColumn 排序规则
     * @author 王涛
     * @date 2014-9-5
     * @version 1.0
     */
    public Cursor getCursorByOrderFiled(String orderByColumn, boolean desc) throws SQLException, ClassNotFoundException;

    /**
     * 
     * @Title: queryForFieldValues
     * @Description: 根据键值对，查询数据
     * @author 王涛
     * @date 2014-6-11
     * @version 1.0
     * @param fieldValues
     *            查询条件
     */
    public List<T> queryForFieldValues(Map<String, Object> fieldValues) throws SQLException, ClassNotFoundException,
            IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: updateTalbeInfo
     * @Description: 更新数据库表
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     * @param ContentValues
     *            需要更新的数据
     * @param fieldValues
     *            更新条件
     */
    public int updateTalbeInfo(ContentValues values, Map<String, Object> fieldValues) throws SQLException;

    /**
     * 
     * @Title: delTalbelInfo
     * @Description: 删除数据库表信息,根据特定的删除条件
     * 
     * @param fieldValues
     *            : 删除的条件
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     * @param fieldValues
     *            删除条件
     */
    public int delTalbelInfo(Map<String, Object> fieldValues) throws SQLException;

    /**
     * 
     * @Title: insertDataToDB
     * @Description: 批量插入数据,在这里使用事务，可以提高操作效率
     * @param dataList
     *            : 需要插入的数据
     * @author 王涛
     * @date 2014-6-23
     * @version 1.0
     */
    public int insertDataToDB(ArrayList<T> dataList) throws SQLException;

    /**
     * 
     * @Title: delete
     * @Description: 删除数据库数据
     * @param whereClause
     *            : where 条件
     * @param whereArgs
     *            : where 条件中的参数值
     * @author 王涛
     * @date 2014-8-8
     * @version 1.0
     */
    public int delete(String whereClause, String[] whereArgs) throws SQLException;

    /**
     * 
     * @Title: queryAllDataByOrderFiled
     * @Description: 按某一个列进行排序
     * @param orderByColumn
     *            要排序的列
     * @param desc
     *            : 降序
     * @author 王涛
     * @date 2014-8-8
     * @version 1.0
     */
    public List<T> queryAllDataByOrderFiled(String orderByColumn, boolean desc) throws SQLException,
            ClassNotFoundException, IllegalAccessException, InstantiationException;

    /**
     * 
     * @Title: queryAllDataByOrderFiled
     * @Description:分页查找
     * @param orderByColumn
     *            要排序的列
     * @param desc
     *            : 降序
     * @param pageSize
     *            : 每页的大小
     * @param offset
     *            ： offset的值
     * @author 王涛
     * @date 2014-8-8
     * @version 1.0
     */
    public List<T> queryAllDataByOrderFiled(String orderByColumn, boolean desc, int pageSize, int offset)
            throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;
    
    /**
     * 
     * @Title: queryAllDataByOrderFiled
     * @Description:分页查找
     * @param orderByColumn
     *            要排序的列
     * @param desc
     *            : 降序
     * @param pageSize
     *            : 每页的大小
     * @param offset
     *            ： offset的值
     * @author 王涛
     * @date 2014-8-8
     * @version 1.0
     */
    public List<T> queryAllDataByOrderFiled(String orderByColumn,String where, boolean desc, int pageSize, int offset)
            throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException;
    

    /**
     * 
     * @Title: insert
     * @Description: 封装插入参数
     * @param values
     *            : 键值对
     * @param nullColumnHack
     *            : 注意这个参数，是在values为null时，指定的为空的列：为什么呢？ 如果第三个参数values 为Null或者元素个数为0，
     *            由于Insert()方法要求必须添加一条除了主键之外其它字段为Null值的记录，为了满足SQL语法的需要， insert语句必须给定一个字段名，如：insert into person(name)
     *            values(NULL)，倘若不给定字段名 ， insert语句就成了这样： insert into person()
     *            values()，显然这不满足标准SQL的语法。对于字段名，建议使用主键之外的字段，如果使用了INTEGER类型的主键字段，执行类似insert into person(personid)
     *            values(NULL)的insert语句后，该主键字段值也不会为NULL。如果第三个参数values 不为Null并且元素的个数大于0 ，可以把第二个参数设置为null
     * @author 王涛
     * @date 2014-8-18
     * @version 1.0
     */
    public long insert(String nullColumnHack, ContentValues values);

    /**
     * 
     * @Title: update
     * @Description: 更新数据库文件
     * @param values
     *            : 键值对
     * @param whereClause
     *            : where 条件
     * @param whereArgs
     *            : where 条件中的参数值
     * @author 王涛
     * @date 2014-8-18
     * @version 1.0
     */
    public int update(ContentValues values, String whereClause, String[] whereArgs);

    /**
     * 
     * @Title: queryColumnTotalValue
     * @Description: 去按某列分组的某几列的和值
     * @param groupBy
     *            : 按某一列进行分组
     * @param totalColumnArrayList
     *            ：需要求和的列
     * @param whereClause
     *            : where 条件
     * @param whereArgs
     *            : where 条件中的参数值
     * @param having
     *            : groupBy 分组过滤条件
     * @param orderby
     *            : 需要排序的列集合
     * @param up
     *            : orderby 集合对应的列排序规则
     * @param pageSize
     *            : 每页的数据
     * @param offset
     *            : 去除前面多少条数据
     * @author 王涛
     * @date 2014-9-4
     * @version 1.0
     */
    public List<T> queryColumnTotalValue(String groupBy, ArrayList<String> totalColumnArrayList, String whereClause,
            String[] whereArgs, String having, ArrayList<String> orderby, boolean[] up, int pageSize, int offset)
            throws SQLException;

    /**
     * 
     * @Title: queryColumnTotalValue
     * @Description: 去按某列分组的某几列的和值
     * @param groupBy
     *            [] : 按某那些列进行分组
     * @param totalColumnArrayList
     *            ：需要求和的列
     * @param whereClause
     *            : where 条件
     * @param whereArgs
     *            : where 条件中的参数值
     * @param having
     *            : groupBy 分组过滤条件
     * @param orderby
     *            : 需要排序的列集合
     * @param up
     *            : orderby 集合对应的列排序规则
     * @param pageSize
     *            : 每页的数据
     * @param offset
     *            : 去除前面多少条数据
     * @author 王涛
     * @date 2014-9-4
     * @version 1.0
     */
    public List<T> queryColumnTotalValue(String[] groupBy, ArrayList<String> totalColumnArrayList, String whereClause,
            String[] whereArgs, String having, ArrayList<String> orderby, boolean[] ordertype, int pageSize, int offset)
            throws SQLException;
}
