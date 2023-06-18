package org.example.Service;

import org.example.Model.Course;
import org.example.database;

import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class CourseStack<S> {
    private LinkedList<Course> courseList = new LinkedList<Course>();
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
        System.out.println("Printing all courses:");

        connect = database.connectDb();
        String selectAllCourses = "SELECT * FROM course";
        statement = connect.createStatement();
        result = statement.executeQuery(selectAllCourses);

        LinkedList<Course> courseList = new LinkedList<>();
        while(result.next()) {
            String courseId = result.getString("courseId");
            String courseName = result.getString("courseName");

            Course course = new Course(courseId, courseName);
            courseList.add(course);
        }

        if (courseList.isEmpty()) {
            System.out.println("List of courses is empty");
        } else {
            // Create a stack to reverse the order of courses
            Stack<Course> courseStack = new Stack<>();

            // Push each course onto the stack
            for (Course course : courseList) {
                courseStack.push(course);
            }

            // Print table header
            System.out.format("|%-10s | %-20s |\n", "Course ID", "Course Name");
            System.out.println("----------------------------------------");

            // Pop each course from the stack and print it
            while (!courseStack.isEmpty()) {
                Course course = courseStack.pop();
                String courseId = course.getCourseId();
                String courseName = course.getCourseName();
                System.out.format("| %-10s | %-20s |\n", courseId, courseName);
                System.out.println();
            }
        }

        int choose = 0;
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

                choose = Integer.parseInt(input.nextLine());
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

        System.out.println("Enter the id of course you want to update: ");
        String courseId = input.nextLine();

        // Prepare a SELECT statement to retrieve the course with the given ID
        String selectCourseById = "SELECT * FROM course WHERE courseId = ?";
        preparedStatement = connect.prepareStatement(selectCourseById);
        preparedStatement.setString(1, courseId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Course with ID " + courseId + " doesn't exist!");
            return;
        }

        // If the course exists, display its current name and prompt the user for a new name
        result.next();
        String currentCourseId = result.getString("courseId");
        String currentCourseName = result.getString("courseName");
        System.out.println("Current course name is: " + currentCourseName);
        System.out.println("Enter new course name: ");
        String newCourseName = input.nextLine();

        // Prepare an UPDATE statement to update the course name in the database
        String updateCourse = "UPDATE course SET courseName = ? WHERE courseId = ?";
        preparedStatement = connect.prepareStatement(updateCourse);
        preparedStatement.setString(1, newCourseName);
        preparedStatement.setString(2, courseId);
        int count = preparedStatement.executeUpdate();

        Stack<Course> tempStack = new Stack<>();

        if (count > 0) {
            // If the update was successful, update the course name in the linked list as well
            System.out.println("Course with ID " + courseId + " has been updated");
            while (!courseList.isEmpty()) {
                Course course = courseList.pop();
                if (course.getCourseId().equals(currentCourseId)) {
                    course.setCourseName(newCourseName);
                }
                tempStack.push(course);
            }
            while (!tempStack.isEmpty()) {
                courseList.push(tempStack.pop());
            }
        } else {
            System.out.println("Update failed for course with ID " + courseId);
        }
    }

    public void deleteCourse() throws SQLException {
        connect = database.connectDb();

        System.out.println("Enter the id of course you want to delete: ");
        String courseId = input.nextLine();

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
        System.out.println("Enter search term: ");
        String searchTerm = input.nextLine();

        connect = database.connectDb();
        String selectMatchingCourses = "SELECT * FROM course WHERE courseId LIKE ? OR courseName LIKE ?";
        preparedStatement = connect.prepareStatement(selectMatchingCourses);
        preparedStatement.setString(1, "%" + searchTerm + "%");
        preparedStatement.setString(2, "%" + searchTerm + "%");
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("No courses found matching search term");
        } else {
            // Print table header
            System.out.format("|%-10s | %-20s |\n", "Course ID", "Course Name");
            System.out.println("----------------------------------------");

            while (result.next()) {
                String courseId = result.getString("courseId");
                String courseName = result.getString("courseName");
                System.out.format("| %-10s | %-20s |\n", courseId, courseName);
                System.out.println();
            }
        }
    }


}
