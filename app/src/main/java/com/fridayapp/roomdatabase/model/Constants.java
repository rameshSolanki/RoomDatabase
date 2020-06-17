package com.fridayapp.roomdatabase.model;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class Constants {
    public static final String CONTENT_AUTHORITY = "com.fridayapp.roomdatabase";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TODOS = "tasks";


    public static final String EXTRA_ID = "com.fridayapp.roomdatabase.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.fridayapp.roomdatabase.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.fridayapp.roomdatabase.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.fridayapp.roomdatabase.EXTRA_PRIORITY";
    public static final String EXTRA_DATE = "com.fridayapp.roomdatabase.EXTRA_DATE";
    public static final String EXTRA_TIME = "com.fridayapp.roomdatabase.EXTRA_TIME";
    public static final String EXTRA_STATUS = "com.fridayapp.roomdatabase.EXTRA_STATUS";
    public static final String EXTRA_UPDATED_ON = "com.fridayapp.roomdatabase.EXTRA_UPDATED_ON";
    public static final String EXTRA_PHOTO = "com.fridayapp.roomdatabase.EXTRA_PHOTO";
    public static final String EXTRA_DONE = "com.fridayapp.roomdatabase.EXTRA_DONE";
    public static final String EXTRA_ALARM = "com.fridayapp.roomdatabase.EXTRA_ALARM";
    public static final String EXTRA_ALARM_ICON = "com.fridayapp.roomdatabase.EXTRA_ALARM_ICON";

    public static final class TodoEntry implements BaseColumns {

        //  The content URI to access the todos data in the provider.
        // content://com.hitanshudhawan.todo/todos
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TODOS);

        // The MIME type for a list of todos.
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY
                + "/" + PATH_TODOS;

        // The MIME type for a single todo.
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY
                + "/" + PATH_TODOS;

    }


}