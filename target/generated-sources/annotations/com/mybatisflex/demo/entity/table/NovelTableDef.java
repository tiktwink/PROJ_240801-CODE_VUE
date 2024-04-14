package com.mybatisflex.demo.entity.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class NovelTableDef extends TableDef {

    public static final NovelTableDef NOVEL = new NovelTableDef();

    public final QueryColumn ID = new QueryColumn(this, "id");

    public final QueryColumn HITS = new QueryColumn(this, "hits");

    public final QueryColumn NOTE = new QueryColumn(this, "note");

    public final QueryColumn RATE = new QueryColumn(this, "rate");

    public final QueryColumn TYPE = new QueryColumn(this, "type");

    public final QueryColumn LIKES = new QueryColumn(this, "likes");

    public final QueryColumn TITLE = new QueryColumn(this, "title");

    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn FOLLOWS = new QueryColumn(this, "follows");

    public final QueryColumn AUTHOR_ID = new QueryColumn(this, "author_id");

    public final QueryColumn RATE_COUNT = new QueryColumn(this, "rate_count");

    public final QueryColumn WORD_COUNT = new QueryColumn(this, "word_count");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    public final QueryColumn VOLUME_COUNT = new QueryColumn(this, "volume_count");

    public final QueryColumn CHAPTER_COUNT = new QueryColumn(this, "chapter_count");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, HITS, NOTE, RATE, TYPE, LIKES, TITLE, STATUS, FOLLOWS, AUTHOR_ID, RATE_COUNT, WORD_COUNT, CREATE_TIME, UPDATE_TIME, VOLUME_COUNT, CHAPTER_COUNT};

    public NovelTableDef() {
        super("", "novel");
    }

}
