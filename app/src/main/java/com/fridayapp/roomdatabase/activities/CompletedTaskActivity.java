package com.fridayapp.roomdatabase.activities;

import android.annotation.SuppressLint;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.adapter.CompletedTaskAdapter;
import com.fridayapp.roomdatabase.database.viewModel.TaskViewModel;
import com.fridayapp.roomdatabase.model.Task;

public class CompletedTaskActivity extends AppCompatActivity implements CompletedTaskAdapter.OnDeleteClickListener {
    CompletedTaskAdapter completedTaskAdapter;
    TaskViewModel taskViewModel;
    RecyclerView recyclerView;
    LinearLayout no_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.completed_task_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Completed todo");
        recyclerView = findViewById(R.id.recycler_view);
        no_task = findViewById(R.id.no_task);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        completedTaskAdapter = new CompletedTaskAdapter(this, this);
        recyclerView.setAdapter(completedTaskAdapter);
        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        //adapter.submitList(tasks);
        taskViewModel.getCompletedTasks().observe(this, completedTaskAdapter::setTasks);


        completedTaskAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onChanged() {
                super.onChanged();

                if (completedTaskAdapter.getItemCount() == 0) {
                    no_task.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    no_task.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }


            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.completed_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
                taskViewModel.deleteCompletedTasks();
                completedTaskAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "All completed todos deleted", Toast.LENGTH_SHORT).show();


            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

            });
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnDeleteClickListener(Task task) {
        taskViewModel.delete(task);
    }
}
