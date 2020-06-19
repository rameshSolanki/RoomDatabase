package com.fridayapp.roomdatabase.activities;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.dbexporterlibrary.ExportDbUtil;
import com.android.dbexporterlibrary.ExporterListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.adapter.TaskAdapter;
import com.fridayapp.roomdatabase.database.viewModel.TaskViewModel;
import com.fridayapp.roomdatabase.model.AppUtils;
import com.fridayapp.roomdatabase.model.Constants;
import com.fridayapp.roomdatabase.model.Task;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TaskAdapter.OnDeleteClickListener, View.OnClickListener, DataListListener, ExporterListener {

    public static final int REQUEST_CODE_NOTIFICATION = 1002;
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    TaskAdapter adapter;
    TextView addTask;
    CardView cardView;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView recyclerView;
    List<Task> tasks;
    Boolean exit = false;
    FloatingActionButton fab;
    LinearLayout no_task;
    BottomSheetDialog bottomSheetDialog, bottomSheetDialog1;
    Toolbar toolbar;
    private TaskViewModel taskViewModel;
    private MenuItem menuItem;
    private ExportDbUtil exportDbUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tasks = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar);
        adapter = new TaskAdapter(this, this, this);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        initViews();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        fab = findViewById(R.id.fab);
        createBottomSheetDialog();
        aboutAppBottomSheetDialog();
        exportDbUtil = new ExportDbUtil(this, "task_database", "Todo backup", this);

        no_task.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });
        /*
          Code for add new task
         */
        addTask.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);
        });
        /*
          Code for Floating Action Button sort by action
         */
        fab.setOnClickListener(view -> {
            sortTaskBy();
            recyclerView.scrollToPosition(tasks.size() - 1);
        });

        /*
          Code for get all tasks
         */

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> {
            if (tasks == null) {
                return;
            }
            //adapter.submitList(tasks);
            adapter.setTasks(tasks);
            toolbar.setSubtitle("Total todo added:- " + tasks.size());
        });

        /*
          Code for task change action if no task show empty else all tasks
         */
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged() {
                super.onChanged();

                if (adapter.getItemCount() == 0) {
                    no_task.setVisibility(View.VISIBLE);
                    //cardView.setVisibility(View.GONE);
                    fab.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    no_task.setVisibility(View.GONE);
                    //cardView.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }


            }
        });
        /*
          Code for Swiped delete action
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle(R.string.confirm);
                    alertDialog.setMessage(R.string.delete_task_msg);
                    alertDialog.setIcon(R.drawable.delete1);
                    alertDialog.setPositiveButton(R.string.yes, (dialog, which) -> {
                        taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(getApplicationContext(), "Todo deleted", Toast.LENGTH_SHORT).show();

                    });
                    alertDialog.setNegativeButton(R.string.cancel, (dialog, which) -> {
                        dialog.cancel();
                        adapter.notifyDataSetChanged();
                    });
                    alertDialog.show();


                }
            }
        }).attachToRecyclerView(recyclerView);

        /*
          Code for Adapter for task edit action
         */
        adapter.setOnItemClickListener(task -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            intent.putExtra(Constants.EXTRA_ID, task.getId());
            intent.putExtra(Constants.EXTRA_TITLE, task.getTitle());
            intent.putExtra(Constants.EXTRA_DESCRIPTION, task.getDescription());
            intent.putExtra(Constants.EXTRA_PRIORITY, task.getPriority());
            intent.putExtra(Constants.EXTRA_DATE, task.getDate());
            intent.putExtra(Constants.EXTRA_TIME, task.getTime());
            intent.putExtra(Constants.EXTRA_UPDATED_ON, task.getUpdatedOn());
            intent.putExtra(Constants.EXTRA_STATUS, task.getAction());
            intent.putExtra(Constants.EXTRA_ALARM, task.getAlarm());
            startActivityForResult(intent, EDIT_TASK_REQUEST);

        });
    }

    /*
      Code for ADD_TASK_REQUEST & EDIT_TASK_REQUEST actions
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(Constants.EXTRA_TITLE);
            String description = data.getStringExtra(Constants.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(Constants.EXTRA_PRIORITY, 1);
            String date = data.getStringExtra(Constants.EXTRA_DATE);
            String time = data.getStringExtra(Constants.EXTRA_TIME);
            int editAction = data.getIntExtra(Constants.EXTRA_STATUS, 1);
            String updated = data.getStringExtra(Constants.EXTRA_UPDATED_ON);
            String alarmDate = data.getStringExtra(Constants.EXTRA_ALARM);
            Task task = new Task();
            //Task task = new Task(title, description, priority, date, time, editAction, updated);
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setDate(date);
            task.setTime(time);
            task.setAction(editAction);
            task.setUpdatedOn(updated);
            task.setItem_check_status(false);
            task.setAlarm(alarmDate);
            taskViewModel.insert(task);

//            StringBuilder buffer = new StringBuilder();
//
//            buffer.append("Title :" + " " + title + "\n \n"
//                    + "Description :" + " " + description + "\n \n"
//                    + "Priority :" + " " + priority + "\n \n"
//                    + "Date :" + " " + date + "\n \n"
//                    + "Time :" + " " + time + "\n \n" + "");
//            showMessage("New Task Info :", buffer.toString());

            AppUtils.showMessage(recyclerView, "Todo saved");

        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(Constants.EXTRA_ID, -1);

            if (id == -1) {
                AppUtils.showMessage(recyclerView, "Todo can't be updated");

                return;
            }
            Task task = new Task();
            String title = data.getStringExtra(Constants.EXTRA_TITLE);
            String description = data.getStringExtra(Constants.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(Constants.EXTRA_PRIORITY, 1);
            String date = data.getStringExtra(Constants.EXTRA_DATE);
            String time = data.getStringExtra(Constants.EXTRA_TIME);
            int editAction = data.getIntExtra(Constants.EXTRA_STATUS, 1);
            String updated = data.getStringExtra(Constants.EXTRA_UPDATED_ON);
            String alarmDate = data.getStringExtra(Constants.EXTRA_ALARM);

            //Task task = new Task(title, description, priority, date, time, editAction, updated);
            task.setId(id);
            task.setTitle(title);
            task.setDescription(description);
            task.setPriority(priority);
            task.setDate(date);
            task.setTime(time);
            task.setAction(editAction);
            task.setUpdatedOn(updated);
            task.setAlarm(alarmDate);
            taskViewModel.update(task);
//            StringBuffer buffer = new StringBuffer();
//
//            buffer.append("Title :" + " " + title + "\n \n"
//                    + "Description :" + " " + description + "\n \n"
//                    + "Priority :" + " " + priority + "\n \n"
//                    + "Date :" + " " + date + "\n \n"
//                    + "Time :" + " " + time + "\n \n" + "");
//            showMessage("Updated Task Info :", buffer.toString());

            AppUtils.showMessage(recyclerView, " Todo updated");

        } else {

            AppUtils.showMessage(recyclerView, " Todo not saved");

        }
    }

    /*
     Code for initalize Views values
     */
    public void initViews() {

        cardView = findViewById(R.id.card_task);
        no_task = findViewById(R.id.no_task);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addTask = findViewById(R.id.add_task);
        recyclerView = findViewById(R.id.recycler_view);

    }
