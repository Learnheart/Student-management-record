package org.example.Service;
import org.example.Model.Student;
import org.example.database;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class StudentStack<S> {
    private Stack<Student> stack = new Stack<>();
    private Scanner input = new Scanner(System.in);
    Validation validate = new Validation();

    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;


//    Stack is used to store the Student objects as they are created. Once all students have been added, the function prints
//    them out in reverse order by popping each element off the stack and printing its attributes.
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

            input.nextLine();
            System.out.print("Enter student name: ");
            String name;
            while (true) {
                name = input.nextLine();
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

            input.nextLine();
            System.out.println("Enter student address: ");
            String address = input.nextLine();

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

            input.nextLine();
            System.out.println("Enter student major: ");
            String major = input.nextLine();

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

            stack.push(student);
        }

        System.out.println("Students added successfully! Printing in reverse order:\n");

        while (!stack.isEmpty()) {
            Student student = stack.pop();
            System.out.println("Student ID: " + student.getStudentId());
            System.out.println("Name: " + student.getStudentName());
            System.out.println("Email: " + student.getStudentEmail());
            System.out.println("Date of Birth: " + student.getStudentBirth());
            System.out.println("Gender: " + student.getGender());
            System.out.println("Address: " + student.getAddress());
            System.out.println("Phone Number: " + student.getPhoneNumber());
            System.out.println("Major: " + student.getMajor());
            System.out.println();
        }
    }


//    In the updated version of the function, we first retrieve the information for the student being deleted from the database
//    and create a Student object to represent it. Then, we push this object onto the stack before deleting the student from the database.
//    This allows us to keep track of the deleted student information so that we can potentially undo the deletion later if needed.
    public void deleteStudent() throws SQLException {
        try (Connection connection = database.connectDb();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM students WHERE studentId = ?")) {

            int studentId = 0;
            boolean validInput = false;
            while (!validInput) {
                System.out.println("Enter the id of student you want to delete: ");
                String inputStr = input.nextLine().trim();
                if (inputStr.isEmpty()) {
                    System.out.println("Invalid input. Please enter an integer value.");
                } else {
                    try {
                        studentId = Integer.parseInt(inputStr);
                        validInput = true;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter an integer value.");
                    }
                }
            }

            // Retrieve the information for the student being deleted
            String selectStd = "SELECT * FROM students WHERE studentId=?";
            PreparedStatement selectStatement = connection.prepareStatement(selectStd);
            selectStatement.setInt(1, studentId);
            ResultSet result = selectStatement.executeQuery();
            Student deletedStudent = null;
            if (result.next()) {
                deletedStudent = new Student(
                        result.getInt("studentId"),
                        result.getString("studentName"),
                        result.getString("studentEmail"),
                        result.getDate("studentBirth").toLocalDate(),
                        result.getString("gender"),
                        result.getString("address"),
                        result.getString("phoneNumber"),
                        result.getString("major")
                );
            }

            preparedStatement.setInt(1, studentId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                // Push the deleted student onto the stack
                stack.push(deletedStudent);
                System.out.println("Student with ID " + studentId + " has been deleted.\n\n");
            } else {
                System.out.println("Student with ID " + studentId + " was not found.\n\n");
            }
        }
    }

//  The code retrieves all the student records from the database with a SELECT query and then creates a stack object to
//  store the formatted string presentation of each record. In the while loop that traverses the ResultSet object,
//  each record is formatted into a string and pushed onto the stack.
//
//  After all the records have been processed, the code prints out the column headers and dashes for formatting. Then, it
// pops each element off the stack and prints it to the console in reverse order, which results in sorting the list by student name in descending order.
    public void sortingByName() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        String query = "SELECT * FROM students ORDER BY studentName DESC";
        result = statement.executeQuery(query);

        Stack<String> stack = new Stack<>();

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

                stack.push(String.format("| %-10s | %-25s | %-30s | %-15s | %-10s | %-20s | %-20s | %s\n",
                        studentId, studentName, studentEmail, studentBirth, gender, address, phoneNumber, major));
            }

            System.out.printf("| %-10s | %-25s | %-30s | %-15s | %-10s | %-20s | %-20s | %s\n",
                    "ID", "Student Name", "Email", "Date of Birth", "Gender", "Address", "Phone Number", "Major");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------");

            while (!stack.empty()) {
                System.out.print(stack.pop());
            }
        }
    }

  /*  In this modified code, we created a `stack` of `Student` objects and pushed the updated `Student` object onto the
    `stack`. Then, we updated the database with the new student information using the `PreparedStatement` object. */
