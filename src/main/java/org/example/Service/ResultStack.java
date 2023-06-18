package org.example.Service;

import org.example.Model.Result;
import org.example.database;

import java.sql.*;
import java.util.*;

public class ResultStack<S> {
    Stack<Result> results = new Stack<>();
    private Scanner input = new Scanner(System.in);

    private Connection connect;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet result;

    public void addResult() {
        connect = database.connectDb();

        System.out.println("Enter the number of result: ");
        int resultAmount = 0;

        while (true) {

            try {

                resultAmount = Integer.parseInt(input.next());
                if (resultAmount < 1) {
                    System.out.println("Please enter a number greater than 0.");
                    continue;
                }
                break;
            } catch (Exception ignore) {
                System.out.println("Please enter a valid integer value: ");
            }
        }

        for (int i = 0; i < resultAmount; i++) {
            System.out.println("Enter student ID:");
            int studentId = input.nextInt();
            input.nextLine();

            // Validate studentId input
            boolean studentExists = false;
            try {
                connect = database.connectDb();
                preparedStatement = connect.prepareStatement("SELECT * FROM students WHERE studentId = ?");
                preparedStatement.setInt(1, studentId);
                ResultSet rs = preparedStatement.executeQuery();
                studentExists = rs.next();
            } catch (Exception e) {
                System.out.println("Error checking if student exists: " + e.getMessage());
            }finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error closing database connection: " + e.getMessage());
                }
            }
            if (!studentExists) {
                System.out.println("Student with ID " + studentId + " does not exist in the database. Please try again.");
                addResult();
                return;
            }


            System.out.println("Enter subject ID:");
            String subjectId = input.nextLine();

