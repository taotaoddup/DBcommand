/**
 * Filename:    TestCase.java
 * Copyright:   Copyright (c)2010
 * Company:     Founder Mobile Media Technology(Beijing) Co.,Ltd.g
 * @version:    1.0
 * @since:       JDK 1.6.0_21
 * Create at:   2014-6-23 下午2:29:16
 * Description:
 * Modification History:
 * Date     Author           Version           Description
 * ------------------------------------------------------------------
 * 2014-6-23    王涛             1.0          1.0 Version
 */
package com.example.dbtest;

import android.test.AndroidTestCase;

import com.founder.poetry.db.table.UserBean;

public class TestCase extends AndroidTestCase {
    public void testSomethingElse() throws Throwable {

        UserBean tempUserBean2 = new UserBean();
        tempUserBean2.setNickname("wangtao69999");
        tempUserBean2.saveToDB();

    }
}
