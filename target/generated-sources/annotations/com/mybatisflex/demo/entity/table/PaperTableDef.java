package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class PaperTableDef extends TableDef {

    public static final PaperTableDef PAPER = new PaperTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn URL = new QueryColumn(this, "url");

    public final QueryColumn JUDGE = new QueryColumn(this, "judge");

    public final QueryColumn EXAM_ID = new QueryColumn(this, "exam_id");

    public final QueryColumn HAS_URL = new QueryColumn(this, "has_url");

    public final QueryColumn TESTED = new QueryColumn(this, "tested");

    public final QueryColumn CHECKED = new QueryColumn(this, "checked");

    public final QueryColumn BASE_SCORE = new QueryColumn(this, "base_score");

    public final QueryColumn CHECK_TIME = new QueryColumn(this, "check_time");

    public final QueryColumn STUDENT_ID = new QueryColumn(this, "student_id");

    public final QueryColumn TEST_TIME = new QueryColumn(this, "test_time");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn JUDGE_SCORE = new QueryColumn(this, "judge_score");

    public final QueryColumn DOUBLE_CHECKED = new QueryColumn(this, "double_checked");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, URL, JUDGE, EXAM_ID, HAS_URL, TESTED, CHECKED, BASE_SCORE, CHECK_TIME, STUDENT_ID, TEST_TIME, CREATE_TIME, JUDGE_SCORE, DOUBLE_CHECKED};

    public PaperTableDef() {
        super("", "paper");
    }

}
