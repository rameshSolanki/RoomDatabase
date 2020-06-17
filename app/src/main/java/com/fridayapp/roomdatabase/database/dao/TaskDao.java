package com.fridayapp.roomdatabase.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.fridayapp.roomdatabase.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Query("DELETE FROM task_table WHERE `action` = 1")
    void deleteCompletedTasks();

    @Query("SELECT * FROM task_table ORDER BY `action` DESC")
    LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE title")
    LiveData<List<Task>> searchAllTask();

    @Query("SELECT * FROM task_table ORDER BY priority_column ASC")
    LiveData<List<Task>> sortByPriorityASC();

    @Query("SELECT * FROM task_table ORDER BY time ASC")
    LiveData<List<Task>> sortByTimeCreatedASC();


    @Query("SELECT * FROM task_table WHERE priority_column = 1")
    LiveData<List<Task>> highPriority();

    @Query("SELECT * FROM task_table WHERE priority_column = 2")
    LiveData<List<Task>> mediumPriority();

    @Query("SELECT * FROM task_table WHERE priority_column = 3")
    LiveData<List<Task>> lowPriority();

    @Query("SELECT * FROM task_table WHERE `action` = 2")
    LiveData<List<Task>> getPendingTask();

    @Query("SELECT * FROM task_table WHERE `action` = 1")
    LiveData<List<Task>> getCompletedTasks();

}
