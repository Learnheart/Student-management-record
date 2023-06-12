package org.example.model;

public class Course {
    private int courseId;
    private String courseName;
    private String score;

    public Course(int courseId, String courseName, String score) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.score = score;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}

