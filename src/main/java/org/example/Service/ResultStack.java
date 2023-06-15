import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Result {
    private static List<Student> students = new ArrayList<>();
    private static int nextStudentID = 123;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Add Student");
            System.out.println("2. View All Students");
            System.out.println("3. Update Student");
            System.out.println("4. Delete Student");
            System.out.println("5. Search Student");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    break;
                case 2:
                    viewAllStudents();
                    break;
                case 3:
                    updateStudent(scanner);
                    break;
                case 4:
                    deleteStudent(scanner);
                    break;
                case 5:
                    searchStudent(scanner);
                    break;
                case 6:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }

            System.out.println();
        }
    }

    private static void addStudent(Scanner scanner) {
        System.out.println("Enter student details:");
        System.out.print("Subject ID: ");
        int subjectID = scanner.nextInt();
        System.out.print("Course ID: ");
        int courseID = scanner.nextInt();
        System.out.print("Midterm Exam Marks: ");
        int midtermExam = scanner.nextInt();
        System.out.print("Final Exam Marks: ");
        int finalExam = scanner.nextInt();

        Student student = new Student(nextStudentID, subjectID, courseID, midtermExam, finalExam);
        students.add(student);
        nextStudentID++;

        System.out.println("Student added successfully. Student ID: " + student.getStudentID());
    }

    private static void viewAllStudents() {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
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

    private static void updateStudent(Scanner scanner) {
        System.out.print("Enter student ID to update: ");
        int studentID = scanner.nextInt();
        Student student = findStudentByID(studentID);

        if (student != null) {
            System.out.println("Enter updated student details:");
            System.out.print("Subject ID: ");
            int subjectID = scanner.nextInt();
            System.out.print("Course ID: ");
            int courseID = scanner.nextInt();
            System.out.print("Midterm Exam Marks: ");
            int midterm = scanner.nextInt();
            System.out.print("Final Exam Marks: ");
            int finalExam = scanner.nextInt();

            student.subjectID = subjectID;
            student.courseID = courseID;
            student.midtermExam = midtermExam;
            student.finalExam = finalExam;
            student.totalMarks = midtermExam + finalExam;
            student.courseResult = student.calculateCourseResult();

            System.out.println("Student with ID " + studentID + " updated successfully.");
        } else {
            System.out.println("Student with ID " + studentID + " not found.");
        }
    }

    private static void deleteStudent(Scanner scanner) {
        System.out.print("Enter student ID to delete: ");
        int studentID = scanner.nextInt();
        Student student = findStudentByID(studentID);

        if (student != null) {
            students.remove(student);
            System.out.println("Student with ID " + studentID + " deleted successfully.");
        } else {
            System.out.println("Student with ID " + studentID + " not found.");
        }
    }

    private static void searchStudent(Scanner scanner) {
        System.out.print("Enter student ID to search: ");
        int studentID = scanner.nextInt();
        Student student = findStudentByID(studentID);

        if (student != null) {
            System.out.println("Student Found:");
            System.out.println("Student ID: " + student.getStudentID());
            System.out.println("Subject ID: " + student.getSubjectID());
            System.out.println("Course ID: " + student.getCourseID());
            System.out.println("Midterm Marks: " + student.getMidtermExam());
            System.out.println("Final Exam Marks: " + student.getFinalExam());
            System.out.println("Total Marks: " + student.getTotalMarks());
            System.out.println("Course Result: " + student.getCourseResult());
        } else {
            System.out.println("Student with ID " + studentID + " not found.");
        }
    }

    private static Student findStudentByID(int studentID) {
        for (Student student : students) {
            if (student.getStudentID() == studentID) {
                return student;
            }
        }
        return null;
    }
}