/*
      Code for Add new Task Dialog
     */
        public void showMessage(String title, String message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setPositiveButton(R.string.new_task, (dialog, id) -> {
                        Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                        startActivityForResult(intent, ADD_TASK_REQUEST);
                    })
                    .setNegativeButton(R.string.cancel, (dialog, id) -> {
                    });
            AlertDialog d = builder.create();
            d.setTitle(title);
            d.show();
        }

    /*
      Code for sortByPriority
     */

    public void sortByPriority() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.sortByPriority().observe(this, tasks -> adapter.setTasks(tasks));
    }

    /*
      Code for sortTaskBy sortByTimeCreated
     */
    public void allTodos() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, tasks -> adapter.setTasks(tasks));
    }
    public void sortByTimeCreated() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.sortByTimeCreated().observe(this, tasks -> {

            adapter.setTasks(tasks);
        });
    }

    public void highPriority() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.highPrioprity().observe(this, tasks -> adapter.setTasks(tasks));
    }

    public void mediumPriority() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.mediumPriority().observe(this, tasks -> adapter.setTasks(tasks));
    }

    public void lowPriority() {
            taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
            taskViewModel.lowPriority().observe(this, tasks -> adapter.setTasks(tasks));
    }
    public void getPendingTask() {
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getPendingTask().observe(this, tasks -> adapter.setTasks(tasks));
    }
    /*
      Code for sortTaskBy Time Created & Preference Color
     */
    public void sortTaskBy() {
        final CharSequence[] choice = {"Time created", "Preference color", "High priority", "Medium priority", "Low priority"};
        int itemSelected = 0;
        new AlertDialog.Builder(this)
                .setTitle("Sort by ?")
                .setIcon(R.drawable.sort_black)
                .setSingleChoiceItems(choice, itemSelected, (dialogInterface, selectedIndex) -> {
                    if (choice[selectedIndex] == "Time Created") {
                        sortByTimeCreated();
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Sorted by Time Created", Toast.LENGTH_SHORT).show();


                    } else if (choice[selectedIndex] == "Preference color") {
                        sortByPriority();
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Sorted by preference color", Toast.LENGTH_SHORT).show();

                    } else if (choice[selectedIndex] == "High priority") {
                        highPriority();
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Sorted by high priority", Toast.LENGTH_SHORT).show();

                    } else if (choice[selectedIndex] == "Medium priority") {
                        mediumPriority();
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Sorted by medium priority", Toast.LENGTH_SHORT).show();

                    } else if (choice[selectedIndex] == "Low priority") {
                        lowPriority();
                        dialogInterface.dismiss();
                        Toast.makeText(getApplicationContext(), "Sorted by low priority", Toast.LENGTH_SHORT).show();

                    }

                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.search);

        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName())
        );
        searchView.setQueryHint("Search todo...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.delete_all_taks) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_all)
                    .setMessage(R.string.confirm)
                    .setIcon(R.drawable.delete_sweep);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                taskViewModel.deleteAllTasks();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "All todos deleted", Toast.LENGTH_SHORT).show();

            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

            });
            builder.show();
        }
        else if (id == R.id.delete_completed_taks) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.delete_all)
                    .setMessage(R.string.confirm)
                    .setIcon(R.drawable.delete_sweep);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> {
                taskViewModel.deleteCompletedTasks();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "All completed todos deleted", Toast.LENGTH_SHORT).show();


            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

            });
            builder.show();
        }

        else if (id == R.id.backup_taks) {
            exportDB(recyclerView);
        }

        else if (id == R.id.import_taks) {
            importDb(recyclerView);
        }

        return super.onOptionsItemSelected(item);
    }

    private void createBottomSheetDialog() {
        LinearLayout shareLinearLayout, uploadLinearLayout, copyLinearLayout;
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
            shareLinearLayout = view.findViewById(R.id.shareLinearLayout);
            uploadLinearLayout = view.findViewById(R.id.uploadLinearLayout);
            copyLinearLayout = view.findViewById(R.id.copyLinearLayout);

            shareLinearLayout.setOnClickListener(this);
            uploadLinearLayout.setOnClickListener(this);
            copyLinearLayout.setOnClickListener(this);

            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }

    }
        private void aboutAppBottomSheetDialog() {
            if (bottomSheetDialog1 == null) {
                View view = LayoutInflater.from(this).inflate(R.layout.about_app, null);

                bottomSheetDialog1 = new BottomSheetDialog(this);
                bottomSheetDialog1.setContentView(view);
            }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shareLinearLayout:
                AppUtils.showMessage(recyclerView, "DELETE");
                bottomSheetDialog.dismiss();
                break;
            case R.id.uploadLinearLayout:
                AppUtils.showMessage(recyclerView, "UPLAOD");
                bottomSheetDialog.dismiss();
                break;
            case R.id.copyLinearLayout:
                AppUtils.showMessage(recyclerView, "EDIT");
                bottomSheetDialog.dismiss();
                break;

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_activity) {
            getPendingTask();taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
            taskViewModel.getAllTasks().observe(this, tasks -> adapter.setTasks(tasks));
        } else if (id == R.id.nav_activity1) {
            getPendingTask();
        }
        else if (id == R.id.nav_activity2) {
            //startAnimatedActivity(new Intent(getApplicationContext(), CompletedTaskActivity.class));
            taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
            //adapter.submitList(tasks);
            taskViewModel.getCompletedTasks().observe(this, adapter::setTasks);
        }
        else if (id == R.id.about) {
            bottomSheetDialog1.show();

        }
        else if (id == R.id.add_new_task) {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivityForResult(intent, ADD_TASK_REQUEST);

        }
