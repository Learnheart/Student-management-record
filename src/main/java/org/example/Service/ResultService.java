package org.example.Service;

import org.example.Model.Result;
import org.example.database;

import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class ResultService {
    private LinkedList<Result> results = new LinkedList<Result>();
    private Scanner input = new Scanner(System.in);

    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;

    public void addResult() throws SQLException {
        connect = database.connectDb();
        String addResult = "INSERT INTO result (studentID, subjectID, courseID, midtermExam, finalExam) VALUES (?,?,?,?,?)";

        System.out.println("Enter number of results you want to input: ");
        int resultAmount = 0;

        while (true) {
            try {
                resultAmount = Integer.parseInt(input.nextLine());
                if (resultAmount < 1) {
                    System.out.println("Please enter a number greater than 0.");
                    continue;
                }
                break;
            } catch (Exception ignore) {
                System.out.println("Please enter a valid integer value: ");
            }
        }

        Stack<Result> tempStack = new Stack<>();

        for (int i = 0; i < resultAmount; i++) {
            System.out.println("Insert " + "result number " + (i + 1));
            System.out.println("Enter student ID: ");
            int studentID = input.nextInt();

            System.out.println("Enter subject ID: ");
            int subjectID = input.nextInt();

            System.out.println("Enter course ID: ");
            int courseID = input.nextInt();

            System.out.println("Enter midterm exam marks: ");
            int midtermExam = input.nextInt();

            System.out.println("Enter final exam marks: ");
            int finalExam = input.nextInt();

            preparedStatement = connect.prepareStatement(addResult);
            preparedStatement.setInt(1, studentID);
            preparedStatement.setInt(2, subjectID);
            preparedStatement.setInt(3, courseID);
            preparedStatement.setInt(4, midtermExam);
            preparedStatement.setInt(5, finalExam);
            preparedStatement.executeUpdate();

            Result result = new Result(studentID, subjectID, courseID, midtermExam, finalExam);
            tempStack.push(result);
        }

        while (!tempStack.isEmpty()) {
            results.push(tempStack.pop());
        }

        System.out.println("Result added successfully!");
    }

    public void deleteResult() throws SQLException {
        connect = database.connectDb();

        String deleteResult = "DELETE FROM result WHERE studentID = ?";
        preparedStatement = connect.prepareStatement(deleteResult);

        System.out.println("Enter the ID of the student whose result you want to delete: ");
        int studentID = input.nextInt();

        Stack<Result> tempStack = new Stack<>();

        while (!results.isEmpty()) {
            Result currentResult = results.pop();
            if (currentResult.getStudentID() == studentID) {
                System.out.println("Result for Student ID " + studentID + " has been deleted!");
                break;
            } else {
                tempStack.push(currentResult);
            }
        }

        while (!tempStack.isEmpty()) {
            results.push(tempStack.pop());
        }

        preparedStatement.setInt(1, studentID);
        int count = preparedStatement.executeUpdate();
        System.out.println("Result for Student ID " + studentID + " has been deleted!");

        if (count == 0) {
            System.out.println("Result for Student ID " + studentID + " not found!");
        }
    }

    public void updateResult() throws SQLException {
        connect = database.connectDb();

        System.out.println("Enter the ID of the student whose result you want to update: ");
        int studentID = input.nextInt();

        String selectResult = "SELECT * FROM result WHERE studentID = ?";
        preparedStatement = connect.prepareStatement(selectResult);
        preparedStatement.setInt(1, studentID);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Result for Student ID " + studentID + " doesn't exist!");
            return;
        }

        result.next();

        int currentStudentID = result.getInt("studentID");
        int currentSubjectID = result.getInt("subjectID");
        int currentCourseID = result.getInt("courseID");
        int currentMidtermExam = result.getInt("midtermExam");
        int currentFinalExam = result.getInt("finalExam");

        System.out.println("Current student ID is: " + currentStudentID);
        System.out.println("Current subject ID is: " + currentSubjectID);
        System.out.println("Current course ID is: " + currentCourseID);
        System.out.println("Current midterm exam marks is: " + currentMidtermExam);
        System.out.println("Current final exam marks is: " + currentFinalExam);

        Stack<Integer> updatesStack = new Stack<>();

        updatesStack.push(studentID);

        System.out.println("Enter new subject ID: ");
        int subjectID = input.nextInt();
        updatesStack.push(subjectID);

        System.out.println("Enter new course ID: ");
        int courseID = input.nextInt();
        updatesStack.push(courseID);

        System.out.println("Enter new midterm exam marks: ");
        int midtermExam = input.nextInt();
        updatesStack.push(midtermExam);

        System.out.println("Enter new final exam marks: ");
        int finalExam = input.nextInt();
        updatesStack.push(finalExam);

        while (!updatesStack.empty()) {
            int id = updatesStack.pop();
            int marks = updatesStack.pop();

            String updateResult = "UPDATE result SET midtermExam = ?, finalExam = ? WHERE studentID = ? AND subjectID = ?";
            preparedStatement = connect.prepareStatement(updateResult);
            preparedStatement.setInt(1, midtermExam);
            preparedStatement.setInt(2, finalExam);
            preparedStatement.setInt(3, id);
            preparedStatement.setInt(4, marks);

            int count = preparedStatement.executeUpdate();

            if (count > 0) {
                System.out.println("Result for Student ID " + id + " has been updated");
            } else {
                System.out.println("Update failed for result with Student ID " + id);
            }
        }
    }

    public void printResult() throws SQLException {
        connect = database.connectDb();

        statement = connect.createStatement();

        String printResult = "SELECT * FROM result";
        result = statement.executeQuery(printResult);

        if (!result.isBeforeFirst()) {
            System.out.println("List of results is empty");
        } else {
            System.out.format("| %-10s | %-10s | %-10s | %-15s | %-15s |\n", "Student ID", "Subject ID", "Course ID", "Midterm Exam", "Final Exam");
            System.out.println("+------------+------------+------------+-----------------+-----------------+");

            while (result.next()) {
                int studentID = result.getInt("studentID");
                int subjectID = result.getInt("subjectID");
                int courseID = result.getInt("courseID");
                int midtermExam = result.getInt("midtermExam");
                int finalExam = result.getInt("finalExam");

                System.out.format("| %-10s | %-10s | %-10s | %-15s | %-15s |\n", studentID, subjectID, courseID, midtermExam, finalExam);
            }
            System.out.println("+------------+------------+------------+-----------------+-----------------+");
        }

        int choice = 0;
        do {
            System.out.println("Enter the number here:\n" +
                    "1. Add result\n" +
                    "2. Update result\n" +
                    "3. Delete result\n" +
                    "4. Search result\n" +
                    "5. Print result list\n" +
                    "0. Back to menu\n" +
                    "Choose: ");

            while (input.hasNext()) {
                choice = Integer.parseInt(input.nextLine());
                if (choice < 0 || choice > 5) {
                    System.out.print("Invalid value, please type a number in the range of 0 - 5: ");
                    continue;
                }
                break;
            }

            switch (choice) {
                case 1:
                    addResult();
                    break;
                case 2:
                    updateResult();
                    break;
                case 3:
                    deleteResult();
                    break;
                case 4:
                    searchResult();
                    break;
                case 5:
                    printResult();
                    break;
                default:
                    break;
            }
        } while (choice != 0);
    }

    public void searchResult() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        System.out.println("Enter the ID of the student you want to search: ");
        int studentID = input.nextInt();

        String searchResult = "SELECT * FROM result WHERE studentID = ?";
        preparedStatement = connect.prepareStatement(searchResult);
        preparedStatement.setInt(1, studentID);

        result = preparedStatement.executeQuery();

        boolean found = false;

        System.out.format("| %-10s | %-10s | %-10s | %-15s | %-15s |\n", "Student ID", "Subject ID", "Course ID", "Midterm Exam", "Final Exam");
        System.out.println("+------------+------------+------------+-----------------+-----------------+");

        while (result.next()) {
            int foundStudentID = result.getInt("studentID");
            int subjectID = result.getInt("subjectID");
            int courseID = result.getInt("courseID");
            int midtermExam = result.getInt("midtermExam");
            int finalExam = result.getInt("finalExam");

            System.out.format("| %-10s | %-10s | %-10s | %-15s | %-15s |\n", foundStudentID, subjectID, courseID, midtermExam, finalExam);
            System.out.println("\n");

            found = true;
        }

        if (!found) {
            System.out.println("No record found");
        }
    }
}