//    The advantage of using a stack in the updateStudent() function is that it allows us to keep track of all the updated student
//    information in the order that they were updated. This means that we can easily print out the updated information for all students
//    at the end of the update process in the correct order.
//
//    Without the use of a stack, it would be difficult to keep track of the updated student information and print them out
//  in the correct order. This is because the order in which we update the students may not be the same as the order in which
//  we retrieve their information from the database. Using a stack ensures that we can easily retrieve the updated information
//  for each student in the order that they were updated, regardless of their original order in the database.
    public void updateStudent() throws SQLException {
        connect = database.connectDb();
        System.out.println("Enter the id of student you want to update: ");
        String studentIdStr = input.nextLine();

        if (studentIdStr.isEmpty()) {
            System.out.println("Invalid ID!");
            System.out.println("\n");
            return;
        }

        int studentId = Integer.parseInt(studentIdStr);

        String selectStd = "SELECT * FROM students WHERE studentId=?";
        preparedStatement = connect.prepareStatement(selectStd);
        preparedStatement.setInt(1, studentId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Student with ID " + studentId + " was not found.\n\n");
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

        System.out.println("\n------------------------------------------\n");
        System.out.println("Enter new student information: ");
        Student student = new Student();

        // set the updated student information
        student.setStudentId(studentId);

        while (true) {
            System.out.println("Enter new student name: ");
            String studentName = input.nextLine();
            if (!validate.validateName(studentName)) {
                System.out.println("Invalid name!");
                System.out.println("Retype student name: " );
                continue;
            }
            student.setStudentName(studentName);
            break;
        }

        while (true) {
            System.out.println("Enter new student email: ");
            String studentEmail = input.next();
            if (!validate.validateEmail(studentEmail)) {
                System.out.println("Invalid email: ");
                System.out.print("Retype Email: ");
                continue;
            }
            student.setStudentEmail(studentEmail);
            break;
        }

        while (true) {
            System.out.println("Enter new student birth date (yyyy-MM-dd): ");
            try {
                DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate studentBirth = LocalDate.parse(input.next(), dateFormat);
                student.setStudentBirth(studentBirth);
                break;
            } catch (Exception Ignore) {
                System.out.println("Invalid date. Please enter a valid date in the format of yyyy-MM-dd: ");
            }
        }

        while (true) {
            System.out.println("Enter student gender: ");
            String gender = input.next();
            if (!validate.validateGender(gender)) {
                System.out.println("Invalid gender: ");
                System.out.print("Retype student gender: ");
                continue;
            }
            student.setGender(gender);
            break;
        }

        input.nextLine();
        System.out.println("Enter student address: ");
        String address = input.nextLine();
        student.setAddress(address);

        while (true) {
            System.out.println("Enter student phone number: ");
            String phoneNumber = input.next();
            if (!validate.validatePhone(phoneNumber)) {
                System.out.println("Invalid phone number: ");
                System.out.print("Retype student phone number: ");
                continue;
            }
            student.setPhoneNumber(phoneNumber);
            break;
        }

        input.nextLine();
        System.out.println("Enter student major: ");
        String major = input.nextLine();
        student.setMajor(major);

        // push the updated student onto the stack
        stack.push(student);

        // update the database with the new student information
        String updateStd = "UPDATE students SET studentName = ?," +
                " studentEmail = ?, " +
                "studentBirth = ?, " +
                "gender = ?," +
                " address = ?," +
                " phoneNumber = ?," +
                " major = ? WHERE studentId = ?";
        preparedStatement = connect.prepareStatement(updateStd);
        preparedStatement.setString(1, student.getStudentName());
        preparedStatement.setString(2, student.getStudentEmail());
        preparedStatement.setDate(3, Date.valueOf(student.getStudentBirth()));
        preparedStatement.setString(4, student.getGender());
        preparedStatement.setString(5, student.getAddress());
        preparedStatement.setString(6, student.getPhoneNumber());
        preparedStatement.setString(7, student.getMajor());
        preparedStatement.setInt(8, studentId);
        preparedStatement.executeUpdate();

        System.out.println("Student with ID " + studentId + " has been updated.\n\n");
    }

//  Complexity time & space O(n)
//    used to temporarily store the student objects retrieved from the database in reverse order
//    the function pushes each student object onto the stack as it is retrieved. Once all the results have been retrieved,
//    the function pops each student object off the stack and prints it out in reverse order.

//    This approach simplifies the code and reduces the amount of memory needed to store the results, since only one student object needs to be stored in memory at a time.
//    It also eliminates the need to query the database twice, which could be more efficient depending on the size of the result set.
    public void printStudents() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();
        String query = "SELECT * FROM students";
        result = statement.executeQuery(query);
        if (!result.isBeforeFirst()) {
            System.out.println("List of students is empty.");
        } else {
            System.out.printf("| %-10s | %-25s | %-30s | %-15s | %-10s | %-20s | %-20s | %s\n",
                    "ID", "Student Name", "Email", "Date of Birth", "Gender", "Address", "Phone Number", "Major");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------");
            while (result.next()) {
                int studentId = result.getInt("studentId");
                String studentName = result.getString("studentName");
                String studentEmail = result.getString("studentEmail");
                LocalDate studentBirth = result.getDate("studentBirth").toLocalDate();
                String gender = result.getString("gender");
                String address = result.getString("address");
                String phoneNumber = result.getString("phoneNumber");
                String major = result.getString("major");

//                System.out.printf("| %-10s | %-25s | %-30s | %-15s | %-10s | %-20s | %-20s | %s",
//                        studentId, studentName, studentEmail, studentBirth, gender, address, phoneNumber, major);
                Student student = new Student(studentId, studentName, studentEmail, studentBirth, gender, address, phoneNumber, major);
                stack.push(student);
            }
            while (!stack.empty()) {
                Student student = stack.pop();
                System.out.printf("| %-10s | %-25s | %-30s | %-15s | %-10s | %-20s | %-20s | %s\n",
                        student.getStudentId(), student.getStudentName(), student.getStudentEmail(), student.getStudentBirth(), student.getGender(),
                        student.getAddress(), student.getPhoneNumber(), student.getMajor());
            }
            System.out.println();
        }

        // Close database connection and statement
        result.close();
        statement.close();
        connect.close();

        int choose = 0;
        do {
            System.out.println("\nEnter the number here: \n" +
                    "1 for add student \t" +
                    "2 for update student \t" +
                    "3 for delete student \t" +
                    "4 for search student \t" +
                    "5 for print student list \t\n" +
                    "6 for sorting ascending by student name \t" +
                    "0 for back to menu \n" +
                    "Choose: ");
            while (input.hasNext()) {
                choose = Integer.parseInt(input.nextLine());
                if (choose < 0 || choose > 6) {
                    System.out.print("Invalid value, please type number in range of 0 - 6: ");
                    continue;
                }
                break;
            }



            switch (choose) {
                case 1:
                    addStudent();
                    printStudents();
                    break;
                case 2:
                    updateStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    searchStudent();
                    break;
                case 5:
                    printStudents();
                    break;
                case 6:
                    sortingByName();
                    break;
                default:
                    break;
            }
        }while (choose != 0);

    }

