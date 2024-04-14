package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class ExamTableDef extends TableDef {

    public static final ExamTableDef EXAM = new ExamTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn NAME = new QueryColumn(this, "name");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn CHECKED = new QueryColumn(this, "checked");

    public final QueryColumn EXAM_POS = new QueryColumn(this, "exam_pos");

    public final QueryColumn EXAM_TIME = new QueryColumn(this, "exam_time");

    public final QueryColumn TEST_CODE = new QueryColumn(this, "test_code");

    public final QueryColumn CHECK_CODE = new QueryColumn(this, "check_code");

    public final QueryColumn CHECK_TIME = new QueryColumn(this, "check_time");

    public final QueryColumn SUBJECT_ID = new QueryColumn(this, "subject_id");

    public final QueryColumn SUBJECT_NAME = new QueryColumn(this, "subject_name");

    public final QueryColumn DOUBLE_CHECKED = new QueryColumn(this, "double_checked");

    public final QueryColumn PLAN_CHECK_TIME = new QueryColumn(this, "plan_check_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, NAME, TITLE, CHECKED, EXAM_POS, EXAM_TIME, TEST_CODE, CHECK_CODE, CHECK_TIME, SUBJECT_ID, SUBJECT_NAME, DOUBLE_CHECKED, PLAN_CHECK_TIME};

    public ExamTableDef() {
        super("", "exam");
    }

}
