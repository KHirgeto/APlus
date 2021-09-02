package com.rafael.apluse.classes;

import java.util.ArrayList;

public class Student {

    String name, email, uid;
    ArrayList<StudentClass> classList;
    ArrayList<Task> taskList;

    public Student() {
    }

    public Student(String name, String email, String uid, ArrayList<StudentClass> classList, ArrayList<Task> taskList) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.classList = classList;
        this.taskList = taskList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<StudentClass> getClassList() {
        return classList;
    }

    public void setClassList(ArrayList<StudentClass> classList) {
        this.classList = classList;
    }

    public ArrayList<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList) {
        this.taskList = taskList;
    }
}
