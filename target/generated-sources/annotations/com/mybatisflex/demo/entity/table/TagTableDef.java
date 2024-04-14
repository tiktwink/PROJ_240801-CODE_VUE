package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class TagTableDef extends TableDef {

    public static final TagTableDef TAG = new TagTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn TAGS = new QueryColumn(this, "tags");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, TAGS};

    public TagTableDef() {
        super("", "tag");
    }

}
