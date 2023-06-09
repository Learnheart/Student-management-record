package org.example.Service;

import org.example.Model.Course;
import org.example.Model.Subject;
import org.example.database;

import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class SubjectStack<S> {
    private Stack<Subject> subjectList = new Stack<>();
    private Scanner input = new Scanner(System.in);

    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;

    public void addSubject() throws SQLException {
        connect = database.connectDb();
        String addSubject = "INSERT INTO subject (subjectId, subjectName) VALUES (?,?)";

        System.out.println("Enter number of subject you want to input: ");
        int subjectAmount = 0;

        while (true) {
            try {

                subjectAmount = Integer.parseInt(input.next());
                if (subjectAmount < 1) {
                    System.out.println("Please enter a number greater than 0.");
                    continue;
                }
                break;
            } catch (Exception ignore) {
                System.out.println("Please enter a valid integer value: ");
            }
        }

        Stack<Subject> tempStack = new Stack<>();

        for (int i = 0; i < subjectAmount; i++) {
            System.out.println("Insert " + "subject number " + (i + 1) );
            System.out.println("Enter subject id: ");

            String subjectId;
            while (true) {
                subjectId = input.next();
                break;
            }
            input.nextLine();
            System.out.println("Enter subject name: ");
            String subjectName;

            while (true) {
                subjectName = input.nextLine();
                break;
            }

            preparedStatement = connect.prepareStatement(addSubject);
            preparedStatement.setString(1, subjectId);
            preparedStatement.setString(2, subjectName);
            preparedStatement.executeUpdate();

            Subject subject = new Subject(subjectId, subjectName);

            tempStack.push(subject);
        }

        while (!tempStack.isEmpty()) {
            subjectList.push(tempStack.pop());
        }

        System.out.println("Subject added successfully!");
    }

    public void deleteSubject() throws SQLException {
        connect = database.connectDb();

        System.out.println("Enter the id of subject you want to delete: ");
        String subjectId = input.nextLine();

        // Prepare a SELECT statement to retrieve the course with the given ID
        String selectSubject = "SELECT * FROM subject WHERE subjectId = ?";
        preparedStatement = connect.prepareStatement(selectSubject);
        preparedStatement.setString(1, subjectId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Subject with ID " + subjectId + " doesn't exist!");
            return;
        }

        // If the course exists, prepare a DELETE statement to delete it from the database
        String deleteSubject = "DELETE FROM subject WHERE subjectId = ?";
        preparedStatement = connect.prepareStatement(deleteSubject);
        preparedStatement.setString(1, subjectId);
        int count = preparedStatement.executeUpdate();

        Stack<Subject> tempStack = new Stack<>();

        if (count > 0) {
            // If delete was successful, delete the course from the linked list and the stack
            System.out.println("Subject with ID " + subjectId + " has been deleted \n\n");
            while (!subjectList.isEmpty()) {
                Subject subject = subjectList.pop();
                if (!subject.getSubjectId().equals(subjectId)) {
                    tempStack.push(subject);
                }
            }
            while (!tempStack.isEmpty()) {
                subjectList.push(tempStack.pop());
            }
        } else {
            System.out.println("Delete failed for subject with ID " + subjectId + "\n\n");
        }
    }


    public void updateSubject() throws SQLException {
        connect = database.connectDb();

        System.out.println("Enter the id of subject you want to update: ");
        String subjectId = input.nextLine();

        String selectSubject = "SELECT * FROM subject WHERE subjectId = ?";
        preparedStatement = connect.prepareStatement(selectSubject);
        preparedStatement.setString(1, subjectId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Subject with ID " + subjectId + " doesn't exist!");
            return;
        }

        result.next();

        String currentSubject = result.getString("subjectName");

        System.out.println("Current subject name is: " + currentSubject);

        Stack<String> updatesStack = new Stack<>();
        updatesStack.push(subjectId);

        System.out.println("Enter new subject name: ");
        String subjectName = input.nextLine();

        updatesStack.push(subjectName);

        while (!updatesStack.empty()) {
            String id = updatesStack.pop();
            String name = updatesStack.pop();

            String updatedSubject = "UPDATE subject SET subjectName = ? WHERE subjectId = ?";
            preparedStatement = connect.prepareStatement(updatedSubject);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            int count = preparedStatement.executeUpdate();
            if (count > 0) {
                System.out.println("Subject ID " + id + " has been updated");
            } else {
                System.out.println("Updated failed for subject with ID " + id);
            }
        }
    }



    public void printSubject() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();
        String print = "SELECT * FROM subject";
        result = statement.executeQuery(print);
        if (!result.isBeforeFirst()) {
            System.out.println("List of subject is empty.");
        } else {
            System.out.println("+------------+--------------------------------+");
            System.out.format("| %-10s | %-30s | \n", "ID", "Subject name");
            System.out.println("+------------+--------------------------------+");

            while (result.next()) {
                String subjectId = result.getString("subjectId");
                String subjectName = result.getString("subjectName");

                Subject subject = new Subject(subjectId, subjectName);
                subjectList.push(subject);
            }
            while (!subjectList.empty()) {
                Subject subject = subjectList.pop();
                System.out.printf("| %-10s | %-30s |\n", subject.getSubjectId(), subject.getSubjectName());
            }
            System.out.println("+------------+--------------------------------+");
            System.out.println();
        }
//        function inside table
        int choose = 0;
        do {
            System.out.println("Enter the number here: \n" +
                    "1 for add subject \t" +
                    "2 for update subject \t" +
                    "3 for delete subject \t" +
                    "4 for search subject \t" +
                    "5 for print subject list \t" +
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
                    addSubject();
                    break;
                case 2:
                    updateSubject();
                    break;
                case 3:
                    deleteSubject();
                    break;
                case 4:
                    searchSubject();
                    break;
                case 5:
                    printSubject();
                    break;
                default:
                    break;
            }
        }while (choose != 0);

    }

    public void searchSubject() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        System.out.println("Enter the ID of the subject you want to search: ");
        String keyword = input.nextLine();

        String searchSubject = "SELECT* FROM subject WHERE subjectId = ?";
        preparedStatement = connect.prepareStatement(searchSubject);
        preparedStatement.setString(1, keyword);

        result = preparedStatement.executeQuery();

        boolean found = false;

        System.out.println("+------------+--------------------------------+");
        System.out.format("| %-10s | %-30s |\n", "ID", "Subject name");
        System.out.println("+------------+--------------------------------+");
        while (result.next()) {
            String subjectId = result.getString("subjectId");
            String subjectName = result.getString("subjectName");

            System.out.format("| %-10s | %-30s |\n", subjectId, subjectName);
            System.out.println("\n");

            found = true;
        }
        System.out.println("+------------+--------------------------------+");
        if (!found) {
            System.out.println("No record found");
        }
    }
}
