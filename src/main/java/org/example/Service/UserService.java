package org.example.Service;

import org.example.Model.Student;
import org.example.Model.Subject;
import org.example.Model.User;
import org.example.database;

import javax.swing.*;
import java.sql.*;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class UserService<S> {
    private LinkedList<User> userList = new LinkedList<User>();
    private StudentStack<Student> studentStack = new StudentStack<>();
    private SubjectStack<Subject> subjectStack = new SubjectStack<>();
    Student student = new Student();
    Subject subject = new Subject();
    User user = new User();
    private Scanner input = new Scanner(System.in);
    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;

    public void login() throws SQLException {
        connect = database.connectDb();

        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.print("Enter username: ");
            String username = input.nextLine();
            System.out.print("Enter password: ");
            String password = input.nextLine();


            try {
                // Retrieve user from database with matching username
                String query = "SELECT * FROM user WHERE username = ?";
                preparedStatement = connect.prepareStatement(query);
                preparedStatement.setString(1, username);
                result = preparedStatement.executeQuery();

                if (result.next()) {
                    String savedPassword = result.getString("password");

                    if (savedPassword.equals(password)) {
                        System.out.println("Login successful!");
                        System.out.println("\n");
                        loggedIn = true;
                    } else {
                        System.out.println("Invalid password.");
                        System.out.println("\n");
                    }
                } else {
                    System.out.println("Invalid username.");
                    System.out.println("\n");
                }
            } catch (Exception e) {
                System.out.println("An error occurred while logging in.");
                e.printStackTrace();
            }
        }

        int choose;
        do {
            System.out.println("Enter the number here: \n" +
                    "1 for open student table \n" +
                    "2 for open subject table \n" +
                    "3 for open course table \n" +
                    "4 for open result table \n" +
                    "5 for exit system \n" +
                    "Choose: ");

            while (true) {
                try {
                    choose = Integer.parseInt(input.nextLine());
                    if (choose < 0 || choose > 5) {
                        System.out.print("Invalid value, please type number in range of 0 - 5: ");
                        continue;
                    }
                    break;
                } catch (Exception ignored) {
                    System.out.print("Retyping (number): ");
                }
            }

            switch (choose) {
                case 1:
                    studentStack.printStudents();
                    break;
                case 2:
                    subjectStack.printSubject();

                    break;
                case 3:

                    break;
                case 4:

                    break;
                default:
                    break;
            }
            if (choose == 5) {
                break;
            }

            System.out.println("\n");
        }
        while (choose != 0);
    }

    public void register() throws SQLException {
        connect = database.connectDb();

        String regis = "INSERT INTO user (username, password) VALUES (?,?)";
        Stack<User> tempStack = new Stack<>();
        // Prompt user for username and password
        System.out.print("Enter username: ");
        String username = input.nextLine();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        // Push username and password onto stacks
        preparedStatement = connect.prepareStatement(regis);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        preparedStatement.executeUpdate();

        User user = new User (username, password);

        tempStack.push(user);

        while (!tempStack.isEmpty()) {
            userList.push(tempStack.pop());
        }
        System.out.println("User registration successful!");
        System.out.println("\n");
    }

}
