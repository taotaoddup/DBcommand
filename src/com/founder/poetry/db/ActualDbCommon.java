/**
 * Filename:    ActualDbCommon.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-8-29 上午11:46:53
 * Description: 与业务相关的数据库库，操作
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-8-29    王涛             1.0          1.0 Version
 */
package com.founder.poetry.db;

import android.content.Context;

public class ActualDbCommon {

    private static ActualDbCommon _Instance;

    public static ActualDbCommon getInstance(Context context) {
        if (_Instance == null) {
            _Instance = new ActualDbCommon();
        }
        return _Instance;
    }

}
