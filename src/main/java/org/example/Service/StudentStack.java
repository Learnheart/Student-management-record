package org.example.Service;
import org.example.Model.Student;
import org.example.database;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;



public class StudentStack<S> {
    private LinkedList<Student> stack = new LinkedList<>();
    private Scanner input = new Scanner(System.in);
    Validation validate = new Validation();

    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;

    public void addStudent() throws SQLException {

        connect = database.connectDb();
        String addStd = "INSERT INTO students(studentId, studentName, studentEmail, studentBirth, gender, address, phoneNumber, major) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        System.out.println("Enter the number of students: ");
        int studentAmount = 0;

        while (true) {

            try {

                studentAmount = Integer.parseInt(input.next());
                if (studentAmount < 1) {
                    System.out.println("Please enter a number greater than 0.");
                    continue;
                }
                break;
            } catch (Exception ignore) {
                System.out.println("Please enter a valid integer value: ");
            }
        }

        Stack<Student> tempStack = new Stack<>();

        for (int i = 0; i < studentAmount; i++) {
            System.out.println("Inserting student no. " + (i + 1));

            System.out.println("Enter student id: ");
            int studentId;
            while (true) {
                studentId = input.nextInt();
                if (!validate.validateNumber(String.valueOf(studentId))) {
                    System.out.println("Invalid id!");
                    System.out.println("Please type correct student id: ");
                    continue;
                }
                break;
            }

            System.out.print("Enter student name: ");
            String name;
            while (true) {
                name = input.next();
                if (!validate.validateName(name)) {
                    System.out.println("Invalid name!");
                    System.out.print("Retype student name: ");
                    continue;
                }
                break;

            }
            System.out.print("Enter student email: ");
            String email;
            while (true) {
                email = input.next();
                if (!validate.validateEmail(email)) {
                    System.out.println("Invalid email: ");
                    System.out.print("Retype Email: ");
                    continue;
                }
                break;
            }

            System.out.println("Enter student date of birth (yyyy-MM-dd): ");
            LocalDate studentDoB;
            while (true) {
                try {
                    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    studentDoB = LocalDate.parse(input.next(), dateFormat);
                    break;
                } catch (Exception ignore) {
                    System.out.println("Invalid date format. Please enter in yyyy-MM-dd format:");
                }
            }

            System.out.println("Enter student gender: ");
            String gender;
            while (true) {
                gender = input.next();
                if (!validate.validateGender(gender)) {
                    System.out.println("Invalid gender: ");
                    System.out.print("Retype student gender: ");
                    continue;
                }
                break;
            }

            System.out.println("Enter student address: ");
            String address = input.next();

            System.out.println("Enter student phone number: ");
            String phoneNumber;
            while (true) {
                phoneNumber = input.next();
                if (!validate.validatePhone(phoneNumber)) {
                    System.out.println("Invalid phone number: ");
                    System.out.print("Retype student phone number: ");
                    continue;
                }
                break;
            }

            System.out.println("Enter student major: ");
            String major = input.next();

            preparedStatement = connect.prepareStatement(addStd);
            preparedStatement.setInt(1, studentId);
            preparedStatement.setString(2,name);
            preparedStatement.setString(3, email);
            preparedStatement.setDate(4, Date.valueOf(studentDoB));
            preparedStatement.setString(5, gender);
            preparedStatement.setString(6, address);
            preparedStatement.setString(7, phoneNumber);
            preparedStatement.setString(8, major);
            preparedStatement.executeUpdate();

            Student student = new Student(studentId, name, email, studentDoB, gender, address, phoneNumber, major);

            tempStack.push(student);
        }

        while (!tempStack.isEmpty()) {
            stack.push(tempStack.pop());
        }

        System.out.println("Students added successfully!");
}

    public void deleteStudent() throws SQLException {
        connect = database.connectDb();
        System.out.println("Enter the id of student you want to delete: ");
        int studentId = input.nextInt();
        boolean found = false;
        String deleteStd = "DELETE FROM students WHERE studentId = ?";
        preparedStatement = connect.prepareStatement(deleteStd);
        preparedStatement.setInt(1, studentId);
        int count = preparedStatement.executeUpdate();
        if (count > 0) {
            found = true;
            while (!stack.isEmpty()) {
                Student currentStudent = stack.pop();
                if (currentStudent.getStudentId() == studentId) {
                    System.out.println("Student with ID " + studentId + " has been deleted.");
                    break;
                } else {
                    stack.push(currentStudent);
                }
            }
        }
        if (!found) {
            System.out.println("Student with ID " + studentId + " was not found.");
        }
    }