//    pushes each Student object onto the stack. Then, if the stack is empty, it prints "No matching records found."
//    Otherwise, it pops each Student object from the stack and prints its data in a formatted table.

    public void searchStudent() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        System.out.println("Enter the ID or name of the student you want to search for: ");
        String keyword = input.nextLine();

        String searchStd = "SELECT * FROM students WHERE studentId = ? OR studentName = ?";
        preparedStatement = connect.prepareStatement(searchStd);
        preparedStatement.setString(1, keyword);
        preparedStatement.setString(2, keyword);

        result = preparedStatement.executeQuery();

        while (result.next()) {
            int studentId = result.getInt("studentId");
            String studentName = result.getString("studentName");
            String studentEmail = result.getString("studentEmail");
            LocalDate studentBirth = LocalDate.parse(result.getString("studentBirth"));
            String gender = result.getString("gender");
            String address = result.getString("address");
            String phoneNumber = result.getString("phoneNumber");
            String major = result.getString("major");

            Student student = new Student(studentId, studentName, studentEmail, studentBirth, gender, address, phoneNumber, major);
            stack.push(student);
        }

        if (stack.isEmpty()) {
            System.out.println("No matching records found.");
        } else {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.format("| %-10s | %-20s | %-30s | %-15s | %-8s | %-25s | %-15s | %-15s |\n", "ID", "Student Name", "Email", "Birth Date", "Gender", "Address", "Phone Number", "Major");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

            while (!stack.empty()) {
                Student student = stack.pop();
                System.out.format("| %-10d | %-20s | %-30s | %-15s | %-8s | %-25s | %-15s | %-15s |\n",
                        student.getStudentId(), student.getStudentName(), student.getStudentEmail(), student.getStudentBirth(),
                        student.getGender(), student.getAddress(), student.getPhoneNumber(), student.getMajor());
                System.out.println("---------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            }
        }
    }



}

