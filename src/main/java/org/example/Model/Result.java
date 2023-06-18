package org.example.Model;

public class Result {
    private int studentId;
    private String subjectId;
    private String courseId;
    private float midtermScore;
    private float finalScore;
    private float totalMarks;
    private CourseResult courseResult;

    public Result(int studentId, String subjectId, String courseId, float midtermScore, float finalScore) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.courseId = courseId;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
        this.totalMarks = midtermScore + finalScore;
        updateTotalScore();
        updateCourseResult();
    }

    public void setMidtermScore(float midtermScore) {
        this.midtermScore = midtermScore;
        updateTotalScore();
        updateCourseResult();
    }

    public void setFinalScore(float finalScore) {
        this.finalScore = finalScore;
        updateTotalScore();
        updateCourseResult();
    }

    public void updateTotalScore() {
        this.totalMarks = (midtermScore + finalScore * 2) / 3;
    }

    public enum CourseResult {
        PASS,
        FAIL
    }

    public void updateCourseResult() {
        if (finalScore >= 4.0) {
            this.courseResult = CourseResult.PASS;
        } else {
            this.courseResult = CourseResult.FAIL;
        }
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public float getMidtermScore() {
        return midtermScore;
    }

    public float getFinalScore() {
        return finalScore;
    }

    public float getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(float totalMarks) {
        this.totalMarks = totalMarks;
    }

    public CourseResult getCourseResult() {
        return courseResult;
    }

    public void setCourseResult(CourseResult courseResult) {
        this.courseResult = courseResult;
    }


}
