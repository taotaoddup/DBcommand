package com.founder.poetry.db.table;

import java.io.Serializable;

import com.founder.poetry.db.DBConstant;
import com.founder.poetry.db.DefaultDao;


public class BaseData implements Serializable {
    // 数据集UUID 使用 java.util.UUID.randomUUID().toString();生成
    public String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    // 保存到数据库
    public boolean saveToDB() {
        return DefaultDao.getInstance(DBConstant.getmInstance()).DataSave(this);
    }

    // 更新数据
    public boolean modifyToDB() {
        return DefaultDao.getInstance(DBConstant.getmInstance()).DataModify(this);
    }

    // 删除数据
    public boolean deleteFromDB() {
        return DefaultDao.getInstance(DBConstant.getmInstance()).DataDelete(this);
    }

    // 判断数据库是否存在
    public boolean isExistInDataBase() {
        return DefaultDao.getInstance(DBConstant.getmInstance()).isExistInDataBase(this);
    }

    // 保存到数据库
    // 为了避免系统存在系统泄漏的危险，这里的context一定要是通过getApplicationContext()获得的
    // public boolean saveToDB(Context context) {
    // return DefaultDao.getInstance(context).DataSave(this);
    // }

    // 更新数据
    // 为了避免系统存在系统泄漏的危险，这里的context一定要是通过getApplicationContext()获得的
    // public boolean modifyToDB(Context context) {
    // return DefaultDao.getInstance(context).DataModify(this);
    // }

    // 删除数据
    // 为了避免系统存在系统泄漏的危险，这里的context一定要是通过getApplicationContext()获得的
    // public boolean deleteFromDB(Context context) {
    // return DefaultDao.getInstance(context).DataDelete(this);
    // }

    // 判断数据库是否存在
    // 为了避免系统存在系统泄漏的危险，这里的context一定要是通过getApplicationContext()获得的
    // public boolean isExistInDataBase(Context context) {
    // return DefaultDao.getInstance(context).isExistInDataBase(this);
    // }
}