//        else if (id == R.id.help) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HelpFragment()).commit();
//            menuItem.setVisible(true);
//        }
        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void startAnimatedActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    /**
     * Code for onBackPressed action
     */
    @Override
    public void onBackPressed() {

        if (exit) {
            finish();

        } else {
            drawer.closeDrawer(GravityCompat.START);
            Toast.makeText(MainActivity.this, getString(R.string.exit_msg), Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(() -> exit = false, 2 * 1000);

        }

    }

    /**
     * Code for Delete operation
     */
    @Override
    public void OnDeleteClick(Task task) {
        //
        taskViewModel.delete(task);
    }

    @Override
    public void updateCheckedItem(Task task) {
        taskViewModel.update(task);
    }

    @Override
    public void success(@NotNull String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void fail(@NotNull String message, @NotNull String exception) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void exportToCsv(View view) {
        exportDbUtil.exportAllTables("todo.csv");
    }

    public void exportSingleTable(View view) {
        exportDbUtil.exportAllTables("todo.csv");
    }
    public void importDb(View view) {
        if (exportDbUtil.isBackupExist("Todo backup")) {
            exportDbUtil.importDBFile("/data/com.fridayapp.roomdatabase/databases/");

        } else {
            Toast.makeText(this, "no Backup", Toast.LENGTH_SHORT).show();

        }
    }

    public void exportDB(View view) {
        exportDbUtil.exportDb("/data/com.fridayapp.roomdatabase/databases/");
    }
}


