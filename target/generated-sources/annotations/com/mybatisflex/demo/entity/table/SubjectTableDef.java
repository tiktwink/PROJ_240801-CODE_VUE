package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SubjectTableDef extends TableDef {

    public static final SubjectTableDef SUBJECT = new SubjectTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn NUM = new QueryColumn(this, "num");

    public final QueryColumn NOTE = new QueryColumn(this, "note");

    public final QueryColumn TYPE = new QueryColumn(this, "type");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn SUGS_GRADE = new QueryColumn(this, "sugs_grade");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, NUM, NOTE, TYPE, TITLE, SUGS_GRADE};

    public SubjectTableDef() {
        super("", "subject");
    }

}
