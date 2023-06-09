package org.example.Service;

import org.example.Model.Course;
import org.example.Model.Student;
import org.example.database;

import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class CourseStack<S> {
    private Stack<Course> courseList = new Stack<>();
    private Scanner input = new Scanner(System.in);

    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;

    public void addCourse() throws SQLException {
        connect = database.connectDb();
        String addCourse = "INSERT INTO course(courseId, courseName) VALUES (?,?)";

        System.out.println("Enter number of course you want to insert: ");;
        int courseAmount;

        while(true) {
            try {
                courseAmount = Integer.parseInt(input.next());
                if (courseAmount < 1) {
                    System.out.println("Please enter a number greater than 0");
                    continue;
                }
                break;
            }catch (Exception ignore) {
                System.out.println("retyping number: ");
            }
        }

        Stack<Course> tempStack = new Stack<>();

        for (int i = 0; i < courseAmount; i++) {
            System.out.println("Insert " + "course number " + (i + 1) );
            System.out.println("Enter course id: ");

            String courseId;
            while (true) {
                courseId = input.next();
                break;
            }
            input.nextLine();
            System.out.println("Enter course name: ");
            String courseName;

            while (true) {
                courseName = input.nextLine();
                break;
            }

            preparedStatement = connect.prepareStatement(addCourse);
            preparedStatement.setString(1, courseId);
            preparedStatement.setString(2, courseName);
            preparedStatement.executeUpdate();

            Course course = new Course(courseId, courseName);

            tempStack.push(course);
        }

        while (!tempStack.isEmpty()) {
            courseList.push(tempStack.pop());
        }

        System.out.println("Subject added successfully!");
    }

    public void printAllCourses() throws SQLException {

        connect = database.connectDb();
        statement = connect.createStatement();
        String query = "SELECT * FROM course";
        result = statement.executeQuery(query);
        if (!result.isBeforeFirst()) {
            System.out.println("List of course is empty.");
        } else {
            System.out.format("+------------+----------------------+\n");
            System.out.format("| %-10s | %-20s | \n", "ID", "Semester");
            System.out.format("+------------+----------------------+\n");

            while (result.next()) {
                String courseId = result.getString("courseId");
                String courseName = result.getString("courseName");

//                System.out.printf("| %-10s | %-25s | %-30s | %-15s | %-10s | %-20s | %-20s | %s",
//                        studentId, studentName, studentEmail, studentBirth, gender, address, phoneNumber, major);
                Course course = new Course(courseId, courseName);
                courseList.push(course);
            }
            while (!courseList.empty()) {
                Course course = courseList.pop();
                System.out.printf("| %-10s | %-20s |\n", course.getCourseId(), course.getCourseName());
            }
            System.out.format("+------------+----------------------+\n");
            System.out.println();
        }

        // Close database connection and statement
        result.close();
        statement.close();
        connect.close();
            int choose = -1;
        do {
            System.out.println("Enter the number here: \n" +
                    "1 for add new course \t" +
                    "2 for update current course \t" +
                    "3 for delete course by ID \t" +
                    "4 for search course \t" +
                    "5 for print course list \t" +
                    "0 for back to menu \n" +
                    "Choose: ");

            while (input.hasNext()) {

                choose = Integer.parseInt(input.next());
                if (choose < 0 || choose > 5) {
                    System.out.print("Invalid value, please type number in range of 0 - 5: ");
                    continue;
                }
                break;
            }

            switch (choose) {
                case 1:
                    addCourse();
                    break;
                case 2:
                    updateCourse();
                    break;
                case 3:
                    deleteCourse();
                    break;
                case 4:
                    searchCourse();
                    break;
                case 5:
                    printAllCourses();
                    break;
                default:
                    break;
            }
        }while (choose != 0);
    }

    public void updateCourse() throws SQLException {
        connect = database.connectDb();
        try {
            System.out.println("Enter the id of course you want to update: ");
            String courseId = input.next();

            String selectCourse = "SELECT * FROM course WHERE courseId = ?";
            preparedStatement = connect.prepareStatement(selectCourse);
            preparedStatement.setString(1, courseId);
            result = preparedStatement.executeQuery();

            if (!result.isBeforeFirst()) {
                System.out.println("Course with ID " + courseId + " doesn't exist!");
                return;
            }

            result.next();

            String currentCourse = result.getString("courseName");

            System.out.println("Current course name is: " + currentCourse);

            Stack<String> updatesStack = new Stack<>();
            updatesStack.push(courseId);

            input.nextLine();

            System.out.println("Enter new course name: ");
            String courseName = input.nextLine();

            updatesStack.push(courseName);

            while (!updatesStack.empty()) {
                String id = updatesStack.pop();
                String name = updatesStack.pop();

                String updatedCourse = "UPDATE course SET courseName = ? WHERE courseId = ?";
                preparedStatement = connect.prepareStatement(updatedCourse);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                int count = preparedStatement.executeUpdate();
                if (count > 0) {
                    System.out.println("Course ID " + id + " has been updated");
                } else {
                    System.out.println("Updated failed for course with ID " + id);
                }
            }
        } finally {
            if (connect != null) {
                connect.close();
            }
        }
    }


    public void deleteCourse() throws SQLException {
        connect = database.connectDb();

        System.out.println("Enter the id of course you want to delete: ");
        String courseId = input.next();

        // Prepare a SELECT statement to retrieve the course with the given ID
        String selectCourseById = "SELECT * FROM course WHERE courseId = ?";
        preparedStatement = connect.prepareStatement(selectCourseById);
        preparedStatement.setString(1, courseId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Course with ID " + courseId + " doesn't exist! \n\n");
            return;
        }

        // If the course exists, prepare a DELETE statement to delete it from the database
        String deleteCourse = "DELETE FROM course WHERE courseId = ?";
        preparedStatement = connect.prepareStatement(deleteCourse);
        preparedStatement.setString(1, courseId);
        int count = preparedStatement.executeUpdate();

        Stack<Course> tempStack = new Stack<>();

        if (count > 0) {
            // If to delete was successful, delete the course from the linked list and the stack
            System.out.println("Course with ID " + courseId + " has been deleted \n\n");
            while (!courseList.isEmpty()) {
                Course course = courseList.pop();
                if (!course.getCourseId().equals(courseId)) {
                    tempStack.push(course);
                }
            }
            while (!tempStack.isEmpty()) {
                courseList.push(tempStack.pop());
            }
        } else {
            System.out.println("Delete failed for course with ID " + courseId + "\n\n");
        }
    }


    public void searchCourse() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        System.out.println("Enter the ID of the semester you want to search: ");
        String keyword = input.next();

        String searchCourse = "SELECT * FROM course WHERE courseId = ? OR courseName = ?";
        preparedStatement = connect.prepareStatement(searchCourse);
        preparedStatement.setString(1, keyword);
        preparedStatement.setString(2, keyword);

        result = preparedStatement.executeQuery();

        boolean found = false;

        System.out.format("+------------+----------------------+\n");
        System.out.format("| %-10s | %-20s |\n", "ID", "Semester");
        System.out.format("+------------+----------------------+\n");

        while (result.next()) {
            String courseId = result.getString("courseId");
            String courseName = result.getString("courseName");

            System.out.format("| %-10s | %-20s | \n", courseId, courseName);
            Course course = new Course(courseId, courseName);
            courseList.push(course);

            found = true;
        }
        System.out.format("+------------+----------------------+");
        System.out.println("\n");
        if (!found) {
            System.out.println("No matching records found.\n\n");
        }
    }

}
