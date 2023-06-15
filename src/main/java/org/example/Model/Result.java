package org.example.Model;

public class Result implements Comparable<Result> {
    private int studentId;
    private int subjectId;
    private int courseId;
    private double midtermScore;
    private double finalScore;
    private double totalMarks;
    private String courseResult;

    public Result(int studentId, int subjectId, int courseId, double midtermScore, double finalScore) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.courseId = courseId;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
        this.totalMarks = calculateTotalMarks();
        this.courseResult = calculateCourseResult();
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public double getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermscore(double midtermScore) {
        this.midtermScore = midtermScore;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public void setFinalSCore(double finalScore) {
        this.finalScore = finalScore;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public String getCourseResult() {
        return courseResult;
    }

    private double calculateTotalMarks() {
        return midtermScore + finalScore;
    }
}
