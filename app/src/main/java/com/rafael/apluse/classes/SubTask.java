package com.rafael.apluse.classes;

public class SubTask {

    String subTask;
    Boolean checked;

    public SubTask() {
    }

    public SubTask(String subTask, Boolean checked) {
        this.subTask = subTask;
        this.checked = checked;
    }

    public String getSubTask() {
        return subTask;
    }

    public void setSubTask(String subTask) {
        this.subTask = subTask;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
}
