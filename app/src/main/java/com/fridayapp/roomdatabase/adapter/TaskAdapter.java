package com.fridayapp.roomdatabase.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.activities.CustomFilter;
import com.fridayapp.roomdatabase.activities.DataListListener;
import com.fridayapp.roomdatabase.activities.TaskDetailsActivity;
import com.fridayapp.roomdatabase.model.Constants;
import com.fridayapp.roomdatabase.model.DateAndTimeUtil;
import com.fridayapp.roomdatabase.model.GetTimeAgo;
import com.fridayapp.roomdatabase.model.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> implements Filterable {
    private final LayoutInflater layoutInflater;
    public List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;
    private Context mContext;
    private OnDeleteClickListener onDeleteClickListener;
    private DataListListener dataListListener;
    CustomFilter filter;

    public TaskAdapter(Context context, OnDeleteClickListener listener, DataListListener dataListListener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
        this.dataListListener = dataListListener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.task_item, parent, false);
        TaskHolder taskHolder = new TaskHolder(view);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaskHolder holder, int position) {
        final Context c = holder.remove.getContext();
        final Context c1 = holder.edit.getContext();
        Task task = getTaskAt(position);
        String timestamp = task.getDate();

        holder.textViewTitle.setText(task.getTitle());
        holder.setNoteTime(timestamp);
        holder.setListeners();

        holder.alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(c1, task.getAlarm(), Toast.LENGTH_SHORT).show();
            }
        });

        if(task.getAlarm().length() > 1 ){
            holder.alarmBtn.setVisibility(View.VISIBLE);
        }
        else {
            holder.alarmBtn.setVisibility(View.GONE);
        }

//        if (task.getAction()) holder.checkBox.setChecked(true);
//        else
//            holder.checkBox.setChecked(false);
//
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                task.setAction(checked);
//                dataListListener.updateCheckedItem(task);
//
//            }
//        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(c, TaskDetailsActivity.class);
            intent.putExtra(Constants.EXTRA_TITLE, task.getTitle());
            intent.putExtra(Constants.EXTRA_DESCRIPTION, task.getDescription());
            intent.putExtra(Constants.EXTRA_DATE, task.getDate());
            intent.putExtra(Constants.EXTRA_TIME, task.getTime());
            intent.putExtra(Constants.EXTRA_PRIORITY, task.getPriority());
            intent.putExtra(Constants.EXTRA_UPDATED_ON, task.getUpdatedOn());
            intent.putExtra(Constants.EXTRA_ALARM, task.getAlarm());

            c.startActivity(intent);
        });

//        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (buttonView.isChecked()) {
//                holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                AppUtils.showMessageLinearLayout(holder.linearLayout, "Task Completed");
//
//            } else {
//                holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                AppUtils.showMessageLinearLayout(holder.linearLayout, "Task Pending");
//
//            }
//        });

        switch (task.getPriority()) {
            case 1:
                holder.linearLayout.setBackgroundColor(Color.parseColor("#F44336"));
                holder.textViewPriority.setText(R.string.high_priority);
                holder.textViewPriority.setTextColor(Color.parseColor("#F44336"));
                break;
            case 2:
                holder.linearLayout.setBackgroundColor(Color.parseColor("#FF9800"));
                holder.textViewPriority.setText(R.string.med_priority);
                holder.textViewPriority.setTextColor(Color.parseColor("#FF9800"));
                break;
            case 3:
                holder.linearLayout.setBackgroundColor(Color.parseColor("#4CAF50"));
                holder.textViewPriority.setText(R.string.low_priority);
                holder.textViewPriority.setTextColor(Color.parseColor("#4CAF50"));
                break;
        }
        switch (task.getAction()) {
            case 1:
                holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //holder.layoutContent.setBackgroundColor(Color.parseColor("#89c3c3c3"));
                holder.textViewTitle.setTextColor(Color.parseColor("#858585"));
                holder.tv_last_modified.setTextColor(Color.parseColor("#858585"));
                holder.textViewPriority.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tv_last_modified.setPaintFlags(holder.textViewTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkBox.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.textViewTitle.setPaintFlags(holder.textViewTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                holder.layoutContent.setBackgroundColor(Color.parseColor("#FFFFFF"));
                holder.checkBox.setVisibility(View.INVISIBLE);
                holder.textViewTitle.setTextColor(Color.parseColor("#ff4b4b4b"));
                holder.tv_last_modified.setTextColor(Color.parseColor("#ff4b4b4b"));
                break;

        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    public Task getTaskAt(int position) {
        return tasks.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        if (filter==null) {
            filter=new CustomFilter((ArrayList<Task>) tasks,this);

        }
        return filter;
    }

    public interface OnDeleteClickListener {
        void OnDeleteClick(Task task);
    }

    public interface OnItemClickListener {
        void OnItemClick(Task task);
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        ImageButton edit, remove, alarmBtn;
        private TextView textViewTitle, tv_last_modified, textViewPriority;
        private LinearLayout linearLayout, layoutContent;
        private CheckBox checkBox;
        private int mPosition;

        public TaskHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            tv_last_modified = itemView.findViewById(R.id.tv_last_modified);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);
            linearLayout = itemView.findViewById(R.id.relative_view);
            checkBox = itemView.findViewById(R.id.statusCheckBox);
            edit = itemView.findViewById(R.id.edit_task);
            alarmBtn = itemView.findViewById(R.id.alarmButton);
            remove = itemView.findViewById(R.id.remove_task);
            layoutContent = itemView.findViewById(R.id.layout_content);

        }
        public void setNoteTime(String time) {
            tv_last_modified.setText(time);
        }
        public void setData(int position) {
            mPosition = position;
        }

        public void setListeners() {

            remove.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.delete_task_msg)
                        .setIcon(R.drawable.delete1)
                        .setTitle(R.string.confirm)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            if (onDeleteClickListener != null) {
                                onDeleteClickListener.OnDeleteClick(tasks.get(mPosition));
                                Toast.makeText(mContext, "Todo Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                        });
                AlertDialog d = builder.create();
                d.show();

            });
            edit.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.OnItemClick(tasks.get(position));
                }
            });
        }

    }
}
