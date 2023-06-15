class Result {
    private int studentID;
    private int subjectID;
    private int courseID;
    private int midtermExam;
    private int finalExam;
    private int totalMarks;
    private String courseResult;

    public Result(int studentID, int subjectID, int courseID, int midtermExam, int finalExam) {
        this.studentID = studentID;
        this.subjectID = subjectID;
        this.courseID = courseID;
        this.midtermExam = midtermExam;
        this.finalExam = finalExam;
        this.totalMarks = midtermExam + finalExam;
        this.courseResult = calculateCourseResult();
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public int getMidtermExam() {
        return midtermExam;
    }

    public void setMidtermExam(int midtermExam) {
        this.midtermExam = midtermExam;
    }

    public int getFinalExam() {
        return finalExam;
    }

    public void setFinalExam(int finalExam) {
        this.finalExam = finalExam;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(int totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getCourseResult() {
        return courseResult;
    }

    public void setCourseResult(String courseResult) {
        this.courseResult = courseResult;
    }

    private String calculateCourseResult() {
        if (totalMarks >= 4) {
            return "Pass";
        } else {
            return "Fail";
        }
    }
}

