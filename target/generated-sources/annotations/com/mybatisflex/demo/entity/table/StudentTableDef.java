package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class StudentTableDef extends TableDef {

    public static final StudentTableDef STUDENT = new StudentTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn NAME = new QueryColumn(this, "name");

    public final QueryColumn BIRTH = new QueryColumn(this, "birth");

    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    public final QueryColumn GENDER = new QueryColumn(this, "gender");

    public final QueryColumn CLASS_ID = new QueryColumn(this, "class_id");

    public final QueryColumn MAJOR_ID = new QueryColumn(this, "major_id");

    public final QueryColumn ENTRY_TIME = new QueryColumn(this, "entry_time");

    public final QueryColumn MAJOR_FLOW_ID = new QueryColumn(this, "major_flow_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, NAME, BIRTH, GRADE, GENDER, CLASS_ID, MAJOR_ID, ENTRY_TIME, MAJOR_FLOW_ID};

    public StudentTableDef() {
        super("", "student");
    }

}