            // Validate subjectId input
            boolean subjectExists = false;
            try {
//                connect = database.connectDb();
                preparedStatement = connect.prepareStatement("SELECT * FROM subject WHERE subjectId = ?");
                preparedStatement.setString(1, subjectId);
                ResultSet rs = preparedStatement.executeQuery();
                subjectExists = rs.next();
            } catch (Exception e) {
                System.out.println("Error checking if subject exists: " + e.getMessage());
            }finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error closing database connection: " + e.getMessage());
                }
            }
            if (!subjectExists) {
                System.out.println("Subject with ID " + subjectId + " does not exist in the database. Please try again.");
//                addResult();
//                return;
            }


            System.out.println("Enter course ID:");
            String courseId = input.nextLine();

            // Validate courseId input
            boolean courseExists = false;
            try {
                connect = database.connectDb();
                preparedStatement = connect.prepareStatement("SELECT * FROM course WHERE courseId = ?");
                preparedStatement.setString(1, courseId);
                ResultSet rs = preparedStatement.executeQuery();
                courseExists = rs.next();
            } catch (Exception e) {
                System.out.println("Error checking if course exists: " + e.getMessage());
            } finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error closing database connection: " + e.getMessage());
                }
            }
            if (!courseExists) {
                System.out.println("Course with ID " + courseId + " does not exist in the database. Please try again.");
                addResult();
                return;
            }

            System.out.println("Enter midterm score:");
            float midtermScore = input.nextFloat();

            System.out.println("Enter final score:");
            float finalScore = input.nextFloat();

            // Validate inputs
            if (midtermScore < 0 || midtermScore > 10 ||
                    finalScore < 0 || finalScore > 10) {
                System.out.println("Invalid input, please try again");
                addResult();
                return;
            }

            Result result = new Result(studentId, subjectId, courseId, midtermScore, finalScore);

            try {
                connect = database.connectDb();
                preparedStatement = connect.prepareStatement("INSERT INTO result (studentId, subjectId, courseId, midterm, final, totalmark, result) VALUES (?, ?, ?, ?, ?, ?, ?)");
                preparedStatement.setInt(1, result.getStudentId());
                preparedStatement.setString(2, result.getSubjectId());
                preparedStatement.setString(3, result.getCourseId());
                preparedStatement.setFloat(4, result.getMidtermScore());
                preparedStatement.setFloat(5, result.getFinalScore());
                preparedStatement.setFloat(6, result.getTotalMarks());
                preparedStatement.setString(7, String.valueOf(result.getCourseResult()));
                preparedStatement.executeUpdate();
                System.out.println("Result saved successfully! \n\n ");
            } catch (Exception e) {
                System.out.println("Error saving result: " + e.getMessage());
            }finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error closing database connection: " + e.getMessage());
                }
            }

            results.push(result);
        }
        while (!results.isEmpty()) {
            results.pop();
        }
    }

    public void printResult() throws SQLException {
        try {
            connect = database.connectDb();

            // Create a statement object
            statement = connect.createStatement();

            // Execute the query to retrieve all results from the result table
            result = statement.executeQuery("SELECT * FROM result");

            // Process the result set and add each result to a list
            List<Result> resultList = new ArrayList<>();
            while (result.next()) {
                Result result1 = new Result(
                        result.getInt("studentId"),
                        result.getString("subjectId"),
                        result.getString("courseId"),
                        result.getFloat("midterm"),
                        result.getFloat("final"));
                result1.setTotalMarks(result.getFloat("totalmark"));
                result1.setCourseResult(Result.CourseResult.valueOf(result.getString("result")));
                resultList.add(result1);
            }

            // Print out the results
            String format = "| %-10s | %-20s | %-20s | %-10s | %-10s | %-10s | %-10s |%n";
            System.out.format(format, "ID", "Subject", "Course", "Midterm", "Final", "Total", "Result");
            System.out.format("+------------+----------------------+----------------------+------------+------------+------------+------------+%n");
            for (Result result : resultList) {
                System.out.format(format,
                        result.getStudentId(),
                        result.getSubjectId(),
                        result.getCourseId(),
                        result.getMidtermScore(),
                        result.getFinalScore(),
                        result.getTotalMarks(),
                        result.getCourseResult().name());
            }
            System.out.format("+------------+----------------------+----------------------+------------+------------+------------+------------+%n");

        } catch (SQLException e) {
            System.out.println("An error occurred while retrieving the results: " + e.getMessage());
        } finally {
            // Close the database resources
            try {
                if (result != null) {
                    result.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                System.out.println("An error occurred while closing the database resources: " + e.getMessage());
            }
        }

        int choose = -1;
        do {
            System.out.println("\nEnter the number here: \n" +
                    "1 for add student result \t" +
                    "2 for update student result \t" +
                    "3 for delete student result \t" +
                    "4 for search student result \t" +
                    "5 for print student result list \t\n" +
                    "6 for sorting by student total mark \t" +
                    "0 for back to menu \n" +
                    "Choose: ");
            while (input.hasNext()) {
                choose = Integer.parseInt(input.next());
                if (choose < 0 || choose > 6) {
                    System.out.print("Invalid value, please type number in range of 0 - 6: ");
                    continue;
                }

                break;
            }

            switch (choose) {
                case 1:
                    addResult();
                    break;
                case 2:
                    updateResult();
                    break;
                case 3:
                    deleteResult();
                    break;
                case 4:
                    searchResult();
                    break;
                case 5:
                    printResult();
                    break;
                case 6:
                    sortByMark();
                    break;
                default:
                    break;
            }

        }while (choose != 0);


    }

    public void updateResult() {
        System.out.println("Enter student ID to update result:");
        int studentId;
        while (true) {
            try {
                studentId = Integer.parseInt(input.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid student ID:");
            }
        }

        // Check if the student ID exists in the database
        boolean studentExists = false;
        try {
            connect = database.connectDb();
            preparedStatement = connect.prepareStatement("SELECT * FROM students WHERE studentId = ?");
            preparedStatement.setInt(1, studentId);
            ResultSet rs = preparedStatement.executeQuery();
            studentExists = rs.next();
        } catch (Exception e) {
            System.out.println("Error checking if student exists: " + e.getMessage());
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (Exception e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }

        }

        // If the student ID exists, retrieve the results for that student and display them for the user to choose which one to update
        if (studentExists) {
            try {
                connect = database.connectDb();
                preparedStatement = connect.prepareStatement("SELECT * FROM result WHERE studentId = ?");
                preparedStatement.setInt(1, studentId);
                ResultSet rs = preparedStatement.executeQuery();

                // Store the results in a stack to enable easy updating of the most recent result first
                LinkedList<Result> resultList = new LinkedList<>();
                while (rs.next()) {
                    Result result = new Result(
                            rs.getInt("studentId"),
                            rs.getString("subjectId"),
                            rs.getString("courseId"),
                            rs.getFloat("midterm"),
                            rs.getFloat("final"));
                    result.setTotalMarks(rs.getFloat("totalmark"));
                    result.setCourseResult(Result.CourseResult.valueOf(rs.getString("result")));
                    resultList.push(result);
                }

                if (resultList.isEmpty()) {
                    System.out.println("No results found for student ID " + studentId);
                    return;
                }

                System.out.println("Select the result to update:");
                int index = 1;
                for (Result result : resultList) {
                    System.out.format("| %d. | %s | %s | %.2f | %.2f |\n",
                            index, result.getSubjectId(), result.getCourseId(),
                            result.getMidtermScore(), result.getFinalScore());
                    index++;
                }
                int choice = input.nextInt();

                // Retrieve the selected result from the stack
                Result selectedResult = null;
                for (int i = 0; i < choice; i++) {
                    selectedResult = resultList.pop();
                }

                // Prompt the user to enter the updated scores and update the database
                System.out.println("Enter updated midterm score:");
                float midtermScore = input.nextFloat();

                System.out.println("Enter updated final score:");
                float finalScore = input.nextFloat();

                // Validate inputs
                if (midtermScore < 0 || midtermScore > 10 ||
                        finalScore < 0 || finalScore > 10) {
                    System.out.println("Invalid input, please try again");
                    return;
                }

                selectedResult.setMidtermScore(midtermScore);
                selectedResult.setFinalScore(finalScore);
                selectedResult.updateTotalScore();
                selectedResult.updateCourseResult();

                try {
                    connect = database.connectDb();
                    preparedStatement = connect.prepareStatement("UPDATE result SET midterm = ?, final = ?, totalmark = ?, result = ? WHERE studentId = ? AND subjectId = ? AND courseId = ?");
                    preparedStatement.setFloat(1, selectedResult.getMidtermScore());
                    preparedStatement.setFloat(2, selectedResult.getFinalScore());
                    preparedStatement.setFloat(3, selectedResult.getTotalMarks());
                    preparedStatement.setString(4, String.valueOf(selectedResult.getCourseResult()));
                    preparedStatement.setInt(5, selectedResult.getStudentId());
                    preparedStatement.setString(6, selectedResult.getSubjectId());
                    preparedStatement.setString(7, selectedResult.getCourseId());
                    preparedStatement.executeUpdate();
                    System.out.println("Result updated successfully!");
                } catch (Exception e) {
                    System.out.println("Error updating result: " + e.getMessage());
                }
                finally {
                    try {
                        if (preparedStatement != null) {
                            preparedStatement.close();
                        }
                        if (connect != null) {
                            connect.close();
                        }
                    } catch (Exception e) {
                        System.out.println("Error closing database connection: " + e.getMessage());
                    }
                }

            } catch (Exception e) {
                System.out.println("Error retrieving results for student ID " + studentId + ": " + e.getMessage());
            }
            finally {
                try {
                    if (preparedStatement != null) {
                        preparedStatement.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (Exception e) {
                    System.out.println("Error closing database connection: " + e.getMessage());
                }
            }
        } else {
            System.out.println("Student with ID " + studentId + " does not exist in the database.");
        }
    }

    public void deleteResult() throws SQLException {
        connect = database.connectDb();

//        System.out.println("Enter the id of student you want to delete result: ");
//        String subjectId = input.nextLine();

        String findResult = "SELECT * FROM result WHERE studentId = ? AND subjectId = ?";

        int studentId = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Enter the id of student you want to delete: ");
            String inputStr = input.nextLine();
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

        System.out.println("Enter the subject id of student you want to delete: ");
        String subjectId = input.nextLine();

        preparedStatement = connect.prepareStatement(findResult);
        preparedStatement.setString(1, String.valueOf(studentId));
        preparedStatement.setString(2, subjectId);
        result = preparedStatement.executeQuery();

        if (!result.isBeforeFirst()) {
            System.out.println("Result with ID " + studentId + " doesn't exist!");
            return;
        }

        // If the course exists, prepare a DELETE statement to delete it from the database
        String deleteSubject = "DELETE FROM result WHERE studentId = ? AND subjectId = ?";
        preparedStatement = connect.prepareStatement(deleteSubject);
        preparedStatement.setString(1, String.valueOf(studentId));
        preparedStatement.setString(2, subjectId);
        int count = preparedStatement.executeUpdate();

        Stack<Result> tempStack = new Stack<>();

        if (count > 0) {
            // If delete was successful, delete the course from the linked list and the stack
            System.out.println("Result with ID " + studentId + " and subject with ID " + subjectId + " has been deleted \n\n");
            while (!results.isEmpty()) {
                Result stdResult = results.pop();
                if (!(stdResult.getStudentId() == (studentId)) && !stdResult.getSubjectId().equals(subjectId)) {
                    tempStack.push(stdResult);
                }
            }
            while (!tempStack.isEmpty()) {
                results.push(tempStack.pop());
            }
        } else {
            System.out.println("Delete failed for student's result with ID " + studentId + " and subject with ID " + subjectId + "\n\n");
        }
    }

    public void searchResult() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        System.out.println("Enter the ID of the student you want to search: ");
        String keyword = input.nextLine();

        String searchResult = "SELECT* FROM result WHERE studentId = ?";
        preparedStatement = connect.prepareStatement(searchResult);
        preparedStatement.setString(1, keyword);

        result = preparedStatement.executeQuery();

        boolean found = false;

        String format = "| %-10s | %-20s | %-20s | %-10s | %-10s | %-10s | %-10s |%n";
        System.out.format(format, "ID", "Subject", "Course", "Midterm", "Final", "Total", "Result");
        System.out.format("+------------+----------------------+----------------------+------------+------------+------------+------------+%n");
        while (result.next()) {

            int studentId = result.getInt("studentId");
            String subjectId = result.getString("subjectId");
            String courseId = result.getString("courseId");
            float midterm = Float.parseFloat(result.getString("midterm"));
            float finalScore = Float.parseFloat(result.getString("final"));
            float totalMark = Float.parseFloat(result.getString("totalmark"));
            String stdResult = result.getString("result");

            System.out.format("| %-10s | %-20s | %-20s | %-10s | %-10s | %-10s | %-10s |%n", studentId, subjectId, courseId, midterm, finalScore, totalMark, stdResult);
            found = true;
        }
        System.out.format("+------------+----------------------+----------------------+------------+------------+------------+------------+%n");
        System.out.println("\n");

        if (!found) {
            System.out.println("No record found");
        }
    }

    public void sortByMark() throws SQLException {
        connect = database.connectDb();
        statement = connect.createStatement();

        String query = "SELECT * FROM result ORDER BY totalmark ASC";
        result = statement.executeQuery(query);

        Stack<String> stack = new Stack<>();

        if (!result.isBeforeFirst()) {
            System.out.println("List of result is empty.");
        } else {
            String format = "| %-10s | %-20s | %-20s | %-10s | %-10s | %-10s | %-10s |%n";
            System.out.format(format, "ID", "Subject", "Course", "Midterm", "Final", "Total", "Result");
            System.out.format("+------------+----------------------+----------------------+------------+------------+------------+------------+%n");
            while (result.next()) {
                int studentId = result.getInt("studentId");
                String subjectId = result.getString("subjectId");
                String courseId = result.getString("courseId");
                float midterm = result.getFloat("midterm");
                float finalScore = result.getFloat("final");
                float total = result.getFloat("totalmark");
                String stdResult = result.getString("result");

//                Sort descending
                stack.push(String.format("| %-10s | %-20s | %-20s | %-10s | %-10s | %-10s | %-10s |%n", studentId, subjectId, courseId, midterm, finalScore, total, stdResult));

//                sort descending
//                System.out.format("| %-10s | %-20s | %-20s | %-10s | %-10s | %-10s | %-10s |%n", studentId, subjectId, courseId, midterm, finalScore, total, stdResult);
            }

            while (!stack.isEmpty()) {
                System.out.print(stack.pop());
            }
        }
        System.out.format("+------------+----------------------+----------------------+------------+------------+------------+------------+%n \n\n");
    }

}