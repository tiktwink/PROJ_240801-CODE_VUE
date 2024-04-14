package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class UserTableDef extends TableDef {

    public static final UserTableDef USER = new UserTableDef();

    public final QueryColumn TOKEN = new QueryColumn(this, "token");

    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn USERNAME = new QueryColumn(this, "username");

    public final QueryColumn USERNICK = new QueryColumn(this, "usernick");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn TOKEN_GENERATE_DT = new QueryColumn(this, "token_generate_dt");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{TOKEN, PASSWORD, USERNAME, USERNICK, TOKEN_GENERATE_DT};

    public UserTableDef() {
        super("", "user");
    }

}
