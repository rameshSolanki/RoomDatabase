package com.fridayapp.roomdatabase.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fridayapp.roomdatabase.R;
import com.fridayapp.roomdatabase.database.viewModel.TaskViewModel;
import com.fridayapp.roomdatabase.model.Task;

import java.util.ArrayList;
import java.util.List;

public class CompletedTaskAdapter extends RecyclerView.Adapter<CompletedTaskAdapter.CompletesTaskHolder> {
    TaskViewModel taskViewModel;
    private List<Task> tasks = new ArrayList<>();
    private OnItemClickListener listener;
    private final LayoutInflater layoutInflater;
    private Context mContext;
    private OnDeleteClickListener onDeleteClickListener;

    public CompletedTaskAdapter(Context context, OnDeleteClickListener listener) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public CompletesTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.completed_task_item, parent, false);
        CompletesTaskHolder completedTaskAdapter = new CompletesTaskHolder(view);
        return new CompletesTaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompletesTaskHolder holder, int position) {
        final Context c = holder.task_delete.getContext();
        Task task = tasks.get(position);
        holder.completed_title.setText(task.getTitle());
        holder.completed_content.setText(task.getDescription());
        holder.completed_date.setText(task.getDate());
        holder.completed_time.setText(task.getTime());
        holder.setListeners();

        switch (task.getPriority()) {
            case 1:
                holder.completed_priority.setText("HIGH");
                holder.completed_priority.setTextColor(Color.parseColor("#F44336"));

                break;
            case 2:
                holder.completed_priority.setText("MEDIUM");
                holder.completed_priority.setTextColor(Color.parseColor("#FF9800"));

                break;
            case 3:
                holder.completed_priority.setText("LOW");
                holder.completed_priority.setTextColor(Color.parseColor("#4CAF50"));

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

    public interface OnDeleteClickListener {
        void OnDeleteClickListener(Task task);
    }

    public interface OnItemClickListener {
        void OnItemClick(Task task);
    }

    class CompletesTaskHolder extends RecyclerView.ViewHolder {
        ImageView task_delete;
        private TextView completed_title, completed_content, completed_priority, completed_date, completed_time;
        LinearLayout linearLayout;
        private int mPosition;

        public CompletesTaskHolder(View itemView) {
            super(itemView);
            completed_title = itemView.findViewById(R.id.completed_title);
            completed_content = itemView.findViewById(R.id.completed_content);
            completed_priority = itemView.findViewById(R.id.completed_priority);
            completed_date = itemView.findViewById(R.id.completed_date);
            completed_time = itemView.findViewById(R.id.completed_time);
            task_delete = itemView.findViewById(R.id.task_delete);
            linearLayout = itemView.findViewById(R.id.linear_completed);
        }

        public void setData(int position) {
            mPosition = position;
        }

        public void setListeners() {

            task_delete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(R.string.delete_task_msg)
                        .setIcon(R.drawable.delete1)
                        .setTitle(R.string.confirm)
                        .setPositiveButton(R.string.yes, (dialog, id) -> {
                            if (onDeleteClickListener != null) {
                                onDeleteClickListener.OnDeleteClickListener(tasks.get(mPosition));
                                Toast.makeText(mContext, "Todo Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton(R.string.cancel, (dialog, id) -> {
                        });
                AlertDialog d = builder.create();
                d.show();

            });
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.OnItemClick(tasks.get(position));
                }
            });
        }

    }
}
