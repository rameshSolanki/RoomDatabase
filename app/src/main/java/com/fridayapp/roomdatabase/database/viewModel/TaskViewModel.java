package com.fridayapp.roomdatabase.database.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import com.fridayapp.roomdatabase.database.repository.TaskRepository;
import com.fridayapp.roomdatabase.model.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;
    private LiveData<List<Task>> getAllTasks;
    private LiveData<List<Task>> sortByPriority;
    private LiveData<List<Task>> sortByTimeCreated;
    private LiveData<List<Task>> highPriority;
    private LiveData<List<Task>> mediumPriority;
    private LiveData<List<Task>> lowPriority;
    private LiveData<List<Task>> getPendingTask;
    private LiveData<List<Task>> getCompletedTasks;
    private LiveData<List<Task>> searchAllTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        getAllTasks = repository.getAllTasks();
        sortByPriority = repository.sortByPriority();
        sortByTimeCreated = repository.sortByTimeCreated();
        highPriority = repository.highPriority();
        mediumPriority = repository.mediumPriority();
        lowPriority = repository.lowPriority();
        getPendingTask = repository.getPendingTask();
        getCompletedTasks = repository.getCompletedTasks();
        searchAllTask = repository.searchAllTask();

    }

    public void insert(Task task) {
        repository.insert(task);
    }

    public void update(Task task) {
        repository.update(task);
    }

    public void delete(Task task) {
        repository.delete(task);
    }

    public void deleteAllTasks() {
        repository.deleteAllTasks();
    }
    public void deleteCompletedTasks() {
        repository.deleteCompletedTasks();
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

    public LiveData<List<Task>> highPrioprity() {
        return highPriority;
    }

    public LiveData<List<Task>> mediumPriority() {
        return mediumPriority;
    }

    public LiveData<List<Task>> lowPriority() {
        return lowPriority;
    }

    public LiveData<List<Task>> getPendingTask() {
        return getPendingTask;
    }

    public LiveData<List<Task>> getCompletedTasks() {
        return getCompletedTasks;
    }

    public LiveData<List<Task>> searchAllTask(String query) {
        return searchAllTask;
    }


}
