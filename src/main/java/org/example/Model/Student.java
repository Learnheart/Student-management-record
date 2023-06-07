package org.example.Model;

import java.time.LocalDate;
import java.util.Date;

public class Student {
        private int studentId;
        private String studentName;
        private String studentEmail;
        private LocalDate studentBirth;
        private String gender;
        private String address;
        private String phoneNumber;
        private String major;

        public Student() {}

        public Student(int studentId, String studentName, String studentEmail, LocalDate studentBirth, String gender, String address, String phoneNumber, String major) {
            this.studentId = studentId;
            this.studentName = studentName;
            this.studentEmail = studentEmail;
            this.studentBirth = studentBirth;
            this.gender = gender;
            this.address = address;
            this.phoneNumber = phoneNumber;
            this.major = major;
        }
        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }
        public String getStudentName() {
        return studentName;
    }

        public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

        public String getStudentEmail() {
            return studentEmail;
        }

        public void setStudentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
        }

        public LocalDate getStudentBirth() {
            return studentBirth;
        }

        public void setStudentBirth(LocalDate studentBirth) {
            this.studentBirth = studentBirth;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }


}
