package com.rafael.apluse.classes;

import java.util.ArrayList;
import java.util.Date;

public class Task {

    String taskName,taskDesc, className, taskDueDate;
    ArrayList<SubTask> subTasks;
    java.util.Date dueDate;

    public Task() {
    }

    public Task(String taskName, String taskDesc, String className,String taskDueDate, java.util.Date dueDate, ArrayList<SubTask> subTasks) {
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.className = className;
        this.dueDate = dueDate;
        this.taskDueDate = taskDueDate;
        this.subTasks = subTasks;
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(ArrayList<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public java.util.Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(String taskDueDate) {
        this.taskDueDate = taskDueDate;
    }
}
