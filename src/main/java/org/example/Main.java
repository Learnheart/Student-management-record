package org.example;

import org.example.Model.Student;
import org.example.Service.StudentStack;
import org.example.database.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {
        StudentStack<Student> studentStack = new StudentStack<>();
        Student student = new Student();
        Connection connection = database.connectDb();
        Scanner scanner = new Scanner(System.in);

        int choose = 0;

        do {
            System.out.println("Enter the number here: \n" +
                    "1 for add student \n" +
                    "2 for print student \n" +
                    "3 for update student \n" +
                    "4 for delete student \n" +
                    "5 for exit system \n" +
                    "Choose: ");

            while (true) {
                try {
                    choose = Integer.parseInt(scanner.nextLine());
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

                    studentStack.addStudent();
                    break;
                case 2:
                    studentStack.printStudents();
                    break;
                case 3:
                    studentStack.updateStudent();
                    break;
                case 4:
                    studentStack.deleteStudent();
                    break;

                default:
                    break;
            }
        }
        while (choose != 5);

    }
}