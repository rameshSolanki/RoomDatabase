package com.fridayapp.roomdatabase.database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.fridayapp.roomdatabase.database.dao.TaskDao;
import com.fridayapp.roomdatabase.model.DateConverter;
import com.fridayapp.roomdatabase.model.Task;

@Database(entities = {Task.class}, version = 5, exportSchema = false)
@TypeConverters(DateConverter.class)

public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;
    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsncTask(instance).execute();
        }
    };

    public static synchronized TaskDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "task_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }

        return instance;
    }

    public abstract TaskDao taskDao();

    private static class PopulateDbAsncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;

        private PopulateDbAsncTask(TaskDatabase db) {
            taskDao = db.taskDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            taskDao.insert(new Task("Title", "Description", 1, "07/10/2018", "08:45",
//                    2, "d MMM yyyy HH:mm aa",true));


            return null;
        }
    }
}
