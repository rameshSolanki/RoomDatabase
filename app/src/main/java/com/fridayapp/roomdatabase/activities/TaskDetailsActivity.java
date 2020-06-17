package com.fridayapp.roomdatabase.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.database.viewModel.TaskViewModel;
import com.fridayapp.roomdatabase.model.Constants;
import com.fridayapp.roomdatabase.model.Task;

import java.util.ArrayList;

public class TaskDetailsActivity extends AppCompatActivity {

    public TextView TvTitle, TvDescription, TvDate, TvTime, TvPriority, TvUpdateDate, TvAlarm;
    ImageView imageView;
    Task task;
    TaskViewModel taskViewModel;
    ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TvTitle = findViewById(R.id.tvTitle);
        TvDescription = findViewById(R.id.tvDesc);
        TvDate = findViewById(R.id.tvDate);
        TvTime = findViewById(R.id.tvTime);
        TvPriority = findViewById(R.id.tvPrio);
        TvUpdateDate = findViewById(R.id.tvUpdated);
        TvAlarm = findViewById(R.id.tvAlarm);
        imageView = findViewById(R.id.imageView1);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        Intent intent = getIntent();
        String title = intent.getStringExtra(Constants.EXTRA_TITLE);
        String description = intent.getStringExtra(Constants.EXTRA_DESCRIPTION);
        String date = intent.getStringExtra(Constants.EXTRA_DATE);
        String time = intent.getStringExtra(Constants.EXTRA_TIME);
        String priority = intent.getStringExtra(Constants.EXTRA_PRIORITY);
        String updateDate = intent.getStringExtra(Constants.EXTRA_UPDATED_ON);
        String alarmDate = intent.getStringExtra(Constants.EXTRA_ALARM);
        //setTitle(title);
        TvTitle.setText(title);
        TvDescription.setText(description);
        TvDate.setText(date);
        TvTime.setText(time);
        TvPriority.setText(priority);
        TvUpdateDate.setText(updateDate);
        TvAlarm.setText(alarmDate);

//        switch (task.getPriority()) {
//            case 1:
//                imageView.setBackgroundColor(Color.parseColor("#F44336"));
//                Tvpriority.setText(R.string.high_priority);
//                Tvpriority.setTextColor(Color.parseColor("#F44336"));
//                break;
//            case 2:
//                imageView.setBackgroundColor(Color.parseColor("#FF9800"));
//                Tvpriority.setText(R.string.med_priority);
//                Tvpriority.setTextColor(Color.parseColor("#FF9800"));
//                break;
//            case 3:
//                imageView.setBackgroundColor(Color.parseColor("#4CAF50"));
//                Tvpriority.setText(R.string.low_priority);
//                Tvpriority.setTextColor(Color.parseColor("#4CAF50"));
//                break;
//        }
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
        getMenuInflater().inflate(R.menu.detail_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.edit) {
            Intent intent = new Intent(getApplicationContext(), AddEditTaskActivity.class);
            intent.putParcelableArrayListExtra("todos", tasks);
            startActivity(intent);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
