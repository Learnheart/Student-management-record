package org.example.Service;
import org.example.Model.Student;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Stack<S> {
    private LinkedList<Student> stack = new LinkedList<>();
    private Scanner input = new Scanner(System.in);

    Validation validate = new Validation();
    public void addStudent() {

        System.out.println("Enter the number of student: ");
        int studentAmount = 0;

        while (true) {
            try {
                studentAmount = Integer.parseInt(input.next());
                if (studentAmount < 0) {
                    System.out.println("Please input number that greater than 0");
                    continue;
                }
                break;
            } catch (Exception Ignore) {
                System.out.println("Please retype correct: ");
            }
        }

        for (int i = 0; i < studentAmount; i++) {
            System.out.println("Re-insert student number " + (i + 1));

            System.out.println("Student id: ");
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

            System.out.print("Student name: ");
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
            System.out.print("Student email: ");
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
                } catch (Exception Ignore) {
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
            String address;
            while (true) {
                address = input.next();
                break;
            }

            System.out.println("Enter student phone number: ");
            String phoneNumber = "";
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
            String major;
            while (true) {
                major = input.next();
                break;
            }

            Student student = new Student(studentId, name, email, studentDoB, gender, address, phoneNumber, major);

            stack.push(student);

        }
    }
    public void deleteStudent() {
        System.out.println("Enter the id of student you want to delete: \n");
        int studentId = input.nextInt();
        boolean found = false;
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).getStudentId() == studentId) {
                stack.remove(i);
                found = true;
                System.out.println("Student with ID " + studentId + " has been deleted.");
                break;
            }
        }
        if (!found) {
            System.out.println("Student with ID " + studentId + " was not found.");
        }
    }


    public void updateStudent() {
        System.out.println("Enter the id of student you want to update: ");
        int studentId = input.nextInt();
        boolean found = false;
        for (int i = 0; i < stack.size(); i++) {
            if (stack.get(i).getStudentId() == studentId) {
                String studentName, studentEmail, gender, address, phoneNumber, major;
                LocalDate studentBirth;

                System.out.println("Enter new student name: ");
                studentName = input.next();
                System.out.println("Enter new student email: ");
                studentEmail = input.next();
                System.out.println("Enter new student birth date (yyyy-MM-dd): ");
                while (true) {
                    try {
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        studentBirth = LocalDate.parse(input.next(), dateFormat);
                        break;
                    } catch (Exception Ignore) {
                        System.out.println("Invalid date. Please enter a valid date in the format of yyyy-MM-dd: ");
                    }
                }
                System.out.println("Enter new student gender: ");
                gender = input.next();
                System.out.println("Enter new student address: ");
                address = input.next();
                System.out.println("Enter new student phone number: ");
                phoneNumber = input.next();
                System.out.println("Enter new student major: ");
                major = input.next();

                stack.get(i).setStudentName(studentName);
                stack.get(i).setStudentEmail(studentEmail);
                stack.get(i).setStudentBirth(studentBirth);
                stack.get(i).setGender(gender);
                stack.get(i).setAddress(address);
                stack.get(i).setPhoneNumber(phoneNumber);
                stack.get(i).setMajor(major);

                found = true;
                System.out.println("Student with ID " + studentId + " has been updated.");
                break;
            }
        }
        if (!found) {
            System.out.println("Student with ID " + studentId + " was not found.");
        }
    }



    public void printStudents() {
        System.out.println("List of students: ");
        for (Student student : stack) {
            System.out.println("ID: " + student.getStudentId() +
                    "\n Student name: " + student.getStudentName() +
                    "\n Email: " + student.getStudentEmail() +
                    "\n Date of birth: " + student.getStudentBirth() +
                    "\n Gender: " + student.getGender() +
                    "\n Address: " + student.getAddress() +
                    "\n Phone number: " + student.getPhoneNumber() +
                    "\n Major: " + student.getMajor());
        }
        System.out.println();

    }
}
