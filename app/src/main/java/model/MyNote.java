package model;

public class MyNote {

    public static final String DATABASE_NAME = "notedb";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "notes";
    public static final String TITLE_NAME = "title";
    public static final String CONTENT_NAME = "content";
    public static final String DATE_NAME = "recorddate";
    public static final String KEY_ID = "_id";
    public String title;
    public String content;
    public String recordDate;
    public int itemId;


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }


}
