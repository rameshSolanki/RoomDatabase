package com.fridayapp.roomdatabase.database.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.fridayapp.roomdatabase.database.TaskDatabase;
import com.fridayapp.roomdatabase.database.dao.TaskDao;
import com.fridayapp.roomdatabase.model.Task;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> getAllTasks;
    private LiveData<List<Task>> sortByPriority;
    private LiveData<List<Task>> sortByTimeCreated;
    private LiveData<List<Task>> highPriority;
    private LiveData<List<Task>> mediumPriority;
    private LiveData<List<Task>> lowPriority;
    private LiveData<List<Task>> getPendingTask;
    private LiveData<List<Task>> getCompletedTasks;
    private LiveData<List<Task>> searchAllTask;

    public TaskRepository(Application application) {

        TaskDatabase database = TaskDatabase.getInstance(application);
        taskDao = database.taskDao();
        getAllTasks = taskDao.getAllTasks();
        sortByPriority = taskDao.sortByPriorityASC();
        sortByTimeCreated = taskDao.sortByTimeCreatedASC();
        highPriority = taskDao.highPriority();
        mediumPriority = taskDao.mediumPriority();
        lowPriority = taskDao.lowPriority();
        getPendingTask = taskDao.getPendingTask();
        getCompletedTasks = taskDao.getCompletedTasks();
        searchAllTask = taskDao.searchAllTask();

    }

    public LiveData<List<Task>> searchAllTask() {

        return searchAllTask;

    }

    public LiveData<List<Task>> getCompletedTasks() {

        return getCompletedTasks;

    }

    public LiveData<List<Task>> getPendingTask() {

        return getPendingTask;

    }

    public LiveData<List<Task>> highPriority() {

        return highPriority;

    }

    public LiveData<List<Task>> mediumPriority() {

        return mediumPriority;

    }

    public LiveData<List<Task>> lowPriority() {

        return lowPriority;

    }

    public void insert(Task task) {
        new InsertTaskAsyncTask(taskDao).execute(task);

    }

    public void update(Task task) {
        new UpdateTaskAsyncTask(taskDao).execute(task);

    }

    public void delete(Task task) {
        new DeleteTaskAsyncTask(taskDao).execute(task);

    }

    public void deleteAllTasks() {
        new DeleteAllTaskAsyncTask(taskDao).execute();

    }

    public LiveData<List<Task>> getAllTasks() {

        return getAllTasks;

    }

    public LiveData<List<Task>> sortByPriority() {

        return sortByPriority;

    }

    public LiveData<List<Task>> sortByTimeCreated() {

        return sortByTimeCreated;

    }
    public void deleteCompletedTasks() {
        new deleteCompletedTasksAsyncTask(taskDao).execute();

    }

    private static class DeleteAllTaskAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;

        private DeleteAllTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteAll();
            return null;
        }
    }
    private static class deleteCompletedTasksAsyncTask extends AsyncTask<Void, Void, Void> {

        private TaskDao taskDao;

        private deleteCompletedTasksAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            taskDao.deleteCompletedTasks();
            return null;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private DeleteTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.delete(tasks[0]);
            return null;
        }
    }

    private static class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private UpdateTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.update(tasks[0]);
            return null;
        }
    }

    private static class InsertTaskAsyncTask extends AsyncTask<Task, Void, Void> {

        private TaskDao taskDao;

        private InsertTaskAsyncTask(TaskDao taskDao) {
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            taskDao.insert(tasks[0]);
            return null;
        }
    }
}
