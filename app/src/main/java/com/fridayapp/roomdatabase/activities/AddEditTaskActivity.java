package com.fridayapp.roomdatabase.activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.alarm.AlarmReceiver;
import com.fridayapp.roomdatabase.model.AppUtils;
import com.fridayapp.roomdatabase.model.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.fridayapp.roomdatabase.model.Constants.EXTRA_ALARM;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_DATE;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_DESCRIPTION;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_DONE;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_ID;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_PRIORITY;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_STATUS;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_TIME;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_TITLE;
import static com.fridayapp.roomdatabase.model.Constants.EXTRA_UPDATED_ON;

public class AddEditTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;
    private int mYear, mMonth, mDay;
    int hour, minute;
    public static final int COMPLETED = 1;
    public static final int PENDING = 2;
    LinearLayout linearLayout;
    private EditText editTextTitle,editTextDescription, editTextDate, editTextTime;
    private RadioGroup mRadioGroup, mRadioGroup1;
    TextView updatedOn, alarmView, updatedTitle;
    Button save, cancel;
    ImageButton alarmBtn;
    int alarm_hour, alarm_minute, alarm_year, alarm_month, alarm_day;
    String alarm = "";
    Task task;
    int BIG_NUM_FOR_ALARM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextDate = findViewById(R.id.edit_text_date);
        editTextTime = findViewById(R.id.edit_text_time);
        mRadioGroup = findViewById(R.id.radioGroup);
        mRadioGroup1 = findViewById(R.id.radioGroupCompleted);
        linearLayout = findViewById(R.id.linear_add);
        updatedOn = findViewById(R.id.update_date);
        updatedTitle = findViewById(R.id.update_title);
        alarmView = findViewById(R.id.alarmView);
        save = findViewById(R.id.buttonSave);
        cancel = findViewById(R.id.buttonCancel);
        alarmBtn = findViewById(R.id.alarmButton);

        editTextDate.setOnClickListener(v -> date());
        editTextTime.setOnClickListener(v -> time());
        save.setOnClickListener(v -> saveTask());
        alarmBtn.setOnClickListener(v -> setAlarm(v));
        cancel.setOnClickListener(v -> AddEditTaskActivity.this.finish());

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            save.setText("Update");
            setTitle("Edit a task");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            setPriorityInViews(intent.getIntExtra(EXTRA_PRIORITY, 1));
            editTextDate.setText(intent.getStringExtra(EXTRA_DATE));
            editTextTime.setText(intent.getStringExtra(EXTRA_TIME));
            updatedOn.setText(intent.getStringExtra(EXTRA_UPDATED_ON));
            setCompletedInViews(intent.getIntExtra(EXTRA_STATUS, 2));
            alarmView.setText(intent.getStringExtra(EXTRA_ALARM));
            editTextTitle.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if (charSequence.toString().trim().length() > 0) {
                        save.setEnabled(true);
                    } else {
                        save.setEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        } else {
            if (alarm.length() > 1) alarmView.setText("Alert at " + alarm + "!");
            else alarmView.setVisibility(View.GONE);
            updatedTitle.setVisibility(View.GONE);
            setTitle("Add a todo");
        }

    }

    /**
     * save new task dialog
     */
    private void saveTask() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy 'at' hh:mm aaa", Locale.getDefault());
        String timeFormat = sdf.format(new Date());
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = getPriorityFromViews();
        String date = editTextDate.getText().toString();
        String time = editTextTime.getText().toString();
        String alarmDate = alarmView.getText().toString();
        int completed = getCompletedFromViews();

        if (title.trim().isEmpty() || description.trim().isEmpty() || date.trim().isEmpty() || time.trim().isEmpty()
                || String.valueOf(priority).trim().isEmpty()) {

            AppUtils.showMessageLinearLayout(linearLayout, getString(R.string.empty_fields));

            return;
        }
        task = new Task();

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DATE, date);
        data.putExtra(EXTRA_TIME, time);
        data.putExtra(EXTRA_UPDATED_ON, timeFormat);
        data.putExtra(EXTRA_STATUS, completed);
        data.putExtra(EXTRA_DONE, task.getItem_check_status());
        data.putExtra(EXTRA_ALARM, alarmDate);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);

        }

        setResult(RESULT_OK, data);