    public void updateStudent() throws SQLException {
        connect = database.connectDb();
        System.out.println("Enter the id of student you want to update: ");
        int studentId = input.nextInt();

        String selectStd = "SELECT * FROM students WHERE studentId=?";
        preparedStatement = connect.prepareStatement(selectStd);
        preparedStatement.setInt(1, studentId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Student with ID " + studentId + " was not found.");
            return;
        }

        // move cursor to first row
        result.next();

        String currentName = result.getString("studentName");
        String currentEmail = result.getString("studentEmail");
        LocalDate currentBirth = result.getDate("studentBirth").toLocalDate();
        String currentGender = result.getString("gender");
        String currentAddress = result.getString("address");
        String currentPhone = result.getString("phoneNumber");
        String currentMajor = result.getString("major");

        System.out.println("Current student information:");
        System.out.println("Name: " + currentName);
        System.out.println("Email: " + currentEmail);
        System.out.println("Birth date: " + currentBirth);
        System.out.println("Gender: " + currentGender);
        System.out.println("Address: " + currentAddress);
        System.out.println("Phone number: " + currentPhone);
        System.out.println("Major: " + currentMajor);

        String updateStd = "UPDATE students SET studentName = ?," +
                " studentEmail = ?, " +
                "studentBirth = ?, " +
                "gender = ?," +
                " address = ?," +
                " phoneNumber = ?," +
                " major = ? WHERE studentId = ?";
        preparedStatement = connect.prepareStatement(updateStd);

        System.out.println("Enter new student name: ");
        String studentName = input.next();
        System.out.println("Enter new student email: ");
        String studentEmail = input.next();
        System.out.println("Enter new student birth date (yyyy-MM-dd): ");
        while (true) {
            try {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate studentBirth = LocalDate.parse(input.next(), dateFormat);
                preparedStatement.setDate(3, Date.valueOf(studentBirth));
                break;
            } catch (Exception Ignore) {
                System.out.println("Invalid date. Please enter a valid date in the format of yyyy-MM-dd: ");
            }
        }
        System.out.println("Enter new student gender: ");
        String gender = input.next();
        System.out.println("Enter new student address: ");
        String address = input.next();
        System.out.println("Enter new student phone number: ");
        String phoneNumber = input.next();
        System.out.println("Enter new student major: ");
        String major = input.next();

        preparedStatement.setString(1, studentName);
        preparedStatement.setString(2, studentEmail);
        preparedStatement.setString(4, gender);
        preparedStatement.setString(5, address);
        preparedStatement.setString(6, phoneNumber);
        preparedStatement.setString(7, major);
        preparedStatement.setInt(8, studentId);

        int count = preparedStatement.executeUpdate();
        if (count > 0) {
            System.out.println("Student with ID " + studentId + " has been updated.");
            return;
        }

        System.out.println("Update failed for student with ID " + studentId);
    }




    public void printStudents() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();
        String query = "SELECT * FROM students";
        result = statement.executeQuery(query);
        if (!result.isBeforeFirst()) {
            System.out.println("List of students is empty.");
        } else {
            while (result.next()) {
                int studentId = result.getInt("studentId");
                String studentName = result.getString("studentName");
                String studentEmail = result.getString("studentEmail");
                LocalDate studentBirth = result.getDate("studentBirth").toLocalDate();
                String gender = result.getString("gender");
                String address = result.getString("address");
                String phoneNumber = result.getString("phoneNumber");
                String major = result.getString("major");

                System.out.println("ID: " + studentId +
                        "\n Student name: " + studentName +
                        "\n Email: " + studentEmail +
                        "\n Date of birth: " + studentBirth +
                        "\n Gender: " + gender +
                        "\n Address: " + address +
                        "\n Phone number: " + phoneNumber +
                        "\n Major: " + major);

                System.out.println();
            }
        }
    }

}

