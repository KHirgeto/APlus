package com.rafael.apluse.classes;

import java.util.ArrayList;

public class StudentClass {

    String className, proName, color;
    ArrayList<Task> tasks;
    ArrayList<Date> dates;
    String proEmail;
    String proPhone;
    String location;

    public StudentClass() {
    }

    public StudentClass(String className, String proName, ArrayList<Date> dates, String color, ArrayList<Task> tasks, String proEmail, String proPhone, String location) {
        this.className = className;
        this.proName = proName;
        this.dates = dates;
        this.color = color;
        this.tasks = tasks;
        this.proEmail = proEmail;
        this.proPhone = proPhone;
        this.location = location;
    }

    public String getProEmail() {
        return proEmail;
    }

    public void setProEmail(String proEmail) {
        this.proEmail = proEmail;
    }

    public String getProPhone() {
        return proPhone;
    }

    public void setProPhone(String proPhone) {
        this.proPhone = proPhone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Date> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Date> dates) {
        this.dates = dates;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public ArrayList<Date> getDate() {
        return dates;
    }

    public void setDate(ArrayList<Date> dates) {
        this.dates = dates;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
