package com.example.firebasedemo;

public class Dataholder {
    private String course,duration,name;

    public Dataholder(String course, String duration, String name) {
        this.course = course;
        this.duration = duration;
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
