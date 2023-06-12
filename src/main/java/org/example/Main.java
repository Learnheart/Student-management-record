package org.example;

import org.example.Model.Student;
import org.example.Model.Subject;
import org.example.Model.User;
import org.example.Service.StudentStack;
import org.example.Service.SubjectStack;
import org.example.Service.UserService;
import org.example.database.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        StudentStack<Student> studentStack = new StudentStack<>();
        SubjectStack<Subject> subjectStack = new SubjectStack<>();
        UserService<User> userStack = new UserService<>();
        User user = new User();
        Student student = new Student();
        Subject subject = new Subject();
        Connection connection = database.connectDb();
        Scanner scanner = new Scanner(System.in);

        boolean loggedIn = false;
        int choose = 0;

        do {
            System.out.println("----------------------------------------------------------");
            System.out.println("\t \t WELCOME TO OUR APPLICATION");
            System.out.println("----------------------------------------------------------\n");

            System.out.println("If you already have an account enter 1 to login");
            System.out.println("If you haven't had an account enter 2 to create new account");
            System.out.println("Enter 3 to exit.");

            choose = scanner.nextInt();
            scanner.nextLine(); // consume newline left-over

            switch (choose) {
                case 1:
                    if (!loggedIn) {
                        userStack.login();
                    } else {
                    }

                    break;
                case 2:
//                    user can registration
                    userStack.register();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice!");
            }

        } while (choose != 0);

    }

}