package org.example;

import org.example.Model.Student;
import org.example.Model.Subject;
import org.example.Service.StudentStack;
import org.example.Service.SubjectStack;
import org.example.database.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        StudentStack<Student> studentStack = new StudentStack<>();
        SubjectStack<Subject> subjectStack = new SubjectStack<>();
        Student student = new Student();
        Subject subject = new Subject();
        Connection connection = database.connectDb();
        Scanner scanner = new Scanner(System.in);

        int choose = 0;

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
                    studentStack.printStudents();
                    clearConsole();
                    break;
                case 2:
                    subjectStack.printSubject();
                    clearConsole();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    System.exit(0);
                default:
                    break;
            }
        }
        while (choose != 0);

    }

    public static void clearConsole() throws IOException, InterruptedException {

        // for Windows
        if (System.getProperty("os.name").contains("Windows")) {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        // for Linux/Mac
        else {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }
    }
}