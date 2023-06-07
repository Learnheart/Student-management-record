package org.example;

import org.example.Model.Student;
import org.example.Service.Stack;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Stack<Student> studentStack = new Stack<>();
        Student student = new Student();
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
                        System.out.print("nhập sai, vui lòng nhập lại số trong khoảng 0 - 5: ");
                        continue;
                    }
                    break;
                } catch (Exception ignored) {
                    System.out.print("Lựa chọn lại (Nhập số): ");
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