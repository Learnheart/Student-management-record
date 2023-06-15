import java.util.Arrays;
import java.util.Comparator;

class Student {
    private int studentID;
    private int subjectID;
    private int courseID;
    private int midtermExam;
    private int finalExam;
    private int totalMarks;
    private String courseResult;

    public Student(int studentID, int subjectID, int courseID, int midtermExam, int finalExam) {
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

    public int getSubjectID() {
        return subjectID;
    }

    public int getCourseID() {
        return courseID;
    }

    public int getMidtermExam() {
        return midtermExam;
    }

    public int getFinalExam() {
        return finalExam;
    }

    public int getTotalMarks() {
        return totalMarks;
    }

    public String getCourseResult() {
        return courseResult;
    }

    private String calculateCourseResult() {
        if (totalMarks >= 4) {
            return "Pass";
        } else {
            return "Fail";
        }
    }
}

public class Result {
    public static void main(String[] args) {
        Student[] students = {
                new Student(123, 1, 01, 8, 9),
                new Student(234, 1, 01, 7, 8),
                new Student(345, 2, 02, 6, 7),
                new Student(456, 2, 02, 5, 4),
                new Student(567, 3, 03, 6, 2)
        };

        Arrays.sort(students, Comparator.comparingInt(Student::getFinalExam).reversed());

        System.out.println("Student Results (sorted in descending order based on final exam score):");
        for (Student student : students) {
            System.out.println("Student ID: " + student.getStudentID());
            System.out.println("Subject ID: " + student.getSubjectID());
            System.out.println("Course ID: " + student.getCourseID());
            System.out.println("Midterm Exam Marks: " + student.getMidtermExam());
            System.out.println("Final Exam Marks: " + student.getFinalExam());
            System.out.println("Total Marks: " + student.getTotalMarks());
            System.out.println("Course Result: " + student.getCourseResult());
            System.out.println();
        }
    }
}