//        Intent success = new Intent(getApplicationContext(), TaskDetailsActivity.class);
//        success.putExtras(data);
//        startActivity(success);
        finish();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public int getCompletedFromViews() {
        int completed = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroupCompleted)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1C:
                completed = COMPLETED;
                break;
            case R.id.radButton2C:
                completed = PENDING;
                break;
        }
        return completed;
    }

    public void setCompletedInViews(int completed) {
        switch (completed) {
            case COMPLETED:
                ((RadioGroup) findViewById(R.id.radioGroupCompleted)).check(R.id.radButton1C);
                break;
            case PENDING:
                ((RadioGroup) findViewById(R.id.radioGroupCompleted)).check(R.id.radButton2C);
                break;
        }
    }

    /**
     * get Priority In Views  dialog
     */
    public int getPriorityFromViews() {
        int priority = 1;
        int checkedId = ((RadioGroup) findViewById(R.id.radioGroup)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.radButton1:
                priority = PRIORITY_HIGH;
                break;
            case R.id.radButton2:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.radButton3:
                priority = PRIORITY_LOW;
        }
        return priority;
    }

    /**
     * set Priority In Views  dialog
     */
    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton1);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton2);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.radioGroup)).check(R.id.radButton3);
        }
    }

    public void startAlarm(int num, int days) {
        int AlarmId = 0;
        int i = 0, k = 0;
        while (i < alarm.length() && alarm.charAt(i) != '/') i++;
        alarm_year = Integer.parseInt(alarm.substring(k, i));
        k = i + 1;
        i++;
        while (i < alarm.length() && alarm.charAt(i) != '/') i++;
        alarm_month = Integer.parseInt(alarm.substring(k, i));
        k = i + 1;
        i++;
        while (i < alarm.length() && alarm.charAt(i) != ' ') i++;
        alarm_day = Integer.parseInt(alarm.substring(k, i));
        k = i + 1;
        i++;
        while (i < alarm.length() && alarm.charAt(i) != ':') i++;
        alarm_hour = Integer.parseInt(alarm.substring(k, i));
        k = i + 1;
        i++;
        alarm_minute = Integer.parseInt(alarm.substring(k));

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", AlarmId);
        intent.putExtra(EXTRA_TITLE, editTextTitle.getText().toString());
        intent.putExtra(EXTRA_STATUS, editTextDescription.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        Calendar alarm_time = Calendar.getInstance();
        alarm_time.set(alarm_year, alarm_month - 1, alarm_day, alarm_hour, alarm_minute);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm_time.getTimeInMillis(), pendingIntent);

    }

    /**
     * Date dialog
     */
    public void date() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            String myFormat = "dd-MM-yyyy";
            SimpleDateFormat sdformat = new SimpleDateFormat(myFormat, Locale.getDefault());
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editTextDate.setText(sdformat.format(calendar.getTime()));

            closeKeyboard();

        }, mYear, mMonth, mDay);
        datePickerDialog.setTitle("Select Date");

        datePickerDialog.show();
    }

    /**
     * Time dialog
     */
    public void time() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        TimePickerDialog mTimePicker;

        mTimePicker = new TimePickerDialog(this, (timePicker, selectedHour, selectedMinute) -> {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
            calendar.set(Calendar.MINUTE, selectedMinute);
            String timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime());
            editTextTime.setText(timeFormat);
            //startAlarm(alarmTimeInMillis);
        }, hour, minute, false);

        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.add_task_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.save_task:
//                saveTask();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    protected void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm exit");
        builder.setMessage("Todo will not be saved ?");
        builder.setIcon(R.drawable.warning);
        builder.create();

        builder.setPositiveButton("Keep editing", (dialog, which) -> {

        });

        builder.setNegativeButton("Discard", (dialog, which) -> AddEditTaskActivity.this.finish());
        builder.show();
    }

    public void setAlarm(View v) {
        //if no alarm clock has been set up before
        //show the current time
        Calendar c = Calendar.getInstance();
        alarm_hour = c.get(Calendar.HOUR_OF_DAY);
        alarm_minute = c.get(Calendar.MINUTE);

        alarm_year = c.get(Calendar.YEAR);
        alarm_month = c.get(Calendar.MONTH) + 1;
        alarm_day = c.get(Calendar.DAY_OF_MONTH);

        new TimePickerDialog(this, this, alarm_hour, alarm_minute, false).show();
        new DatePickerDialog(this, this, alarm_year, alarm_month - 1, alarm_day).show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        alarm_year = year;
        alarm_month = monthOfYear + 1;
        alarm_day = dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        alarm_hour = hourOfDay;
        alarm_minute = minute;
        alarm = alarm_year + "/" + alarm_month + "/" + alarm_day + " " + alarm_hour + ":" + alarm_minute;
        alarmView.setText("Alert at " + alarm + "!");
        alarmView.setVisibility(View.VISIBLE);
        startAlarm(alarm_minute, alarm_hour);
        Toast.makeText(this, "Alarm will be on at " + alarm + " !", Toast.LENGTH_LONG).show();
    }
}
