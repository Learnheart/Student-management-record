import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    private String calculateCourseResult() {
        if (totalMarks >= 40) {
            return "Pass";
        } else {
            return "Fail";
        }
    }
}

public class Main {
    private static List<Result> results = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("1. Add Result");
            System.out.println("2. View All Results");
            System.out.println("3. Update Result");
            System.out.println("4. Delete Result");
            System.out.println("5. Search Result");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 1:
                    addResult(scanner);
                    break;
                case 2:
                    viewAllResults();
                    break;
                case 3:
                    updateResult(scanner);
                    break;
                case 4:
                    deleteResult(scanner);
                    break;
                case 5:
                    searchResult(scanner);
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

    private static void addResult(Scanner scanner) {
        System.out.println("Enter result details:");
        System.out.print("Student ID: ");
        int studentID = scanner.nextInt();
        System.out.print("Subject ID: ");
        int subjectID = scanner.nextInt();
        System.out.print("Course ID: ");
        int courseID = scanner.nextInt();
        System.out.print("Midterm Exam Marks: ");
        int midtermExam = scanner.nextInt();
        System.out.print("Final Exam Marks: ");
        int finalExam = scanner.nextInt();

        Result result = new Result(studentID, subjectID, courseID, midtermExam, finalExam);
        results.add(result);

        System.out.println("Result added successfully.");
    }

    private static void viewAllResults() {
        if (results.isEmpty()) {
            System.out.println("No results found.");
        } else {
            System.out.println("All Results:");
            for (Result result : results) {
                printResultDetails(result);
            }
        }
    }

    private static void updateResult(Scanner scanner) {
        System.out.print("Enter the Student ID to update result: ");
        int studentID = scanner.nextInt();

        Result resultToUpdate = findResultByStudentID(studentID);
        if (resultToUpdate == null) {
            System.out.println("Result not found for Student ID " + studentID);
            return;
        }

        System.out.println("Enter updated result details:");
        System.out.print("Subject ID: ");
        int subjectID = scanner.nextInt();
        System.out.print("Course ID: ");
        int courseID = scanner.nextInt();
        System.out.print("Midterm Exam Marks: ");
        int midtermExam = scanner.nextInt();
        System.out.print("Final Exam Marks: ");
        int finalExam = scanner.nextInt();

        resultToUpdate.setSubjectID(subjectID);
        resultToUpdate.setCourseID(courseID);
        resultToUpdate.setMidtermExam(midtermExam);
        resultToUpdate.setFinalExam(finalExam);
        resultToUpdate.setTotalMarks(midtermExam + finalExam);
        resultToUpdate.setCourseResult(resultToUpdate.calculateCourseResult());

        System.out.println("Result updated successfully.");
    }

    private static void deleteResult(Scanner scanner) {
        System.out.print("Enter the Student ID to delete result: ");
        int studentID = scanner.nextInt();

        Result resultToDelete = findResultByStudentID(studentID);
        if (resultToDelete == null) {
            System.out.println("Result not found for Student ID " + studentID);
            return;
        }

        results.remove(resultToDelete);
        System.out.println("Result deleted successfully.");
    }

    private static void searchResult(Scanner scanner) {
        System.out.print("Enter the Student ID to search result: ");
        int studentID = scanner.nextInt();

        Result result = findResultByStudentID(studentID);
        if (result == null) {
            System.out.println("Result not found for Student ID " + studentID);
        } else {
            System.out.println("Result Found:");
            printResultDetails(result);
        }
    }

    private static Result findResultByStudentID(int studentID) {
        for (Result result : results) {
            if (result.getStudentID() == studentID) {
                return result;
            }
        }
        return null;
    }

    private static void printResultDetails(Result result) {
        System.out.println("Student ID: " + result.getStudentID());
        System.out.println("Subject ID: " + result.getSubjectID());
        System.out.println("Course ID: " + result.getCourseID());
        System.out.println("Midterm Exam Marks: " + result.getMidtermExam());
        System.out.println("Final Exam Marks: " + result.getFinalExam());
        System.out.println("Total Marks: " + result.getTotalMarks());
        System.out.println("Course Result: " + result.getCourseResult());
        System.out.println();
    }
}
