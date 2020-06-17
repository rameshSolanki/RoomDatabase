package com.fridayapp.roomdatabase.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;


@Entity(tableName = "task_table")
public class Task implements Parcelable {
    @ColumnInfo(name = "date")
    @TypeConverters(DateConverter.class)
    public String date;

    @ColumnInfo(name = "time")
    public String time;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "priority_column")
    public int priority;

    @ColumnInfo(name = "action")
    private int action;

    @ColumnInfo(name = "updated_on")
    private String updatedOn;

    @ColumnInfo(name = "item_check_status")
    private Boolean item_check_status;

    @ColumnInfo(name = "alarm")
    private String alarm;

    public Task() {
    }

    protected Task(Parcel in) {
        date = in.readString();
        time = in.readString();
        id = in.readInt();
        title = in.readString();
        description = in.readString();
        priority = in.readInt();
        action = in.readInt();
        updatedOn = in.readString();
        alarm = in.readString();
        byte tmpItem_check_status = in.readByte();
        item_check_status = tmpItem_check_status == 0 ? null : tmpItem_check_status == 1;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;

    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Boolean getItem_check_status() {
        return item_check_status;
    }

    public void setItem_check_status(Boolean item_check_status) {
        this.item_check_status = item_check_status;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(time);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeInt(priority);
        dest.writeInt(action);
        dest.writeString(updatedOn);
        dest.writeString(alarm);
        dest.writeByte((byte) (item_check_status == null ? 0 : item_check_status ? 1 : 2));
    }
}
